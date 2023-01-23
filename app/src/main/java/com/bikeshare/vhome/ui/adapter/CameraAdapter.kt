package com.bikeshare.vhome.ui.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.bikeshare.vhome.data.itemmodel.CameraItem
import com.bikeshare.vhome.databinding.ItemListcamBinding


class CameraAdapter(val cameraItems: ArrayList<CameraItem>?) :
    RecyclerView.Adapter<CameraAdapter.CameraViewHolder>() {

    var onItemClick: ((Bundle) -> Unit)? = null

    inner class CameraViewHolder(private val binding: ItemListcamBinding) :
        RecyclerView.ViewHolder(binding.root) {

        var checkBox: CheckBox = binding.checkBox

        init {
            /*checkBox.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, isChecked ->
                //Toast.makeText(compoundButton.context,compoundButton.text.toString()+isChecked.toString(),Toast.LENGTH_SHORT).show()
                cameraItems!!.get(adapterPosition).isChecked = isChecked
                onItemClick?.invoke(cameraItems)
            })*/
            checkBox.setOnClickListener {
                val bundle = Bundle()
                bundle.putInt("position",adapterPosition)
                bundle.putBoolean("isChecked",checkBox.isChecked)
                onItemClick?.invoke(bundle)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CameraViewHolder {
        val binding = ItemListcamBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CameraViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CameraViewHolder, position: Int) {
        holder.checkBox.setText(cameraItems!!.get(position).cameraName)
        holder.checkBox.setChecked(cameraItems!!.get(position).isChecked)
    }

    override fun getItemCount(): Int {
        return cameraItems!!.size
    }
}