package com.tweety.booksearchapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tweety.booksearchapp.data.model.Book
import com.tweety.booksearchapp.databinding.ItemBookPreviewBinding
import com.tweety.booksearchapp.ui.common.BookDiffCallback

class BookSearchAdapter : ListAdapter<Book, BookSearchViewHolder>(BookDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookSearchViewHolder {
        return BookSearchViewHolder(
            ItemBookPreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: BookSearchViewHolder, position: Int) {
        val book = currentList[position]
        holder.bind(book)
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(book) }
        }
    }

    private var onItemClickListener: ((Book) -> Unit)? = null

    fun setOnItemClickListener(listener: (Book) -> Unit) {
        onItemClickListener = listener
    }
}