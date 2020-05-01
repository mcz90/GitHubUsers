package com.czyzewski

import com.czyzewski.models.RateLimitModel
import com.czyzewski.ui.models.RateLimitUiModel
import java.text.DateFormat


fun RateLimitModel.toUiModel(): RateLimitUiModel {
    val formatter: DateFormat = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
    formatter.timeZone = java.util.TimeZone.getTimeZone("GMT+2")
    val timeToReset = formatter.format(reset)
    return RateLimitUiModel(
        timeToReset = timeToReset,
        remaining = remaining,
        total = limit
    )
}