package com.example.core_data.api

import com.example.core_data.model.AccountResponse
import com.example.core_data.model.AuthLoginRequest
import com.example.core_data.model.AuthRegisterRequest
import com.example.core_data.model.AuthResponse
import com.example.core_data.model.ChangePasswordRequest
import com.example.core_data.model.CreateCategoryRequest
import com.example.core_data.model.CreateTransferRequest
import com.example.core_data.model.CreateTravelRequest
import com.example.core_data.model.EditCategoryRequest
import com.example.core_data.model.EditTransferRequest
import com.example.core_data.model.EditTravelRequest
import com.example.core_data.model.ExpenseRequestDTO
import com.example.core_data.model.ExpenseResponseDTO
import com.example.core_data.model.ExpenseUpdateRequestDTO
import com.example.core_data.model.InviteRequest
import com.example.core_data.model.InvitesResponse
import com.example.core_data.model.LogoutRequest
import com.example.core_data.model.MyTravelsResponse
import com.example.core_data.model.RefreshRequest
import com.example.core_data.model.TravelExpenseAnalyticsDTO
import com.example.core_data.model.TravelExpensesResponseDTO
import com.example.core_data.model.TravelMembersResponse
import com.example.core_data.model.TravelResponse
import com.example.core_data.model.TransferResponse
import com.example.core_data.model.TransfersListResponse
import com.example.core_data.model.CategoriesListResponse
import com.example.core_data.model.CategoryResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.HTTP
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface TTravelsApi {
    @POST("auth/register")
    suspend fun register(@Body body: AuthRegisterRequest): Response<AuthResponse>

    @POST("auth/login")
    suspend fun login(@Body body: AuthLoginRequest): Response<AuthResponse>

    @POST("account/refresh")
    suspend fun refresh(@Body body: RefreshRequest): Response<AuthResponse>

    @POST("account/password/change")
    suspend fun changePassword(@Body body: ChangePasswordRequest): Response<Unit>

    @POST("account/logout")
    suspend fun logout(@Body body: LogoutRequest): Response<Unit>

    @GET("account/me")
    suspend fun getCurrentUser(): Response<AccountResponse>

    @GET("account/invites")
    suspend fun getInvites(): Response<InvitesResponse>

    @POST("account/invites/respond/{inviteId}")
    suspend fun respondToInvite(
        @Path("inviteId") inviteId: Long,
        @Query("accept") accept: Boolean
    ): Response<Unit>

    @GET("travels")
    suspend fun getMyTravels(): Response<MyTravelsResponse>

    @POST("travels")
    suspend fun createTravel(@Body body: CreateTravelRequest): Response<TravelResponse>

    @GET("travels/{travelId}")
    suspend fun getTravel(@Path("travelId") travelId: Long): Response<TravelResponse>

    @PATCH("travels/{travelId}")
    suspend fun editTravel(
        @Path("travelId") travelId: Long,
        @Body body: EditTravelRequest
    ): Response<TravelResponse>

    @DELETE("travels/{travelId}")
    suspend fun deleteTravel(@Path("travelId") travelId: Long): Response<Unit>

    @POST("travels/{travelId}/close")
    suspend fun closeTravel(@Path("travelId") travelId: Long): Response<Unit>

    @POST("travels/{travelId}/reopen")
    suspend fun reopenTravel(@Path("travelId") travelId: Long): Response<Unit>

    @GET("travels/{travelId}/members")
    suspend fun getTravelMembers(@Path("travelId") travelId: Long): Response<TravelMembersResponse>

    @POST("travels/{travelId}/members/invite")
    suspend fun inviteMembers(
        @Path("travelId") travelId: Long,
        @Body body: InviteRequest
    ): Response<Unit>

    @POST("travels/{travelId}/members/leave")
    suspend fun leaveTravel(@Path("travelId") travelId: Long): Response<Unit>

    @DELETE("travels/{travelId}/members/kick/{userId}")
    suspend fun kickMember(
        @Path("travelId") travelId: Long,
        @Path("userId") userId: Long
    ): Response<Unit>

    @GET("travels/{travelId}/categories")
    suspend fun getCategories(@Path("travelId") travelId: Long): Response<CategoriesListResponse>

    @POST("travels/{travelId}/categories")
    suspend fun createCategory(
        @Path("travelId") travelId: Long,
        @Body body: CreateCategoryRequest
    ): Response<CategoryResponse>

    @PUT("travels/{travelId}/categories/{id}")
    suspend fun editCategory(
        @Path("travelId") travelId: Long,
        @Path("id") categoryId: Long,
        @Body body: EditCategoryRequest
    ): Response<CategoryResponse>

    @GET("travels/{travelId}/expenses")
    suspend fun getTravelExpenses(
        @Path("travelId") travelId: Long
    ): Response<TravelExpensesResponseDTO>

    @POST("travels/{travelId}/expenses")
    suspend fun createExpense(
        @Path("travelId") travelId: Long,
        @Body body: ExpenseRequestDTO
    ): Response<ExpenseResponseDTO>

    @PATCH("travels/{travelId}/expenses/{expenseId}")
    suspend fun updateExpense(
        @Path("travelId") travelId: Long,
        @Path("expenseId") expenseId: Long,
        @Body body: ExpenseUpdateRequestDTO
    ): Response<ExpenseResponseDTO>

    @DELETE("travels/{travelId}/expenses/{expenseId}")
    suspend fun deleteExpense(
        @Path("travelId") travelId: Long,
        @Path("expenseId") expenseId: Long
    ): Response<Unit>

    @POST("travels/{travelId}/expenses/{expenseId}/participants")
    suspend fun addParticipantsToExpense(
        @Path("travelId") travelId: Long,
        @Path("expenseId") expenseId: Long,
        @Body participants: Map<Long, Double>
    ): Response<ExpenseResponseDTO>

    @HTTP(
        method = "DELETE",
        path = "travels/{travelId}/expenses/{expenseId}/participants",
        hasBody = true
    )
    suspend fun removeParticipantsFromExpense(
        @Path("travelId") travelId: Long,
        @Path("expenseId") expenseId: Long,
        @Body participantsIds: List<Long>
    ): Response<ExpenseResponseDTO>

    @GET("travels/{travelId}/transfers")
    suspend fun getTransfers(
        @Path("travelId") travelId: Long
    ): Response<TransfersListResponse>

    @POST("travels/{travelId}/transfers")
    suspend fun createTransfer(
        @Path("travelId") travelId: Long,
        @Body body: CreateTransferRequest
    ): Response<TransferResponse>

    @PUT("travels/{travelId}/transfers/{id}")
    suspend fun editTransfer(
        @Path("travelId") travelId: Long,
        @Path("id") transferId: Long,
        @Body body: EditTransferRequest
    ): Response<TransferResponse>

    @GET("travels/{travelId}/analytic")
    suspend fun getExpenseReport(
        @Path("travelId") travelId: Long
    ): Response<TravelExpenseAnalyticsDTO>
}
