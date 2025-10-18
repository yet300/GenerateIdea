package com.yet.generate.feature.root

import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.webhistory.WebNavigationOwner
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import com.yet.generate.feature.chat.ChatComponent

@OptIn(ExperimentalDecomposeApi::class)
interface RootComponent :
    BackHandlerOwner,
    WebNavigationOwner {
    val childStack: Value<ChildStack<*, Child>>

    fun onBackClicked()

    sealed class Child {
        data class Chat(
            val component: ChatComponent,
        ) : Child()
    }
}
