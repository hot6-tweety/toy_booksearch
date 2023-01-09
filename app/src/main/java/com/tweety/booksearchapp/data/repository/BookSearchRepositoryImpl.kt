package com.tweety.booksearchapp.data.repository

import com.tweety.booksearchapp.common.RetrofitInstance.api
import com.tweety.booksearchapp.model.SearchResponse
import retrofit2.Response

class BookSearchRepositoryImpl : BookSearchRepository {
    override suspend fun searchBooks(query: String, sort: String, page: Int, size: Int): Response<SearchResponse> {
        return api.searchBooks(query, sort, page, size)
    }
}