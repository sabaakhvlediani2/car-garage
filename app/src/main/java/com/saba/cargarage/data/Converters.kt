package com.saba.cargarage.data

import androidx.room.TypeConverter

/** Room type converters for enum columns. */
class Converters {
    @TypeConverter
    fun fuelToString(fuel: FuelType): String = fuel.name

    @TypeConverter
    fun stringToFuel(value: String): FuelType = FuelType.valueOf(value)
}
