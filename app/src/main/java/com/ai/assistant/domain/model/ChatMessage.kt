package com.ai.assistant.domain.model

import java.util.UUID

enum class MessageAuthor { USER, ASSISTANT }

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val author: MessageAuthor,
    val text: String,
    val source: String? = null,
    val evidenceType: String? = null,
    val evidenceSource: String? = null,
    val requestId: String? = null,
)

data class ChatResponse(
    val answer: String,
    val source: String?,
    val evidenceType: String?,
    val evidenceSource: String?,
    val conversationId: String,
    val requestId: String?,
)
