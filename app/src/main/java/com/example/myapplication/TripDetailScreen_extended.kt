import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.myapplication.Trip
import com.example.myapplication.TripViewModel

@Composable
fun TripDetailScreen(
    tripId: String?,
    navController: NavHostController,
    tripViewModel: TripViewModel
) {
    var trip by remember { mutableStateOf<Trip?>(null) }

    LaunchedEffect(tripId) {
        if (tripId != null) {
            trip = tripViewModel.getTripById(tripId)
        }
    }

    if (trip == null) {
        Text("Поездка не найдена")
        return
    }

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
    ) {

        var selectedTab by remember { mutableStateOf(0) }
        val tabs = listOf("Участники", "Финансы", "Отчет")

        TabRow(
            selectedTabIndex = selectedTab,
            modifier = Modifier.fillMaxWidth()
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = selectedTab == index,
                    onClick = { selectedTab = index }
                )
            }
        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "${trip!!.startTown} ✈ ${trip!!.endTown}",
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF333333)
            )

            Text(
                text = "Даты: ${trip!!.startDate} - ${trip!!.endDate}",
                fontSize = 16.sp,
                color = Color(0xFF666666),
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(top = 4.dp)
            )

            Text(
                text = "Бюджет: ${trip!!.budget}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF666666),
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            when (selectedTab) {
                0 -> ParticipantsTab(trip!!, tripViewModel, navController)
                1 -> FinanceTab(trip!!, tripViewModel)
                2 -> ReportTab(trip!!)
            }
        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Button(
                    onClick = { navController.navigate("main") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFFBE7),
                        contentColor = Color(0xFF333333)
                    )
                ) {
                    Text(
                        "Список поездок",
                        fontWeight = FontWeight.SemiBold
                    )
                }


                Button(
                    onClick = { navController.navigate("edit_trip/${trip!!.id}") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFFBE7),
                        contentColor = Color(0xFF333333)
                    )
                ) {
                    Text(
                        "Редактировать",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun ParticipantsTab(trip: Trip, tripViewModel: TripViewModel, navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            "Участники:",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color(0xFF333333)
        )

        if (trip.participants.isEmpty()) {
            Text(
                "Участники пока не добавлены",
                modifier = Modifier.padding(top = 16.dp),
                color = Color(0xFF666666)
            )
        } else {
            Column(modifier = Modifier.padding(top = 16.dp)) {
                trip.participants.forEach { user ->
                    Text(
                        "• ${user.name} (${user.email})",
                        modifier = Modifier.padding(vertical = 4.dp),
                        color = Color(0xFF333333)
                    )
                }
            }
        }

        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFFCF7),
                contentColor = Color(0xFF333333)
            )
        ) {
            Text("Добавить участника")
        }
    }
}

@Composable
fun FinanceTab(trip: Trip, tripViewModel: TripViewModel) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            "Финансы:",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color(0xFF333333)
        )

        val totalExpenses = trip.expenses.sumOf { it.amount }

        Column(modifier = Modifier.padding(top = 16.dp)) {
            Text(
                "Общие расходы: $totalExpenses",
                color = Color(0xFF333333)
            )

            Text(
                "Планируемый бюджет: ${trip.budget}",
                modifier = Modifier.padding(top = 8.dp),
                color = Color(0xFF333333)
            )

            if (trip.expenses.isEmpty()) {
                Text(
                    "Расходы пока не добавлены",
                    modifier = Modifier.padding(top = 16.dp),
                    color = Color(0xFF666666)
                )
            } else {
                Text(
                    "Количество статей расходов: ${trip.expenses.size}",
                    modifier = Modifier.padding(top = 8.dp),
                    color = Color(0xFF333333)
                )
            }
        }

        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFFCF7),
                contentColor = Color(0xFF333333)
            )
        ) {
            Text("Добавить расход")
        }
    }
}

@Composable
fun ReportTab(trip: Trip) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            "Отчет по поездке:",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color(0xFF333333)
        )

        Column(modifier = Modifier.padding(top = 16.dp)) {
            Text(
                "Маршрут: ${trip.startTown} - ${trip.endTown}",
                color = Color(0xFF333333)
            )

            Text(
                "Продолжительность: ${trip.startDate} - ${trip.endDate}",
                modifier = Modifier.padding(top = 8.dp),
                color = Color(0xFF333333)
            )

            Text(
                "Количество участников: ${trip.participants.size}",
                modifier = Modifier.padding(top = 8.dp),
                color = Color(0xFF333333)
            )

            Text(
                "Статус: ${if (trip.participants.isNotEmpty()) "Завершена" else "Активна"}",
                modifier = Modifier.padding(top = 8.dp),
                color = Color(0xFF333333)
            )
        }
    }
}