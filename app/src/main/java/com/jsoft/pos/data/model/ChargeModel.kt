package com.jsoft.pos.data.model

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.jsoft.pos.data.BaseDao
import com.jsoft.pos.data.entity.Charge
import com.jsoft.pos.data.entity.ItemCharge

@Dao
abstract class ChargeDao : BaseDao<Charge> {

    @Query("SELECT * FROM charge")
    abstract fun findAllCharges(): LiveData<List<Charge>>

    @Query("SELECT * FROM charge")
    abstract fun findAllChargesSync(): List<Charge>

    @Query("SELECT t.* FROM charge t " +
            "INNER JOIN item_charge it ON it.charge_id = t.id " +
            "WHERE it.item_id = :itemId ")
    abstract fun findChargeAssociations(itemId: Long): List<Charge>

    @Query("SELECT * FROM charge WHERE id = :id LIMIT 1")
    abstract fun findById(id: Int): LiveData<Charge>

    @Query("SELECT * FROM charge WHERE id = :id LIMIT 1")
    abstract fun findByIdSync(id: Int): Charge

    @Query("SELECT COUNT(*) FROM charge")
    abstract fun findCount(): LiveData<Long>

    @Query("SELECT * FROM item_charge WHERE charge_id = :taxId")
    protected abstract fun findItemChargesSync(taxId: Int): List<ItemCharge>

    @Insert
    protected abstract fun saveItemCharges(itemTaxes: List<ItemCharge>)

    @Delete
    protected abstract fun deleteItemCharges(itemTaxes: List<ItemCharge>)

    @Transaction
    open fun save(charge: Charge) {
        if (charge.id > 0) {
            update(charge)
        } else {
            insert(charge)
        }
    }

    @Transaction
    open fun save(charge: Charge, itemIds: Collection<Long>?) {
        var id = charge.id
        if (charge.id > 0) {
            update(charge)
        } else {
            id = insertAndGet(charge).toInt()
        }

        val t = findByIdSync(id)
        itemIds?.takeUnless {
            deleteItemCharges(findItemChargesSync(charge.id))
            return@takeUnless it.isEmpty()
        }?.apply {
            assignCharge(t, this)
        }
    }

    fun findItemChargeAssociations(itemId: Long): List<Charge> {
        val charges = findAllChargesSync()

        if (itemId > 0) {
            val ts = findChargeAssociations(itemId)
            charges.forEach {
                it._checked = ts.contains(it)
            }
        }

        return charges
    }

    private fun assignCharge(charge: Charge, itemIds: Collection<Long>) {
        val itemCharges = itemIds.map { ItemCharge(itemId = it, chargeId = charge.id) }
        saveItemCharges(itemCharges)
    }

}