package com.jsoft.pos.data.model

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.jsoft.pos.data.BaseDao
import com.jsoft.pos.data.entity.Discount
import com.jsoft.pos.data.entity.ItemDiscount

@Dao
abstract class DiscountDao : BaseDao<Discount> {

    @Query("SELECT * FROM discount")
    abstract fun findAllDiscounts(): LiveData<List<Discount>>

    @Query("SELECT * FROM discount")
    abstract fun findAllDiscountsSync(): List<Discount>

    @Query("SELECT d.* FROM discount d " +
            "INNER JOIN item_discount id ON id.discount_id = d.id " +
            "WHERE id.item_id = :itemId ")
    abstract fun findDiscountAssociations(itemId: Long): List<Discount>

    @Query("SELECT * FROM discount WHERE id = :id LIMIT 1")
    abstract fun findById(id: Int): LiveData<Discount>

    @Query("SELECT * FROM discount WHERE id = :id LIMIT 1")
    abstract fun findByIdSync(id: Int): Discount

    @Query("SELECT COUNT(*) FROM discount")
    abstract fun findCount(): LiveData<Long>

    @Query("SELECT * FROM item_discount WHERE discount_id = :discountId")
    protected abstract fun findItemDiscountsSync(discountId: Int): List<ItemDiscount>

    @Insert
    protected abstract fun saveItemDiscounts(itemDiscounts: List<ItemDiscount>)

    @Delete
    protected abstract fun deleteItemDiscounts(itemDiscounts: List<ItemDiscount>)

    @Query("SELECT * FROM discount WHERE unique_name = :name LIMIT 1")
    abstract fun findByUniqueNameSync(name: String): Discount?

    @Transaction
    open fun save(discount: Discount) {
        discount.uniqueName = discount.uniqueName.toUpperCase()
        if (discount.id > 0) {
            update(discount)
        } else {
            insert(discount)
        }
    }

    @Transaction
    open fun save(discount: Discount, itemIds: Collection<Long>?) {
        var id = discount.id
        discount.uniqueName = discount.uniqueName.toUpperCase()
        if (discount.id > 0) {
            update(discount)
        } else {
            id = insertAndGet(discount).toInt()
        }
        val t = findByIdSync(id)

        itemIds?.takeUnless {
            deleteItemDiscounts(findItemDiscountsSync(discount.id))
            return@takeUnless it.isEmpty()
        }?.apply {
            assignDiscount(t, this)
        }
    }

    fun findItemDiscountAssociations(itemId: Long): List<Discount> {
        val discounts = findAllDiscountsSync()

        if (itemId > 0) {
            val ts = findDiscountAssociations(itemId)
            discounts.forEach {
                it._checked = ts.contains(it)
            }
        }

        return discounts
    }

    private fun assignDiscount(discount: Discount, itemIds: Collection<Long>) {
        val itemDiscounts = itemIds.map { ItemDiscount(itemId = it, discountId = discount.id) }
        saveItemDiscounts(itemDiscounts)
    }
}