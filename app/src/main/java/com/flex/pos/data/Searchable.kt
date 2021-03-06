package com.flex.pos.data

interface Searchable {

    val query: String
    val objects: MutableList<Any>

    companion object {
        const val BASE_QUERY = "SELECT * FROM %s WHERE 1 = 1 "
    }

}
