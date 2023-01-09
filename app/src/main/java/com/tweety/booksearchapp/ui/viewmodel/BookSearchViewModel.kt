package com.tweety.booksearchapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tweety.booksearchapp.data.repository.BookSearchRepository
import com.tweety.booksearchapp.model.SearchResponse
import kotlinx.coroutines.launch

class BookSearchViewModel(private val bookSearchRepository: BookSearchRepository) : ViewModel() {

    private val _searchResult = MutableLiveData<SearchResponse>()
    val searchResult: LiveData<SearchResponse> get() = _searchResult

    // 리포지토리에 있는 searchBooks 함수를 코루틴 스코프 안에서 호출
    fun searchBooks(query: String) = viewModelScope.launch {
        val response = bookSearchRepository.searchBooks(query, "accuracy", 1, 15)
        if (response.isSuccessful) {
            response.body()?.let { body ->
                _searchResult.postValue(body)
            }
        }
    }
}