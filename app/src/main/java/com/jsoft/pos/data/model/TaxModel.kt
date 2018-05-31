package com.jsoft.pos.data.model

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.jsoft.pos.data.BaseDao
import com.jsoft.pos.data.entity.ItemJoinVO
import com.jsoft.pos.data.entity.ItemTax
import com.jsoft.pos.data.entity.Tax

@Dao
abstract class TaxDao : BaseDao<Tax> {

    @Query("SELECT * FROM tax")
    abstract fun findAllTaxes(): LiveData<List<Tax>>

    @Query("SELECT i.id as itemId, i.name, it.id as joinedId FROM item i LEFT JOIN item_tax it ON it.item_id = i.id ")
    abstract fun findItemTaxAssociations(): LiveData<List<ItemJoinVO>>

    @Query("SELECT * FROM tax WHERE id IN (SELECT tax_id FROM item_tax WHERE item_id = :itemId)")
    abstract fun findByItemSync(itemId: Long): List<Tax>

    @Query("SELECT * FROM tax WHERE id = :id LIMIT 1")
    abstract fun findById(id: Int): LiveData<Tax>

    @Query("SELECT * FROM tax WHERE id = :id LIMIT 1")
    abstract fun findByIdSync(id: Int): Tax

    @Query("SELECT COUNT(*) FROM tax")
    abstract fun findCount(): LiveData<Long>

    @Query("SELECT * FROM item_tax WHERE tax_id = :taxId")
    protected abstract fun findItemTaxesSync(taxId: Int): List<ItemTax>

    @Insert
    protected abstract fun saveItemTaxes(itemTaxes: List<ItemTax>)

    @Delete
    protected abstract fun deleteItemTaxes(itemTaxes: List<ItemTax>)

    @Transaction
    open fun save(tax: Tax) {
        if (tax.id > 0) {
            update(tax)
        } else {
            insert(tax)
        }
    }

    @Transaction
    open fun save(tax: Tax, items: MutableList<ItemJoinVO>?) {
        var id = tax.id
        if (tax.id > 0) {
            update(tax)
        } else {
            id = insertAndGet(tax).toInt()
        }

        val t = findByIdSync(id)
        items?.apply { assignTax(t, this) }
    }

    @Transaction
    protected open fun assignTax(tax: Tax, items: MutableList<ItemJoinVO>) {
        deleteItemTaxes(findItemTaxesSync(tax.id))
        val itemTaxes = items.map { ItemTax(itemId = it.itemId, taxId = tax.id) }
        saveItemTaxes(itemTaxes)
    }

}