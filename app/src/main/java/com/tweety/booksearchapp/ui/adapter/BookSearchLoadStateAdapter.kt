package com.tweety.booksearchapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.tweety.booksearchapp.databinding.ItemLoadStateBinding

class BookSearchLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<BookSearchLoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): BookSearchLoadStateViewHolder {
        return BookSearchLoadStateViewHolder(
            ItemLoadStateBinding.inflate(LayoutInflater.from(parent.context), parent, false), retry
        )
    }

    override fun onBindViewHolder(holder: BookSearchLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }
}