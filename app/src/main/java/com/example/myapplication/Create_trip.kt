package com.example.myapplication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

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
fun Create_trip(
    navController: NavController,
    tripViewModel: TripViewModel = viewModel()
)  {
    var start_town by remember { mutableStateOf("") }
    var end_town by remember { mutableStateOf("") }
    var start_date by remember { mutableStateOf("") }
    var end_date by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf("") }


    Box(
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
    ) {

        Text(
            text = "Путешествуй вместе с нами!",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF333333),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 140.dp)


        )




        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 700.dp)
                .align(Alignment.BottomCenter)
                .padding( 30.dp)
                .border(
                    width = 2.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(
                        topStart = 32.dp,
                        topEnd = 32.dp
                    )
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
                    .padding(32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                OutlinedTextField(
                    value = start_town,
                    onValueChange = { start_town = it },
                    label = { Text("Город отправления", fontWeight = FontWeight.ExtraBold) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = end_town,
                    onValueChange = { end_town = it },
                    label = { Text("Город прибытия", fontWeight = FontWeight.ExtraBold) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)

                )


                OutlinedTextField(
                    value = start_date,
                    onValueChange = { start_date= it },
                    label = { Text("Дата начала", fontWeight = FontWeight.ExtraBold) },
                    modifier = Modifier.width(150.dp),

                    shape = RoundedCornerShape(12.dp)


                )
                OutlinedTextField(
                    value = end_date,
                    onValueChange = { end_date = it },
                    label = { Text("Дата конца", fontWeight = FontWeight.ExtraBold) },
                    modifier = Modifier.width(150.dp).offset(x=155.dp).offset(y=-80.dp),

                    shape = RoundedCornerShape(12.dp)


                )

                OutlinedTextField(
                    value = budget,
                    onValueChange = { budget= it },
                    label = { Text("Планируемый бюджет", fontWeight = FontWeight.ExtraBold) },
                    modifier = Modifier.width(200.dp).offset(y=-80.dp),
                    shape = RoundedCornerShape(12.dp)

                )



                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (start_town.isNotEmpty() && end_town.isNotEmpty()) {
                            val newTrip = Trip(
                                startTown = start_town,
                                endTown = end_town,
                                startDate = start_date,
                                endDate = end_date,
                                budget = budget
                            )
                            tripViewModel.addTrip(newTrip)
                            navController.navigate("main")
                        }
                    },
                    modifier = Modifier.fillMaxWidth().offset(y = -90.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFDD2D),
                        contentColor = Color(0xFF333333)
                    )
                ) {
                    Text("Создать", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                }



                Button(
                    onClick = { navController.navigate("main") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = -90.dp)
                        .border(
                            width = 4.dp,
                            color = Color(0xFFFFDD2D),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFFFFF),
                        contentColor = Color(0xFF333333)
                    )
                ) {
                    Text(
                        "Список поездок",
                        fontWeight = FontWeight.ExtraBold, fontSize = 16.sp
                    )
                }



            }

        }
    }
}