package com.yet.generate.feature.chat

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import com.yet.generate.feature.chat.model.ChatMessage
import com.yet.generate.feature.chat.model.ComplexityLevel
import com.yet.generate.feature.chat.model.IdeaFilters

interface ChatComponent {
    val model: Value<Model>
    val childBottomSheetNavigation: Value<ChildSlot<*, BottomSheetChild>>

    fun onInputTextChanged(text: String)
    fun onSendMessage()
    fun onGenerateRandom()
    fun onComplexitySelected(complexity: ComplexityLevel)
    fun onFiltersUpdated(filters: IdeaFilters)
    fun onClearError()
    fun onDismissBottomSheet()
    fun onFilterClick()

    data class Model(
        val messages: List<ChatMessage>,
        val inputText: String,
        val isLoading: Boolean,
        val selectedComplexity: ComplexityLevel,
        val filters: IdeaFilters,
        val error: String?
    )

    sealed interface BottomSheetChild {
        data class FilterChild(
            val currentFilters: IdeaFilters,
            val onFiltersChanged: (IdeaFilters) -> Unit,
            val onDismiss: () -> Unit
        ) : BottomSheetChild
    }
}
