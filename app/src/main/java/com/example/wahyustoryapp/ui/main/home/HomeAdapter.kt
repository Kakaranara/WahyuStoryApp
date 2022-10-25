package com.example.wahyustoryapp.ui.main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.wahyustoryapp.R
import com.example.wahyustoryapp.data.database.Story
import com.example.wahyustoryapp.databinding.ListItem2Binding
import java.util.*

class HomeAdapter() :
    PagingDataAdapter<Story, HomeAdapter.ListViewHolder>(DIFF_CALLBACK) {
    inner class ListViewHolder(val binding: ListItem2Binding) : ViewHolder(binding.root) {
        fun bind(data: Story) {
            binding.apply {
                /**
                 * untuk nama transisi yang memiliki kencendrungan mirip
                 * diberikan random uuid agar transition name view selalu berbeda
                 */
                itemTitle2.apply {
                    transitionName = UUID.randomUUID().toString()
                    text = data.name
                }
//                itemDescription2.apply {
//                    transitionName = UUID.randomUUID().toString()
//                    text = data.description
//                }
                itemDate2.apply {
                    transitionName = data.createdAt
                    itemDate2.text =
                        itemView.resources.getString(R.string.date_format, data.createdAt)
                }

                storyImage2.apply {
                    transitionName = data.photoUrl
                    Glide.with(binding.root.context)
                        .load(data.photoUrl)
                        .into(this)
                }

                btnDetail.setOnClickListener {
                    listener.setButtonClickListener(
                        data,
                        storyImage2,
                        itemTitle2,
//                        itemDescription2,
                        itemDate2
                    )
                }
            }
        }
    }

    private lateinit var listener: OnItemCallbackListener

    fun setOnclick(listener: OnItemCallbackListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = ListItem2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }

    }


    interface OnItemCallbackListener {
        fun setButtonClickListener(data: Story, image: View, name: View, date: View)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}