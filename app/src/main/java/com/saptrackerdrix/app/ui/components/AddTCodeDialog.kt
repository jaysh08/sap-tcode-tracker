package com.saptrackerdrix.app.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.saptrackerdrix.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTCodeDialog(
    onDismiss: () -> Unit,
    onAddTCode: (code: String, purpose: String, module: String, notes: String) -> Unit
) {
    var code by remember { mutableStateOf("") }
    var purpose by remember { mutableStateOf("") }
    var module by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    
    val focusManager = LocalFocusManager.current
    
    // Success animation state
    var showSuccess by remember { mutableStateOf(false) }
    
    // Shake animation for validation
    var shakeEnabled by remember { mutableStateOf(false) }
    val shakeOffset by animateFloatAsState(
        targetValue = if (shakeEnabled) 10f else 0f,
        animationSpec = keyframes {
            durationMillis = 400
            0f at 0
            -10f at 100
            10f at 200
            -10f at 300
            0f at 400
        },
        label = "shake"
    )
    
    // Button animation
    val buttonScale by animateFloatAsState(
        targetValue = if (code.isNotBlank() && purpose.isNotBlank()) 1f else 0.95f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "buttonScale"
    )
    
    // Success animation
    val successScale by animateFloatAsState(
        targetValue = if (showSuccess) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "successScale"
    )
    
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .offset(x = shakeOffset.dp)
                .shadow(24.dp, RoundedCornerShape(28.dp)),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                SAPBlueDark.copy(alpha = 0.1f),
                                Color.Transparent
                            )
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Header with gradient
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.horizontalGradient(Gradient1),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Animated plus icon
                                val infiniteTransition = rememberInfiniteTransition(label = "plus")
                                val rotation by infiniteTransition.animateFloat(
                                    initialValue = 0f,
                                    targetValue = 360f,
                                    animationSpec = infiniteRepeatable(
                                        animation = tween(3000, easing = LinearEasing),
                                        repeatMode = RepeatMode.Restart
                                    ),
                                    label = "rotation"
                                )
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier
                                            .size(28.dp)
                                            .then(
                                                if (!showSuccess) Modifier.scale(1f) else Modifier.scale(0f)
                                            )
                                    )
                                }
                                
                                // Success checkmark
                                AnimatedVisibility(
                                    visible = showSuccess,
                                    enter = scaleIn(spring(dampingRatio = Spring.DampingRatioMediumBouncy)) + fadeIn()
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "✨",
                                            style = MaterialTheme.typography.titleLarge
                                        )
                                    }
                                }
                                
                                Column {
                                    Text(
                                        text = "Add New",
                                        style = MaterialTheme.typography.headlineSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "SAP TCode",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.White.copy(alpha = 0.8f)
                                    )
                                }
                            }
                            
                            IconButton(onClick = onDismiss) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // TCode input
                    OutlinedTextField(
                        value = code,
                        onValueChange = { code = it.uppercase().filter { c -> !c.isWhitespace() } },
                        label = { Text("TCode") },
                        placeholder = { Text("e.g. SE11, MB03, VA01") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SAPBlue,
                            focusedLabelColor = SAPBlue,
                        ),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Characters,
                            imeAction = ImeAction.Next
                        ),
                        leadingIcon = {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = SAPBlue.copy(alpha = 0.1f)
                            ) {
                                Text(
                                    text = "TX",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = SAPBlue,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Purpose input
                    OutlinedTextField(
                        value = purpose,
                        onValueChange = { purpose = it },
                        label = { Text("Purpose") },
                        placeholder = { Text("What does this TCode do?") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SAPOrange,
                            focusedLabelColor = SAPOrange,
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        leadingIcon = {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = SAPOrange.copy(alpha = 0.1f)
                            ) {
                                Text(
                                    text = "📝",
                                    style = MaterialTheme.typography.labelMedium,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Module input
                    OutlinedTextField(
                        value = module,
                        onValueChange = { module = it },
                        label = { Text("Module (optional)") },
                        placeholder = { Text("e.g. MM, SD, FI, ABAP") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SAPGreen,
                            focusedLabelColor = SAPGreen,
                        ),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Characters,
                            imeAction = ImeAction.Next
                        ),
                        leadingIcon = {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = SAPGreen.copy(alpha = 0.1f)
                            ) {
                                Text(
                                    text = "📦",
                                    style = MaterialTheme.typography.labelMedium,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Notes input
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Notes (optional)") },
                        placeholder = { Text("Any additional notes...") },
                        maxLines = 3,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SAPPurple,
                            focusedLabelColor = SAPPurple,
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                        leadingIcon = {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = SAPPurple.copy(alpha = 0.1f)
                            ) {
                                Text(
                                    text = "💡",
                                    style = MaterialTheme.typography.labelMedium,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(28.dp))
                    
                    // Add button with animation
                    Button(
                        onClick = {
                            if (code.isBlank() || purpose.isBlank()) {
                                shakeEnabled = true
                                return@Button
                            }
                            focusManager.clearFocus()
                            onAddTCode(code, purpose, module, notes)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .scale(buttonScale),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SAPBlue)
                    ) {
                        AnimatedVisibility(
                            visible = !showSuccess,
                            enter = fadeIn() + scaleIn(),
                            exit = fadeOut() + scaleOut()
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "Add to Collection",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                        
                        AnimatedVisibility(
                            visible = showSuccess,
                            enter = fadeIn() + scaleIn(),
                            exit = fadeOut() + scaleOut()
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "✨",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = "Added!",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}