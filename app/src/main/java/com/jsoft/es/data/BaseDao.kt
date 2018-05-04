package com.jsoft.es.data

import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Update

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(t: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAndGet(t: T): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(t: T)

    @Delete
    fun delete(t: T)

}