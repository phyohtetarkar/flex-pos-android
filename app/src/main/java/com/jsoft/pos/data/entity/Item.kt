package com.jsoft.pos.data.entity

import android.arch.persistence.room.*
import android.databinding.BaseObservable
import android.databinding.Bindable
import com.jsoft.pos.BR

@Entity(foreignKeys = [
    ForeignKey(entity = Tax::class,
            parentColumns = ["id"],
            childColumns = ["tax_id"]),
    ForeignKey(entity = Discount::class,
            parentColumns = ["id"],
            childColumns = ["discount_id"]),
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
        var cost: Double = 0.0,
        var image: String = "",
        var remark: String = "",
        var available: Boolean = true,

        @ColumnInfo(name = "tax_id")
        var taxId: Int? = null,
        @ColumnInfo(name = "discount_id")
        var discountId: Int? = null,
        @ColumnInfo(name = "unit_id")
        var unitId: Int? = null,
        @ColumnInfo(name = "category_id")
        var categoryId: Int? = null
) : BaseObservable() {

    @Bindable
    @Ignore
    var tax: Tax? = Tax(name = "choose")
        set(value) {
            field = value
            taxId = value?.id ?: 0
            notifyChange()
        }

    @Bindable
    @Ignore
    var discount: Discount? = Discount(name = "choose")
        set(value) {
            field = value
            discountId = value?.id ?: 0
            notifyChange()
        }

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
    var amountDesc: String? = ""
        private set
        get() {
            return if ((amount - amount.toInt()) % 10 == 0.0) {
                "${amount.toInt()} $unit"
            } else {
                "$amount $unit"
            }
        }
}