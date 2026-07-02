package com.saba.cargarage.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/** Data-access object for the [Car] table. */
@Dao
interface CarDao {

    @Query("SELECT * FROM cars ORDER BY make, model")
    fun observeAll(): Flow<List<Car>>

    @Query("SELECT * FROM cars WHERE id = :id")
    fun observeById(id: Long): Flow<Car?>

    @Query("SELECT COUNT(*) FROM cars")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(car: Car): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cars: List<Car>)

    @Update
    suspend fun update(car: Car)

    @Delete
    suspend fun delete(car: Car)
}
