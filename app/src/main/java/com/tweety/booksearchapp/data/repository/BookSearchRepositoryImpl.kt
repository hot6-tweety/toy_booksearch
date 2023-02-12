package com.tweety.booksearchapp.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.tweety.booksearchapp.common.Constants.PAGING_SIZE
import com.tweety.booksearchapp.common.Sort
import com.tweety.booksearchapp.data.common.BookSearchApi
import com.tweety.booksearchapp.data.db.AppDatabase
import com.tweety.booksearchapp.data.model.Book
import com.tweety.booksearchapp.data.model.SearchResponse
import com.tweety.booksearchapp.data.repository.BookSearchRepositoryImpl.PreferencesKeys.CACHE_DELETE_MODE
import com.tweety.booksearchapp.data.repository.BookSearchRepositoryImpl.PreferencesKeys.SORT_MODE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookSearchRepositoryImpl @Inject constructor(
    private val db: AppDatabase,
    private val dataStore: DataStore<Preferences>,
    private val api: BookSearchApi
) : BookSearchRepository {

    // network
    override suspend fun searchBooks(query: String, sort: String, page: Int, size: Int): Response<SearchResponse> {
        return api.searchBooks(query, sort, page, size)
    }

    // Room
    override suspend fun insertBooks(book: Book) {
        db.bookDao().insertBook(book)
    }

    override suspend fun deleteBooks(book: Book) {
        db.bookDao().deleteBook(book)
    }

    override fun getFavoriteBooks(): Flow<List<Book>> {
        return db.bookDao().getFavoriteBooks()
    }

    // DataStore
    private object PreferencesKeys {
        val SORT_MODE = stringPreferencesKey("sort_mode")
        val CACHE_DELETE_MODE = booleanPreferencesKey("cache_delete_mode")
    }

    override suspend fun saveSortMode(mode: String) {
        dataStore.edit { prefs ->
            prefs[SORT_MODE] = mode
        }

    }

    override suspend fun getSortMode(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    exception.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { prefs ->
                prefs[SORT_MODE] ?: Sort.ACCURACY.value
            }
    }

    override suspend fun saveCacheDeleteMode(mode: Boolean) {
        dataStore.edit { prefs ->
            prefs[CACHE_DELETE_MODE] = mode
        }
    }

    override suspend fun getCacheDeleteMode(): Flow<Boolean> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    exception.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { prefs ->
                prefs[CACHE_DELETE_MODE] ?: false
            }
    }

    // Paging
    override fun getFavoritePagingBooks(): Flow<PagingData<Book>> {
        val pagingSourceFactory = { db.bookDao().getFavoritePagingBooks() }

        return Pager(
            config = PagingConfig(
                pageSize = PAGING_SIZE,
                enablePlaceholders = false,
                maxSize = PAGING_SIZE * 3
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    override fun searchBooksPaging(query: String, sort: String): Flow<PagingData<Book>> {
        val pagingSourceFactory = { BookSearchPagingSource(api, query, sort) }

        return Pager(
            config = PagingConfig(
                pageSize = PAGING_SIZE,
                enablePlaceholders = false,
                maxSize = PAGING_SIZE * 3
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }
}