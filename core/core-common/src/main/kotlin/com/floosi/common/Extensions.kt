package com.floosi.common

import kotlinx.datetime.LocalDateTime
import java.math.BigDecimal
import java.math.RoundingMode

fun BigDecimal.isPositive(): Boolean = this > BigDecimal.ZERO

fun BigDecimal.isNegative(): Boolean = this < BigDecimal.ZERO

fun BigDecimal.toFormattedString(): String = setScale(2, RoundingMode.HALF_UP).toString()

fun LocalDateTime.toDateOnlyString(): String {
    return buildString {
        append(year)
        append('-')
        append(monthNumber.toString().padStart(2, '0'))
        append('-')
        append(dayOfMonth.toString().padStart(2, '0'))
    }
}
