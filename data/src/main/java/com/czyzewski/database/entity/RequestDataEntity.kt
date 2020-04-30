package com.czyzewski.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "requestData")
data class RequestDataEntity(
    @PrimaryKey
    val id: Long = 0,
    val nextRemainingParam: Long = 0,
    val allLoaded: Boolean = false
)
