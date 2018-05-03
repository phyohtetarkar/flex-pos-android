package com.jsoft.es.data.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity
data class Sale(
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0,
        @ColumnInfo(name = "issue_date")
        var issueDate: Date = Date(),
        @ColumnInfo(name = "total_item")
        var totalItem: Int = 0,
        var discount: Int = 0,
        @ColumnInfo(name = "tax_amount")
        var taxAmount: Int = 0,
        @ColumnInfo(name = "sub_total_price")
        var subTotalPrice: Int = 0,
        @ColumnInfo(name = "total_price")
        var totalPrice: Int = 0,
        @ColumnInfo(name = "pay_price")
        var payPrice: Int = 0,
        var change: Int = 0,
        var receipt: String = "",
        var remark: String = ""

)