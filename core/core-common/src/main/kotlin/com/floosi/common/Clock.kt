package com.floosi.common

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

interface Clock {
    fun now(): LocalDateTime
}

class SystemClock(
    private val timeZone: TimeZone = TimeZone.currentSystemDefault(),
) : Clock {

    override fun now(): LocalDateTime {
        return kotlinx.datetime.Clock.System.now().toLocalDateTime(timeZone)
    }
}
