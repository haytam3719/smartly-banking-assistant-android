package com.ai.assistant.data.repository

import com.ai.assistant.data.remote.BackendConfig
import com.ai.assistant.data.remote.BankingApi
import com.ai.assistant.data.remote.dto.ChatRequestDto
import com.ai.assistant.domain.model.ChatResponse
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val api: BankingApi,
) : ChatRepository {
    override suspend fun sendMessage(message: String, conversationId: String?): Result<ChatResponse> =
        runCatching {
            val response = api.chat(
                subjectId = BackendConfig.DEMO_SUBJECT_ID,
                customerId = BackendConfig.DEMO_CUSTOMER_ID,
                request = ChatRequestDto(
                    customerId = BackendConfig.DEMO_CUSTOMER_ID,
                    message = message,
                    conversationId = conversationId,
                    locale = BackendConfig.LOCALE,
                ),
            )
            val evidence = response.sources.firstOrNull()
            ChatResponse(
                answer = response.answer,
                source = response.source,
                evidenceType = evidence?.type,
                evidenceSource = evidence?.source,
                conversationId = response.conversationId,
                requestId = response.requestId,
            )
        }
}
