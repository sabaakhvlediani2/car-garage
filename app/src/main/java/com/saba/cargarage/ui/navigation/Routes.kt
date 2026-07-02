package com.saba.cargarage.ui.navigation

/** All navigation destinations in one place. */
object Routes {
    const val LIST = "list"
    const val ADD = "add"
    const val DETAIL = "detail/{carId}"
    const val EDIT = "edit/{carId}"
    const val ABOUT = "about"
    const val STATS = "stats"

    const val ARG_CAR_ID = "carId"

    fun detail(id: Long) = "detail/$id"
    fun edit(id: Long) = "edit/$id"
}
