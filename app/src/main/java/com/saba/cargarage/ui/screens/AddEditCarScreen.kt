package com.saba.cargarage.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.saba.cargarage.data.Car
import com.saba.cargarage.data.FuelType
import com.saba.cargarage.ui.CarViewModel

/**
 * Add / edit form. When [carId] is null it creates a new car; otherwise it
 * loads and updates the existing one. All fields are validated before saving.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditCarScreen(
    viewModel: CarViewModel,
    carId: Long?,
    onDone: () -> Unit
) {
    val editing = carId != null
    val existing by (if (editing) viewModel.carById(carId!!) else remember { viewModel.carById(-1) })
        .collectAsState()

    var make by rememberSaveable { mutableStateOf("") }
    var model by rememberSaveable { mutableStateOf("") }
    var year by rememberSaveable { mutableStateOf("") }
    var price by rememberSaveable { mutableStateOf("") }
    var mileage by rememberSaveable { mutableStateOf("") }
    var fuel by rememberSaveable { mutableStateOf(FuelType.PETROL) }
    var available by rememberSaveable { mutableStateOf(true) }
    var prefilled by rememberSaveable { mutableStateOf(false) }
    var showErrors by rememberSaveable { mutableStateOf(false) }

    // Prefill exactly once when the existing car arrives.
    LaunchedEffect(existing) {
        val c = existing
        if (editing && c != null && !prefilled) {
            make = c.make; model = c.model; year = c.year.toString()
            price = c.price.toString(); mileage = c.mileage.toString()
            fuel = c.fuelType; available = c.available; prefilled = true
        }
    }

    val yearInt = year.toIntOrNull()
    val priceDouble = price.toDoubleOrNull()
    val mileageInt = mileage.toIntOrNull()

    val makeError = make.isBlank()
    val modelError = model.isBlank()
    val yearError = yearInt == null || yearInt < 1900 || yearInt > 2100
    val priceError = priceDouble == null || priceDouble <= 0.0
    val mileageError = mileageInt == null || mileageInt < 0
    val formValid = !makeError && !modelError && !yearError && !priceError && !mileageError

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (editing) "Edit car" else "New car",
                        fontWeight = FontWeight.ExtraBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onDone) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(Modifier.height(4.dp))

            OutlinedTextField(
                value = make,
                onValueChange = { make = it },
                label = { Text("Make") },
                singleLine = true,
                isError = showErrors && makeError,
                supportingText = { if (showErrors && makeError) Text("Required") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = model,
                onValueChange = { model = it },
                label = { Text("Model") },
                singleLine = true,
                isError = showErrors && modelError,
                supportingText = { if (showErrors && modelError) Text("Required") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = year,
                onValueChange = { year = it.filter(Char::isDigit).take(4) },
                label = { Text("Year") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = showErrors && yearError,
                supportingText = { if (showErrors && yearError) Text("Enter a year between 1900 and 2100") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = price,
                onValueChange = { price = it.filter { ch -> ch.isDigit() || ch == '.' } },
                label = { Text("Price (USD)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = showErrors && priceError,
                supportingText = { if (showErrors && priceError) Text("Enter a price greater than 0") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = mileage,
                onValueChange = { mileage = it.filter(Char::isDigit) },
                label = { Text("Mileage (km)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = showErrors && mileageError,
                supportingText = { if (showErrors && mileageError) Text("Enter mileage (0 or more)") },
                modifier = Modifier.fillMaxWidth()
            )

            FuelDropdown(selected = fuel, onSelected = { fuel = it })

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Available for sale", style = MaterialTheme.typography.titleMedium)
                    Text(
                        if (available) "Shown as available" else "Marked as sold",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(checked = available, onCheckedChange = { available = it })
            }

            Button(
                onClick = {
                    showErrors = true
                    if (formValid) {
                        val car = Car(
                            id = if (editing) carId!! else 0,
                            make = make.trim(),
                            model = model.trim(),
                            year = yearInt!!,
                            price = priceDouble!!,
                            mileage = mileageInt!!,
                            fuelType = fuel,
                            available = available
                        )
                        if (editing) viewModel.updateCar(car) else viewModel.addCar(car)
                        onDone()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Text(if (editing) "Save changes" else "Add to inventory", fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FuelDropdown(selected: FuelType, onSelected: (FuelType) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = selected.label,
            onValueChange = {},
            readOnly = true,
            label = { Text("Fuel type") },
            trailingIcon = {
                Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            FuelType.entries.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.label) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}
