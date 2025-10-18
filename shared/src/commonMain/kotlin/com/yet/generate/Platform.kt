package com.yet.generate

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform