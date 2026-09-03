package com.ai.assistant.data.repository

import com.ai.assistant.domain.model.ChatResponse

interface ChatRepository {
    suspend fun sendMessage(message: String, conversationId: String?): Result<ChatResponse>
}
