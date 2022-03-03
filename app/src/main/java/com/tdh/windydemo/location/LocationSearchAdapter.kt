package com.tdh.windydemo.location

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tdh.windydemo.databinding.ItemLocationSearchViewBinding
import com.tdh.windydemo.model.Location

class LocationSearchAdapter(
    private val locationList: MutableList<Location>,
    private val toggleSelectListener: (location: Location, isSelected: Boolean) -> Unit
) : RecyclerView.Adapter<LocationSearchAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
            ItemLocationSearchViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val model = locationList[position]
        holder.binding.locationNameTv.text = model.name
        holder.binding.tickIv.visibility = if (model.isSelected) View.VISIBLE else View.INVISIBLE

        holder.itemView.setOnClickListener {
            model.isSelected = !model.isSelected
            toggleSelectListener.invoke(model, model.isSelected)
        }
    }

    override fun getItemCount(): Int {
        return locationList.size
    }

    fun updateData(locationList: MutableList<Location>) {
        this.locationList.clear()
        this.locationList.addAll(locationList)
    }

    class VH(val binding: ItemLocationSearchViewBinding) : RecyclerView.ViewHolder(binding.root)
}