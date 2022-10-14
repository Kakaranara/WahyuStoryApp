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
import java.util.*

class HomeAdapter(private val listItem: List<Story>) :
    RecyclerView.Adapter<HomeAdapter.ListViewHolder>() {
    class ListViewHolder(val binding: ListItem2Binding) : ViewHolder(binding.root)

    private lateinit var listener: OnItemCallbackListener

    fun setOnclick(listener: OnItemCallbackListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = ListItem2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listItem[position]
        holder.apply {
            /**
             * transisi move tidak bekerja jika transition name berada di xml
             * (animasi lain seperti explode dapat bekerja)
             * saya juga sebenarnya tidak tahu mengapa,
             * hanya trial dan error meletakkan kode transition name disini (yang seharusnya di xml)
             * dan berhasil !!
             */
            binding.apply {
                /**
                 * untuk nama transisi yang memiliki kencendrungan mirip
                 * diberikan random uuid agar transition name view selalu berbeda
                 */
                itemTitle2.apply {
                    transitionName = UUID.randomUUID().toString()
                    text = data.name
                }
                itemDescription2.apply {
                    transitionName = UUID.randomUUID().toString()
                    text = data.description
                }
                itemDate2.apply {
                    transitionName = data.createdAt
                    itemDate2.text =
                        holder.itemView.resources.getString(R.string.date_format, data.createdAt)
                }

                storyImage2.apply {
                    transitionName = data.photoUrl
                    Glide.with(holder.binding.root.context)
                        .load(data.photoUrl)
                        .into(this)
                }

                btnDetail.setOnClickListener {
                    listener.setButtonClickListener(
                        data,
                        storyImage2,
                        itemTitle2,
                        itemDescription2,
                        itemDate2
                    )
                }
            }
        }

    }

    override fun getItemCount(): Int = listItem.size

    interface OnItemCallbackListener {
        fun setButtonClickListener(data: Story, image: View, name: View, desc: View, date: View)
    }

}