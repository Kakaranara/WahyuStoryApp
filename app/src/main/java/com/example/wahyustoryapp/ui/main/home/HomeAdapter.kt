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
import com.example.wahyustoryapp.helper.LatLong

class HomeAdapter() :
    PagingDataAdapter<Story, HomeAdapter.ListViewHolder>(DIFF_CALLBACK) {
    inner class ListViewHolder(val binding: ListItem2Binding) : ViewHolder(binding.root) {
        fun bind(data: Story, position: Int) {
            binding.apply {
                /**
                 * apply shared element
                 */
                itemTitle2.apply {
                    transitionName = data.name + position
                    text = data.name
                }

                circleImageView2.transitionName = "profile $position"

                itemDate2.apply {
                    transitionName = data.createdAt
                    itemDate2.text =
                        itemView.resources.getString(R.string.date_format, data.createdAt)
                }

                if (data.lat != null && data.lon != null) {
                    btnCheckLocation.apply {
                        visibility = View.VISIBLE
                        val latLng = LatLong(data.lat, data.lon)
                        setOnClickListener {
                            listener.setDetailMapsClickListener(latLng)
                        }

                    }
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
                        itemDate2,
                        circleImageView2
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
            holder.bind(data, position)
        }

    }


    interface OnItemCallbackListener {
        fun setButtonClickListener(
            data: Story,
            image: View,
            name: View,
            date: View,
            profileImage: View
        )

        fun setDetailMapsClickListener(
            data: LatLong
        )
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