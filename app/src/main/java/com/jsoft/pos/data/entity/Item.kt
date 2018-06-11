package com.jsoft.pos.data.entity

import android.arch.persistence.room.*
import android.databinding.BaseObservable
import android.databinding.Bindable
import com.jsoft.pos.BR
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*

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
        var cost: Double = 0.0,
        var image: String = "",
        var available: Boolean = true,

        @ColumnInfo(name = "unit_id")
        var unitId: Int? = null,
        @ColumnInfo(name = "category_id")
        var categoryId: Int? = null
) : BaseObservable(), Checkable {

    enum class AssignType {
        CHARGE, DISCOUNT
    }

    @Ignore
    override var _name: String = ""
        get() = name

    @Ignore
    override var _checked: Boolean = false

    @Bindable
    @Ignore
    var unit: Unit? = Unit()
        set(value) {
            field = value
            unitId = value?.id ?: 0
            notifyPropertyChanged(BR.unit)
        }

    @Bindable
    @Ignore
    var category: Category? = Category()
        set(value) {
            field = value
            categoryId = value?.id ?: 0
            notifyPropertyChanged(BR.category)
        }

    @Ignore
    var charges: List<Charge>? = null
    @Ignore
    var discounts: List<Discount>? = null

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
            return "${amount.toSimplifyString()} $unit"
        }
}

fun Double.toSimplifyString(): String {
    return if ((this - this.toInt()) % 10 == 0.0) {
        "${this.toInt()}"
    } else {
        "$this"
    }
}

fun Double.round(): Double {
    val df = DecimalFormat.getInstance(Locale.ENGLISH) as DecimalFormat
    df.applyPattern("###.##")
    df.roundingMode = RoundingMode.HALF_UP
    df.maximumFractionDigits = 2
    return df.format(this).toDouble()
}