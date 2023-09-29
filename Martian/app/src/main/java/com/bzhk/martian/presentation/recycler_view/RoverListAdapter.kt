package com.bzhk.martian.presentation.recycler_view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bzhk.martian.data.Photo
import com.bzhk.martian.databinding.ItemPhotoBinding

class RoverListAdapter(
    private val onClick: (Photo) -> Unit
) : ListAdapter<Photo, RoverDataViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): RoverDataViewHolder {
        val binding = ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RoverDataViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: RoverDataViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            photo.load(item.imgSrc)
            textSolDate.text = "Sol ${item.sol}"
        }
        holder.binding.root.setOnClickListener {
            onClick(item)
        }
    }
}

class DiffUtilCallback : DiffUtil.ItemCallback<Photo>() {
    override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean =
        oldItem.camera.id == newItem.camera.id


    override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean =
        oldItem == newItem
}

class RoverDataViewHolder(
    val binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root)