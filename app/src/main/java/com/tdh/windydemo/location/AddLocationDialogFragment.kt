package com.tdh.windydemo.location

import androidx.fragment.app.viewModels
import com.tdh.libase.base.BaseDialogFragment
import com.tdh.windydemo.R
import com.tdh.windydemo.databinding.FragmentDialogAddLocationBinding

class AddLocationDialogFragment : BaseDialogFragment<FragmentDialogAddLocationBinding>() {
    private val addLocationViewModel: AddLocationViewModel by viewModels()

    override fun getLayoutId(): Int {
        return R.layout.fragment_dialog_add_location
    }

    override fun getWidth() = 1.0f

    override fun getHeight() = 1.0f

    override fun initViewModels() {
        addViewModel(addLocationViewModel)
    }

    override fun observeViewModel() {

    }

    override fun setupView() {

    }

    override fun initData() {

    }
}