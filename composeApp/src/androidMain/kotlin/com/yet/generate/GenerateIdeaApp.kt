package com.yet.generate

import android.app.Application
import com.yet.generate.di.InitKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class GenerateIdeaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        InitKoin {
            androidLogger()
            androidContext(this@GenerateIdeaApp)
        }
    }
}