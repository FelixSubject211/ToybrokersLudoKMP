package com.toybrokers.ludo.domain.entities

import java.util.UUID as JavaUUID

actual class UUID actual constructor(value: String) {
    actual companion object {
        actual fun randomUUID(): UUID = UUID(JavaUUID.randomUUID().toString())
    }
}