package com.ai.assistant.data.remote

import com.ai.assistant.data.remote.dto.ChatRequestDto
import com.ai.assistant.data.remote.dto.ChatResponseDto
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface BankingApi {
    @POST("chat")
    suspend fun chat(
        @Header("X-Demo-Subject-Id") subjectId: String,
        @Header("X-Demo-Customer-Id") customerId: String,
        @Body request: ChatRequestDto,
    ): ChatResponseDto
}
