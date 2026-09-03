package com.ai.assistant.ui.chat.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ai.assistant.domain.model.ChatMessage

@Composable
fun UserMessageBubble(message: ChatMessage, modifier: Modifier = Modifier) {
    MessageEntrance(modifier) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                shape = RoundedCornerShape(22.dp, 22.dp, 6.dp, 22.dp),
                tonalElevation = 1.dp,
                modifier = Modifier.widthIn(max = 320.dp),
            ) { Text(message.text, Modifier.padding(horizontal = 17.dp, vertical = 12.dp)) }
        }
    }
}

@Composable
fun AssistantMessageBubble(message: ChatMessage, modifier: Modifier = Modifier) {
    MessageEntrance(modifier) {
        Column(horizontalAlignment = Alignment.Start) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                SmartlyMark()
                Spacer(Modifier.size(8.dp))
                Text("Smartly AI", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
            }
            Surface(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(6.dp, 22.dp, 22.dp, 22.dp),
                tonalElevation = 1.dp,
                modifier = Modifier.padding(top = 7.dp).widthIn(max = 340.dp),
            ) { Text(message.text, Modifier.padding(horizontal = 17.dp, vertical = 13.dp)) }
            SourceBadge(message.evidenceType, message.evidenceSource, message.source, Modifier.padding(top = 7.dp))
        }
    }
}

@Composable
fun SmartlyMark() {
    Box(
        Modifier.size(24.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center,
    ) { Text("S", color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold) }
}

@Composable
private fun MessageEntrance(modifier: Modifier, content: @Composable () -> Unit) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }
    AnimatedVisibility(visible, modifier, enter = fadeIn() + slideInVertically { it / 5 }) { content() }
}
