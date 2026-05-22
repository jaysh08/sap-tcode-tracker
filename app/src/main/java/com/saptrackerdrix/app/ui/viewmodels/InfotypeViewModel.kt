package com.saptrackerdrix.app.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.saptrackerdrix.app.data.model.Infotype
import com.saptrackerdrix.app.data.repository.DatabaseProvider
import com.saptrackerdrix.app.data.InfotypeData
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class InfotypeUiState(
    val infotypes: List<Infotype> = emptyList(),
    val searchQuery: String = "",
    val isSearching: Boolean = false,
    val showFavoritesOnly: Boolean = false,
    val isLoading: Boolean = true
)

class InfotypeViewModel(private val context: Context) : ViewModel() {
    private val database = DatabaseProvider.getInstance(context)
    private val dao = database.infotypeDao()
    
    private val _searchQuery = MutableStateFlow("")
    private val _showFavoritesOnly = MutableStateFlow(false)
    private val _isLoading = MutableStateFlow(true)
    
    val uiState: StateFlow<InfotypeUiState> = combine(
        _searchQuery,
        _showFavoritesOnly
    ) { query, favoritesOnly ->
        Pair(query, favoritesOnly)
    }.flatMapLatest { (query, favoritesOnly) ->
        when {
            query.isNotBlank() -> dao.searchInfotypes(query)
            favoritesOnly -> dao.getFavorites()
            else -> dao.getAllInfotypes()
        }.map { infotypes ->
            InfotypeUiState(
                infotypes = infotypes,
                searchQuery = query,
                isSearching = query.isNotBlank(),
                showFavoritesOnly = favoritesOnly,
                isLoading = false
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = InfotypeUiState()
    )
    
    init {
        viewModelScope.launch {
            // Check if infotypes are already seeded
            val count = dao.getCount()
            if (count == 0) {
                // Seed the database with infotypes
                dao.insertAll(InfotypeData.getAllInfotypes())
            }
            _isLoading.value = false
        }
    }
    
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    fun toggleFavoritesFilter() {
        _showFavoritesOnly.value = !_showFavoritesOnly.value
    }
    
    fun toggleFavorite(infotype: Infotype) {
        viewModelScope.launch {
            dao.updateFavorite(infotype.code, !infotype.isFavorite)
        }
    }
    
    fun clearSearch() {
        _searchQuery.value = ""
    }
}

class InfotypeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InfotypeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InfotypeViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}