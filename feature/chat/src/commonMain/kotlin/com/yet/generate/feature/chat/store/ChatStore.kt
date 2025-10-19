package com.yet.generate.feature.chat.store

import com.arkivanov.mvikotlin.core.store.Store
import com.yet.generate.feature.chat.model.ChatMessage
import com.yet.generate.feature.chat.model.ComplexityLevel
import com.yet.generate.feature.chat.model.IdeaFilters

internal interface ChatStore : Store<ChatStore.Intent, ChatStore.State, ChatStore.Label> {
    
    data class State(
        val messages: List<ChatMessage> = emptyList(),
        val inputText: String = "",
        val isLoading: Boolean = false,
        val selectedComplexity: ComplexityLevel = ComplexityLevel.MEDIUM,
        val filters: IdeaFilters = IdeaFilters(),
        val error: String? = null
    )

    sealed class Intent {
        data class UpdateInputText(val text: String) : Intent()
        data object SendMessage : Intent()
        data object GenerateRandomIdea : Intent()
        data class SelectComplexity(val complexity: ComplexityLevel) : Intent()
        data class UpdateFilters(val filters: IdeaFilters) : Intent()
        data object ClearError : Intent()
    }

    sealed class Msg {
        data class InputTextChanged(val text: String) : Msg()
        data class MessageAdded(val message: ChatMessage) : Msg()
        data class LoadingChanged(val isLoading: Boolean) : Msg()
        data class ComplexitySelected(val complexity: ComplexityLevel) : Msg()
        data class FiltersUpdated(val filters: IdeaFilters) : Msg()
        data class ErrorOccurred(val error: String) : Msg()
        data object ErrorCleared : Msg()
    }

    sealed class Action {
        data object LoadInitialData : Action()
    }

    sealed class Label {
        data class ShowError(val message: String) : Label()
    }

}
