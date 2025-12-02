package com.example.myapplication

data class Trip(
    val id: String = System.currentTimeMillis().toString(),
    val startTown: String,
    val endTown: String,
    val startDate: String,
    val endDate: String,
    val budget: String,
    val participants: List<User> = emptyList(),
    val expenses: List<Expense> = emptyList()
)

data class User(
    val id: String = System.currentTimeMillis().toString(),
    val name: String,
    val email: String
)

data class Expense(
    val id: String = System.currentTimeMillis().toString(),
    val title: String,
    val amount: Double,
    val category: String,
    val paidBy: String
)