package com.tweety.booksearchapp.ui.viewmodel

import androidx.lifecycle.*
import com.tweety.booksearchapp.data.repository.BookSearchRepository
import com.tweety.booksearchapp.model.SearchResponse
import kotlinx.coroutines.launch

class BookSearchViewModel(
    private val bookSearchRepository: BookSearchRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

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

    // SavedState
    var query = String()
        set(value) {
            field = value
            savedStateHandle.set(SAVE_STATE_KEY, value)
        }

    // ViewModel 초기화 시 사용자가 입력한 텍스트를 확인하고 없으면 공백 할당
    init {
        query = savedStateHandle.get<String>(SAVE_STATE_KEY) ?: ""
    }

    companion object {
        private const val SAVE_STATE_KEY = "query"
    }
}