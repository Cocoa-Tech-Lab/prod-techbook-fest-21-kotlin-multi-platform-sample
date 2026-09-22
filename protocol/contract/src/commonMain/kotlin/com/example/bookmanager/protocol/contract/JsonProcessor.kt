package com.example.bookmanager.protocol.contract

import kotlinx.serialization.json.Json

// DTOと同一モジュールで宣言することで、
// このjsonProcessorはdtoを自動で認識できる
val jsonProcessor: Json = Json {
    ignoreUnknownKeys = true
}