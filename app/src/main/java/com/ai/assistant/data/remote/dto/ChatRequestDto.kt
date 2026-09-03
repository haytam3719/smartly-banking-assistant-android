package com.ai.assistant.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ChatRequestDto(
    @SerializedName("customer_id") val customerId: String,
    val message: String,
    @SerializedName("conversation_id") val conversationId: String?,
    val locale: String,
)
