package com.jsoft.es.ui.views.nav

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jsoft.es.MainActivity
import com.jsoft.es.R
import com.jsoft.es.ui.views.category.CategoryActivity
import com.jsoft.es.ui.views.item.ItemActivity
import com.jsoft.es.ui.views.unit.UnitActivity
import kotlinx.android.synthetic.main.fragment_resources.*

class ResourcesFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_resources, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_res_products.setOnClickListener(this)
        tv_res_categories.setOnClickListener(this)
        tv_res_units.setOnClickListener(this)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val activity = context as MainActivity?
        activity?.title = "Resources"
    }

    override fun onClick(view: View) {
        var clazz: Class<*>? = null
        when (view.id) {
            R.id.tv_res_products -> clazz = ItemActivity::class.java
            R.id.tv_res_categories -> clazz = CategoryActivity::class.java
            R.id.tv_res_units -> clazz = UnitActivity::class.java
        }

        if (clazz != null) {
            val i = Intent(activity, clazz)
            startActivity(i)
        }
    }
}
