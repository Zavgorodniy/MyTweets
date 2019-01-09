package com.zavgorodniy.mytweets.utils

enum class TokenType(val type: String) {
    TYPE_AUTH("auth"),
    TYPE_BEARER("bearer");

    operator fun invoke() = type
}