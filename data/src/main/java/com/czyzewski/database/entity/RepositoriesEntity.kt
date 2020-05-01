package com.czyzewski.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RepositoriesEntity(
    @PrimaryKey
    val userId: Long,
    val repos: List<RepositoryEntity>
)