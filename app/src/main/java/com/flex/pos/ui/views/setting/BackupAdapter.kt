package com.flex.pos.ui.views.setting

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.flex.pos.BR
import com.flex.pos.databinding.BackupBinding
import kotlinx.android.synthetic.main.layout_backup.view.*
import java.text.SimpleDateFormat
import java.util.*

class BackupAdapter : ListAdapter<String, BackupAdapter.BackupViewHolder>(DIFF_CALLBACK) {

    var onRestore: ((Int) -> Unit)? = null
    var onDelete: ((Int) -> Unit)? = null

    private val formatFrom = SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH)
    private val formatTo = SimpleDateFormat("yyyy/MM/dd hh:mm:ss a", Locale.ENGLISH)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BackupViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = BackupBinding.inflate(inflater, parent, false)
        return BackupViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BackupViewHolder, position: Int) {
        holder.bind(formatTo.format(formatFrom.parse(getItem(position))))
    }

    fun getItemAt(position: Int): String? {
        return getItem(position)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem.contentEquals(newItem)
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem.contentEquals(newItem)
            }

        }
    }

    inner class BackupViewHolder(private var binding: BackupBinding) : RecyclerView.ViewHolder(binding.root) {

        init {

            binding.root.imvDeleteBackup.setOnClickListener {
                onDelete?.invoke(adapterPosition)
            }

            binding.root.imvRestoreBackup.setOnClickListener {
                onRestore?.invoke(adapterPosition)
            }
        }

        fun bind(obj: Any) {
            binding.setVariable(BR.obj, obj)
            binding.executePendingBindings()
        }

    }

}