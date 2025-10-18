package com.yet.generate.data.di

import com.yet.generate.api.di.ApiModule
import com.yet.generate.data.repository.IdeaRepositoryImpl
import com.yet.generate.domain.repository.IdeaRepository
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module(includes = [ApiModule::class])
@ComponentScan("com.yet.generate.data")
class DataModule
