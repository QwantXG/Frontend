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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay

// Состояния для регистрации
sealed class RegistrationState {
    object Idle : RegistrationState()
    object Loading : RegistrationState()
    data class Success(val message: String) : RegistrationState()
    data class Error(val message: String) : RegistrationState()
}

@Composable
fun Registration(navController: NavController) {
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var registrationState by remember { mutableStateOf<RegistrationState>(RegistrationState.Idle) }
    val existingUsers = remember {
        listOf(
            "79273630708",
            "79273456789",
            "79995554433"
        )
    }
    suspend fun mockRegistration(name: String, phone: String, password: String): RegistrationState {
        delay(2000)
        if (name.length < 2) {
            return RegistrationState.Error("Имя должно содержать не менее 2 символов")
        }

        if (phone.length < 10) {
            return RegistrationState.Error("Номер телефона должен содержать не менее 10 цифр")
        }

        if (password.length < 8) {
            return RegistrationState.Error("Пароль должен содержать не менее 8 символов")
        }
        if (existingUsers.contains(phone)) {
            return RegistrationState.Error("Пользователь с таким номером уже существует")
        }
        return RegistrationState.Success("Аккаунт успешно создан! Добро пожаловать, $name!")
    }
    LaunchedEffect(registrationState) {
        if (registrationState is RegistrationState.Success) {
            delay(1500)
            navController.navigate("welcome") {
                popUpTo("registration") { inclusive = true }
            }
        }
    }

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
            text = "Регистрация",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF333333),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 160.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 700.dp)
                .align(Alignment.BottomCenter)
                .padding(30.dp)
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
                    value = name,
                    onValueChange = {
                        name = it
                        if (registrationState is RegistrationState.Error) {
                            registrationState = RegistrationState.Idle
                        }
                    },
                    label = { Text("Имя", fontWeight = FontWeight.ExtraBold) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    isError = registrationState is RegistrationState.Error,

                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = {
                        phone = it
                        if (registrationState is RegistrationState.Error) {
                            registrationState = RegistrationState.Idle
                        }
                    },
                    label = { Text("Номер телефона", fontWeight = FontWeight.ExtraBold) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    isError = registrationState is RegistrationState.Error

                )
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        if (registrationState is RegistrationState.Error) {
                            registrationState = RegistrationState.Idle
                        }
                    },
                    label = { Text("Пароль", fontWeight = FontWeight.ExtraBold) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    isError = registrationState is RegistrationState.Error,
                    placeholder = { Text("Не менее 8 символов") }
                )
                when (registrationState) {
                    is RegistrationState.Error -> {
                        Text(
                            text = (registrationState as RegistrationState.Error).message,
                            color = Color.Red,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }
                    is RegistrationState.Success -> {
                        Text(
                            text = (registrationState as RegistrationState.Success).message,
                            color = Color(0xFF4CAF50),
                            fontSize = 14.sp,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }
                    else -> {
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        registrationState = RegistrationState.Loading
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFDD2D),
                        contentColor = Color(0xFF333333)
                    ),
                    enabled = registrationState != RegistrationState.Loading &&
                            name.isNotEmpty() && phone.isNotEmpty() && password.isNotEmpty()
                ) {
                    if (registrationState == RegistrationState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color(0xFF333333),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Создать аккаунт", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                    }
                }
                Button(
                    onClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFFFFF),
                        contentColor = Color(0xFF333333)
                    ),
                    enabled = registrationState != RegistrationState.Loading
                ) {
                    Text(
                        "Уже есть аккаунт? Войти",
                        color = Color(0xFF333333)
                    )
                }
            }
        }
    }


    LaunchedEffect(registrationState) {
        if (registrationState == RegistrationState.Loading) {
            registrationState = mockRegistration(name, phone, password)
        }
    }
}