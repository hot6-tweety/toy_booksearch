package com.tweety.booksearchapp.data.repository

import com.tweety.booksearchapp.data.common.RetrofitInstance.api
import com.tweety.booksearchapp.data.db.AppDatabase
import com.tweety.booksearchapp.data.model.Book
import com.tweety.booksearchapp.data.model.SearchResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class BookSearchRepositoryImpl(private val db: AppDatabase) : BookSearchRepository {

    // network
    override suspend fun searchBooks(query: String, sort: String, page: Int, size: Int): Response<SearchResponse> {
        return api.searchBooks(query, sort, page, size)
    }

    // database
    override suspend fun insertBooks(book: Book) {
        db.bookDao().insertBook(book)
    }

    override suspend fun deleteBooks(book: Book) {
        db.bookDao().deleteBook(book)
    }

    override fun getFavoriteBooks(): Flow<List<Book>> {
        return db.bookDao().getFavoriteBooks()
    }

}