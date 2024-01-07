package com.example.booksearchapp.ui.view

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.WorkManager
import com.example.booksearchapp.R
import com.example.booksearchapp.data.db.BookSearchDatabase
import com.example.booksearchapp.data.repository.BookSearchRepositoryImpl
import com.example.booksearchapp.databinding.ActivityMainBinding
import com.example.booksearchapp.ui.viewmodel.BookSearchViewModel
import com.example.booksearchapp.ui.viewmodel.BookSearchViewModelProviderFactory
import com.example.booksearchapp.util.Constants.DATASTORE_NAME

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var bookSearchViewModel: BookSearchViewModel

    // Error
//    private val bookSearchViewModel: BookSearchViewModel by lazy {
//        val database = BookSearchDatabase.getInstance(this)
//        val bookSearchRepository = BookSearchRepositoryImpl(database)
//        ViewModelProvider(this@MainActivity, object : AbstractSavedStateViewModelFactory(this@MainActivity, null) {
//            override fun <T : ViewModel> create(
//                key: String,
//                modelClass: Class<T>,
//                handle: SavedStateHandle
//            ): T {
//                if (modelClass.isAssignableFrom(BookSearchViewModel::class.java)) {
//                    return BookSearchViewModel(bookSearchRepository, handle) as T
//                }
//                throw IllegalArgumentException("ViewModel class not found")
//            }
//        })[BookSearchViewModel::class.java]
//    }

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val Context.dataStore by preferencesDataStore(DATASTORE_NAME)
    private val workManager = WorkManager.getInstance(application)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//        setupBottomNavigationView()
//        if (savedInstanceState == null) {
//            binding.bottomNavigationView.selectedItemId = R.id.fragment_search
//        }

        setupJetPackNavigation()

        val database = BookSearchDatabase.getInstance(this)
        val bookSearchRepository = BookSearchRepositoryImpl(database, dataStore)
        val factory = BookSearchViewModelProviderFactory(bookSearchRepository, workManager, this)
        bookSearchViewModel = ViewModelProvider(this, factory)[BookSearchViewModel::class.java]
    }

    private fun setupJetPackNavigation() {
        val host = supportFragmentManager
            .findFragmentById(R.id.booksearch_nav_host_fragment) as NavHostFragment ?: return
        navController = host.navController
        binding.bottomNavigationView.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.fragment_search, R.id.fragment_favorite, R.id.fragment_settings)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

//    private fun setupBottomNavigationView() {
//        binding.bottomNavigationView.setOnItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.fragment_search -> {
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.frame_layout, SearchFragment())
//                        .commit()
//                    true
//                }
//
//                R.id.fragment_favorite -> {
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.frame_layout, FavoriteFragment())
//                        .commit()
//                    true
//                }
//
//                R.id.fragment_settings -> {
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.frame_layout, SettingsFragment())
//                        .commit()
//                    true
//                }
//
//                else -> false
//            }
//        }
//    }
}
