package com.czyzewski.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import javax.annotation.Nullable

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: Long,
    val login: String,
    val nodeId: String,
    val avatarUrl: String,
    val shouldSync:Boolean,
    val repositories: List<RepositoryEntity>
)