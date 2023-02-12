package com.tweety.booksearchapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tweety.booksearchapp.common.collectLatestStateFlow
import com.tweety.booksearchapp.databinding.FragmentFavoriteBinding
import com.tweety.booksearchapp.ui.adapter.BookSearchPagingAdapter
import com.tweety.booksearchapp.ui.viewmodel.BookSearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    //    private lateinit var bookSearchViewModel: BookSearchViewModel
    private val bookSearchViewModel: BookSearchViewModel by activityViewModels<BookSearchViewModel>()
    private lateinit var bookSearchAdapter: BookSearchPagingAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        bookSearchViewModel = (activity as MainActivity).bookSearchViewModel

        setupRecycleView()
        setupTouchHelper(view)

        // FavoriteFragment 라이프사이클과 Flow 구독시작 연동
        collectLatestStateFlow(bookSearchViewModel.favoritePagingBooks) {
            bookSearchAdapter.submitData(it)
        }

    }

    private fun setupRecycleView() {
        bookSearchAdapter = BookSearchPagingAdapter()
        binding.rvFavoriteBooks.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            adapter = bookSearchAdapter
        }
        bookSearchAdapter.setOnItemClickListener {
            val action = FavoriteFragmentDirections.actionNavigationFavoriteToNavigationBook(it)
            findNavController().navigate(action)
        }
    }

    // 왼쪽 스와이프 시 추가된 데이터 삭제
    private fun setupTouchHelper(view: View) {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val pagedBook = bookSearchAdapter.peek(position)
                pagedBook?.let { book ->
                    bookSearchViewModel.deleteBook(book)
                    Snackbar.make(view, "Book has deleted", Snackbar.LENGTH_SHORT).apply {
                        setAction("Undo") {
                            bookSearchViewModel.saveBook(book)
                        }
                    }.show()

                }
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvFavoriteBooks)
        }
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}