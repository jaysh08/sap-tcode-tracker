package com.saptrackerdrix.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.saptrackerdrix.app.data.model.Infotype
import com.saptrackerdrix.app.data.model.TCode
import com.saptrackerdrix.app.ui.viewmodels.InfotypeViewModel
import com.saptrackerdrix.app.ui.viewmodels.InfotypeViewModelFactory
import com.saptrackerdrix.app.ui.viewmodels.TCodeViewModel
import com.saptrackerdrix.app.ui.theme.SAPYellow

data class TabItem(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    tcodes: List<TCode>,
    searchQuery: String,
    isSearching: Boolean,
    showFavoritesOnly: Boolean,
    recentlyAddedId: String?,
    recentlySearchedId: String?,
    onSearchQueryChange: (String) -> Unit,
    onClearSearch: () -> Unit,
    onToggleFavorites: () -> Unit,
    onAddTCode: (String, String, String, String) -> Unit,
    onDeleteTCode: (TCode) -> Unit,
    onToggleFavorite: (TCode) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    
    val tabs = listOf(
        TabItem("T-Codes", Icons.Default.Search),
        TabItem("Infotypes", Icons.Default.List)
    )
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "SAP Tracker 🐱",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                tabs.forEachIndexed { index, tab ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp),
                                    tint = if (selectedTab == index) SAPYellow else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = tab.title,
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                    )
                }
            }
            
            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = {
                    slideInHorizontally { it } + fadeIn() togetherWith slideOutHorizontally { -it } + fadeOut()
                },
                label = "tabContent"
            ) { tab ->
                when (tab) {
                    0 -> TCodeTabContent(
                        tcodes = tcodes,
                        searchQuery = searchQuery,
                        isSearching = isSearching,
                        showFavoritesOnly = showFavoritesOnly,
                        recentlyAddedId = recentlyAddedId,
                        recentlySearchedId = recentlySearchedId,
                        onSearchQueryChange = onSearchQueryChange,
                        onClearSearch = onClearSearch,
                        onToggleFavorites = onToggleFavorites,
                        onAddTCode = onAddTCode,
                        onDeleteTCode = onDeleteTCode,
                        onToggleFavorite = onToggleFavorite
                    )
                    1 -> InfotypeTabContent()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TCodeTabContent(
    tcodes: List<TCode>,
    searchQuery: String,
    isSearching: Boolean,
    showFavoritesOnly: Boolean,
    recentlyAddedId: String?,
    recentlySearchedId: String?,
    onSearchQueryChange: (String) -> Unit,
    onClearSearch: () -> Unit,
    onToggleFavorites: () -> Unit,
    onAddTCode: (String, String, String, String) -> Unit,
    onDeleteTCode: (TCode) -> Unit,
    onToggleFavorite: (TCode) -> Unit
) {
    HomeScreen(
        tcodes = tcodes,
        searchQuery = searchQuery,
        isSearching = isSearching,
        showFavoritesOnly = showFavoritesOnly,
        recentlyAddedId = recentlyAddedId,
        recentlySearchedId = recentlySearchedId,
        onSearchQueryChange = onSearchQueryChange,
        onClearSearch = onClearSearch,
        onToggleFavorites = onToggleFavorites,
        onAddTCode = onAddTCode,
        onDeleteTCode = onDeleteTCode,
        onToggleFavorite = onToggleFavorite
    )
}

@Composable
fun InfotypeTabContent() {
    val viewModel: InfotypeViewModel = viewModel(
        factory = InfotypeViewModelFactory(androidx.compose.ui.platform.LocalContext.current)
    )
    
    val uiState by viewModel.uiState.collectAsState()
    
    InfotypeScreen(
        infotypes = uiState.infotypes,
        searchQuery = uiState.searchQuery,
        isSearching = uiState.isSearching,
        showFavoritesOnly = uiState.showFavoritesOnly,
        isLoading = uiState.isLoading,
        onSearchQueryChange = viewModel::updateSearchQuery,
        onClearSearch = viewModel::clearSearch,
        onToggleFavorites = viewModel::toggleFavoritesFilter,
        onToggleFavorite = viewModel::toggleFavorite
    )
}