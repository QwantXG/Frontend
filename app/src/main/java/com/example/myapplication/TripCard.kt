package com.example.myapplication

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TripCard(trip: Trip, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "${trip.startTown} ✈ ${trip.endTown}",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF333333)
            )
            Text(
                text = "${trip.startDate} - ${trip.endDate}",
                color = Color(0xFF666666),
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = "Бюджет: ${trip.budget}",
                color = Color(0xFF666666),
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 2.dp)
            )
            if (trip.participants.isNotEmpty()) {
                Text(
                    text = "Участников: ${trip.participants.size}",
                    color = Color(0xFF666666),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}