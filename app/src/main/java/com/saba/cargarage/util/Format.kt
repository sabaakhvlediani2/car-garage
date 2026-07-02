package com.saba.cargarage.util

import java.util.Locale

/** "$24,500" — grouped, no decimals. */
fun Double.asPrice(): String =
    "$" + String.format(Locale.US, "%,.0f", this)

/** "32,000 km" */
fun Int.asMileage(): String =
    String.format(Locale.US, "%,d km", this)
