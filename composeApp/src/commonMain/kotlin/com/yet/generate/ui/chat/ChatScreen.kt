package com.yet.generate.ui.chat

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.yet.generate.feature.chat.ChatComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    component: ChatComponent,
    modifier: Modifier = Modifier
) {
    val model by component.model.subscribeAsState()
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val childSlot by component.childBottomSheetNavigation.subscribeAsState()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Idea Generator") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            InputArea(
                inputText = model.inputText,
                isLoading = model.isLoading,
                onInputTextChanged = component::onInputTextChanged,
                onSendMessage = component::onSendMessage,
                onFilterClick = component::onFilterClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Messages list
            Box(modifier = Modifier.weight(1f)) {
                if (model.messages.isEmpty() && !model.isLoading) {
                    EmptyState(
                        selectedComplexity = model.selectedComplexity,
                        onComplexitySelected = component::onComplexitySelected,
                        onGenerateRandom = component::onGenerateRandom,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    MessagesList(
                        messages = model.messages,
                        isLoading = model.isLoading,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            // Error message
            model.error?.let { error ->
                ErrorBanner(
                    error = error,
                    onDismiss = component::onClearError
                )
            }
        }
    }

    // Filter bottom sheet
    childSlot.child?.instance?.let { child ->
        when (child) {
            is ChatComponent.BottomSheetChild.FilterChild -> {
                FilterBottomSheet(
                    sheetState = bottomSheetState,
                    currentFilters = child.currentFilters,
                    onFiltersChanged = child.onFiltersChanged,
                    onDismiss = child.onDismiss
                )
            }
        }
    }
}