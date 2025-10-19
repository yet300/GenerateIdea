package com.yet.generate.ui.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.yet.generate.feature.chat.model.IdeaFilters

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    sheetState: SheetState,
    currentFilters: IdeaFilters,
    onFiltersChanged: (IdeaFilters) -> Unit,
    onDismiss: () -> Unit
) {
    var niche by remember(currentFilters) { mutableStateOf(currentFilters.niche) }
    var type by remember(currentFilters) { mutableStateOf(currentFilters.type) }
    var budget by remember(currentFilters) { mutableStateOf(currentFilters.budget?.toString() ?: "") }
    var timeEstimate by remember(currentFilters) { mutableStateOf(currentFilters.timeEstimate ?: "") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Filters",
                    style = MaterialTheme.typography.headlineSmall
                )

                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close"
                    )
                }
            }

            HorizontalDivider()

            // Niche
            OutlinedTextField(
                value = niche,
                onValueChange = { niche = it },
                label = { Text("Niche/Topic") },
                placeholder = { Text("e.g., AI in healthcare") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Type selector
            var expandedType by remember { mutableStateOf(false) }
            val ideaTypes = listOf(
                "startup",
                "hackathon",
                "pet_project",
                "research",
                "business",
                "app",
                "service"
            )

            ExposedDropdownMenuBox(
                expanded = expandedType,
                onExpandedChange = { expandedType = it }
            ) {
                OutlinedTextField(
                    value = type,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Idea Type") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedType) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expandedType,
                    onDismissRequest = { expandedType = false }
                ) {
                    ideaTypes.forEach { ideaType ->
                        DropdownMenuItem(
                            text = { Text(ideaType) },
                            onClick = {
                                type = ideaType
                                expandedType = false
                            }
                        )
                    }
                }
            }

            // Budget
            OutlinedTextField(
                value = budget,
                onValueChange = { budget = it.filter { char -> char.isDigit() } },
                label = { Text("Budget (optional)") },
                placeholder = { Text("e.g., 50000") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                prefix = { Text("$") }
            )

            // Time estimate
            var expandedTime by remember { mutableStateOf(false) }
            val timeEstimates = listOf(
                "1-2 weeks",
                "1 month",
                "3 months",
                "6 months",
                "1 year",
                "2+ years"
            )

            ExposedDropdownMenuBox(
                expanded = expandedTime,
                onExpandedChange = { expandedTime = it }
            ) {
                OutlinedTextField(
                    value = timeEstimate,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Time Estimate (optional)") },
                    placeholder = { Text("Select time estimate") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTime) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expandedTime,
                    onDismissRequest = { expandedTime = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("None") },
                        onClick = {
                            timeEstimate = ""
                            expandedTime = false
                        }
                    )
                    timeEstimates.forEach { time ->
                        DropdownMenuItem(
                            text = { Text(time) },
                            onClick = {
                                timeEstimate = time
                                expandedTime = false
                            }
                        )
                    }
                }
            }

            HorizontalDivider()

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        niche = ""
                        type = "startup"
                        budget = ""
                        timeEstimate = ""
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Reset")
                }

                Button(
                    onClick = {
                        onFiltersChanged(
                            IdeaFilters(
                                niche = niche,
                                type = type,
                                budget = budget.toIntOrNull(),
                                timeEstimate = timeEstimate.ifBlank { null }
                            )
                        )
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Apply")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
