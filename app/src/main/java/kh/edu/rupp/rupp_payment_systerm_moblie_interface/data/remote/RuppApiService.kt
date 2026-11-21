package kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.remote

import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.AcademicYearResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.ApiResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.FacultyResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.LoginRequest
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.PaymentResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.ReceiptResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.SemesterResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.TuitionCalculationResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.TuitionFeeResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.remote.dto.AuthResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.remote.dto.CreatePaymentRequest
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.remote.dto.MajorResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.remote.dto.OtpChallengeResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.remote.dto.OtpVerifyRequest
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.remote.dto.PagedResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.remote.dto.PayServiceFeeRequest
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.remote.dto.ServiceFeeResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.remote.dto.PaymentStatusResult
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.remote.dto.RefreshTokenRequest
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.remote.dto.StudentResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.remote.dto.UpdateAcademicInfoRequest
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.remote.dto.UserResponse
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Streaming

/**
 * Student-facing endpoints of the RUPP Payment backend (base path /api/v1).
 * Admin and finance endpoints are left out because the mobile app is for students.
 */
interface RuppApiService {

    // ---- Auth (public) ----
    @POST("api/v1/auth/login")
    suspend fun login(@Body body: LoginRequest): ApiResponse<AuthResponse>

    @POST("api/v1/auth/refresh")
    suspend fun refresh(@Body body: RefreshTokenRequest): ApiResponse<AuthResponse>

    @GET("api/v1/auth/me")
    suspend fun me(): ApiResponse<UserResponse>

    // ---- Student profile & academic setup ----
    @GET("api/v1/students/my-profile")
    suspend fun myProfile(): ApiResponse<StudentResponse>

    @PUT("api/v1/students/my-academic-info")
    suspend fun updateAcademicInfo(@Body body: UpdateAcademicInfoRequest): ApiResponse<StudentResponse>

    @GET("api/v1/faculties")
    suspend fun faculties(): ApiResponse<List<FacultyResponse>>

    @GET("api/v1/academic-years")
    suspend fun academicYears(): ApiResponse<List<AcademicYearResponse>>

    @GET("api/v1/majors")
    suspend fun majors(@Query("facultyId") facultyId: Long?): ApiResponse<List<MajorResponse>>

    @GET("api/v1/semesters/active")
    suspend fun activeSemester(): ApiResponse<SemesterResponse>

    // ---- Tuition ----
    @GET("api/v1/tuition-fees/calculate")
    suspend fun calculateMyTuition(): ApiResponse<TuitionCalculationResponse>

    @GET("api/v1/tuition-fees/my-fee")
    suspend fun myFee(): ApiResponse<TuitionFeeResponse>

    // ---- Payments ----
    @POST("api/v1/payments")
    suspend fun createPayment(@Body body: CreatePaymentRequest): ApiResponse<PaymentResponse>

    @GET("api/v1/payments/my-payments")
    suspend fun myPayments(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sortBy") sortBy: String = "paymentDate",
        @Query("sortDir") sortDir: String = "desc"
    ): ApiResponse<PagedResponse<PaymentResponse>>

    @GET("api/v1/payments/{id}")
    suspend fun payment(@Path("id") id: Long): ApiResponse<PaymentResponse>

    // ---- Campus service fees: parking, library, lab... ----
    @GET("api/v1/service-fees/my")
    suspend fun myServiceFees(): ApiResponse<List<ServiceFeeResponse>>

    @POST("api/v1/service-fees/{id}/pay")
    suspend fun payServiceFee(@Path("id") id: Long, @Body body: PayServiceFeeRequest): ApiResponse<PaymentResponse>

    // ---- OTP verification (payment must be PENDING) ----
    @POST("api/v1/payments/{paymentId}/verification/otp")
    suspend fun requestOtp(@Path("paymentId") paymentId: Long): ApiResponse<OtpChallengeResponse>

    @POST("api/v1/payments/verification/otp/{challengeId}/resend")
    suspend fun resendOtp(@Path("challengeId") challengeId: String): ApiResponse<OtpChallengeResponse>

    @POST("api/v1/payments/verification/otp/{challengeId}/verify")
    suspend fun verifyOtp(
        @Path("challengeId") challengeId: String,
        @Body body: OtpVerifyRequest
    ): ApiResponse<PaymentStatusResult>

    // ---- Receipts (students must use the /receipts/payment/... paths; /payments/{id}/receipt returns 403) ----
    @GET("api/v1/receipts/payment/{paymentId}")
    suspend fun receiptForPayment(@Path("paymentId") paymentId: Long): ApiResponse<ReceiptResponse>

    @Streaming
    @GET("api/v1/receipts/payment/{paymentId}/pdf")
    suspend fun receiptPdf(@Path("paymentId") paymentId: Long): ResponseBody
}
