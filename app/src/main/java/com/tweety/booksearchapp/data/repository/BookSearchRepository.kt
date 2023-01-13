package com.tweety.booksearchapp.data.repository

import com.tweety.booksearchapp.data.model.Book
import com.tweety.booksearchapp.data.model.SearchResponse
import kotlinx.coroutines.flow.Flow
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

    fun getFavoriteBooks(): Flow<List<Book>>
}