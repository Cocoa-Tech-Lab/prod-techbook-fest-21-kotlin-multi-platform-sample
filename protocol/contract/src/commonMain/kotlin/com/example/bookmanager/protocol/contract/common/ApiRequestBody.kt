package com.example.bookmanager.protocol.contract.common

import kotlinx.serialization.Serializable

interface ApiRequestBody

@Serializable
data object EmptyRequestBody : ApiRequestBody