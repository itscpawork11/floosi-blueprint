package com.floosi.domain.model

data class Currency(
    val code: String,
    val name: String,
    val symbol: String,
    val decimalPlaces: Int,
) {
    companion object {
        val EGP: Currency = Currency("EGP", "Egyptian Pound", "ج.م", 2)
        val USD: Currency = Currency("USD", "US Dollar", "$", 2)
        val EUR: Currency = Currency("EUR", "Euro", "€", 2)
        val SAR: Currency = Currency("SAR", "Saudi Riyal", "﷼", 2)
        val AED: Currency = Currency("AED", "UAE Dirham", "د.إ", 2)
        val DEFAULT: Currency = EGP
    }
}
