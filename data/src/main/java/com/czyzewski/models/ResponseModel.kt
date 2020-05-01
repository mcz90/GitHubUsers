package com.czyzewski.models

@Suppress("UNCHECKED_CAST")
data class ResponseModel<Dto>(
    val body: Dto,
    val requestQuery: RequestQueryModel
)