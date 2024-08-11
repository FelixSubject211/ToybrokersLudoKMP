package com.toybrokers.ludo.core.domain.entities

import platform.Foundation.NSUUID

actual class UUID actual constructor(value: String) {
    actual companion object {
        actual fun randomUUID() = UUID(NSUUID().UUIDString())
    }
}