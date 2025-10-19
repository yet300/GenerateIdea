package com.yet.generate.feature.chat.model

import kotlin.time.Clock
import kotlin.time.ExperimentalTime

data class ChatMessage @OptIn(ExperimentalTime::class) constructor(
    val id: String,
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = Clock.System.now().epochSeconds,
    val ideaDetails: IdeaDetails? = null
)