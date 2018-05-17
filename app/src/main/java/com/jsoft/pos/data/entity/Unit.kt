package com.jsoft.pos.data.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(indices = [(Index(value = ["unique_name"], unique = true))])
data class Unit(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,
        var name: String = "",
        @ColumnInfo(name = "unique_name")
        var uniqueName: String = ""
)