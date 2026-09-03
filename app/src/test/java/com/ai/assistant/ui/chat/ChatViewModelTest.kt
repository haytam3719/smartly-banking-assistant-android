package com.ai.assistant.ui.chat

import com.ai.assistant.MainDispatcherRule
import com.ai.assistant.data.repository.ChatRepository
import com.ai.assistant.domain.model.ChatResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChatViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    @Test fun successfulRequestPreservesConversationId() = runTest(mainDispatcherRule.dispatcher) {
        val repository = FakeRepository()
        val viewModel = ChatViewModel(repository)
        viewModel.onInputChanged("Quel est mon solde ?")
        viewModel.sendMessage()

        assertTrue(viewModel.uiState.value.isLoading)
        assertEquals(1, viewModel.uiState.value.messages.size)
        testScheduler.advanceUntilIdle()
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals("conversation-1", viewModel.uiState.value.conversationId)
        assertEquals(2, viewModel.uiState.value.messages.size)

        viewModel.onInputChanged("Et ma carte ?")
        viewModel.sendMessage()
        testScheduler.advanceUntilIdle()
        assertEquals("conversation-1", repository.calls.last().second)
    }

    @Test fun emptyInputDoesNothing() = runTest(mainDispatcherRule.dispatcher) {
        val repository = FakeRepository()
        val viewModel = ChatViewModel(repository)
        viewModel.onInputChanged("   ")
        viewModel.sendMessage()
        testScheduler.advanceUntilIdle()
        assertTrue(repository.calls.isEmpty())
        assertTrue(viewModel.uiState.value.messages.isEmpty())
    }

    @Test fun networkErrorCanBeRetriedWithoutDuplicatingUserMessage() = runTest(mainDispatcherRule.dispatcher) {
        val repository = FakeRepository(failuresRemaining = 1)
        val viewModel = ChatViewModel(repository)
        viewModel.onInputChanged("Mon solde")
        viewModel.sendMessage()
        testScheduler.advanceUntilIdle()
        assertFalse(viewModel.uiState.value.isLoading)
        assertTrue(viewModel.uiState.value.error?.contains("réessayer") == true)
        assertEquals(1, viewModel.uiState.value.messages.size)

        viewModel.retryLastMessage()
        testScheduler.advanceUntilIdle()
        assertNull(viewModel.uiState.value.error)
        assertEquals(2, viewModel.uiState.value.messages.size)
        assertEquals(2, repository.calls.size)
    }

    private class FakeRepository(var failuresRemaining: Int = 0) : ChatRepository {
        val calls = mutableListOf<Pair<String, String?>>()
        override suspend fun sendMessage(message: String, conversationId: String?): Result<ChatResponse> {
            calls += message to conversationId
            if (failuresRemaining-- > 0) return Result.failure(java.io.IOException())
            return Result.success(ChatResponse("Réponse", "RAG", null, null, "conversation-1", "request-1"))
        }
    }
}
