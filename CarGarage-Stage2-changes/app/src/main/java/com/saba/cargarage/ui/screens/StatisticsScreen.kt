package com.saba.cargarage.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.saba.cargarage.stats.CarStats
import com.saba.cargarage.ui.CarViewModel
import com.saba.cargarage.ui.components.BarChart
import com.saba.cargarage.ui.components.DonutChart
import com.saba.cargarage.ui.theme.ChartDiesel
import com.saba.cargarage.ui.theme.ChartElectric
import com.saba.cargarage.ui.theme.ChartHybrid
import com.saba.cargarage.ui.theme.ChartPetrol
import com.saba.cargarage.util.asPrice

/**
 * Stage 2 feature — the analytics dashboard. Reads the live inventory from the
 * shared [CarViewModel], reduces it to [CarStats] and visualises it with the
 * custom-drawn donut and bar charts.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    viewModel: CarViewModel,
    onOpenDrawer: () -> Unit
) {
    val cars by viewModel.cars.collectAsState()
    val stats = CarStats.from(cars)
    val fuelColors = listOf(ChartPetrol, ChartDiesel, ChartHybrid, ChartElectric)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Statistics", fontWeight = FontWeight.ExtraBold) },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
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
        if (cars.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Add cars to see statistics", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Summary tiles
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SummaryTile("Total cars", stats.total.toString(), Modifier.weight(1f))
                SummaryTile("Available", "${stats.available}/${stats.total}", Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SummaryTile("Inventory value", stats.totalValue.asPrice(), Modifier.weight(1f))
                SummaryTile("Avg. price", stats.averagePrice.asPrice(), Modifier.weight(1f))
            }

            ChartCard(title = "Cars by fuel type") {
                DonutChart(
                    entries = stats.countByFuel.filter { it.value > 0f },
                    colors = fuelColorsFor(stats),
                    centerLabel = stats.total.toString(),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            ChartCard(title = "Inventory value by fuel type") {
                BarChart(
                    entries = stats.valueByFuel,
                    barColors = fuelColors,
                    valueAsInt = false
                )
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

/** Colors aligned to the non-empty slices shown in the donut. */
private fun fuelColorsFor(stats: CarStats): List<androidx.compose.ui.graphics.Color> {
    val all = listOf(ChartPetrol, ChartDiesel, ChartHybrid, ChartElectric)
    return stats.countByFuel.mapIndexedNotNull { index, entry ->
        if (entry.value > 0f) all[index % all.size] else null
    }
}

@Composable
private fun SummaryTile(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(4.dp))
            Text(
                value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun ChartCard(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(16.dp))
            content()
        }
    }
}
