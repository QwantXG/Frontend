package com.example.myapplication

import TripDetailScreen
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun TravelApp() {
    val navController = rememberNavController()

    val tripViewModel: TripViewModel = viewModel()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("auth") { AuthScreen(navController) }
        composable("registration") { Registration(navController) }
        composable("welcome") { Welcome(navController) }
        composable("main") {
            MainScreen(navController, tripViewModel)
        }
        composable("trip_detail/{tripId}") { backStackEntry ->
            val tripId = backStackEntry.arguments?.getString("tripId")
            TripDetailScreen(tripId = tripId, navController = navController, tripViewModel = tripViewModel)
        }
        composable("trip_detail") {
            TripDetailScreen(tripId = null, navController = navController, tripViewModel = tripViewModel)
        }
        composable("create_trip") {
            Create_trip(navController, tripViewModel)
        }}}
