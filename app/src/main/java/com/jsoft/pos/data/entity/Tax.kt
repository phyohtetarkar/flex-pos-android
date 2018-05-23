package com.jsoft.pos.data.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

@Entity
data class Tax(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,
        var name: String = "",
        var value: Double = 0.0,
        var included: Boolean = true
) : Checkable {

    @Ignore
    override var _name: String = name

    @Ignore
    override var _checked: Boolean = true

    val taxDesc: String
        get() {
            return if ((value - value.toInt()) % 10 == 0.0) {
                "${value.toInt()} %"
            } else {
                "$value %"
            }
        }
}