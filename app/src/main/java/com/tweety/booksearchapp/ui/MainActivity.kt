package com.tweety.booksearchapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.tweety.booksearchapp.R
import com.tweety.booksearchapp.data.repository.BookSearchRepositoryImpl
import com.tweety.booksearchapp.databinding.ActivityMainBinding
import com.tweety.booksearchapp.ui.viewmodel.BookSearchViewModel
import com.tweety.booksearchapp.ui.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    lateinit var bookSearchViewModel: BookSearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupBottomNavigation()

        val bookSearchRepository = BookSearchRepositoryImpl()
        val factory = ViewModelFactory(bookSearchRepository, this)
        bookSearchViewModel = ViewModelProvider(this, factory)[BookSearchViewModel::class.java]
    }

    private fun setupBottomNavigation() {
        val navController = supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment)?.findNavController()

        navController?.let {
            binding.bottomNavigationView.setupWithNavController(navController)
        }


    }
}