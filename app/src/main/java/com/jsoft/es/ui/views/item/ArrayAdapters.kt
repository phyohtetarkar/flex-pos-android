package com.jsoft.es.ui.views.item

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.jsoft.es.R
import com.jsoft.es.data.entity.Category
import com.jsoft.es.data.entity.Unit

class CategoryArrayAdapter(context: Context?, resource: Int) : ArrayAdapter<Category>(context, resource) {

    var list: MutableList<Category> = mutableListOf()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val tv: TextView = super.getView(position, convertView, parent) as TextView

        if (position > 0) {
            val category = list.get(position)
            tv.text = category.name
        } else {
            tv.setText(R.string.choose_category)
        }

        return tv
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val tv: TextView = super.getDropDownView(position, convertView, parent) as TextView

        if (position > 0) {
            val category = list.get(position)
            tv.text = category.name
        } else {
            tv.setText(R.string.choose_category)
        }

        return tv
    }

    override fun getCount(): Int {
        return if (list.size > 0) list.size else 1
    }

    override fun getItem(position: Int): Category {
        return list.get(position)
    }

    fun setCategories(list: MutableList<Category>) {
        this.list = list
        notifyDataSetChanged()
    }

}

class UnitArrayAdapter(context: Context?, resource: Int) : ArrayAdapter<Unit>(context, resource) {

    var list: MutableList<Unit> = mutableListOf()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val tv: TextView = super.getView(position, convertView, parent) as TextView

        if (position > 0) {
            val category = list.get(position)
            tv.text = category.name
        } else {
            tv.setText(R.string.choose_unit)
        }

        return tv
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val tv: TextView = super.getDropDownView(position, convertView, parent) as TextView

        if (position > 0) {
            val category = list.get(position)
            tv.text = category.name
        } else {
            tv.setText(R.string.choose_unit)
        }

        return tv
    }

    override fun getCount(): Int {
        return if (list.size > 0) list.size else 1
    }

    override fun getItem(position: Int): Unit {
        return list.get(position)
    }

    fun setUnits(list: MutableList<Unit>) {
        this.list = list
        notifyDataSetChanged()
    }

}