package com.yet.generate.ui.chat

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun InputArea(
    inputText: String,
    isLoading: Boolean,
    onInputTextChanged: (String) -> Unit,
    onSendMessage: () -> Unit,
    onFilterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        tonalElevation = 3.dp
    ) {
        Column {
            if (isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                // Filter button
                IconButton(
                    onClick = onFilterClick,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filters"
                    )
                }

                // Text field
                OutlinedTextField(
                    value = inputText,
                    onValueChange = onInputTextChanged,
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Enter your niche or idea...") },
                    enabled = !isLoading,
                    maxLines = 4,
                    shape = MaterialTheme.shapes.large
                )

                // Send button
                FilledIconButton(
                    onClick = onSendMessage,
                    enabled = inputText.isNotBlank() && !isLoading,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send"
                    )
                }
            }
        }
    }
}
