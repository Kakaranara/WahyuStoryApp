package com.example.wahyustoryapp.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.wahyustoryapp.data.database.Story
import com.example.wahyustoryapp.databinding.ListItemBinding

class HomeAdapter(private val listItem: List<Story>) :
    RecyclerView.Adapter<HomeAdapter.ListViewHolder>() {
    class ListViewHolder(val binding: ListItemBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listItem[position]
        holder.apply {
            binding.apply {
                itemTitle.text = data.name
                itemDescription.text = data.description
            }
        }
        Glide.with(holder.binding.root.context)
            .load(data.photoUrl)
            .into(holder.binding.storyImage)
    }

    override fun getItemCount(): Int = listItem.size

}