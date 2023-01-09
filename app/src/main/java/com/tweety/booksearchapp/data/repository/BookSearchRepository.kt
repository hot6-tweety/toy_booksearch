package com.tweety.booksearchapp.data.repository

import com.tweety.booksearchapp.model.SearchResponse
import retrofit2.Response

interface BookSearchRepository {
    suspend fun searchBooks(
        query: String,
        sort: String,
        page: Int,
        size: Int,
    ): Response<SearchResponse>
}