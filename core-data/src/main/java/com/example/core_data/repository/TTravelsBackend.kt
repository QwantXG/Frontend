package com.example.core_data.repository

import com.example.core_data.api.TTravelsApi
import com.example.core_data.model.AccountResponse
import com.example.core_data.model.AuthRegisterRequest
import com.example.core_data.model.AuthLoginRequest
import com.example.core_data.model.AuthResponse
import com.example.core_data.model.RefreshRequest
import com.example.core_data.model.ChangePasswordRequest
import com.example.core_data.model.LogoutRequest
import com.example.core_data.model.InviteRequest
import com.example.core_data.model.CreateTravelRequest
import com.example.core_data.model.EditTravelRequest
import com.example.core_data.model.CreateCategoryRequest
import com.example.core_data.model.EditCategoryRequest
import com.example.core_data.model.ExpenseRequestDTO
import com.example.core_data.model.ExpenseUpdateRequestDTO
import com.example.core_data.model.CreateTransferRequest
import com.example.core_data.model.EditTransferRequest
import com.example.core_data.model.CategoriesListResponse
import com.example.core_data.model.CategoryResponse
import com.example.core_data.model.ExpenseResponseDTO
import com.example.core_data.model.InvitesResponse
import com.example.core_data.model.MyTravelsResponse
import com.example.core_data.model.TravelExpenseAnalyticsDTO
import com.example.core_data.model.TravelExpensesResponseDTO
import com.example.core_data.model.TravelMembersResponse
import com.example.core_data.model.TravelResponse
import com.example.core_data.model.TransferResponse
import com.example.core_data.model.TransfersListResponse
import com.example.core_data.network.ApiFactory
import com.example.core_data.network.NetworkDefaults
import com.example.core_data.network.NetworkResult
import com.example.core_data.network.safeApiCall
import com.example.core_data.network.InMemoryTokensStore
import com.example.core_data.network.RefreshAuthenticator
import com.example.core_data.network.TokensStore
import com.example.core_data.network.defaultJson
import com.example.core_data.network.toAuthTokens
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient

class TTravelsBackend(
    private val api: TTravelsApi,
    private val json: Json = defaultJson(),
    val tokensStore: TokensStore? = null
) {

    companion object {
        fun create(
            baseUrl: String = NetworkDefaults.DEFAULT_BASE_URL,
            tokensStore: TokensStore? = null,
            json: Json = defaultJson(),
            configureClient: (OkHttpClient.Builder.() -> Unit)? = null
        ): TTravelsBackend {
            val store = tokensStore ?: InMemoryTokensStore()

            val refreshApi = ApiFactory.createApi(
                baseUrl = baseUrl,
                tokensProvider = store,
                json = json,
                authenticator = null,
                preemptiveRefreshCall = null,
                configureClient = configureClient
            )
            val refreshCall: suspend (String) -> retrofit2.Response<AuthResponse> = { token ->
                refreshApi.refresh(RefreshRequest(token))
            }
            val authenticator = RefreshAuthenticator(store, refreshCall)

            val apiInstance = ApiFactory.createApi(
                baseUrl = baseUrl,
                tokensProvider = store,
                json = json,
                authenticator = authenticator,
                preemptiveRefreshCall = refreshCall,
                configureClient = configureClient
            )
            return TTravelsBackend(apiInstance, json, store)
        }
    }

    val rawApi: TTravelsApi
        get() = api

    // Auth / Account
    suspend fun register(request: AuthRegisterRequest): NetworkResult<AuthResponse> =
        safeApiCall(json) { api.register(request) }.alsoStoreTokens()

    suspend fun login(request: AuthLoginRequest): NetworkResult<AuthResponse> =
        safeApiCall(json) { api.login(request) }.alsoStoreTokens()

    suspend fun refresh(request: RefreshRequest): NetworkResult<AuthResponse> =
        safeApiCall(json) { api.refresh(request) }.alsoStoreTokens()

    suspend fun changePassword(request: ChangePasswordRequest): NetworkResult<Unit> =
        safeApiCall(json) { api.changePassword(request) }

    suspend fun logout(request: LogoutRequest): NetworkResult<Unit> =
        safeApiCall(json) { api.logout(request) }.alsoClearTokens()

    suspend fun getCurrentUser(): NetworkResult<AccountResponse> =
        safeApiCall(json) { api.getCurrentUser() }

    suspend fun getInvites(): NetworkResult<InvitesResponse> =
        safeApiCall(json) { api.getInvites() }

    suspend fun respondToInvite(inviteId: Long, accept: Boolean): NetworkResult<Unit> =
        safeApiCall(json) { api.respondToInvite(inviteId, accept) }

    // Travels
    suspend fun getMyTravels(): NetworkResult<MyTravelsResponse> =
        safeApiCall(json) { api.getMyTravels() }

    suspend fun createTravel(request: CreateTravelRequest): NetworkResult<TravelResponse> =
        safeApiCall(json) { api.createTravel(request) }

    suspend fun getTravel(travelId: Long): NetworkResult<TravelResponse> =
        safeApiCall(json) { api.getTravel(travelId) }

    suspend fun editTravel(
        travelId: Long,
        request: EditTravelRequest
    ): NetworkResult<TravelResponse> =
        safeApiCall(json) { api.editTravel(travelId, request) }

    suspend fun deleteTravel(travelId: Long): NetworkResult<Unit> =
        safeApiCall(json) { api.deleteTravel(travelId) }

    suspend fun closeTravel(travelId: Long): NetworkResult<Unit> =
        safeApiCall(json) { api.closeTravel(travelId) }

    suspend fun reopenTravel(travelId: Long): NetworkResult<Unit> =
        safeApiCall(json) { api.reopenTravel(travelId) }

    // Travel members
    suspend fun getTravelMembers(travelId: Long): NetworkResult<TravelMembersResponse> =
        safeApiCall(json) { api.getTravelMembers(travelId) }

    suspend fun inviteMembers(
        travelId: Long,
        request: InviteRequest
    ): NetworkResult<Unit> = safeApiCall(json) { api.inviteMembers(travelId, request) }

    suspend fun leaveTravel(travelId: Long): NetworkResult<Unit> =
        safeApiCall(json) { api.leaveTravel(travelId) }

    suspend fun kickMember(travelId: Long, userId: Long): NetworkResult<Unit> =
        safeApiCall(json) { api.kickMember(travelId, userId) }

    // Categories
    suspend fun getCategories(travelId: Long): NetworkResult<CategoriesListResponse> =
        safeApiCall(json) { api.getCategories(travelId) }

    suspend fun createCategory(
        travelId: Long,
        request: CreateCategoryRequest
    ): NetworkResult<CategoryResponse> =
        safeApiCall(json) { api.createCategory(travelId, request) }

    suspend fun editCategory(
        travelId: Long,
        categoryId: Long,
        request: EditCategoryRequest
    ): NetworkResult<CategoryResponse> =
        safeApiCall(json) { api.editCategory(travelId, categoryId, request) }

    // Expenses
    suspend fun getTravelExpenses(travelId: Long): NetworkResult<TravelExpensesResponseDTO> =
        safeApiCall(json) { api.getTravelExpenses(travelId) }

    suspend fun createExpense(
        travelId: Long,
        request: ExpenseRequestDTO
    ): NetworkResult<ExpenseResponseDTO> = safeApiCall(json) { api.createExpense(travelId, request) }

    suspend fun updateExpense(
        travelId: Long,
        expenseId: Long,
        request: ExpenseUpdateRequestDTO
    ): NetworkResult<ExpenseResponseDTO> =
        safeApiCall(json) { api.updateExpense(travelId, expenseId, request) }

    suspend fun deleteExpense(travelId: Long, expenseId: Long): NetworkResult<Unit> =
        safeApiCall(json) { api.deleteExpense(travelId, expenseId) }

    suspend fun addParticipantsToExpense(
        travelId: Long,
        expenseId: Long,
        participants: Map<Long, Double>
    ): NetworkResult<ExpenseResponseDTO> =
        safeApiCall(json) { api.addParticipantsToExpense(travelId, expenseId, participants) }

    suspend fun removeParticipantsFromExpense(
        travelId: Long,
        expenseId: Long,
        participantsIds: List<Long>
    ): NetworkResult<ExpenseResponseDTO> =
        safeApiCall(json) { api.removeParticipantsFromExpense(travelId, expenseId, participantsIds) }

    // Transfers
    suspend fun getTransfers(travelId: Long): NetworkResult<TransfersListResponse> =
        safeApiCall(json) { api.getTransfers(travelId) }

    suspend fun createTransfer(
        travelId: Long,
        request: CreateTransferRequest
    ): NetworkResult<TransferResponse> =
        safeApiCall(json) { api.createTransfer(travelId, request) }

    suspend fun editTransfer(
        travelId: Long,
        transferId: Long,
        request: EditTransferRequest
    ): NetworkResult<TransferResponse> =
        safeApiCall(json) { api.editTransfer(travelId, transferId, request) }

    // Analytics
    suspend fun getExpenseReport(travelId: Long): NetworkResult<TravelExpenseAnalyticsDTO> =
        safeApiCall(json) { api.getExpenseReport(travelId) }

    private fun NetworkResult<AuthResponse>.alsoStoreTokens(): NetworkResult<AuthResponse> {
        if (this is NetworkResult.Success) {
            this.data.toAuthTokens()?.let { tokensStore?.saveTokens(it) }
        }
        return this
    }

    private fun <T> NetworkResult<T>.alsoClearTokens(): NetworkResult<T> {
        if (this is NetworkResult.Success) {
            tokensStore?.clear()
        }
        return this
    }
}
