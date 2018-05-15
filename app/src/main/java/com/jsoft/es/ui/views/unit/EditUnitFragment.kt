package com.jsoft.es.ui.views.unit

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import com.jsoft.es.BR
import com.jsoft.es.R
import com.jsoft.es.data.entity.Unit
import com.jsoft.es.ui.utils.ValidatorUtils
import com.jsoft.es.ui.utils.ValidatorUtils.NOT_EMPTY

class EditUnitFragment : DialogFragment() {

    private var unitId: Int = 0

    private lateinit var viewModel: EditUnitViewModel
    private lateinit var binding: ViewDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.getInt("id")?.apply { unitId = this }

        viewModel = ViewModelProviders.of(this).get(EditUnitViewModel::class.java)

        isCancelable = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.fragment_edit_unit, container, false)
        binding.setVariable(BR.isValidUnitName, true)
        binding.setVariable(BR.unit, viewModel.unit)

        if (unitId > 0) {
            viewModel.unitLiveData.observe(this, Observer { viewModel.unit.set(it) })
            viewModel.unitInput.value = unitId
        } else {
            viewModel.unit.set(Unit())
        }

        binding.setVariable(BR.delegate, object : EditUnitDialogDelegate {
            override fun onSaveClick() {
                val unit = viewModel.unit.get()
                val valid = ValidatorUtils.isValid(unit?.name, NOT_EMPTY)

                if (!valid) {
                    binding.setVariable(BR.isValidUnitName, false)
                } else {
                    viewModel.save()
                    dismiss()
                }
            }

            override fun onCancelClick() {
                dismiss()
            }

        })

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val window = dialog.window

        if (window != null) {
            window.attributes.windowAnimations = R.style.DialogAnimation
        }
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
