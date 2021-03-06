package com.flex.pos.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverter
import android.arch.persistence.room.TypeConverters
import com.flex.pos.data.entity.*
import com.flex.pos.data.entity.Unit
import com.flex.pos.data.model.*
import java.util.*

@Database(version = 1, entities = [
    Item::class,
    Tax::class,
    ItemTax::class,
    ItemDiscount::class,
    Discount::class,
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

    abstract fun taxDao(): TaxDao

    abstract fun discountDao(): DiscountDao

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
