package com.unilibre.asistenterecetas.presentation.components

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import kotlinx.coroutines.delay

@Composable
fun TypewriterText(text: String, modifier: Modifier = Modifier, style: TextStyle) {
    var displayedText by remember { mutableStateOf("") }
    LaunchedEffect(text) {
        displayedText = ""
        for (i in text.indices) {
            displayedText += text[i]
            delay(30L)
        }
    }
    Text(text = displayedText, modifier = modifier, style = style)
}
