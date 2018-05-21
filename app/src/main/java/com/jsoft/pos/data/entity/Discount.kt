package com.jsoft.pos.data.entity

import android.arch.persistence.room.PrimaryKey

data class Discount(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,
        var percentage: Int = 0,
        var description: String = ""
)