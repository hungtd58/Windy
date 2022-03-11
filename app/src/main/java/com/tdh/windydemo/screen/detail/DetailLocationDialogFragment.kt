package com.tdh.windydemo.screen.detail

import androidx.fragment.app.viewModels
import com.tdh.libase.base.BaseDialogFragment
import com.tdh.windydemo.R
import com.tdh.windydemo.databinding.FragmentDialogDetailLocationBinding
import com.tdh.windydemo.screen.location.AddLocationViewModel

class DetailLocationDialogFragment : BaseDialogFragment<FragmentDialogDetailLocationBinding>() {
    private val detailLocationViewModel: DetailLocationViewModel by viewModels(ownerProducer = { requireActivity() })

    override fun getLayoutId() = R.layout.fragment_dialog_detail_location

    override fun getWidth() = 1f

    override fun getHeight() = 1f

    override fun initViewModels() {
        addViewModel(detailLocationViewModel)
    }

    override fun observeViewModel() {
        detailLocationViewModel.forecastWeatherDataModel.observe(viewLifecycleOwner) {

        }
    }

    override fun setupView() {

    }

    override fun initData() {

    }
}