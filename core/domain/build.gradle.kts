plugins {
    alias(libs.plugins.local.kotlin.multiplatform)
}

kotlin {

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}