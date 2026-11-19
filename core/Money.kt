package com.nexora.core

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

/** Los importes se guardan como Long en la unidad mínima (COP: 1 peso, sin decimales). */
object Money {
    private val symbols = DecimalFormatSymbols(Locale("es", "CO")).apply {
        groupingSeparator = '.'; decimalSeparator = ','
    }
    private val fmt = DecimalFormat("#,##0", symbols)

    fun format(amount: Long): String =
        (if (amount < 0) "-$" else "$") + fmt.format(kotlin.math.abs(amount))

    fun formatSigned(amount: Long): String =
        (if (amount >= 0) "+$" else "-$") + fmt.format(kotlin.math.abs(amount))
}
