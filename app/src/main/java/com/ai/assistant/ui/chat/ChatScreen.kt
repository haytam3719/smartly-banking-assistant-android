package com.ai.assistant.ui.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ai.assistant.domain.model.ChatMessage
import com.ai.assistant.domain.model.MessageAuthor
import com.ai.assistant.ui.chat.components.AssistantMessageBubble
import com.ai.assistant.ui.chat.components.ChatInput
import com.ai.assistant.ui.chat.components.SuggestionChipRow
import com.ai.assistant.ui.chat.components.TypingIndicator
import com.ai.assistant.ui.chat.components.UserMessageBubble
import com.ai.assistant.ui.theme.AIAssistantTheme

@Composable
fun ChatRoute(viewModel: ChatViewModel = hiltViewModel()) {
    val state = viewModel.uiState.collectAsStateWithLifecycle().value
    ChatScreen(state, viewModel::onInputChanged, viewModel::sendMessage, viewModel::sendSuggestion, viewModel::retryLastMessage)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    state: ChatUiState,
    onInputChanged: (String) -> Unit,
    onSend: () -> Unit,
    onSuggestion: (String) -> Unit,
    onRetry: () -> Unit,
) {
    val listState = rememberLazyListState()
    val itemCount = state.messages.size + if (state.isLoading || state.error != null) 1 else 0
    LaunchedEffect(itemCount) { if (itemCount > 0) listState.animateScrollToItem(itemCount - 1) }

    Scaffold(
        modifier = Modifier.fillMaxSize().imePadding(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { SmartlyTopBar() },
        bottomBar = {
            Surface(tonalElevation = 3.dp) {
                ChatInput(state.input, !state.isLoading, onInputChanged, onSend)
            }
        },
    ) { padding ->
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 22.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            if (state.messages.isEmpty()) item { WelcomeContent(!state.isLoading, onSuggestion) }
            items(state.messages, key = { it.id }) { message ->
                if (message.author == MessageAuthor.USER) UserMessageBubble(message) else AssistantMessageBubble(message)
            }
            if (state.isLoading) item { TypingIndicator() }
            state.error?.let { error -> item { ErrorMessage(error, onRetry) } }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SmartlyTopBar() {
    Column {
        TopAppBar(
            title = {
                Column {
                    Text("Smartly AI", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                    Text("Votre assistant bancaire", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            },
            actions = {
                Row(Modifier.padding(end = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(7.dp).clip(CircleShape).then(Modifier).padding(0.dp), contentAlignment = Alignment.Center) {
                        Surface(color = MaterialTheme.colorScheme.tertiary, shape = CircleShape, modifier = Modifier.fillMaxSize()) {}
                    }
                    Spacer(Modifier.size(6.dp)); Text("Disponible", style = MaterialTheme.typography.labelMedium)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
        )
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = .55f))
    }
}

@Composable
private fun WelcomeContent(enabled: Boolean, onSuggestion: (String) -> Unit) {
    Column(Modifier.fillMaxWidth().padding(top = 24.dp)) {
        Text("Bonjour", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
        Text("Comment puis-je vous aider aujourd’hui ?", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(top = 5.dp))
        Text("Consultez vos comptes ou posez une question sur nos services.", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 12.dp, bottom = 22.dp))
        SuggestionChipRow(enabled, onSuggestion)
    }
}

@Composable
private fun ErrorMessage(error: String, onRetry: () -> Unit) {
    Surface(color = MaterialTheme.colorScheme.errorContainer, shape = MaterialTheme.shapes.large) {
        Column(Modifier.padding(16.dp)) {
            Text(error, color = MaterialTheme.colorScheme.onErrorContainer)
            Button(onClick = onRetry, Modifier.padding(top = 8.dp)) { Text("Réessayer") }
        }
    }
}

private val previewMessages = listOf(
    ChatMessage(author = MessageAuthor.USER, text = "Quel est mon solde ?"),
    ChatMessage(author = MessageAuthor.ASSISTANT, text = "Votre solde disponible est de 2450.75 EUR.", source = "get_account_balance", evidenceType = "TOOL", evidenceSource = "account.balance.read"),
)

@Preview(showBackground = true, name = "Welcome light") @Composable private fun WelcomePreview() = AIAssistantTheme(dynamicColor = false) { ChatScreen(ChatUiState(), {}, {}, {}, {}) }
@Preview(showBackground = true, name = "Conversation light") @Composable private fun ConversationPreview() = AIAssistantTheme(dynamicColor = false) { ChatScreen(ChatUiState(messages = previewMessages), {}, {}, {}, {}) }
@Preview(showBackground = true, name = "Loading") @Composable private fun LoadingPreview() = AIAssistantTheme(dynamicColor = false) { ChatScreen(ChatUiState(messages = previewMessages, isLoading = true), {}, {}, {}, {}) }
@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES, name = "Conversation dark") @Composable private fun DarkPreview() = AIAssistantTheme(darkTheme = true, dynamicColor = false) { ChatScreen(ChatUiState(messages = previewMessages), {}, {}, {}, {}) }
