package com.example.bookmanager.protocol.contract.details

import kotlinx.serialization.Serializable

interface ApiRequestBody

@Serializable
data object EmptyRequestBody : ApiRequestBody