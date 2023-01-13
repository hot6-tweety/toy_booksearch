package com.tweety.booksearchapp.ui

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tweety.booksearchapp.common.Constants.SEARCH_BOOKS_TIME_DELAY
import com.tweety.booksearchapp.common.collectLatestStateFlow
import com.tweety.booksearchapp.databinding.FragmentSearchBinding
import com.tweety.booksearchapp.ui.adapter.BookSearchLoadStateAdapter
import com.tweety.booksearchapp.ui.adapter.BookSearchPagingAdapter
import com.tweety.booksearchapp.ui.viewmodel.BookSearchViewModel

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var bookSearchViewModel: BookSearchViewModel
    private lateinit var bookSearchAdapter: BookSearchPagingAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bookSearchViewModel = (activity as MainActivity).bookSearchViewModel

        setupRecycleView()
        searchBooks()
        setupLoadState()

        collectLatestStateFlow(bookSearchViewModel.searchPagingResult) {
            bookSearchAdapter.submitData(it)
        }
    }

    private fun setupRecycleView() {
        bookSearchAdapter = BookSearchPagingAdapter()
        binding.rvSearchResult.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            adapter = bookSearchAdapter.withLoadStateFooter(
                footer = BookSearchLoadStateAdapter(bookSearchAdapter::retry)
            )
        }
        bookSearchAdapter.setOnItemClickListener {
            val action = SearchFragmentDirections.actionNavigationSearchToNavigationBook(it)
            findNavController().navigate(action)
        }
    }

    private fun searchBooks() {
        var startTime = System.currentTimeMillis()
        var endTime: Long

        // SaveState 로 저장한 텍스트를 불러옴
        binding.etSearch.text = Editable.Factory.getInstance().newEditable(bookSearchViewModel.query)


        binding.etSearch.addTextChangedListener { text: Editable? ->
            endTime = System.currentTimeMillis()
            // EditText에 Text 입력 시 딜레이를 주어 검색
            if (endTime - startTime >= SEARCH_BOOKS_TIME_DELAY) {
                text?.let {
                    val query = it.toString().trim()
                    if (query.isNotEmpty()) {
                        bookSearchViewModel.searchBooksPaging(query)
                        bookSearchViewModel.query = query
                    }
                }
            }
            startTime = endTime
        }
    }

    private fun setupLoadState() {
        bookSearchAdapter.addLoadStateListener { combinedLoadStates ->
            val loadState = combinedLoadStates.source
            val isListEmpty = bookSearchAdapter.itemCount < 1
                    && loadState.refresh is LoadState.NotLoading
                    && loadState.append.endOfPaginationReached

            binding.tvEmptyResult.isVisible = isListEmpty
            binding.rvSearchResult.isVisible = !isListEmpty

            binding.progressBar.isVisible = loadState.refresh is LoadState.Loading

//            binding.btnRetry.isVisible = loadState.refresh is LoadState.Error
//                    || loadState.append is LoadState.Error
//                    || loadState.prepend is LoadState.Error
//            val errorState: LoadState.Error? = loadState.append as? LoadState.Error
//                ?: loadState.prepend as? LoadState.Error
//                ?: loadState.refresh as? LoadState.Error
//            errorState?.let {
//                Toast.makeText(requireContext(), it.error.message, Toast.LENGTH_SHORT).show()
//            }
        }

//        binding.btnRetry.setOnClickListener {
//            bookSearchAdapter.retry()
//        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}