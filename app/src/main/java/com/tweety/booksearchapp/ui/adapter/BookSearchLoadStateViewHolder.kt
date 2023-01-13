package com.tweety.booksearchapp.ui.adapter

import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.tweety.booksearchapp.databinding.ItemLoadStateBinding

class BookSearchLoadStateViewHolder(
    private val binding: ItemLoadStateBinding, retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.btnRetry.setOnClickListener {
            retry.invoke()
        }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            binding.tvError.text = "Error occurred"
        }
        // 로딩 상태에 따라 다른 위젯이 노출 됨
        binding.progressBar.isVisible = loadState is LoadState.Loading
        binding.btnRetry.isVisible = loadState is LoadState.Error
        binding.tvError.isVisible = loadState is LoadState.Error
    }
}