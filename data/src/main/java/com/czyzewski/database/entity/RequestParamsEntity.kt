package com.czyzewski.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "requestData")
data class RequestParamsEntity(
    @PrimaryKey
    val id: Long = 0,
    val sinceParam: Long = 0L,
    val allLoaded: Boolean = false
)
