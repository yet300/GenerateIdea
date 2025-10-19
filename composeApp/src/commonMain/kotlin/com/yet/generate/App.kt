package com.yet.generate

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.yet.generate.feature.root.PreviewRootComponent
import com.yet.generate.feature.root.RootComponent
import com.yet.generate.ui.root.RootScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(root: RootComponent = PreviewRootComponent()) {
    MaterialTheme {
        RootScreen(root)
    }
}