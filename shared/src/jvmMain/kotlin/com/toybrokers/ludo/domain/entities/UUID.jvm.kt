package com.toybrokers.ludo.domain.entities

import java.util.UUID as JavaUUID

actual class UUID actual constructor(private val value: String) {
    actual companion object {
        actual fun randomUUID(): UUID = UUID(JavaUUID.randomUUID().toString())
    }

    actual override fun toString(): String {
        return value
    }
}