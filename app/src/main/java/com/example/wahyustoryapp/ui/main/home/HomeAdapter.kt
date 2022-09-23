package com.example.wahyustoryapp.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.wahyustoryapp.data.database.Story
import com.example.wahyustoryapp.databinding.ListItem2Binding
import com.example.wahyustoryapp.databinding.ListItemBinding

class HomeAdapter(private val listItem: List<Story>) :
    RecyclerView.Adapter<HomeAdapter.ListViewHolder>() {
    class ListViewHolder(val binding: ListItem2Binding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = ListItem2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listItem[position]
        holder.apply {
            binding.apply {
                itemTitle2.text = data.name
                itemDescription2.text = data.description
            }
        }
        Glide.with(holder.binding.root.context)
            .load(data.photoUrl)
            .into(holder.binding.storyImage2)
    }

    override fun getItemCount(): Int = listItem.size

}