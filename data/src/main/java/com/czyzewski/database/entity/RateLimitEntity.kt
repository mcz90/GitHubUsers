package com.czyzewski.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.czyzewski.models.RateLimitModel
import java.util.*


@Entity(tableName = "rateLimit")
data class RateLimitEntity(
    @PrimaryKey
    val id: Long = 0,
    val requestsLimit: Long = 0,
    val remainingRequest: Long = 0,
    val timeUntilReset: Date = Date()
) {
    fun toModel() = RateLimitModel(
        limit = requestsLimit,
        remaining = remainingRequest,
        reset = timeUntilReset.time
    )
}
