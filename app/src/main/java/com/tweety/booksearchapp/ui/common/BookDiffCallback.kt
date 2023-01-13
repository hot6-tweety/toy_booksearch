package com.tweety.booksearchapp.ui.common

import androidx.recyclerview.widget.DiffUtil
import com.tweety.booksearchapp.data.model.Book

class BookDiffCallback : DiffUtil.ItemCallback<Book>() {

    override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem.isbn == newItem.isbn
    }

    override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem == newItem
    }
}