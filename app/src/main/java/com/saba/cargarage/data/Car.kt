package com.saba.cargarage.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Fuel type of a car. Stored in Room as its name via [Converters]. */
enum class FuelType(val label: String) {
    PETROL("Petrol"),
    DIESEL("Diesel"),
    HYBRID("Hybrid"),
    ELECTRIC("Electric")
}

/**
 * A single car in the garage inventory.
 * This is both the Room table row and the domain model used across the UI.
 */
@Entity(tableName = "cars")
data class Car(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val make: String,
    val model: String,
    val year: Int,
    val price: Double,
    val mileage: Int,
    val fuelType: FuelType,
    val available: Boolean = true
) {
    val title: String get() = "$make $model"
}
