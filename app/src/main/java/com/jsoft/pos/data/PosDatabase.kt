package com.jsoft.pos.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverter
import android.arch.persistence.room.TypeConverters
import com.jsoft.pos.data.entity.*
import com.jsoft.pos.data.entity.Unit
import com.jsoft.pos.data.model.*
import java.util.*

@Database(version = 1, entities = [
    Item::class,
    Tax::class,
    ItemTax::class,
    Discount::class,
    Category::class,
    Unit::class,
    Sale::class,
    SaleItem::class])
@TypeConverters(DateConverter::class, DiscountTypeConverter::class)
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

class DiscountTypeConverter {

    @TypeConverter
    fun fromOrdinal(ordinal: Int): DiscountType {
        return DiscountType.values()[ordinal]
    }

    @TypeConverter
    fun toOrdinal(type: DiscountType): Int {
        return type.ordinal
    }

}
