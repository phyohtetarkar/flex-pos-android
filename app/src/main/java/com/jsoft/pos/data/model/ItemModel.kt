package com.jsoft.pos.data.model

import android.arch.lifecycle.LiveData
import android.arch.paging.DataSource
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.arch.persistence.db.SimpleSQLiteQuery
import android.arch.persistence.db.SupportSQLiteQuery
import android.arch.persistence.room.*
import android.databinding.BaseObservable
import com.jsoft.pos.data.BaseDao
import com.jsoft.pos.data.Searchable
import com.jsoft.pos.data.entity.*
import com.jsoft.pos.data.entity.Unit

class ItemRepository(
        private val dao: ItemDao,
        private val unitDao: UnitDao? = null,
        private val categoryDao: CategoryDao? = null,
        private val chargeDao: ChargeDao? = null,
        private val discountDao: DiscountDao? = null
) {

    fun findItemsChecked(search: ItemSearch, id: Int, checkedIds: Collection<Long>?, with: Item.AssignType): List<Item> {

        val items = dao.findItemsSync(SimpleSQLiteQuery(search.query, search.objects.toTypedArray()))
        val itemsFiltered: List<Item>? = id.let {
            if (it > 0) {
                when(with) {
                    Item.AssignType.CHARGE -> dao.findAssociationsByCharge(it)
                    Item.AssignType.DISCOUNT -> dao.findAssociationsByDiscount(it)
                }
            } else {
                null
            }

        }

        items.forEach {
            if (checkedIds != null) {
                it._checked = checkedIds.contains(it.id)
            } else {
                it._checked = itemsFiltered?.contains(it) ?: false
            }
        }


        return items
    }

    fun findItemVOs(search: ItemVOSearch): LiveData<PagedList<ItemVO>> {
        return LivePagedListBuilder(dao.findItemVOs(SimpleSQLiteQuery(search.query, search.objects.toTypedArray())), 20).build()
    }

    fun getItem(id: Long): Item {
        if (id > 0) {
            val item = dao.findByIdSync(id)
            item.categoryId?.apply {
                item.category = categoryDao?.findByIdSync(this)
            }

            item.unitId?.apply {
                item.unit = unitDao?.findByIdSync(this)
            }

            item.charges = chargeDao?.findChargeAssociations(id)
            item.discounts = discountDao?.findDiscountAssociations(id)

            return item
        }

        return Item()
    }

    fun save(item: Item, taxes: List<Charge>?, discounts: List<Discount>?) {
        dao.save(item, taxes.orEmpty(), discounts.orEmpty())
    }

    fun delete(item: Item) {
        dao.delete(item)
    }

}

class ItemSearch : BaseObservable(), Searchable {

    var name: String? = null
        set(name) {
            field = name
            notifyChange()
        }

    override val query: String
        get() {
            val sb = StringBuilder()
            objects.clear()
            sb.append("SELECT * FROM item WHERE 1 = 1 ")

            name?.apply {
                sb.append("AND UPPER(i.name) LIKE ? ")
                objects.add("${toUpperCase()}%")
            }

            return sb.toString()
        }

    override val objects: MutableList<Any> = mutableListOf()

}

class ItemVOSearch : BaseObservable(), Searchable {

    var name: String? = null
        set(name) {
            field = name
            notifyChange()
        }

    var categoryId: Int = 0
        set(value) {
            field = value
            notifyChange()
        }

    var isAvailable: Boolean? = null
        set(available) {
            field = available
            notifyChange()
        }

    override val objects: MutableList<Any> = mutableListOf()

    override val query: String
        get() {
            objects.clear()
            val sb = StringBuilder()
            sb.append("SELECT i.id, " +
                    "i.name, " +
                    "i.barcode, " +
                    "i.image, " +
                    "u.name as unit, " +
                    "c.name as category, " +
                    "c.color, " +
                    "i.amount, " +
                    "i.price " +
                    "FROM item i " +
                    "LEFT OUTER JOIN unit u ON u.id = i.unit_id " +
                    "LEFT OUTER JOIN category c ON c.id = i.category_id " +
                    "WHERE 1 = 1 ")

            name?.takeUnless { it.isEmpty() }?.apply {
                sb.append("and UPPER(c.name) LIKE ? ")
                objects.add("${toUpperCase()}%")
            }

            categoryId.takeIf { it > 0 }?.apply {
                sb.append("and i.category_id = ? ")
                objects.add(this)
            }

            isAvailable?.also {
                sb.append("and i.available = ? ")
                objects.add(it)
            }

            return sb.toString()
        }

}

@Dao
abstract class ItemDao : BaseDao<Item> {

    @RawQuery(observedEntities = [Item::class, Category::class, Unit::class])
    abstract fun findItemVOs(query: SupportSQLiteQuery): DataSource.Factory<Int, ItemVO>

    @RawQuery
    abstract fun findItemsSync(query: SupportSQLiteQuery): List<Item>

    @Query("SELECT i.* FROM item i " +
            "INNER JOIN item_charge it ON it.item_id = i.id " +
            "WHERE it.charge_id = :chargeId ")
    abstract fun findAssociationsByCharge(chargeId: Int): List<Item>

    @Query("SELECT i.* FROM item i " +
            "INNER JOIN item_discount id ON id.item_id = i.id " +
            "WHERE id.discount_id = :discountId ")
    abstract fun findAssociationsByDiscount(discountId: Int): List<Item>

    @Query("SELECT * FROM item WHERE id = :id LIMIT 1")
    abstract fun findById(id: Long): LiveData<Item>

    @Query("SELECT * FROM item WHERE id = :id LIMIT 1")
    abstract fun findByIdSync(id: Long): Item

    @Query("SELECT COUNT(*) FROM item")
    abstract fun findCount(): LiveData<Long>

    @Query("SELECT * FROM item_charge WHERE item_id = :itemId")
    protected abstract fun findItemChargesSync(itemId: Long): List<ItemCharge>
    @Query("SELECT * FROM item_discount WHERE item_id = :itemId")
    protected abstract fun findItemDiscountsSync(itemId: Long): List<ItemDiscount>

    @Insert
    protected abstract fun saveItemCharges(itemTaxes: List<ItemCharge>)
    @Insert
    protected abstract fun saveItemDiscounts(itemDiscounts: List<ItemDiscount>)

    @Delete
    protected abstract fun deleteItemCharges(itemTaxes: List<ItemCharge>)
    @Delete
    protected abstract fun deleteItemDiscounts(itemDiscounts: List<ItemDiscount>)

    @Transaction
    open fun save(item: Item, charges: List<Charge>, discounts: List<Discount>) {
        var itemId = item.id
        if (item.id > 0) {
            update(item)
        } else {
            itemId = insertAndGet(item)
        }

        charges.takeUnless { it.isEmpty() }?.apply { assignCharges(itemId, this) }
        discounts.takeUnless { it.isEmpty() }?.apply { assignDiscounts(itemId, this) }

    }

    private fun assignCharges(itemId: Long, charges: List<Charge>) {
        deleteItemCharges(findItemChargesSync(itemId))
        val itemCharges = charges.filter { it._checked }.map { ItemCharge(itemId = itemId, chargeId = it.id) }
        saveItemCharges(itemCharges)
    }

    private fun assignDiscounts(itemId: Long, discounts: List<Discount>) {
        deleteItemDiscounts(findItemDiscountsSync(itemId))
        val itemDiscounts = discounts.filter { it._checked }.map { ItemDiscount(itemId = itemId, discountId = it.id) }
        saveItemDiscounts(itemDiscounts)
    }

}