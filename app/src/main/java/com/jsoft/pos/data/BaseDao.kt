package com.jsoft.pos.data

import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Update

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.FAIL)
    fun insert(t: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAndGet(t: T): Long

    @Update(onConflict = OnConflictStrategy.FAIL)
    fun update(t: T)

    @Delete
    fun delete(t: T)

}