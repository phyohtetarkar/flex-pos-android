package com.jsoft.pos.data.model

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.jsoft.pos.data.BaseDao
import com.jsoft.pos.data.entity.ItemTax
import com.jsoft.pos.data.entity.Tax

@Dao
abstract class TaxDao : BaseDao<Tax> {

    @Query("SELECT * FROM tax")
    abstract fun findAllTaxes(): LiveData<List<Tax>>

    @Query("SELECT * FROM tax")
    abstract fun findAllTaxesSync(): List<Tax>

    @Query("SELECT t.* FROM tax t " +
            "INNER JOIN item_tax it ON it.tax_id = t.id " +
            "WHERE it.item_id = :itemId ")
    abstract fun findTaxAssociations(itemId: Long): List<Tax>

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

    @Query("SELECT * FROM tax WHERE unique_name = UPPER(:name) LIMIT 1")
    abstract fun findByUniqueNameSync(name: String): Tax?

    @Transaction
    open fun save(tax: Tax) {
        tax.uniqueName = tax.name.toUpperCase()
        if (tax.id > 0) {
            update(tax)
        } else {
            insert(tax)
        }
    }

    @Transaction
    open fun save(tax: Tax, itemIds: Collection<Long>?) {
        var id = tax.id
        tax.uniqueName = tax.name.toUpperCase()
        if (tax.id > 0) {
            update(tax)
        } else {
            id = insertAndGet(tax).toInt()
        }

        val t = findByIdSync(id)
        itemIds?.takeUnless {
            deleteItemTaxes(findItemTaxesSync(tax.id))
            return@takeUnless it.isEmpty()
        }?.apply {
            assignTax(t, this)
        }
    }

    fun findItemTaxAssociations(itemId: Long): List<Tax> {
        val taxes = findAllTaxesSync()

        if (itemId > 0) {
            val ts = findTaxAssociations(itemId)
            taxes.forEach {
                it._checked = ts.contains(it)
            }
        }

        return taxes
    }

    private fun assignTax(tax: Tax, itemIds: Collection<Long>) {
        val itemTaxes = itemIds.map { ItemTax(itemId = it, taxId = tax.id) }
        saveItemTaxes(itemTaxes)
    }

}