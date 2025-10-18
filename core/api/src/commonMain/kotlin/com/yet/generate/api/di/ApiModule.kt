package com.yet.generate.api.di

import com.yet.generate.api.ApiClientBuilder
import com.yet.generate.api.IdeaGeneratorApi
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan("com.yet.generate.api")
class ApiModule{

    @Single
    fun provideIdeaGeneratorApi(): IdeaGeneratorApi {
        return ApiClientBuilder().build()
    }
}