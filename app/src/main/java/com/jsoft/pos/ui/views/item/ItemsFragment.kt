package com.jsoft.pos.ui.views.item

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jsoft.pos.MainActivity
import com.jsoft.pos.R
import com.jsoft.pos.data.model.ItemSearch
import com.jsoft.pos.ui.utils.RecyclerViewItemTouchListener
import kotlinx.android.synthetic.main.fragment_items.*

class ItemsFragment : Fragment() {

    private lateinit var viewModel: ItemsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(ItemsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_items, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ItemAdapter()

        recyclerViewItems.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            addOnItemTouchListener(RecyclerViewItemTouchListener(this, object : RecyclerViewItemTouchListener.OnTouchListener {
                override fun onTouch(view: View, position: Int) {
                    adapter.getItemAt(position)?.apply {
                        showEdit((id))
                    }

                }

                override fun onLongTouch(view: View, position: Int) {

                }
            }))

            this.adapter = adapter
        }

        fabItems.setOnClickListener { showEdit(0) }

        val stub = viewStubItems.inflate()

        viewModel.items.observe(this, Observer {
            adapter.submitList(it)
            if (it!!.isEmpty()) {
                stub.visibility = View.VISIBLE
            } else {
                stub.visibility = View.GONE
            }
        })

    }

    override fun onResume() {
        super.onResume()
        viewModel.searchModel.value = ItemSearch()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val activity = context as MainActivity
        activity.setTitle(R.string.title_items)
    }

    private fun showEdit(id: Long) {
        val i = Intent(context, EditItemActivity::class.java)
        i.putExtra("id", id)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //startActivity(i, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle())
            startActivity(i)
        } else {
            startActivity(i)
        }

    }

    companion object {
        val INSTANCE: ItemsFragment
            get() = ItemsFragment()
    }

}