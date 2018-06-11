package com.jsoft.pos.ui.views.charge

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.util.DiffUtil
import com.jsoft.pos.MainActivity
import com.jsoft.pos.R
import com.jsoft.pos.data.entity.Charge
import com.jsoft.pos.ui.views.SimpleListAdapter
import com.jsoft.pos.ui.views.SimpleListFragment
import com.jsoft.pos.ui.views.SimpleListViewModel

class ChargesFragment : SimpleListFragment<Charge>() {

    private lateinit var viewModel: ChargesViewModel
    private lateinit var adapter: SimpleListAdapter<Charge>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(ChargesViewModel::class.java)

        adapter = SimpleListAdapter(object : DiffUtil.ItemCallback<Charge>() {
            override fun areItemsTheSame(oldItem: Charge?, newItem: Charge?): Boolean {
                return oldItem?.id == newItem?.id
            }

            override fun areContentsTheSame(oldItem: Charge?, newItem: Charge?): Boolean {
                return oldItem == newItem
            }

        }, R.layout.layout_simple_list_item_2)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val app = context as MainActivity
        app.setTitle(R.string.charges)
    }

    override val _adapter: SimpleListAdapter<Charge>
        get() = adapter

    override val _viewModel: SimpleListViewModel<Charge>
        get() = viewModel

    override fun onItemTouch(position: Int) {
        showEdit(adapter.getItemAt(position).id)
    }

    override fun showEdit(id: Any) {
        val i = Intent(context, EditChargeActivity::class.java)
        i.putExtra("id", id as Int)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //startActivity(i, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle())
            startActivity(i)
        } else {
            startActivity(i)
        }
    }

    companion object {
        val INSTANCE: ChargesFragment
            get() {
                return ChargesFragment()
            }
    }

}