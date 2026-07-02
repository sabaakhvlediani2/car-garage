package com.saba.cargarage

import android.app.Application
import com.saba.cargarage.data.CarDatabase
import com.saba.cargarage.data.CarRepository

/**
 * Application holder — lazily builds the Room database and the repository once,
 * and hands the repository to the ViewModel factory.
 */
class CarApplication : Application() {
    val database: CarDatabase by lazy { CarDatabase.getInstance(this) }
    val repository: CarRepository by lazy { CarRepository(database.carDao()) }
}
