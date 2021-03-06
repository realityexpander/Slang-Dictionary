package com.realityexpander.dictionary.feature_dictionary.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.realityexpander.dictionary.feature_dictionary.domain.model.WordInfo

@Composable
fun WordInfoItem(
    wordInfo: WordInfo,
    modifier: Modifier = Modifier,
    viewModel: WordInfoViewModel
) {

    Column(modifier = modifier) {
            Row(verticalAlignment =  Alignment.CenterVertically) {
                Text(
                    text = wordInfo.word,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.onPrimary,
                    style = MaterialTheme.typography.h4
                )
                if (wordInfo.audio.isNotEmpty()) {
                    IconButton(onClick = {
                        viewModel.playAudio(wordInfo.audio)
                    }) {
                        Icon(imageVector = Icons.Default.PlayArrow, "Play")
                    }
                }
            }
        if (wordInfo.phonetic.isNotEmpty()) {
            Text(text = wordInfo.phonetic, fontWeight = FontWeight.Light)
            Spacer(modifier = Modifier.height(16.dp))
        }
        // Text(text = "Origin: " + wordInfo.origin) // API doesn't provide origin

        wordInfo.meanings.forEach { meaning ->
            Text(
                text = meaning.partOfSpeech,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h5
            )
            meaning.definitions.forEachIndexed { i, definition ->
                Text(text = "${i + 1}. ${definition.definition}")
                Spacer(modifier = Modifier.height(8.dp))
                definition.example?.let { example ->
                    if (example.isEmpty()) return@let

                    Text(
                        text = "Example: $example",
                        fontWeight = FontWeight.Thin,
                        fontStyle = FontStyle.Italic
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}