package com.jsoft.pos.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverter
import android.arch.persistence.room.TypeConverters
import com.jsoft.pos.data.entity.*
import com.jsoft.pos.data.entity.Unit
import com.jsoft.pos.data.model.CategoryDao
import com.jsoft.pos.data.model.ItemDao
import com.jsoft.pos.data.model.SaleDao
import com.jsoft.pos.data.model.UnitDao
import java.util.*
import kotlin.Long

@Database(version = 1, entities = [
    Item::class,
    Category::class,
    Unit::class,
    Sale::class,
    SaleItem::class])
@TypeConverters(DateConverter::class)
abstract class PosDatabase : RoomDatabase() {

    abstract fun unitDao(): UnitDao

    abstract fun categoryDao(): CategoryDao

    abstract fun itemDao(): ItemDao

    abstract fun saleDao(): SaleDao

    companion object {
        const val DB_NAME = "pos"
    }

}

class DateConverter {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

}
