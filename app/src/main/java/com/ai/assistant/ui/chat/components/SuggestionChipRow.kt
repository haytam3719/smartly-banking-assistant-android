package com.ai.assistant.ui.chat.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

val ChatSuggestions = listOf(
    "Quel est mon solde ?",
    "Affiche mes dernières transactions.",
    "Quel est mon plafond de carte ?",
    "Quel est le statut de mon virement TR4587 ?",
    "Quels sont les frais d'un virement international ?",
)

@Composable
fun SuggestionChipRow(enabled: Boolean, onSuggestion: (String) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(7.dp)) {
        ChatSuggestions.forEach { suggestion ->
            AssistChip(
                onClick = { onSuggestion(suggestion) },
                enabled = enabled,
                label = { Text(suggestion, style = MaterialTheme.typography.bodyMedium) },
                colors = AssistChipDefaults.assistChipColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
            )
        }
    }
}
