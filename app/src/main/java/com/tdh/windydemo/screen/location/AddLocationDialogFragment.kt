package com.tdh.windydemo.screen.location

import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.tdh.libase.base.BaseDialogFragment
import com.tdh.windydemo.R
import com.tdh.windydemo.databinding.FragmentDialogAddLocationBinding
import isNothing

class AddLocationDialogFragment : BaseDialogFragment<FragmentDialogAddLocationBinding>() {
    private val addLocationViewModel: AddLocationViewModel by viewModels()
    private lateinit var locationSearchAdapter: LocationSearchAdapter
    var onDismissListener : (() -> Unit)? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_dialog_add_location
    }

    override fun getWidth() = 0.9f

    override fun getHeight() = 0.6f

    override fun initViewModels() {
        addViewModel(addLocationViewModel)
    }

    override fun observeViewModel() {
        addLocationViewModel.locationList.observe(viewLifecycleOwner) {
            locationSearchAdapter.updateData(it)
            locationSearchAdapter.notifyDataSetChanged()
        }
    }

    override fun setupView() {
        binding.cancelTv.setOnClickListener {
            dismissAllowingStateLoss()
        }
        binding.searchCityEdt.addTextChangedListener {
            binding.clearIc.visibility =
                if (it?.toString()?.isNothing() == true) View.GONE else View.VISIBLE
            addLocationViewModel.searchLocation(it?.toString())
        }
        binding.clearIc.setOnClickListener {
            binding.searchCityEdt.setText("")
        }

        locationSearchAdapter = LocationSearchAdapter(mutableListOf()) { location, isSelected ->
            addLocationViewModel.toggleSelectLocation(location, isSelected)
        }
        binding.locationRv.adapter = locationSearchAdapter
    }

    override fun initData() {
        addLocationViewModel.searchLocation("")
    }

    override fun onDestroy() {
        onDismissListener?.invoke()
        super.onDestroy()
    }
}