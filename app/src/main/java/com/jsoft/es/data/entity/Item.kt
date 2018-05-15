package com.jsoft.es.data.entity

import android.arch.persistence.room.*

@Entity(foreignKeys = [
    ForeignKey(entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["category_id"])
], indices = [
    (Index(value = ["category_id"]))
])
data class Item(
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0,
        var name: String = "",
        var code: String = "",
        var barcode: String = "",
        var image: String = "",
        var remark: String = "",
        var available: Boolean = true,
        @ColumnInfo(name = "category_id")
        var categoryId: Int = 0
) {
    @Ignore
    var category: Category? = Category(name = "choose")
        set(value) {
            field = value
            value?.id?.apply { categoryId = this }
        }
}

data class ItemVO(
        var id: Long,
        var name: String,
        var code: String,
        var image: String,
        var unit: String?,
        var category: String,
        var color: String,
        var price: Int?
)

data class ItemDetailVO(
        @Embedded
        var item: Item = Item(),
        @Relation(parentColumn = "id", entityColumn = "item_id", entity = ItemPricing::class)
        var pricing: MutableList<ItemPricing> = mutableListOf()
)