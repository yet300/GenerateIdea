package com.yet.generate.ui.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.yet.generate.feature.root.RootComponent
import com.yet.generate.ui.chat.ChatScreen

@Composable
fun RootScreen(component: RootComponent) {
    val childStack by component.childStack.subscribeAsState()

    Children(
        stack = childStack,
    ) { child ->
        when (val instance = child.instance) {
            is RootComponent.Child.Chat -> ChatScreen(component = instance.component)
        }
    }
}