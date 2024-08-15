package com.toybrokers.ludo.domain.entities

expect class UUID(value: String) {
    companion object {
        fun randomUUID(): UUID
    }
}