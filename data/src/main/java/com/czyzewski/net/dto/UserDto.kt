package com.czyzewski.net.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class UserDto(
    val login: String,
    val id: Long,
    @SerialName("node_id")
    val nodeId: String,
    @SerialName("avatar_url")
    val avatarUrl: String
)