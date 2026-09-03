package com.ai.assistant.ui.chat

import com.ai.assistant.domain.model.ChatMessage

data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val input: String = "",
    val isLoading: Boolean = false,
    val conversationId: String? = null,
    val error: String? = null,
    val lastFailedMessage: String? = null,
)
