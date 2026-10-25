package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.data.ProfileManager
import com.example.data.UserProfile
import com.example.ui.components.AnimeGlowCyan
import com.example.ui.components.AnimeGlowGold
import com.example.ui.components.AnimeGlowPink
import com.example.ui.components.GlassCard
import java.io.File

import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ProfileScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val context = LocalContext.current
    val profileManager = remember { ProfileManager.getInstance(context) }
    val userProfile by profileManager.profileState.collectAsStateWithLifecycle()
    var showChangeNameDialog by remember { mutableStateOf(false) }

    // Modern Android Photo Picker (zero broad permissions needed)
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            profileManager.saveProfileImageFromUri(uri)
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 600.dp)
                .padding(
                    top = contentPadding.calculateTopPadding() + 12.dp,
                    bottom = contentPadding.calculateBottomPadding() + 24.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Bar with Back navigation
            ProfileHeaderBanner(onBackClick = onBackClick)

            Spacer(modifier = Modifier.height(20.dp))

            // Main Glassmorphism Profile Container
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 12.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 1. Profile Picture / Logo
                    ProfileAvatarSection(
                        profile = userProfile,
                        onChoosePictureClick = {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Display Name
                    Text(
                        text = userProfile.name,
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.testTag("profile_display_name_text")
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "STUDYPAL Scholar",
                        color = AnimeGlowGold,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(26.dp))

                    // 2. Change Name Option
                    ChangeNameButton(
                        onClick = { showChangeNameDialog = true }
                    )
                }
            }
        }

        // Change Name Dialog
        if (showChangeNameDialog) {
            ChangeNameDialog(
                currentName = userProfile.name,
                onDismiss = { showChangeNameDialog = false },
                onSave = { newName ->
                    if (profileManager.saveName(newName)) {
                        showChangeNameDialog = false
                    }
                }
            )
        }
    }
}

@Composable
private fun ProfileHeaderBanner(onBackClick: () -> Unit) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.12f))
                    .border(1.dp, Color.White.copy(alpha = 0.25f), CircleShape)
                    .testTag("profile_back_btn")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Back to Home",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Text(
                    text = "Profile",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Manage your student picture and name",
                    color = Color.White.copy(alpha = 0.75f),
                    fontSize = 11.5.sp
                )
            }
        }
    }
}

@Composable
private fun ProfileAvatarSection(
    profile: UserProfile,
    onChoosePictureClick: () -> Unit
) {
    val hasCustomImage = profile.imagePath != null && File(profile.imagePath).exists()

    Box(
        modifier = Modifier
            .size(136.dp)
            .clickable(onClick = onChoosePictureClick)
            .testTag("profile_avatar_container"),
        contentAlignment = Alignment.Center
    ) {
        // Outer glow & border ring
        Box(
            modifier = Modifier
                .size(136.dp)
                .clip(CircleShape)
                .background(
                    Brush.sweepGradient(
                        listOf(AnimeGlowPink, AnimeGlowCyan, AnimeGlowGold, AnimeGlowPink)
                    )
                )
                .padding(3.5.dp)
                .clip(CircleShape)
                .background(Color(0xFF131728)),
            contentAlignment = Alignment.Center
        ) {
            if (hasCustomImage) {
                AsyncImage(
                    model = File(profile.imagePath!!),
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .testTag("profile_avatar_image")
                )
            } else {
                // Default initial avatar (first letter of user's name or "S")
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(AnimeGlowPink, AnimeGlowCyan)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = profile.displayInitial,
                        color = Color.White,
                        fontSize = 54.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.testTag("profile_avatar_initial")
                    )
                }
            }
        }

        // Camera edit icon badge
        Box(
            modifier = Modifier
                .size(38.dp)
                .align(Alignment.BottomEnd)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        listOf(AnimeGlowPink, AnimeGlowCyan)
                    )
                )
                .border(2.dp, Color.White, CircleShape)
                .clickable(onClick = onChoosePictureClick)
                .testTag("change_photo_badge"),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.PhotoCamera,
                contentDescription = "Change profile picture",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
    }

    Spacer(modifier = Modifier.height(10.dp))

    Text(
        text = "Tap to choose photo",
        color = Color.White.copy(alpha = 0.70f),
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium
    )
}

@Composable
private fun ChangeNameButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(
                        AnimeGlowPink.copy(alpha = 0.85f),
                        AnimeGlowCyan.copy(alpha = 0.85f)
                    )
                )
            )
            .border(
                1.dp,
                Brush.horizontalGradient(
                    listOf(Color.White.copy(0.6f), Color.White.copy(0.3f))
                ),
                RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp)
            .testTag("change_name_btn"),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Rounded.Edit,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(19.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Change Name",
                color = Color.White,
                fontSize = 15.5.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
private fun ChangeNameDialog(
    currentName: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var nameInput by remember { mutableStateOf(currentName) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xF20F1426),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                Brush.verticalGradient(
                    listOf(Color.White.copy(0.50f), Color.White.copy(0.15f))
                )
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .shadow(24.dp, RoundedCornerShape(24.dp), spotColor = AnimeGlowPink.copy(0.35f))
                .testTag("change_name_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Change Name",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Enter your preferred name for STUDYPAL",
                    color = Color.White.copy(alpha = 0.75f),
                    fontSize = 12.5.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(18.dp))

                OutlinedTextField(
                    value = nameInput,
                    onValueChange = {
                        nameInput = it
                        if (it.trim().isNotEmpty()) {
                            errorMessage = null
                        }
                    },
                    singleLine = true,
                    placeholder = {
                        Text("Enter your name", color = Color.White.copy(alpha = 0.5f))
                    },
                    isError = errorMessage != null,
                    supportingText = {
                        if (errorMessage != null) {
                            Text(errorMessage!!, color = Color(0xFFFF6B6B), fontSize = 11.5.sp)
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = AnimeGlowCyan,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.35f),
                        errorBorderColor = Color(0xFFFF6B6B),
                        cursorColor = AnimeGlowCyan
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("change_name_input_field")
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Actions: Cancel & Save
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Cancel
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.White.copy(alpha = 0.10f))
                            .border(1.dp, Color.White.copy(alpha = 0.22f), RoundedCornerShape(14.dp))
                            .clickable(onClick = onDismiss)
                            .padding(vertical = 12.dp)
                            .testTag("change_name_cancel_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Cancel",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // Save
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(AnimeGlowPink, AnimeGlowCyan)
                                )
                            )
                            .clickable {
                                val trimmed = nameInput.trim()
                                if (trimmed.isEmpty()) {
                                    errorMessage = "Name cannot be empty or whitespace only"
                                } else {
                                    onSave(trimmed)
                                }
                            }
                            .padding(vertical = 12.dp)
                            .testTag("change_name_save_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Save",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
