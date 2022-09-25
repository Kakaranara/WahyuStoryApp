package com.example.wahyustoryapp.ui.main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.wahyustoryapp.R
import com.example.wahyustoryapp.data.database.Story
import com.example.wahyustoryapp.databinding.ListItem2Binding

class HomeAdapter(private val listItem: List<Story>) :
    RecyclerView.Adapter<HomeAdapter.ListViewHolder>() {
    class ListViewHolder(val binding: ListItem2Binding) : ViewHolder(binding.root)

    private lateinit var listener: OnItemCallbackListener

    fun setOnclick(listener: OnItemCallbackListener){
        this.listener = listener
    }

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
                itemDate2.text =
                    holder.itemView.resources.getString(R.string.date_format, data.createdAt)
                storyImage2.apply {
                    // transisi move tidak bekerja jika transition name berada di xml
                    // saya juga sebenarnya tidak tahu mengapa, hanya trial dan error meletakkan kode disini
                    // dan berhasil !!
                    transitionName = data.photoUrl
                    Glide.with(holder.binding.root.context)
                        .load(data.photoUrl)
                        .into(this)
                }

                btnDetail.setOnClickListener{
                    listener.setButtonClickListener(data, storyImage2)
                }
            }
        }

    }

    override fun getItemCount(): Int = listItem.size

    interface OnItemCallbackListener{
        fun setButtonClickListener(data: Story, image: View)
    }

}