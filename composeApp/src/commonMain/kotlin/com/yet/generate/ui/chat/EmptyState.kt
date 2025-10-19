package com.yet.generate.ui.chat

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yet.generate.feature.chat.model.ComplexityLevel
import com.yet.generate.ui.component.EnumSegmentedButtonRow



@Composable
fun EmptyState(
    selectedComplexity: ComplexityLevel,
    onComplexitySelected: (ComplexityLevel) -> Unit,
    onGenerateRandom: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Complexity selector
        EnumSegmentedButtonRow(
            selectedValue = selectedComplexity,
            onValueChange = onComplexitySelected,
            getLabel = { it.displayName }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Generate random button
        Button(
            onClick = onGenerateRandom,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(56.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Lightbulb,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Generate Random Idea")
        }
    }
}