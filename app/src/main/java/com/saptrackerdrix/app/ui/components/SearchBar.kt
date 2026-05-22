package com.saptrackerdrix.app.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.saptrackerdrix.app.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClearQuery: () -> Unit,
    isFavoritesOnly: Boolean,
    onToggleFavorites: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    
    var localQuery by remember { mutableStateOf(query) }
    
    LaunchedEffect(query) {
        localQuery = query
    }
    
    val infiniteTransition = rememberInfiniteTransition(label = "searchIcon")
    val searchIconScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "searchIconScale"
    )
    
    var favoritesAnimating by remember { mutableStateOf(false) }
    val favoritesScale by animateFloatAsState(
        targetValue = if (favoritesAnimating) 1.3f else if (isFavoritesOnly) 1.1f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label = "favoritesScale"
    )
    
    LaunchedEffect(favoritesAnimating) {
        if (favoritesAnimating) {
            delay(300)
            favoritesAnimating = false
        }
    }
    
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(
                            MaterialTheme.colorScheme.surface,
                            if (isFavoritesOnly) SAPYellow.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface
                        )
                    )
                )
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = if (localQuery.isNotBlank()) SAPBlue else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp).then(if (localQuery.isNotBlank()) Modifier.scale(searchIconScale) else Modifier)
            )
            
            OutlinedTextField(
                value = localQuery,
                onValueChange = { newValue ->
                    localQuery = newValue
                    onQueryChange(newValue)
                },
                placeholder = {
                    Text(
                        text = "Search tcodes or purpose...",
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                },
                singleLine = true,
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = SAPBlue,
                    unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                    cursorColor = SAPBlue
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() })
            )
            
            AnimatedVisibility(visible = localQuery.isNotBlank(), enter = scaleIn() + fadeIn(), exit = scaleOut() + fadeOut()) {
                IconButton(onClick = { localQuery = ""; onClearQuery() }, modifier = Modifier.size(32.dp)) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Clear", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(20.dp))
                }
            }
            
            IconButton(onClick = { favoritesAnimating = true; onToggleFavorites() }, modifier = Modifier.scale(favoritesScale)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = if (isFavoritesOnly) Icons.Filled.Star else Icons.Outlined.Star,
                        contentDescription = "Favorites",
                        tint = if (isFavoritesOnly) SAPYellow else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(24.dp)
                    )
                    AnimatedVisibility(visible = isFavoritesOnly, enter = fadeIn(), exit = fadeOut()) {
                        Text(text = "Favs", style = MaterialTheme.typography.labelSmall, color = SAPYellow, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}