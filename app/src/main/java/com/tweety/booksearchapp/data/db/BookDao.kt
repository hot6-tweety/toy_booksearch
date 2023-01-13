package com.tweety.booksearchapp.data.db

import androidx.room.*
import com.tweety.booksearchapp.data.model.Book
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book)

    @Delete
    suspend fun deleteBook(book: Book)

    @Query("SELECT * FROM books")
    fun getFavoriteBooks(): Flow<List<Book>>
}