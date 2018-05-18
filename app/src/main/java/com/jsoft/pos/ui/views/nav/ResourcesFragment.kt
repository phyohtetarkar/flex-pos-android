package com.jsoft.pos.ui.views.nav

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jsoft.pos.MainActivity
import com.jsoft.pos.R
import com.jsoft.pos.databinding.ResourcesBinding
import com.jsoft.pos.ui.views.category.CategoriesFragment
import com.jsoft.pos.ui.views.item.ItemsFragment
import com.jsoft.pos.ui.views.unit.UnitsFragment
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