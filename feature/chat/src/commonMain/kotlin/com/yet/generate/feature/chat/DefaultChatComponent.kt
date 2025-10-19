package com.yet.generate.feature.chat

import com.app.common.decompose.asValue
import com.app.common.decompose.coroutineScope
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.yet.generate.feature.chat.model.ComplexityLevel
import com.yet.generate.feature.chat.model.IdeaFilters
import com.yet.generate.feature.chat.store.ChatStore
import com.yet.generate.feature.chat.store.ChatStoreFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent

class DefaultChatComponent(
    componentContext: ComponentContext,
) : ComponentContext by componentContext,
    ChatComponent,
    KoinComponent {
    
    private val store = instanceKeeper.getStore { ChatStoreFactory().create() }

    private val bottomSheetSlot = SlotNavigation<BottomSheetConfiguration>()


    override val model: Value<ChatComponent.Model> = store.asValue().map {
        ChatComponent.Model(
            messages = it.messages,
            inputText = it.inputText,
            isLoading = it.isLoading,
            selectedComplexity = it.selectedComplexity,
            filters = it.filters,
            error = it.error,
        )
    }

    override val childBottomSheetNavigation: Value<ChildSlot<*, ChatComponent.BottomSheetChild>> =
        childSlot(
            source = bottomSheetSlot,
            serializer = BottomSheetConfiguration.serializer(),
            handleBackButton = true,
            childFactory = ::createChildSheet,
        )

    init {
        coroutineScope().launch {
            store.labels.collect { label ->
                when (label) {
                    is ChatStore.Label.ShowError -> {
                        // Error is already in state, UI can display it
                    }
                }
            }
        }
    }

    override fun onInputTextChanged(text: String) {
        store.accept(ChatStore.Intent.UpdateInputText(text))
    }

    override fun onSendMessage() {
        store.accept(ChatStore.Intent.SendMessage)
    }

    override fun onGenerateRandom() {
        store.accept(ChatStore.Intent.GenerateRandomIdea)
    }

    override fun onComplexitySelected(complexity: ComplexityLevel) {
        store.accept(ChatStore.Intent.SelectComplexity(complexity))
    }

    override fun onFiltersUpdated(filters: IdeaFilters) {
        store.accept(ChatStore.Intent.UpdateFilters(filters))
        bottomSheetSlot.dismiss()
    }

    override fun onClearError() {
        store.accept(ChatStore.Intent.ClearError)
    }

    override fun onDismissBottomSheet() {
        bottomSheetSlot.dismiss()
    }

    override fun onFilterClick() {
        bottomSheetSlot.activate(BottomSheetConfiguration.Filter)
    }

    private fun createChildSheet(
        config: BottomSheetConfiguration,
        componentContext: ComponentContext,
    ): ChatComponent.BottomSheetChild =
        when (config) {
            BottomSheetConfiguration.Filter -> {
                @OptIn(ExperimentalCoroutinesApi::class)
                val currentFilters = store.stateFlow.value.filters
                ChatComponent.BottomSheetChild.FilterChild(
                    currentFilters = currentFilters,
                    onFiltersChanged = ::onFiltersUpdated,
                    onDismiss = ::onDismissBottomSheet
                )
            }
        }

    @Serializable
    private sealed interface BottomSheetConfiguration {
        @Serializable
        data object Filter : BottomSheetConfiguration
    }
}
