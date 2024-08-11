package com.toybrokers.ludo.core.domain.entities

import kotlin.random.Random

actual class UUID actual constructor(value: String) {
    actual companion object {
        actual fun randomUUID() = UUID(Random.nextBytes(16).joinToString(""))
    }
}