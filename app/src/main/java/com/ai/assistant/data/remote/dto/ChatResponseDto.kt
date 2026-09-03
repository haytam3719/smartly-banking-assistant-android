package com.ai.assistant.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ChatResponseDto(
    val answer: String,
    val source: String? = null,
    val sources: List<EvidenceDto> = emptyList(),
    @SerializedName("conversation_id") val conversationId: String,
    @SerializedName("request_id") val requestId: String? = null,
)
