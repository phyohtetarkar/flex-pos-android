package com.jsoft.es.ui.views.nav

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jsoft.es.MainActivity
import com.jsoft.es.R
import com.jsoft.es.ui.views.category.CategoriesFragment
import com.jsoft.es.ui.views.item.ItemsFragment
import com.jsoft.es.ui.views.unit.UnitsFragment
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
        val activity = context as MainActivity
        activity.setTitle(R.string.title_Resources)
    }

    override fun onClick(view: View) {
        var fragment: Fragment? = null
        when (view.id) {
            R.id.tv_res_products -> fragment = ItemsFragment.INSTANCE
            R.id.tv_res_categories -> fragment = CategoriesFragment.INSTANCE
            R.id.tv_res_units -> fragment = UnitsFragment.INSTANCE
        }

        fragmentManager?.beginTransaction()
                ?.addToBackStack(null)
                ?.replace(R.id.contentMain, fragment)
                ?.commit()

        val main = activity as MainActivity
        main.animateToArrow()

    }
}
