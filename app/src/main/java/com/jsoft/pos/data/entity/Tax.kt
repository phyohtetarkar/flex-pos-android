package com.jsoft.pos.data.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

@Entity
data class Tax(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,
        var name: String = "",
        var amount: Double = 0.0,
        var included: Boolean = true
) : Checkable {

    @Ignore
    override var _name: String = ""
        get() = "$name (${amount.toSimplifyString()}%)"

    @Ignore
    override var _checked: Boolean = false

    val taxDesc: String
        get() = "${amount.toSimplifyString()} %"
}