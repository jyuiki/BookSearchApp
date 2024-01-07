package com.example.booksearchapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booksearchapp.data.model.Book
import com.example.booksearchapp.data.model.SearchResponse
import com.example.booksearchapp.data.repository.BookSearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookSearchViewModel(
    private val bookSearchRepository: BookSearchRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    //Api
    private val _searchResult = MutableLiveData<SearchResponse>()
    val searchResult: LiveData<SearchResponse> get() = _searchResult

    fun searchBooks(query: String) = viewModelScope.launch(Dispatchers.IO) {
        val response = bookSearchRepository.searchBooks(query, getSortMode(), 1, 15)
        if (response.isSuccessful) {
            response.body()?.let { body ->
                _searchResult.postValue(body)
            }
        }
    }

    // Room
    fun saveBook(book: Book) = viewModelScope.launch(Dispatchers.IO) {
        bookSearchRepository.insertBooks(book)
    }

    fun deleteBook(book: Book) = viewModelScope.launch(Dispatchers.IO) {
        bookSearchRepository.deleteBooks(book)
    }

    //    val favoriteBooks: Flow<List<ListBook>> = bookSearchRepository.getFavoriteBooks()
    val favoriteBooks: StateFlow<List<Book>> = bookSearchRepository.getFavoriteBooks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), listOf())

    var query = String()
        set(value) {
            field = value
            savedStateHandle[SAVE_STATE_KEY] = value
        }

    init {
        query = savedStateHandle.get<String>(SAVE_STATE_KEY) ?: ""
    }

    companion object {
        private const val SAVE_STATE_KEY = "query"
    }

    // DataStore
    fun saveSortMode(value:String) = viewModelScope.launch {
        bookSearchRepository.saveSortMode(value)
    }

    suspend fun getSortMode() = withContext(viewModelScope.coroutineContext) {
        bookSearchRepository.getSortMode().first()
    }


    /**
     * Datastore 관련해서 Preferences Datastore와  ProtoDatastore 를 사용할때
     * 함수를 호출할때  ui 스레드에서 호출해도 작업은 내부에서 Dispatcher.IO로 이동하여 처리하기 때문에
     *
     * 직접 withContext를 통해 Dispatchers.IO 로 변경해주지 않아도 될것같습니다
     *
     * 반환값이 필요없는 경우엔 그냥 viewModelScope.launch() {},
     * 반환값이 반드시 필요할경우엔 viewModelScope.async..await()
     * 혹은 withContext(viewModelScope.coroutineContext) 로 감싸서 호출하면 될거같습니다
     *
     * retrofit, room, datastore 까지 주로 쓰이는 네트워크 관련 작업이 모두 main-safe 되어있네요 ㄷ
     *
     * 참고)
     *
     * https://developer.android.com/codelabs/android-preferences-datastore#3
     */

}
