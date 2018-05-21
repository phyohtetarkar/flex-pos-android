package com.jsoft.pos.data.entity

import android.arch.persistence.room.*
import android.databinding.BaseObservable
import android.databinding.Bindable
import com.jsoft.pos.BR

@Entity(foreignKeys = [
    ForeignKey(entity = Unit::class,
            parentColumns = ["id"],
            childColumns = ["unit_id"]),
    ForeignKey(entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["category_id"])
], indices = [
    Index(value = ["unit_id"]),
    Index(value = ["category_id"])
])
data class Item(
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0,
        var name: String = "",
        var barcode: String = "",
        var amount: Double = 0.0,
        var price: Double = 0.0,
        var image: String = "",
        var remark: String = "",
        var available: Boolean = true,
        @ColumnInfo(name = "unit_id")
        var unitId: Int = 0,
        @ColumnInfo(name = "category_id")
        var categoryId: Int = 0
) : BaseObservable() {

    @Bindable
    @Ignore
    var unit: Unit? = Unit(name = "choose")
        set(value) {
            field = value
            unitId = value?.id ?: 0
            notifyPropertyChanged(BR.unit)
        }

    @Bindable
    @Ignore
    var category: Category? = Category(name = "choose")
        set(value) {
            field = value
            categoryId = value?.id ?: 0
            notifyPropertyChanged(BR.category)
        }
}

data class ItemVO(
        var id: Long,
        var name: String,
        var barcode: String,
        var image: String,
        var unit: String,
        var category: String,
        var color: String,
        var amount: Double,
        var price: Double
) {
    @Ignore
    var amountDesc: String? = null
        get() {
            if ((amount - amount.toInt()) % 10 == 0.0) {
                return "${amount.toInt()} $unit"
            } else {
                return "$amount $unit"
            }
        }
}