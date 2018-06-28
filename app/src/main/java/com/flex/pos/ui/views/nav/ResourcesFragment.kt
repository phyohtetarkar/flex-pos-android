package com.flex.pos.ui.views.nav

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.flex.pos.MainActivity
import com.flex.pos.R
import com.flex.pos.databinding.ResourcesBinding
import com.flex.pos.ui.utils.ServiceLocator
import com.flex.pos.ui.views.category.CategoriesFragment
import com.flex.pos.ui.views.discount.DiscountsFragment
import com.flex.pos.ui.views.item.ItemsFragment
import com.flex.pos.ui.views.tax.TaxesFragment
import com.flex.pos.ui.views.unit.UnitsFragment
import kotlinx.android.synthetic.main.fragment_resources.*

class ResourcesFragment : Fragment(), View.OnClickListener {

    private lateinit var viewModel: ResourcesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(ResourcesViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<ResourcesBinding>(inflater, R.layout.fragment_resources, container, false)
        binding.setLifecycleOwner(this)
        binding.vm = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_res_products.setOnClickListener(this)
        tv_res_categories.setOnClickListener(this)
        tv_res_units.setOnClickListener(this)
    }

    override fun onStart() {
        super.onStart()
        val activity = activity as MainActivity
        activity.setTitle(R.string.items)
    }

    override fun onClick(view: View) {
        var fragment: Fragment? = null
        when (view.id) {
            R.id.tv_res_products -> fragment = ServiceLocator.locate(ItemsFragment::class.java)
            R.id.tv_res_categories -> fragment = ServiceLocator.locate(CategoriesFragment::class.java)
            R.id.tv_res_units -> fragment = ServiceLocator.locate(UnitsFragment::class.java)
        }

        fragmentManager?.beginTransaction()
                ?.addToBackStack(null)
                ?.replace(R.id.contentMain, fragment)
                ?.commit()

        val main = activity as MainActivity
        main.animateToArrow()

    }

}
