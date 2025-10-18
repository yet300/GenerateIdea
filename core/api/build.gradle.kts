import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import java.util.Properties

plugins {
    alias(libs.plugins.local.kotlin.multiplatform)
    alias(libs.plugins.local.koin)
    alias(libs.plugins.buildkonfig)
}

kotlin {

    sourceSets {
        commonMain.dependencies {
            implementation(libs.bundles.ktor)
            implementation(libs.kotlinx.serialization.json)
        }
        
        androidMain.dependencies {
            implementation(libs.ktor.client.android)
        }
        
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        
        desktopMain.dependencies {
            implementation(libs.ktor.client.cio)
        }
    }
}

val secretProperties = Properties().apply {
    load(File(rootDir, "secret.properties").inputStream())
}
val apiBaseUrl = secretProperties.getProperty("API_BASE_URL")

buildkonfig {
    packageName = "com.yet.generate.api"
    
    defaultConfigs {
        buildConfigField(STRING, "API_BASE_URL", "\"$apiBaseUrl\"")
    }
}