package com.saptrackerdrix.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.saptrackerdrix.app.ui.screens.HomeScreen
import com.saptrackerdrix.app.ui.theme.SAPTcodeTrackerTheme
import com.saptrackerdrix.app.ui.viewmodels.TCodeViewModel
import com.saptrackerdrix.app.ui.viewmodels.TCodeViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            SAPTcodeTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: TCodeViewModel = viewModel(
                        factory = TCodeViewModelFactory(applicationContext)
                    )
                    
                    val uiState by viewModel.uiState.collectAsState()
                    
                    HomeScreen(
                        tcodes = uiState.tcodes,
                        searchQuery = uiState.searchQuery,
                        isSearching = uiState.isSearching,
                        showFavoritesOnly = uiState.showFavoritesOnly,
                        recentlyAddedId = uiState.recentlyAddedId,
                        recentlySearchedId = uiState.recentlySearchedId,
                        onSearchQueryChange = viewModel::updateSearchQuery,
                        onClearSearch = viewModel::clearSearch,
                        onToggleFavorites = viewModel::toggleFavoritesFilter,
                        onAddTCode = viewModel::addTCode,
                        onDeleteTCode = viewModel::deleteTCode,
                        onToggleFavorite = viewModel::toggleFavorite
                    )
                }
            }
        }
    }
}