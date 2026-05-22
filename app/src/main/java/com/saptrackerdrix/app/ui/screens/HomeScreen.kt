package com.saptrackerdrix.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.saptrackerdrix.app.data.model.TCode
import com.saptrackerdrix.app.ui.components.*
import com.saptrackerdrix.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
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
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedTCode by remember { mutableStateOf<TCode?>(null) }
    
    // FAB animation
    var fabVisible by remember { mutableStateOf(true) }
    val fabScale by animateFloatAsState(
        targetValue = if (fabVisible) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "fabScale"
    )
    
    // Track total count for animation
    var previousCount by remember { mutableIntStateOf(tcodes.size) }
    val countChanged = tcodes.size != previousCount
    if (countChanged) {
        previousCount = tcodes.size
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.background.copy(alpha = 0.95f)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            HomeHeader()
            
            // Search bar
            SearchBar(
                query = searchQuery,
                onQueryChange = onSearchQueryChange,
                onClearQuery = onClearSearch,
                isFavoritesOnly = showFavoritesOnly,
                onToggleFavorites = onToggleFavorites,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            
            // Content
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                if (tcodes.isEmpty()) {
                    EmptyState(
                        isSearching = isSearching,
                        showFavoritesOnly = showFavoritesOnly,
                        onAddClick = { showAddDialog = true }
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Stats header
                        item {
                            StatsHeader(
                                count = tcodes.size,
                                isSearching = isSearching,
                                searchQuery = searchQuery,
                                showFavoritesOnly = showFavoritesOnly
                            )
                        }
                        
                        // TCode cards with animations
                        items(
                            items = tcodes,
                            key = { it.id }
                        ) { tCode ->
                            val isHighlighted = tCode.id == recentlyAddedId || tCode.id == recentlySearchedId
                            
                            TCodeCard(
                                tCode = tCode,
                                isHighlighted = isHighlighted,
                                isRecentlyAdded = tCode.id == recentlyAddedId,
                                isRecentlySearched = tCode.id == recentlySearchedId,
                                onTCodeClick = {
                                    if (isSearching || showFavoritesOnly) {
                                        // Could highlight searched item
                                    }
                                },
                                onFavoriteClick = { onToggleFavorite(tCode) },
                                onDeleteClick = { onDeleteTCode(tCode) }
                            )
                        }
                        
                        // Bottom spacing for FAB
                        item {
                            Spacer(modifier = Modifier.height(80.dp))
                        }
                    }
                }
            }
        }
        
        // Animated FAB
        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
                .scale(fabScale),
            containerColor = SAPBlue,
            contentColor = Color.White,
            shape = CircleShape
        ) {
            val infiniteTransition = rememberInfiniteTransition(label = "fab")
            val rotation by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(4000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "fabRotation"
            )
            
            // Pulse effect
            val pulseScale by infiniteTransition.animateFloat(
                initialValue = 1f,
                targetValue = 1.15f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1500, easing = EaseInOutSine),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "pulse"
            )
            
            Box(
                modifier = Modifier
                    .scale(pulseScale)
                    .size(56.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add TCode",
                    modifier = Modifier.size(28.dp)
                )
            }
        }
        
        // Add dialog
        if (showAddDialog) {
            AddTCodeDialog(
                onDismiss = { showAddDialog = false },
                onAddTCode = { code, purpose, module, notes ->
                    onAddTCode(code, purpose, module, notes)
                    showAddDialog = false
                }
            )
        }
    }
}

@Composable
fun HomeHeader() {
    Text(
        text = "T-Codes",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
fun StatsHeader(
    count: Int,
    isSearching: Boolean,
    searchQuery: String,
    showFavoritesOnly: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            AnimatedContent(
                targetState = count,
                transitionSpec = {
                    if (targetState > initialState) {
                        (slideInVertically { it } + fadeIn()) togetherWith
                                (slideOutVertically { -it } + fadeOut())
                    } else {
                        (slideInVertically { -it } + fadeIn()) togetherWith
                                (slideOutVertically { it } + fadeOut())
                    }
                },
                label = "countAnimation"
            ) { count ->
                Text(
                    text = "$count TCode${if (count != 1) "s" else ""}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            
            AnimatedVisibility(
                visible = isSearching || showFavoritesOnly,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    text = when {
                        isSearching -> "Searching: \"$searchQuery\""
                        showFavoritesOnly -> "Showing favorites"
                        else -> ""
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun EmptyState(
    isSearching: Boolean,
    showFavoritesOnly: Boolean,
    onAddClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "empty")
    val floatAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float"
    )
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.offset(y = floatAnim.dp)
        ) {
            Text(
                text = when {
                    isSearching -> "🔍"
                    showFavoritesOnly -> "⭐"
                    else -> "📚"
                },
                style = MaterialTheme.typography.displayLarge
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = when {
                isSearching -> "No results found"
                showFavoritesOnly -> "No favorites yet"
                else -> "Start Your SAP Journey!"
            },
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Text(
            text = when {
                isSearching -> "Try a different search term"
                showFavoritesOnly -> "Tap the ⭐ on tcodes to add favorites"
                else -> "Add your first TCode to begin tracking\nyour SAP learning progress"
            },
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        if (!isSearching && !showFavoritesOnly) {
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = onAddClick,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SAPBlue)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Add First TCode",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}