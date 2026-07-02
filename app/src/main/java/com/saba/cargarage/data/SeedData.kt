package com.saba.cargarage.data

/** Initial demo inventory so the list and charts are not empty on first launch. */
object SeedData {
    val cars = listOf(
        Car(make = "Toyota", model = "Camry", year = 2021, price = 24500.0, mileage = 32000, fuelType = FuelType.PETROL),
        Car(make = "Tesla", model = "Model 3", year = 2023, price = 41900.0, mileage = 12000, fuelType = FuelType.ELECTRIC),
        Car(make = "BMW", model = "320d", year = 2019, price = 27800.0, mileage = 68000, fuelType = FuelType.DIESEL),
        Car(make = "Toyota", model = "Prius", year = 2022, price = 28900.0, mileage = 21000, fuelType = FuelType.HYBRID),
        Car(make = "Mercedes", model = "C200", year = 2020, price = 33500.0, mileage = 45000, fuelType = FuelType.PETROL),
        Car(make = "Nissan", model = "Leaf", year = 2021, price = 22300.0, mileage = 27000, fuelType = FuelType.ELECTRIC, available = false),
        Car(make = "Volkswagen", model = "Golf", year = 2018, price = 16400.0, mileage = 89000, fuelType = FuelType.DIESEL)
    )
}
