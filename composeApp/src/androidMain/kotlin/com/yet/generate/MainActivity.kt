package com.yet.generate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.defaultComponentContext
import com.yet.generate.feature.root.DefaultRootComponent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val root =
            DefaultRootComponent(
                componentContext = defaultComponentContext(),
            )

        setContent {
            App(root)
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}