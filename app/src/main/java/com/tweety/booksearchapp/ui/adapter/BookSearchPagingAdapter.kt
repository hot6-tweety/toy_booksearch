package com.tweety.booksearchapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.tweety.booksearchapp.data.model.Book
import com.tweety.booksearchapp.databinding.ItemBookPreviewBinding
import com.tweety.booksearchapp.ui.common.BookDiffCallback

class BookSearchPagingAdapter : PagingDataAdapter<Book, BookSearchViewHolder>(BookDiffCallback()) {

    override fun onBindViewHolder(holder: BookSearchViewHolder, position: Int) {
        val pagedBook = getItem(position)
        pagedBook?.let { book ->
            holder.bind(book)
            holder.itemView.setOnClickListener {
                onItemClickListener?.let { it(book) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookSearchViewHolder {
        return BookSearchViewHolder(
            ItemBookPreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    private var onItemClickListener: ((Book) -> Unit)? = null

    fun setOnItemClickListener(listener: (Book) -> Unit) {
        onItemClickListener = listener
    }


}