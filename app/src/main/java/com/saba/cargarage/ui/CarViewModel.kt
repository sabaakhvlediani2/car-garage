package com.saba.cargarage.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.saba.cargarage.CarApplication
import com.saba.cargarage.data.Car
import com.saba.cargarage.data.CarRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * MVVM ViewModel. Owns all inventory state and the only place the UI mutates
 * data. The View (Compose screens) observes [cars] and calls the intent methods.
 */
class CarViewModel(private val repository: CarRepository) : ViewModel() {

    val cars: StateFlow<List<Car>> = repository.cars
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    init {
        // Populate demo data on the very first launch.
        viewModelScope.launch { repository.seedIfEmpty() }
    }

    /** Emits a single car by id (null while loading or if deleted). */
    fun carById(id: Long): StateFlow<Car?> = repository.car(id)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    fun addCar(car: Car) = viewModelScope.launch { repository.add(car) }

    fun updateCar(car: Car) = viewModelScope.launch { repository.update(car) }

    fun deleteCar(car: Car) = viewModelScope.launch { repository.delete(car) }

    companion object {
        /** Factory that pulls the shared repository from [CarApplication]. */
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CarApplication
                CarViewModel(app.repository)
            }
        }
    }
}
