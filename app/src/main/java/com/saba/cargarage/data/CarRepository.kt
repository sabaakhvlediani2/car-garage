package com.saba.cargarage.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository — the single API the ViewModel talks to. It hides Room behind a
 * clean interface, which keeps the MVVM layers decoupled and testable.
 */
class CarRepository(private val dao: CarDao) {

    val cars: Flow<List<Car>> = dao.observeAll()

    fun car(id: Long): Flow<Car?> = dao.observeById(id)

    suspend fun add(car: Car): Long = dao.insert(car)

    suspend fun update(car: Car) = dao.update(car)

    suspend fun delete(car: Car) = dao.delete(car)

    /** Seeds a few demo cars the very first time the app runs (empty DB). */
    suspend fun seedIfEmpty() {
        if (dao.count() == 0) {
            dao.insertAll(SeedData.cars)
        }
    }
}
