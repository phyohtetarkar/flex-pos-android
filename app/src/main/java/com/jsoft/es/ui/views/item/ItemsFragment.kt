package com.jsoft.es.ui.views.item

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jsoft.es.MainActivity
import com.jsoft.es.R
import com.jsoft.es.data.model.ItemSearch
import com.jsoft.es.ui.custom.SimpleDividerItemDecoration
import com.jsoft.es.ui.utils.RecyclerViewItemTouchListener
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
            setHasFixedSize(true)
            addItemDecoration(SimpleDividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            addOnItemTouchListener(RecyclerViewItemTouchListener(this, object : RecyclerViewItemTouchListener.OnTouchListener {
                override fun onTouch(view: View, position: Int) {
                    adapter.getItemAt(position)?.apply {
                        showEdit((id), view)
                    }

                }

                override fun onLongTouch(view: View, position: Int) {

                }
            }))

            this.adapter = adapter
        }

        fabItems.setOnClickListener { showEdit(0, it) }

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

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val activity = context as MainActivity
        activity.setTitle(R.string.title_items)
    }

    private fun showEdit(id: Long, view: View?) {
        val options = view?.let { ActivityOptionsCompat.makeScaleUpAnimation(view, view.x.toInt(), view.y.toInt(), view.width, view.height) }
        val i = Intent(activity, EditItemActivity::class.java)
        i.putExtra("id", id)

        context?.apply {
            ActivityCompat.startActivity(this, i, options?.toBundle())
        }

    }

    companion object {
        val INSTANCE: ItemsFragment
            get() = ItemsFragment()
    }

}