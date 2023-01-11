package com.tweety.booksearchapp.data.repository

import androidx.lifecycle.LiveData
import com.tweety.booksearchapp.data.model.Book
import com.tweety.booksearchapp.data.model.SearchResponse
import retrofit2.Response

interface BookSearchRepository {

    // network
    suspend fun searchBooks(
        query: String,
        sort: String,
        page: Int,
        size: Int,
    ): Response<SearchResponse>

    // database
    suspend fun insertBooks(book: Book)

    suspend fun deleteBooks(book: Book)

    fun getFavoriteBooks(): LiveData<List<Book>>
}