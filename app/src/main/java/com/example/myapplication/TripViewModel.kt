

package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TripViewModel : ViewModel() {
    private val _trips = MutableStateFlow<List<Trip>>(emptyList())
    val trips: StateFlow<List<Trip>> = _trips

    init {
        println("TripViewModel создан")
    }

    fun addTrip(newTrip: Trip) {
        viewModelScope.launch {
            println(" Добавляем поездку: ${newTrip.startTown} -> ${newTrip.endTown}")
            _trips.value = _trips.value + newTrip
            println("Теперь поездок: ${_trips.value.size}")
        }
    }

    fun getTripById(tripId: String): Trip? {
        return _trips.value.find { it.id == tripId }
    }
}