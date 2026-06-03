package com.floosi.common

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Locale

object AmountFormatter {

    private val format: NumberFormat = NumberFormat.getNumberInstance(Locale("ar", "EG")).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
        isGroupingUsed = true
    }

    fun format(amount: BigDecimal, currencySymbol: String = "ج.م"): String {
        val formatted = format.format(amount.setScale(2, RoundingMode.HALF_UP))
        return "$formatted $currencySymbol"
    }
}
