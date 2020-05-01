package com.czyzewski.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RepositoryEntity(
    @PrimaryKey
    val repoId: Long,
    val name: String
)