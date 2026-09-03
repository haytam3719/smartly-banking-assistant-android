package com.ai.assistant.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ai.assistant.data.repository.ChatRepository
import com.ai.assistant.domain.model.ChatMessage
import com.ai.assistant.domain.model.MessageAuthor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: ChatRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    fun onInputChanged(value: String) = _uiState.update { it.copy(input = value, error = null) }

    fun sendMessage() = submit(_uiState.value.input, appendUserMessage = true)

    fun sendSuggestion(suggestion: String) = submit(suggestion, appendUserMessage = true)

    fun retryLastMessage() {
        val message = _uiState.value.lastFailedMessage ?: return
        submit(message, appendUserMessage = false)
    }

    private fun submit(rawMessage: String, appendUserMessage: Boolean) {
        val message = rawMessage.trim()
        if (message.isEmpty() || _uiState.value.isLoading) return

        val conversationId = _uiState.value.conversationId
        _uiState.update { state ->
            state.copy(
                messages = if (appendUserMessage) state.messages + ChatMessage(
                    author = MessageAuthor.USER,
                    text = message,
                ) else state.messages,
                input = "",
                isLoading = true,
                error = null,
                lastFailedMessage = null,
            )
        }

        viewModelScope.launch {
            repository.sendMessage(message, conversationId)
                .onSuccess { response ->
                    _uiState.update { state ->
                        state.copy(
                            messages = state.messages + ChatMessage(
                                author = MessageAuthor.ASSISTANT,
                                text = response.answer,
                                source = response.source,
                                evidenceType = response.evidenceType,
                                evidenceSource = response.evidenceSource,
                                requestId = response.requestId,
                            ),
                            isLoading = false,
                            conversationId = response.conversationId,
                        )
                    }
                }
                .onFailure {
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            error = "Impossible de contacter l’assistant pour le moment. Veuillez réessayer.",
                            lastFailedMessage = message,
                        )
                    }
                }
        }
    }
}
