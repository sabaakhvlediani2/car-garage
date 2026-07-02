package com.saba.cargarage.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/** Room database — single source of truth for car inventory. */
@Database(entities = [Car::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class CarDatabase : RoomDatabase() {

    abstract fun carDao(): CarDao

    companion object {
        @Volatile
        private var INSTANCE: CarDatabase? = null

        fun getInstance(context: Context): CarDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    CarDatabase::class.java,
                    "car_garage.db"
                ).build().also { INSTANCE = it }
            }
    }
}
