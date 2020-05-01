package com.czyzewski.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val userId: Long,
    val login: String,
    val nodeId: String,
    val avatarUrl: String,
    val shouldSync: Boolean,
    val repositories: RepositoriesEntity
)