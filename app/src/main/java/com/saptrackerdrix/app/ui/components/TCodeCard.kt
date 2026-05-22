package com.saptrackerdrix.app.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.saptrackerdrix.app.data.model.TCode
import com.saptrackerdrix.app.ui.theme.*
import java.util.*

@Composable
fun TCodeCard(
    tCode: TCode,
    isHighlighted: Boolean,
    isRecentlyAdded: Boolean,
    isRecentlySearched: Boolean,
    onTCodeClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isVisible by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    
    LaunchedEffect(tCode.id) {
        isVisible = true
    }
    
    val gradient = remember(tCode.id) {
        gradients[tCode.id.hashCode().mod(gradients.size).let { if (it < 0) it + gradients.size else it }]
    }
    
    // Entry animation for added/searched cards
    val entryScale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "entryScale"
    )
    
    // Pulse animation for highlighted cards
    val pulseScale by rememberInfiniteTransition(label = "pulse").animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )
    
    val scale = if (isHighlighted || isRecentlyAdded || isRecentlySearched) pulseScale else entryScale
    
    // Shake animation for recently searched
    val shakeOffset by animateFloatAsState(
        targetValue = if (isRecentlySearched) 0f else 0f,
        animationSpec = keyframes {
            durationMillis = 500
            0f at 0
            -10f at 100
            10f at 200
            -10f at 300
            10f at 400
            0f at 500
        },
        label = "shake"
    )
    
    // Celebration particles effect indicator
    val glowAlpha by animateFloatAsState(
        targetValue = if (isRecentlyAdded || isRecentlySearched) 0.6f else 0f,
        animationSpec = tween(500),
        label = "glow"
    )
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale)
            .offset(x = shakeOffset.dp)
            .shadow(
                elevation = if (isHighlighted) 16.dp else 8.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = gradient[0].copy(alpha = glowAlpha)
            )
            .clickable { onTCodeClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(gradient),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(16.dp)
        ) {
            Column(Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // TCode badge
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = tCode.code,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                    
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        // Favorite button with bounce animation
                        var favoriteAnimating by remember { mutableStateOf(false) }
                        IconButton(
                            onClick = {
                                favoriteAnimating = true
                                onFavoriteClick()
                            }
                        ) {
                            Icon(
                                imageVector = if (tCode.favorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (tCode.favorite) SAPRed else Color.White.copy(alpha = 0.7f),
                                modifier = Modifier
                                    .size(28.dp)
                                    .then(
                                        if (favoriteAnimating) Modifier.scale(1.3f) else Modifier
                                    )
                            )
                        }
                        
                        // Delete button
                        IconButton(onClick = { showDeleteConfirm = true }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color.White.copy(alpha = 0.7f),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = tCode.purpose,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                if (tCode.module.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = tCode.module.uppercase(),
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White.copy(alpha = 0.9f),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
                
                if (tCode.notes.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = tCode.notes,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                
                // New badge for recently added
                if (isRecentlyAdded) {
                    Spacer(modifier = Modifier.height(8.dp))
                    NewBadge()
                }
            }
        }
    }
    
    // Delete confirmation dialog
    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Delete TCode?", color = MaterialTheme.colorScheme.onSurface) },
            text = { Text("Are you sure you want to delete ${tCode.code}?", color = MaterialTheme.colorScheme.onSurfaceVariant) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteConfirm = false
                        onDeleteClick()
                    }
                ) {
                    Text("Delete", color = SAPRed)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun NewBadge() {
    val infiniteTransition = rememberInfiniteTransition(label = "badge")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )
    
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color.White.copy(alpha = alpha * 0.9f)
    ) {
        Text(
            text = "✨ NEW!",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = SAPPurple,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}