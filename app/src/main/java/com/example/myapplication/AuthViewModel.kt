import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_data.model.AuthLoginRequest
import com.example.myapplication.BackendProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val backend = BackendProvider.get()

    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        data class Success(val token: String) : AuthState()
        data class Error(val message: String) : AuthState()
    }

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun login(phone: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val response = backend.login(AuthLoginRequest(phone, password))
                if (response.isSuccessful) {
                    _authState.value = AuthState.Success(response.body()?.token ?: "")
                } else {
                    _authState.value = AuthState.Error("Ошибка: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Ошибка сети: ${e.message}")
            }
        }
    }
}