import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import java.util.Properties

plugins {
    alias(libs.plugins.local.kotlin.multiplatform)
    alias(libs.plugins.local.koin)
    alias(libs.plugins.ktor.fit)
    alias(libs.plugins.buildkonfig)
}

kotlin {

    sourceSets {
        commonMain.dependencies {
            implementation(libs.bundles.ktorfit)
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