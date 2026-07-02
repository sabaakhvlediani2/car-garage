package com.saba.cargarage.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.saba.cargarage.ui.CarViewModel
import com.saba.cargarage.ui.screens.AboutScreen
import com.saba.cargarage.ui.screens.AddEditCarScreen
import com.saba.cargarage.ui.screens.CarDetailScreen
import com.saba.cargarage.ui.screens.CarListScreen
import com.saba.cargarage.ui.screens.StatisticsScreen

/**
 * Navigation graph. A single [CarViewModel] is shared by all destinations so
 * that the inventory stays consistent across the list, detail and forms.
 */
@Composable
fun CarNavHost(
    navController: NavHostController,
    onOpenDrawer: () -> Unit
) {
    val viewModel: CarViewModel = viewModel(factory = CarViewModel.Factory)

    NavHost(navController = navController, startDestination = Routes.LIST) {

        composable(Routes.LIST) {
            CarListScreen(
                viewModel = viewModel,
                onOpenDrawer = onOpenDrawer,
                onAddClick = { navController.navigate(Routes.ADD) },
                onCarClick = { id -> navController.navigate(Routes.detail(id)) }
            )
        }

        composable(Routes.ADD) {
            AddEditCarScreen(
                viewModel = viewModel,
                carId = null,
                onDone = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument(Routes.ARG_CAR_ID) { type = NavType.LongType })
        ) { entry ->
            val id = entry.arguments?.getLong(Routes.ARG_CAR_ID) ?: return@composable
            CarDetailScreen(
                viewModel = viewModel,
                carId = id,
                onBack = { navController.popBackStack() },
                onEdit = { navController.navigate(Routes.edit(id)) },
                onDeleted = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.EDIT,
            arguments = listOf(navArgument(Routes.ARG_CAR_ID) { type = NavType.LongType })
        ) { entry ->
            val id = entry.arguments?.getLong(Routes.ARG_CAR_ID)
            AddEditCarScreen(
                viewModel = viewModel,
                carId = id,
                onDone = { navController.popBackStack() }
            )
        }

        composable(Routes.ABOUT) {
            AboutScreen(onOpenDrawer = onOpenDrawer)
        }

        composable(Routes.STATS) {
            StatisticsScreen(viewModel = viewModel, onOpenDrawer = onOpenDrawer)
        }
    }
}
