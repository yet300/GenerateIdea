package com.yet.generate.feature.chat

import com.app.common.decompose.PreviewComponentContext
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.webhistory.WebNavigationOwner
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.yet.generate.domain.model.Complexity
import com.yet.generate.feature.chat.model.ChatMessage
import com.yet.generate.feature.chat.model.ComplexityLevel
import com.yet.generate.feature.chat.model.IdeaDetails
import com.yet.generate.feature.chat.model.IdeaFilters

@OptIn(ExperimentalDecomposeApi::class)
class PreviewChatComponent : ChatComponent,
    ComponentContext by PreviewComponentContext,
    WebNavigationOwner.NoOp {
    
    override val model: Value<ChatComponent.Model> = MutableValue(
        ChatComponent.Model(
            messages = listOf(
                ChatMessage(
                    id = "1",
                    text = "Welcome! I can help you generate creative ideas.",
                    isUser = false
                ),
                ChatMessage(
                    id = "2",
                    text = "AI in healthcare",
                    isUser = true
                ),
                ChatMessage(
                    id = "3",
                    text = "Create a telemedicine platform that uses AI to provide preliminary diagnoses...",
                    isUser = false,
                    ideaDetails = IdeaDetails(
                        type = "startup",
                        complexity = Complexity(
                            mental = "high",
                            implementation = "high",
                            teamSize = "5+"
                        ),
                        tags = listOf("AI", "Healthcare", "Telemedicine"),
                        timeEstimate = "6-12 months",
                        unique = true,
                        similarityScore = 0.85
                    )
                )
            ),
            inputText = "",
            isLoading = false,
            selectedComplexity = ComplexityLevel.MEDIUM,
            filters = IdeaFilters(),
            error = null
        )
    )

    override val childBottomSheetNavigation: Value<ChildSlot<*, ChatComponent.BottomSheetChild>> =
        MutableValue(ChildSlot<Any, ChatComponent.BottomSheetChild>(null))

    override fun onInputTextChanged(text: String) {}
    override fun onSendMessage() {}
    override fun onGenerateRandom() {}
    override fun onComplexitySelected(complexity: ComplexityLevel) {}
    override fun onFiltersUpdated(filters: IdeaFilters) {}
    override fun onClearError() {}
    override fun onDismissBottomSheet() {}
    override fun onFilterClick() {}
}
