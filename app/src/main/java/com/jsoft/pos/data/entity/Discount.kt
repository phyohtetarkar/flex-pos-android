package com.jsoft.pos.data.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

@Entity
data class Discount(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,
        var name: String = "",
        var value: Double = 0.00,
        var type: DiscountType = DiscountType.PERCENTAGE
) : Checkable {

    @Ignore
    override var _name: String = name

    @Ignore
    override var _checked: Boolean = false

    val discountDesc: String
        get() {
            val u = when (type) {
                DiscountType.PERCENTAGE -> "%"
                DiscountType.AMOUNT -> ""
            }
            return if ((value - value.toInt()) % 10 == 0.0) {
                "${value.toInt()} $u"
            } else {
                "$value $u"
            }
        }
}

enum class DiscountType {
    PERCENTAGE, AMOUNT
}