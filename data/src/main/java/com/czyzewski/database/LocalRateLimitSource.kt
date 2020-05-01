package com.czyzewski.database

import com.czyzewski.database.dao.RateLimitDao
import com.czyzewski.database.entity.RateLimitEntity
import com.czyzewski.models.RateLimitModel
import java.util.*

class LocalRateLimitSource(private val rateLimitDao: RateLimitDao) {
    suspend fun getRateLimit(): RateLimitEntity {
        return rateLimitDao.getRequestParams()
    }

    suspend fun saveRateLimit(model: RateLimitModel) {
        rateLimitDao.insert(
            RateLimitEntity(
                requestsLimit = model.limit,
                remainingRequest = model.remaining,
                timeUntilReset = Date(model.reset)
            )
        )
    }
}