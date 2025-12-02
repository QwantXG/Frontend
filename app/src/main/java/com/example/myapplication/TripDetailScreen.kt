package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun CreateTab(
    navController: NavController,
    tripViewModel: TripViewModel = viewModel()
)  {
    val trips by tripViewModel.trips.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFDD2D),
                        Color(0xFFFBE885),
                        Color(0xFFF5F5F5)
                    )
                )
            )
    )  {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4f)
                .padding(25.dp),
            verticalArrangement = Arrangement.Center
        )  {
            Text(
                text = "Куда отправимся?",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(bottom = 30.dp),
                color = Color(0xFF333333)
            )

            Card(
                onClick = {  },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .border(
                        width = 2.dp,
                         color = Color.Gray,
                        shape = RoundedCornerShape(16.dp)
                    ),
                colors = CardDefaults.cardColors(containerColor = Color.White)

            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                )

                {
                    Icon(imageVector = Icons.Default.Flight, "Создать",
                        modifier = Modifier.size(60.dp),
                        tint = Color(0xFF333333),)
                    Button(
                        onClick = { navController.navigate("create_trip") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFDD2D),
                            contentColor = Color(0xFF333333)
                        )
                    ) {
                        Text("Создать поездку", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                    }


                }
            }
        }


        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.6f)
                .padding(16.dp)
                .border(
                    width = 2.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(16.dp)
                ),
            shape = RoundedCornerShape(
                topStart = 32.dp,
                topEnd = 32.dp
            ),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp)
            ) {
                Text(
                    text = "Мои поездки",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(bottom = 24.dp),
                    color = Color(0xFF333333)
                )

                if (trips.isEmpty()) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "У вас пока нет поездок",
                            color = Color(0xFF666666),
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Создайте первую поездку!",
                            color = Color(0xFF999999),
                            fontSize = 14.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                } else {

                    LazyColumn {
                        items(trips) { trip ->
                            TripCard(
                                trip = trip,
                                onClick = {
                                    navController.navigate("trip_detail/${trip.id}")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}