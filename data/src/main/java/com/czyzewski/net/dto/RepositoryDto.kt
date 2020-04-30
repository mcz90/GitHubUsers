package com.czyzewski.net.dto

import com.google.gson.annotations.SerializedName

data class RepositoryDto(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String
)
