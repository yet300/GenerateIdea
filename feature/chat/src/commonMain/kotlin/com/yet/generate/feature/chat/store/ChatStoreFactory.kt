package com.yet.generate.feature.chat.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.yet.generate.domain.repository.IdeaRepository
import com.yet.generate.feature.chat.model.ChatMessage
import com.yet.generate.feature.chat.model.ComplexityLevel
import com.yet.generate.feature.chat.model.IdeaDetails
import com.yet.generate.feature.chat.model.IdeaFilters
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

internal class ChatStoreFactory : KoinComponent {
    private val storeFactory: StoreFactory by inject()
    private val ideaRepository: IdeaRepository by inject()

    fun create(): ChatStore =
        object :
            ChatStore,
            Store<ChatStore.Intent, ChatStore.State, ChatStore.Label> by storeFactory.create(
                name = "ChatStore",
                initialState = ChatStore.State(),
                bootstrapper = SimpleBootstrapper(ChatStore.Action.LoadInitialData),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl,
            ) {}

    private object ReducerImpl : Reducer<ChatStore.State, ChatStore.Msg> {
        override fun ChatStore.State.reduce(msg: ChatStore.Msg): ChatStore.State =
            when (msg) {
                is ChatStore.Msg.InputTextChanged -> copy(inputText = msg.text)
                is ChatStore.Msg.MessageAdded -> copy(
                    messages = messages + msg.message,
                    inputText = ""
                )
                is ChatStore.Msg.LoadingChanged -> copy(isLoading = msg.isLoading)
                is ChatStore.Msg.ComplexitySelected -> copy(selectedComplexity = msg.complexity)
                is ChatStore.Msg.FiltersUpdated -> copy(filters = msg.filters)
                is ChatStore.Msg.ErrorOccurred -> copy(error = msg.error, isLoading = false)
                ChatStore.Msg.ErrorCleared -> copy(error = null)
            }
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<ChatStore.Intent, ChatStore.Action, ChatStore.State, ChatStore.Msg, ChatStore.Label>() {
        
        override fun executeAction(action: ChatStore.Action) {
            when (action) {
                ChatStore.Action.LoadInitialData -> loadInitialData()
            }
        }

        override fun executeIntent(intent: ChatStore.Intent) {
            when (intent) {
                is ChatStore.Intent.UpdateInputText -> updateInputText(intent.text)
                ChatStore.Intent.SendMessage -> sendMessage()
                ChatStore.Intent.GenerateRandomIdea -> generateRandomIdea()
                is ChatStore.Intent.SelectComplexity -> selectComplexity(intent.complexity)
                is ChatStore.Intent.UpdateFilters -> updateFilters(intent.filters)
                ChatStore.Intent.ClearError -> dispatch(ChatStore.Msg.ErrorCleared)
            }
        }

        private fun loadInitialData() {
            // No initial message - start with empty screen
        }

        private fun updateInputText(text: String) {
            dispatch(ChatStore.Msg.InputTextChanged(text))
        }

        @OptIn(ExperimentalUuidApi::class)
        private fun sendMessage() {
            val currentState = state()
            val userInput = currentState.inputText.trim()
            
            if (userInput.isEmpty()) return

            // Add user message
            val userMessage = ChatMessage(
                id = Uuid.random().toString(),
                text = userInput,
                isUser = true
            )
            dispatch(ChatStore.Msg.MessageAdded(userMessage))
            dispatch(ChatStore.Msg.LoadingChanged(true))

            // Generate idea based on user input
            scope.launch {
                try {
                    val filters = currentState.filters
                    val complexity = currentState.selectedComplexity.toComplexity()
                    
                    val result = ideaRepository.generateIdea(
                        niche = userInput,
                        type = filters.type,
                        budget = filters.budget,
                        complexityConstraints = complexity,
                        timeEstimate = filters.timeEstimate
                    )

                    result.onSuccess { response ->
                        val ideaMessage = ChatMessage(
                            id = Uuid.random().toString(),
                            text = response.idea,
                            isUser = false,
                            ideaDetails = IdeaDetails(
                                type = response.type,
                                complexity = response.complexity,
                                tags = response.tags,
                                timeEstimate = response.timeEstimate,
                                unique = response.unique,
                                similarityScore = response.similarityScore
                            )
                        )
                        dispatch(ChatStore.Msg.MessageAdded(ideaMessage))
                        dispatch(ChatStore.Msg.LoadingChanged(false))
                    }.onFailure { error ->
                        dispatch(ChatStore.Msg.ErrorOccurred(
                            error.message ?: "Failed to generate idea"
                        ))
                        publish(ChatStore.Label.ShowError(
                            error.message ?: "Failed to generate idea"
                        ))
                    }
                } catch (e: Exception) {
                    dispatch(ChatStore.Msg.ErrorOccurred(
                        e.message ?: "An unexpected error occurred"
                    ))
                    publish(ChatStore.Label.ShowError(
                        e.message ?: "An unexpected error occurred"
                    ))
                }
            }
        }

        @OptIn(ExperimentalUuidApi::class)
        private fun generateRandomIdea() {
            dispatch(ChatStore.Msg.LoadingChanged(true))

            // Add system message
            val systemMessage = ChatMessage(
                id = Uuid.random().toString(),
                text = "Generating a random idea for you...",
                isUser = false
            )
            dispatch(ChatStore.Msg.MessageAdded(systemMessage))

            scope.launch {
                try {
                    val currentState = state()
                    val filters = currentState.filters
                    
                    val result = ideaRepository.generateRandom(
                        count = 1,
                        filters = buildMap {
                            put("type", filters.type)
                            filters.timeEstimate?.let { put("time_estimate", it) }
                        }
                    )

                    result.onSuccess { response ->
                        if (response.ideas.isNotEmpty()) {
                            val idea = response.ideas.first()
                            val ideaMessage = ChatMessage(
                                id = Uuid.random().toString(),
                                text = idea.idea,
                                isUser = false,
                                ideaDetails = IdeaDetails(
                                    type = idea.type,
                                    complexity = idea.complexity,
                                    tags = idea.tags,
                                    timeEstimate = idea.timeEstimate,
                                    unique = idea.unique,
                                    similarityScore = idea.similarityScore
                                )
                            )
                            dispatch(ChatStore.Msg.MessageAdded(ideaMessage))
                        }
                        dispatch(ChatStore.Msg.LoadingChanged(false))
                    }.onFailure { error ->
                        dispatch(ChatStore.Msg.ErrorOccurred(
                            error.message ?: "Failed to generate random idea"
                        ))
                        publish(ChatStore.Label.ShowError(
                            error.message ?: "Failed to generate random idea"
                        ))
                    }
                } catch (e: Exception) {
                    dispatch(ChatStore.Msg.ErrorOccurred(
                        e.message ?: "An unexpected error occurred"
                    ))
                    publish(ChatStore.Label.ShowError(
                        e.message ?: "An unexpected error occurred"
                    ))
                }
            }
        }

        private fun selectComplexity(complexity: ComplexityLevel) {
            dispatch(ChatStore.Msg.ComplexitySelected(complexity))
        }

        private fun updateFilters(filters: IdeaFilters) {
            dispatch(ChatStore.Msg.FiltersUpdated(filters))
        }
    }
}
