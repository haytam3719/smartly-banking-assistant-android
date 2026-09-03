package com.ai.assistant.data.repository

import com.ai.assistant.data.remote.BackendConfig
import com.ai.assistant.data.remote.BankingApi
import com.ai.assistant.data.remote.dto.ChatRequestDto
import com.ai.assistant.data.remote.dto.ChatResponseDto
import com.ai.assistant.data.remote.dto.EvidenceDto
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ChatRepositoryImplTest {
    @Test fun mapsRequestHeadersBodyAndResponse() = runTest {
        val api = RecordingApi()
        val result = ChatRepositoryImpl(api).sendMessage("Bonjour", "conversation-old").getOrThrow()

        assertEquals(BackendConfig.DEMO_SUBJECT_ID, api.subjectId)
        assertEquals(BackendConfig.DEMO_CUSTOMER_ID, api.customerId)
        assertEquals(ChatRequestDto("C1024", "Bonjour", "conversation-old", "fr-FR"), api.request)
        assertEquals("conversation-new", result.conversationId)
        assertEquals("account.balance.read", result.evidenceSource)
    }

    private class RecordingApi : BankingApi {
        lateinit var subjectId: String
        lateinit var customerId: String
        lateinit var request: ChatRequestDto
        override suspend fun chat(subjectId: String, customerId: String, request: ChatRequestDto): ChatResponseDto {
            this.subjectId = subjectId; this.customerId = customerId; this.request = request
            return ChatResponseDto("Réponse", "get_account_balance", listOf(EvidenceDto("TOOL", "account.balance.read")), "conversation-new", "request")
        }
    }
}
