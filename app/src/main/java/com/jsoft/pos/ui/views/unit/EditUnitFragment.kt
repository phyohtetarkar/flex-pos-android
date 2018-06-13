package com.jsoft.pos.ui.views.unit

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import com.jsoft.pos.BR
import com.jsoft.pos.R
import com.jsoft.pos.databinding.EditUnitBinding

class EditUnitFragment : DialogFragment() {

    private var unitId: Int = 0

    private lateinit var viewModel: EditUnitViewModel
    private lateinit var binding: EditUnitBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.apply {
            unitId = getInt("id", 0)
        }

        arguments?.getInt("id")?.apply { unitId = this }

        viewModel = ViewModelProviders.of(this).get(EditUnitViewModel::class.java)

        isCancelable = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = EditUnitBinding.inflate(inflater, container, false)
        binding.setLifecycleOwner(this)
        binding.isValidUnitName = true
        binding.vm = viewModel

        viewModel.unitInput.value = unitId

        binding.delegate = object : EditUnitDialogDelegate {
            override fun onSaveClick() {
                viewModel.save()
            }

            override fun onCancelClick() {
                dismiss()
            }

        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val window = dialog.window

        if (window != null) {
            window.attributes.windowAnimations = R.style.DialogAnimation
        }

        viewModel.saveSuccess.observe(this, Observer {
            if (it == true) {
                dismiss()
            }
        })

        viewModel.nameValid.observe(this, Observer {
            binding.setVariable(BR.isValidUnitName, it ?: true)
        })
    }

    override fun onStart() {
        super.onStart()
        val window = dialog.window

        window?.setLayout(MATCH_PARENT, WRAP_CONTENT)
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

interface EditUnitDialogDelegate {

    fun onSaveClick()

    fun onCancelClick()

}
