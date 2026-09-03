package com.ai.assistant.ui.chat.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun TypingIndicator(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "typing")
    Column(modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            SmartlyMark(); Spacer(Modifier.size(8.dp))
            Text("Smartly AI", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
        }
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainer,
            shape = RoundedCornerShape(6.dp, 22.dp, 22.dp, 22.dp),
            modifier = Modifier.padding(top = 7.dp),
        ) {
            Row(Modifier.padding(horizontal = 18.dp, vertical = 15.dp), horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                repeat(3) { index ->
                    val opacity by transition.animateFloat(
                        initialValue = .25f,
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(tween(500, delayMillis = index * 140), RepeatMode.Reverse),
                        label = "dot$index",
                    )
                    Box(Modifier.size(7.dp).alpha(opacity).background(MaterialTheme.colorScheme.primary, CircleShape))
                }
            }
        }
    }
}
