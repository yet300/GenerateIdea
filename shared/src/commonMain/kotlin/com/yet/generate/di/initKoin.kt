package com.yet.generate.di

import com.app.common.di.CommonModule
import com.yet.generate.data.di.DataModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.ksp.generated.module
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
fun InitKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        modules(

            DataModule().module,
            CommonModule().module,
        )
        config?.invoke(this)
    }
}