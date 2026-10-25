package com.example.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.AddComment
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material.icons.rounded.Psychology
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.chat.data.ImageQuotaManager
import com.example.chat.data.OpenAiResult
import com.example.chat.data.OpenAiService
import com.example.chat.model.ChatMessage
import com.example.chat.model.MessageRole
import com.example.chat.ui.MarkdownText
import com.example.chat.util.ImageSaver
import com.example.ui.components.AnimeGlowCyan
import com.example.ui.components.AnimeGlowGold
import com.example.ui.components.AnimeGlowPink
import com.example.ui.components.GlassCard
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun ChatAiScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    // OpenAI and Quota Singletons
    val openAiService = remember { OpenAiService() }
    val quotaManager = remember { ImageQuotaManager.getInstance(context) }

    // EPHEMERAL IN-MEMORY STATE: Strictly discarded on exit
    var messages by remember { mutableStateOf<List<ChatMessage>>(emptyList()) }
    var inputText by remember { mutableStateOf("") }
    var isThinking by remember { mutableStateOf(false) }
    var thinkingStatusText by remember { mutableStateOf("Thinking...") }
    var activeJob by remember { mutableStateOf<Job?>(null) }
    var showApiKeyDialog by remember { mutableStateOf(false) }
    var isImageMode by remember { mutableStateOf(false) }
    var quotaRemaining by remember { mutableStateOf(quotaManager.getRemainingCount()) }

    // Discard entire conversation session when leaving the screen
    DisposableEffect(Unit) {
        onDispose {
            activeJob?.cancel()
            messages = emptyList()
        }
    }

    // System Back Handler
    BackHandler {
        activeJob?.cancel()
        messages = emptyList()
        onBackClick()
    }

    // Auto-scroll to newest message
    LaunchedEffect(messages.size, isThinking) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    fun handleSend(promptText: String) {
        val trimmed = promptText.trim()
        if (trimmed.isEmpty() || isThinking) return

        // Clear input field
        inputText = ""

        val isImgReq = isImageMode || OpenAiService.isImageRequest(trimmed)
        val userMsg = ChatMessage(
            role = MessageRole.USER,
            text = trimmed
        )
        messages = messages + userMsg

        if (isImgReq) {
            // Check Daily Image Quota (Strict 2/day)
            if (!quotaManager.canGenerateImage()) {
                val quotaMsg = ChatMessage(
                    role = MessageRole.ASSISTANT,
                    text = ImageQuotaManager.QUOTA_EXCEEDED_MESSAGE,
                    isError = true
                )
                messages = messages + quotaMsg
                return
            }

            // Execute Image Generation
            isThinking = true
            thinkingStatusText = "Generating image with AI..."
            val cleanPrompt = OpenAiService.extractImagePrompt(trimmed)

            activeJob = coroutineScope.launch {
                val result = openAiService.generateImage(cleanPrompt)
                isThinking = false
                when (result) {
                    is OpenAiResult.Success -> {
                        quotaManager.recordImageGenerated()
                        quotaRemaining = quotaManager.getRemainingCount()
                        messages = messages + ChatMessage(
                            role = MessageRole.ASSISTANT,
                            text = cleanPrompt,
                            imageUrl = result.data,
                            isImage = true
                        )
                    }
                    is OpenAiResult.Error -> {
                        messages = messages + ChatMessage(
                            role = MessageRole.ASSISTANT,
                            text = "❌ Image Generation Failed: ${result.message}",
                            isError = true
                        )
                    }
                }
            }
        } else {
            // Execute Chat Completion
            isThinking = true
            thinkingStatusText = "Thinking..."

            activeJob = coroutineScope.launch {
                val result = openAiService.sendChatCompletion(messages)
                isThinking = false
                when (result) {
                    is OpenAiResult.Success -> {
                        messages = messages + ChatMessage(
                            role = MessageRole.ASSISTANT,
                            text = result.data
                        )
                    }
                    is OpenAiResult.Error -> {
                        messages = messages + ChatMessage(
                            role = MessageRole.ASSISTANT,
                            text = "❌ Error: ${result.message}",
                            isError = true
                        )
                    }
                }
            }
        }
    }

    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val navBarHeight = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val imeBottom = WindowInsets.ime.asPaddingValues().calculateBottomPadding()

    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag("chat_ai_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = if (imeBottom > navBarHeight) imeBottom else navBarHeight)
        ) {
            // ChatGPT Styled Top Bar
            ChatTopBar(
                isThinking = isThinking,
                thinkingStatus = thinkingStatusText,
                quotaRemaining = quotaRemaining,
                hasKey = openAiService.hasValidApiKey(),
                onBackClick = {
                    activeJob?.cancel()
                    messages = emptyList()
                    onBackClick()
                },
                onNewChatClick = {
                    activeJob?.cancel()
                    isThinking = false
                    messages = emptyList()
                    Toast.makeText(context, "New chat started", Toast.LENGTH_SHORT).show()
                },
                onKeyClick = {
                    showApiKeyDialog = true
                },
                statusBarHeight = statusBarHeight
            )

            // Chat Message Stream or Blank Welcome State
            if (messages.isEmpty() && !isThinking) {
                BlankChatWelcomeState(
                    onPromptSelected = { prompt ->
                        handleSend(prompt)
                    },
                    modifier = Modifier.weight(1f)
                )
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp),
                    contentPadding = PaddingValues(top = 12.dp, bottom = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(messages, key = { it.id }) { msg ->
                        ChatMessageItem(
                            message = msg,
                            context = context
                        )
                    }

                    if (isThinking) {
                        item {
                            ThinkingIndicatorBubble(statusText = thinkingStatusText)
                        }
                    }
                }
            }

            // Bottom Multi-line Input Bar
            ChatInputBar(
                inputText = inputText,
                onInputChanged = { inputText = it },
                isThinking = isThinking,
                isImageMode = isImageMode,
                onToggleImageMode = { isImageMode = !isImageMode },
                onSendClick = { handleSend(inputText) },
                onStopClick = {
                    activeJob?.cancel()
                    isThinking = false
                    messages = messages + ChatMessage(
                        role = MessageRole.ASSISTANT,
                        text = "Response stopped.",
                        isError = true
                    )
                }
            )
        }

        // Optional Dialog for OpenAI API Key setup
        if (showApiKeyDialog) {
            ApiKeyConfigDialog(
                currentKey = openAiService.effectiveApiKey,
                onDismiss = { showApiKeyDialog = false },
                onSaveKey = { newKey ->
                    openAiService.runtimeApiKeyOverride = newKey
                    showApiKeyDialog = false
                    Toast.makeText(context, "API Key saved for current session", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}

@Composable
private fun ChatTopBar(
    isThinking: Boolean,
    thinkingStatus: String,
    quotaRemaining: Int,
    hasKey: Boolean,
    onBackClick: () -> Unit,
    onNewChatClick: () -> Unit,
    onKeyClick: () -> Unit,
    statusBarHeight: androidx.compose.ui.unit.Dp
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)),
        shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
        color = Color(0xF00D1122),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            Brush.verticalGradient(listOf(Color.White.copy(0.30f), Color.White.copy(0.10f)))
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = statusBarHeight + 6.dp, bottom = 10.dp, start = 12.dp, end = 12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Exit / Back button
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.12f))
                        .testTag("chat_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Exit Chat AI",
                        tint = Color.White
                    )
                }

                // Center Title & Status Indicator
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(listOf(AnimeGlowPink, AnimeGlowCyan))
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Psychology,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Chat AI",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }

                    if (isThinking) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            CircularProgressIndicator(
                                color = AnimeGlowCyan,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(11.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = thinkingStatus,
                                color = AnimeGlowCyan,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    } else {
                        Text(
                            text = "Image Quota: $quotaRemaining/2 today",
                            color = if (quotaRemaining > 0) AnimeGlowGold else Color(0xFFFF5964),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Right Actions: API Key indicator & "New Chat" Flush button
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onKeyClick,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(
                                if (hasKey) Color.White.copy(alpha = 0.12f)
                                else Color(0xFFFF5964).copy(alpha = 0.25f)
                            )
                            .testTag("chat_api_key_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Key,
                            contentDescription = "API Key Settings",
                            tint = if (hasKey) AnimeGlowGold else Color(0xFFFF5964),
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    IconButton(
                        onClick = onNewChatClick,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(AnimeGlowPink.copy(0.4f), AnimeGlowCyan.copy(0.4f))
                                )
                            )
                            .border(1.dp, Color.White.copy(alpha = 0.3f), CircleShape)
                            .testTag("new_chat_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.AddComment,
                            contentDescription = "New Chat Session",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BlankChatWelcomeState(
    onPromptSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(68.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(listOf(AnimeGlowPink, AnimeGlowCyan))
                )
                .shadow(16.dp, CircleShape, spotColor = AnimeGlowPink),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.AutoAwesome,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Welcome to STUDYPAL AI Tutor",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Your personal tutor for CBSE Classes 9-12 powered by OpenRouter (${OpenAiService.CHAT_MODEL}). Ask academic questions, debug code, or generate diagram concepts.",
            color = Color.White.copy(alpha = 0.75f),
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Suggested Prompt Chips
        val samplePrompts = listOf(
            "Explain Newton's Laws with real-life examples",
            "Write a Python program for Binary Search",
            "Solve: Integrate x * sin(x) dx step-by-step",
            "/image Draw a labeled cross-section of a plant cell"
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            samplePrompts.forEach { prompt ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White.copy(alpha = 0.10f),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        Color.White.copy(alpha = 0.20f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onPromptSelected(prompt) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (prompt.startsWith("/image")) Icons.Rounded.Image else Icons.Rounded.AutoAwesome,
                            contentDescription = null,
                            tint = if (prompt.startsWith("/image")) AnimeGlowGold else AnimeGlowCyan,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = prompt,
                            color = Color.White.copy(alpha = 0.90f),
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatMessageItem(
    message: ChatMessage,
    context: Context
) {
    val isUser = message.role == MessageRole.USER
    val scope = rememberCoroutineScope()
    var isSaving by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        if (!isUser) {
            // Assistant Avatar
            val avatarBrush = if (message.isError) {
                Brush.linearGradient(listOf(Color(0xFFFF5964), Color(0xFFFF3344)))
            } else {
                Brush.linearGradient(listOf(AnimeGlowPink, AnimeGlowCyan))
            }

            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(avatarBrush),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (message.isError) Icons.Rounded.ErrorOutline else Icons.Rounded.Psychology,
                    contentDescription = "AI Tutor",
                    tint = Color.White,
                    modifier = Modifier.size(19.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
        }

        // Bubble Content
        val bubbleBrush = if (isUser) {
            Brush.linearGradient(listOf(Color(0xFF8A4FFF), Color(0xFF6C35DE)))
        } else if (message.isError) {
            Brush.linearGradient(listOf(Color(0x35FF5964), Color(0x35FF5964)))
        } else {
            Brush.linearGradient(listOf(Color(0xE013182E), Color(0xE013182E)))
        }

        Column(
            modifier = Modifier
                .widthIn(max = 320.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 18.dp,
                        topEnd = 18.dp,
                        bottomStart = if (isUser) 18.dp else 4.dp,
                        bottomEnd = if (isUser) 4.dp else 18.dp
                    )
                )
                .background(bubbleBrush)
                .border(
                    1.dp,
                    if (isUser) Color.White.copy(alpha = 0.30f)
                    else if (message.isError) Color(0xFFFF5964).copy(alpha = 0.60f)
                    else Color.White.copy(alpha = 0.16f),
                    RoundedCornerShape(
                        topStart = 18.dp,
                        topEnd = 18.dp,
                        bottomStart = if (isUser) 18.dp else 4.dp,
                        bottomEnd = if (isUser) 4.dp else 18.dp
                    )
                )
                .padding(14.dp)
        ) {
            if (message.isImage && message.imageUrl != null) {
                // Generated Image Card
                Text(
                    text = "Prompt: ${message.text}",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 12.5.sp,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Black.copy(alpha = 0.3f))
                ) {
                    AsyncImage(
                        model = message.imageUrl,
                        contentDescription = message.text,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // "Save to Gallery" Button
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(enabled = !isSaving) {
                            isSaving = true
                            scope.launch {
                                val saveResult = ImageSaver.saveImageToGallery(context, message.imageUrl)
                                isSaving = false
                                saveResult.onSuccess {
                                    Toast.makeText(context, "Image saved to Gallery!", Toast.LENGTH_SHORT).show()
                                }.onFailure { e ->
                                    Toast.makeText(context, "Failed to save: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        .testTag("save_to_gallery_btn")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 9.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isSaving) {
                            CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Saving...", color = Color.White, fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                        } else {
                            Icon(
                                imageVector = Icons.Rounded.Download,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Save to Gallery",
                                color = Color.White,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            } else {
                // Text Message with Full Markdown Support
                if (isUser) {
                    Text(
                        text = message.text,
                        color = Color.White,
                        fontSize = 15.sp,
                        lineHeight = 21.sp
                    )
                } else {
                    MarkdownText(
                        text = message.text,
                        baseColor = if (message.isError) Color(0xFFFF8A92) else Color(0xFFF1F5F9)
                    )
                }
            }
        }
    }
}

@Composable
private fun ThinkingIndicatorBubble(statusText: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(Brush.linearGradient(listOf(AnimeGlowPink, AnimeGlowCyan))),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Psychology,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(19.dp)
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xD013182E),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(0.18f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(
                    color = AnimeGlowCyan,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = statusText,
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun ChatInputBar(
    inputText: String,
    onInputChanged: (String) -> Unit,
    isThinking: Boolean,
    isImageMode: Boolean,
    onToggleImageMode: () -> Unit,
    onSendClick: () -> Unit,
    onStopClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        color = Color(0xF20F1428),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            Brush.horizontalGradient(
                if (isImageMode) listOf(AnimeGlowGold.copy(0.6f), AnimeGlowPink.copy(0.6f))
                else listOf(Color.White.copy(0.25f), Color.White.copy(0.10f))
            )
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Mode Toggle: Text Chat vs DALL-E 3 Image Mode
            IconButton(
                onClick = onToggleImageMode,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        if (isImageMode) AnimeGlowGold.copy(alpha = 0.25f)
                        else Color.White.copy(alpha = 0.10f)
                    )
                    .testTag("chat_mode_toggle_btn")
            ) {
                Icon(
                    imageVector = Icons.Rounded.Image,
                    contentDescription = "Toggle Image Generation Mode",
                    tint = if (isImageMode) AnimeGlowGold else Color.White.copy(alpha = 0.70f),
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Expanding Multi-line Text Input
            OutlinedTextField(
                value = inputText,
                onValueChange = onInputChanged,
                placeholder = {
                    Text(
                        text = if (isImageMode) "Describe image to generate..." else "Message Chat AI...",
                        color = Color.White.copy(alpha = 0.50f),
                        fontSize = 14.5.sp
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .testTag("chat_input_field"),
                maxLines = 4,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = AnimeGlowCyan
                )
            )

            Spacer(modifier = Modifier.width(6.dp))

            // Action Button: Send or Stop
            if (isThinking) {
                IconButton(
                    onClick = onStopClick,
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFF5964))
                        .testTag("chat_stop_btn")
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Stop,
                        contentDescription = "Stop Response",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            } else {
                IconButton(
                    onClick = onSendClick,
                    enabled = inputText.trim().isNotEmpty(),
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(
                            if (inputText.trim().isNotEmpty()) Brush.linearGradient(
                                listOf(AnimeGlowPink, AnimeGlowCyan)
                            )
                            else Brush.linearGradient(
                                listOf(Color.White.copy(0.12f), Color.White.copy(0.12f))
                            )
                        )
                        .testTag("chat_send_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.Send,
                        contentDescription = "Send message",
                        tint = if (inputText.trim().isNotEmpty()) Color.White else Color.White.copy(alpha = 0.40f),
                        modifier = Modifier.size(19.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ApiKeyConfigDialog(
    currentKey: String,
    onDismiss: () -> Unit,
    onSaveKey: (String) -> Unit
) {
    var keyText by remember { mutableStateOf(if (currentKey.length > 8) currentKey.take(6) + "..." else "") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xF2101426),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(0.3f)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(AnimeGlowGold.copy(0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Key,
                        contentDescription = null,
                        tint = AnimeGlowGold,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "OpenRouter API Key",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Enter an OpenRouter API Key (sk-or-v1-...) for this session, or configure OPENROUTER_API_KEY permanently in the Secrets panel in AI Studio.",
                    color = Color.White.copy(alpha = 0.75f),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 17.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = keyText,
                    onValueChange = { keyText = it },
                    placeholder = { Text("sk-or-v1-...", color = Color.White.copy(0.4f)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AnimeGlowCyan,
                        unfocusedBorderColor = Color.White.copy(0.3f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.White.copy(0.12f))
                            .clickable(onClick = onDismiss)
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Cancel", color = Color.White, fontWeight = FontWeight.Medium)
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Brush.linearGradient(listOf(AnimeGlowPink, AnimeGlowCyan)))
                            .clickable { onSaveKey(keyText.trim()) }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Save Key", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
