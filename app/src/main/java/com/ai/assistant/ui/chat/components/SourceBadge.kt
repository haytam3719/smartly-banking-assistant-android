package com.ai.assistant.ui.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SourceBadge(type: String?, evidenceSource: String?, fallbackSource: String?, modifier: Modifier = Modifier) {
    val text = when {
        !type.isNullOrBlank() && !evidenceSource.isNullOrBlank() -> "$type • $evidenceSource"
        !fallbackSource.isNullOrBlank() -> "Source : $fallbackSource"
        else -> return
    }
    Text(
        text = text,
        modifier = modifier.background(
            MaterialTheme.colorScheme.surfaceContainerHigh,
            RoundedCornerShape(8.dp),
        ).padding(horizontal = 8.dp, vertical = 4.dp),
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}
