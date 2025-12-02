package com.example.myapplication

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun MainScreen(navController: NavController, tripViewModel: TripViewModel) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Поездки","Дом","one","two")

    Scaffold(
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, title ->
                    NavigationBarItem(
                        icon = {
                            when(index) {
                                0 -> Icon(Icons.Default.Flight, "Поездки")
                                1 -> Icon(Icons.Default.Home, "Дом")
                                2 -> Icon(Icons.Default.QuestionMark,"one")
                                3 -> Icon(Icons.Default.QuestionMark,"two")
                            }
                        },
                        label = { Text(title) },
                        selected = selectedTab == index,
                        onClick = { selectedTab = index }
                    )
                }
            }
        }) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when(selectedTab) {
                0 -> CreateTab(navController,tripViewModel)
                1 -> HomeTab()
                2 -> Tab1()
                3 -> Tab2()
            }
        }
    }
}



@Composable
fun HomeTab() {
    Text("Экран 1 ")
}

@Composable
fun Tab1(){
    Text("Экран 2")
}

@Composable
fun Tab2() {
    Text("Экран 3")
}