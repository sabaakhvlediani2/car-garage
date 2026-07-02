package com.saba.cargarage.stats

import com.saba.cargarage.data.Car
import com.saba.cargarage.data.FuelType

/** One bar/slice: a label, a numeric value and the raw count behind it. */
data class ChartEntry(val label: String, val value: Float, val count: Int)

/**
 * Pure, testable summary of the inventory used by the statistics screen.
 * Kept out of the UI layer so the charts only render, never compute.
 */
data class CarStats(
    val total: Int,
    val available: Int,
    val sold: Int,
    val totalValue: Double,
    val averagePrice: Double,
    val countByFuel: List<ChartEntry>,
    val valueByFuel: List<ChartEntry>
) {
    companion object {
        fun from(cars: List<Car>): CarStats {
            val total = cars.size
            val available = cars.count { it.available }
            val totalValue = cars.sumOf { it.price }

            val countByFuel = FuelType.entries.map { fuel ->
                val group = cars.filter { it.fuelType == fuel }
                ChartEntry(fuel.label, group.size.toFloat(), group.size)
            }
            val valueByFuel = FuelType.entries.map { fuel ->
                val group = cars.filter { it.fuelType == fuel }
                ChartEntry(fuel.label, group.sumOf { it.price }.toFloat(), group.size)
            }

            return CarStats(
                total = total,
                available = available,
                sold = total - available,
                totalValue = totalValue,
                averagePrice = if (total == 0) 0.0 else totalValue / total,
                countByFuel = countByFuel,
                valueByFuel = valueByFuel
            )
        }
    }
}
