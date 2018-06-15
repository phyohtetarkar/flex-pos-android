package com.jsoft.pos.ui.views.unit

import android.app.AlertDialog
import android.app.Dialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import com.jsoft.pos.R
import com.jsoft.pos.databinding.EditUnitBinding

class EditUnitFragment : DialogFragment() {

    private lateinit var viewModel: EditUnitViewModel
    private var binding: EditUnitBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(EditUnitViewModel::class.java)

        isCancelable = false
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        binding = EditUnitBinding.inflate(inflater)

        binding?.setLifecycleOwner(this)
        binding?.vm = viewModel

        binding?.btnCancelUnit?.setOnClickListener {
            dismiss()
        }

        binding?.btnSaveUnit?.setOnClickListener {
            viewModel.save()
        }

        builder.setView(binding?.root)

        return builder.create()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val window = dialog.window

        if (window != null) {
            //window.attributes.windowAnimations = R.style.DialogAnimation
        }

        viewModel.saveSuccess.observe(this, Observer {
            if (it == true) {
                dismiss()
            }
        })

        viewModel.nameNotEmpty.observe(this, Observer {
            if (it == false) {
                binding?.editTextUnit?.error = resources.getString(R.string.error_empty_input_format, "Unit name")
            }
        })

        viewModel.nameUnique.observe(this, Observer {
            if (it == false) {
                binding?.editTextUnit?.error = resources.getString(R.string.error_name_conflict_format, "Unit name")
            }
        })

        viewModel.unitInput.value = arguments?.getInt("id", 0)
    }

    companion object {
        internal fun getInstance(id: Int): EditUnitFragment {
            val frag = EditUnitFragment()

            val args = Bundle()
            args.putInt("id", id)
            frag.arguments = args

            return frag
        }
    }

}
