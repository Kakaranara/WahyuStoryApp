package com.example.wahyustoryapp.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.wahyustoryapp.databinding.LoadingStateBinding

class LoadingStateAdapter : LoadStateAdapter<LoadingStateAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: LoadingStateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.errorMsg.text = loadState.error.localizedMessage
            }
            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.errorMsg.isVisible = loadState is LoadState.Error

        }
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        val view = LoadingStateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }
}