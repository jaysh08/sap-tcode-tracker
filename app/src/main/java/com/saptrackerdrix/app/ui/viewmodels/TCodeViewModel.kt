package com.saptrackerdrix.app.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.saptrackerdrix.app.data.TCodeData
import com.saptrackerdrix.app.data.model.TCode
import com.saptrackerdrix.app.data.repository.DatabaseProvider
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class TCodeUiState(
    val tcodes: List<TCode> = emptyList(),
    val searchQuery: String = "",
    val isSearching: Boolean = false,
    val showFavoritesOnly: Boolean = false,
    val recentlyAddedId: String? = null,
    val recentlySearchedId: String? = null,
    val isLoading: Boolean = true
)

class TCodeViewModel(private val context: Context) : ViewModel() {
    private val database = DatabaseProvider.getInstance(context)
    private val dao = database.tCodeDao()
    
    private val _searchQuery = MutableStateFlow("")
    private val _showFavoritesOnly = MutableStateFlow(false)
    private val _recentlyAddedId = MutableStateFlow<String?>(null)
    private val _recentlySearchedId = MutableStateFlow<String?>(null)
    private val _isLoading = MutableStateFlow(true)
    
    val uiState: StateFlow<TCodeUiState> = combine(
        _searchQuery,
        _showFavoritesOnly,
        _recentlyAddedId,
        _recentlySearchedId
    ) { query, favoritesOnly, addedId, searchedId ->
        Triple(Triple(query, favoritesOnly, addedId), searchedId, Unit)
    }.flatMapLatest { (params, searchedId, _) ->
        val (query, favoritesOnly, addedId) = params
        when {
            query.isNotBlank() -> dao.searchTCodes(query)
            favoritesOnly -> dao.getFavorites()
            else -> dao.getAllTCodes()
        }.map { tcodes ->
            TCodeUiState(
                tcodes = tcodes,
                searchQuery = query,
                isSearching = query.isNotBlank(),
                showFavoritesOnly = favoritesOnly,
                recentlyAddedId = addedId,
                recentlySearchedId = searchedId,
                isLoading = false
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TCodeUiState()
    )
    
    init {
        viewModelScope.launch {
            try {
                // Check if tcodes are already seeded
                val count = dao.getCount()
                if (count == 0) {
                    // Seed the database with HCM T-Codes
                    TCodeData.getAllTCodes().forEach { tCode ->
                        dao.insertTCode(tCode)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    fun toggleFavoritesFilter() {
        _showFavoritesOnly.value = !_showFavoritesOnly.value
        _searchQuery.value = ""
    }
    
    fun addTCode(code: String, purpose: String, module: String = "", notes: String = "") {
        viewModelScope.launch {
            val tCode = TCode(
                code = code.uppercase(),
                purpose = purpose,
                module = module,
                notes = notes
            )
            dao.insertTCode(tCode)
            _recentlyAddedId.value = tCode.id
        }
    }
    
    fun updateTCode(tCode: TCode) {
        viewModelScope.launch {
            dao.updateTCode(tCode)
        }
    }
    
    fun deleteTCode(tCode: TCode) {
        viewModelScope.launch {
            dao.deleteTCode(tCode)
        }
    }
    
    fun toggleFavorite(tCode: TCode) {
        viewModelScope.launch {
            dao.updateTCode(tCode.copy(favorite = !tCode.favorite))
        }
    }
    
    fun setRecentlyAddedId(id: String?) {
        _recentlyAddedId.value = id
    }
    
    fun setRecentlySearchedId(id: String?) {
        _recentlySearchedId.value = id
    }
    
    fun clearSearch() {
        _searchQuery.value = ""
    }
}

class TCodeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TCodeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TCodeViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}