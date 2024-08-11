package com.toybrokers.ludo.core.domain.entities

expect class UUID(value: String) {
    companion object {
        fun randomUUID(): UUID
    }
}