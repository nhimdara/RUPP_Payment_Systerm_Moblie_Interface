package kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonObject
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.AcademicYearResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.ApiResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.FacultyResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.LoginRequest
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.PaymentResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.ReceiptResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.SemesterResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.TuitionCalculationResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.TuitionFeeResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.remote.ApiClient
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.remote.SessionStore
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.remote.dto.AuthResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.remote.dto.CreatePaymentRequest
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.remote.dto.MajorResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.remote.dto.OtpChallengeResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.remote.dto.OtpVerifyRequest
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.remote.dto.PagedResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.remote.dto.PayServiceFeeRequest
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.remote.dto.ServiceFeeResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.remote.dto.PaymentStatusResult
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.remote.dto.StudentResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.remote.dto.UpdateAcademicInfoRequest
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.remote.dto.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.io.File
import java.io.IOException

/** Result of a backend call, with the details the UI needs to show a useful message. */
sealed interface ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>
    data class Failure(
        val message: String,
        val httpCode: Int? = null,
        /** OTP: wrong code returns 400 with data.remainingAttempts. */
        val remainingAttempts: Int? = null,
        /** OTP resend: 429 with a Retry-After header in seconds. */
        val retryAfterSeconds: Int? = null,
        val fieldErrors: Map<String, String> = emptyMap()
    ) : ApiResult<Nothing>
}

inline fun <T> ApiResult<T>.onSuccess(block: (T) -> Unit): ApiResult<T> {
    if (this is ApiResult.Success) block(data)
    return this
}

inline fun <T> ApiResult<T>.onFailure(block: (ApiResult.Failure) -> Unit): ApiResult<T> {
    if (this is ApiResult.Failure) block(this)
    return this
}

/** Everything the Home dashboard shows, loaded in parallel. */
data class DashboardData(
    val profile: StudentResponse,
    val tuition: TuitionCalculationResponse,
    val activeSemester: SemesterResponse?,
    /** Newest payments of every kind (tuition and campus services). */
    val recent: List<PaymentResponse> = emptyList(),
    /** Campus service fees with my status (drives the parking / library cards). */
    val services: List<ServiceFeeResponse> = emptyList()
)

/** Everything the Pay flow needs before the student picks a method. */
data class PaymentSetup(
    val fee: TuitionFeeResponse,
    val tuition: TuitionCalculationResponse,
    val activeSemester: SemesterResponse?,
    val services: List<ServiceFeeResponse> = emptyList()
)

class RuppRepository(
    private val api: ApiClient,
    val session: SessionStore
) {
    private val gson = Gson()

    // ---------- Auth ----------
    suspend fun login(usernameOrEmail: String, password: String): ApiResult<AuthResponse> =
        call { api.service.login(LoginRequest(usernameOrEmail.trim(), password)) }
            .onSuccess { session.saveAuth(it) }

    suspend fun me(): ApiResult<UserResponse> = call { api.service.me() }

    fun logout() = session.clearAuth()

    // ---------- Student ----------
    suspend fun dashboard(): ApiResult<DashboardData> = coroutineScope {
        val profile = async { call { api.service.myProfile() } }
        val tuition = async { call { api.service.calculateMyTuition() } }
        val semester = async { call { api.service.activeSemester() } }
        val recent = async { call { api.service.myPayments(0, 5) } }
        val services = async { call { api.service.myServiceFees() } }
        val p = profile.await()
        val t = tuition.await()
        when {
            p is ApiResult.Failure -> p
            t is ApiResult.Failure -> t
            else -> ApiResult.Success(
                DashboardData(
                    profile = (p as ApiResult.Success).data,
                    tuition = (t as ApiResult.Success).data,
                    activeSemester = (semester.await() as? ApiResult.Success)?.data,
                    recent = (recent.await() as? ApiResult.Success)?.data?.content.orEmpty(),
                    services = (services.await() as? ApiResult.Success)?.data.orEmpty()
                )
            )
        }
    }

    suspend fun profile(): ApiResult<StudentResponse> = call { api.service.myProfile() }

    suspend fun faculties(): ApiResult<List<FacultyResponse>> = call { api.service.faculties() }
    suspend fun academicYears(): ApiResult<List<AcademicYearResponse>> = call { api.service.academicYears() }
    suspend fun majors(facultyId: Long?): ApiResult<List<MajorResponse>> = call { api.service.majors(facultyId) }

    suspend fun updateAcademicInfo(facultyId: Long, academicYearId: Long, majorId: Long?): ApiResult<StudentResponse> =
        call { api.service.updateAcademicInfo(UpdateAcademicInfoRequest(facultyId, academicYearId, majorId)) }

    // ---------- Payments ----------
    suspend fun paymentSetup(): ApiResult<PaymentSetup> = coroutineScope {
        val fee = async { call { api.service.myFee() } }
        val tuition = async { call { api.service.calculateMyTuition() } }
        val semester = async { call { api.service.activeSemester() } }
        val services = async { call { api.service.myServiceFees() } }
        val f = fee.await()
        val t = tuition.await()
        when {
            f is ApiResult.Failure -> f
            t is ApiResult.Failure -> t
            else -> ApiResult.Success(
                PaymentSetup(
                    fee = (f as ApiResult.Success).data,
                    tuition = (t as ApiResult.Success).data,
                    activeSemester = (semester.await() as? ApiResult.Success)?.data,
                    services = (services.await() as? ApiResult.Success)?.data.orEmpty()
                )
            )
        }
    }

    suspend fun createPayment(
        feeStructureId: Long,
        method: String,
        transactionRef: String,
        amount: Double,
        remarks: String?
    ): ApiResult<PaymentResponse> = call {
        api.service.createPayment(CreatePaymentRequest(feeStructureId, method, transactionRef, amount, remarks))
    }

    suspend fun serviceFees(): ApiResult<List<ServiceFeeResponse>> = call { api.service.myServiceFees() }

    suspend fun payServiceFee(serviceFeeId: Long, method: String, transactionRef: String, remarks: String?): ApiResult<PaymentResponse> =
        call { api.service.payServiceFee(serviceFeeId, PayServiceFeeRequest(method, transactionRef, remarks)) }

    suspend fun myPayments(page: Int, size: Int = 10): ApiResult<PagedResponse<PaymentResponse>> =
        call { api.service.myPayments(page, size) }

    suspend fun payment(id: Long): ApiResult<PaymentResponse> = call { api.service.payment(id) }

    suspend fun requestOtp(paymentId: Long): ApiResult<OtpChallengeResponse> = call { api.service.requestOtp(paymentId) }
    suspend fun resendOtp(challengeId: String): ApiResult<OtpChallengeResponse> = call { api.service.resendOtp(challengeId) }
    suspend fun verifyOtp(challengeId: String, code: String): ApiResult<PaymentStatusResult> =
        call { api.service.verifyOtp(challengeId, OtpVerifyRequest(code)) }

    suspend fun receipt(paymentId: Long): ApiResult<ReceiptResponse> = call { api.service.receiptForPayment(paymentId) }

    /** Downloads the official PDF receipt into the app cache and returns the file. */
    suspend fun downloadReceiptPdf(context: Context, paymentId: Long, receiptNumber: String): ApiResult<File> =
        withContext(Dispatchers.IO) {
            try {
                val dir = File(context.cacheDir, "receipts").apply { mkdirs() }
                val file = File(dir, "receipt-$receiptNumber.pdf")
                api.service.receiptPdf(paymentId).use { body ->
                    file.outputStream().use { out -> body.byteStream().copyTo(out) }
                }
                ApiResult.Success(file)
            } catch (e: Exception) {
                toFailure(e)
            }
        }

    // ---------- Plumbing ----------
    private suspend fun <T> call(block: suspend () -> ApiResponse<T>): ApiResult<T> =
        withContext(Dispatchers.IO) {
            try {
                val response = block()
                val data = response.data
                if (response.success && data != null) ApiResult.Success(data)
                else ApiResult.Failure(response.message.ifBlank { "Request failed" })
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                toFailure(e)
            }
        }

    private fun toFailure(e: Exception): ApiResult.Failure = when (e) {
        is HttpException -> {
            val code = e.code()
            val raw = runCatching { e.response()?.errorBody()?.string() }.getOrNull()
            val json = runCatching { gson.fromJson(raw, JsonObject::class.java) }.getOrNull()
            val message = json?.get("message")?.takeIf { it.isJsonPrimitive }?.asString
            val data = json?.get("data")?.takeIf { it.isJsonObject }?.asJsonObject
            val remaining = data?.get("remainingAttempts")?.takeIf { it.isJsonPrimitive }?.asInt
            val fieldErrors = data?.entrySet()
                ?.filter { it.value.isJsonPrimitive && it.key != "remainingAttempts" }
                ?.associate { it.key to it.value.asString }
                .orEmpty()
            val retryAfter = e.response()?.headers()?.get("Retry-After")?.toIntOrNull()
            ApiResult.Failure(
                message = message ?: defaultMessage(code),
                httpCode = code,
                remainingAttempts = remaining,
                retryAfterSeconds = retryAfter,
                fieldErrors = fieldErrors
            )
        }
        is IOException -> ApiResult.Failure(
            "Can't reach the RUPP server at ${session.baseUrl}. Check that the backend is running, the server address is right, " +
                "and (Android 17+) that RUPP Pay is allowed to access devices on your local network."
        )
        else -> ApiResult.Failure(e.message ?: "Something went wrong")
    }

    private fun defaultMessage(code: Int) = when (code) {
        401 -> "Your session has expired. Please sign in again."
        403 -> "You don't have permission to do that."
        404 -> "Not found."
        409 -> "This action conflicts with the current payment status."
        410 -> "The code has expired. Request a new one."
        423 -> "Too many wrong attempts. Verification is locked for 15 minutes."
        429 -> "Please wait before requesting another code."
        in 500..599 -> "The server had a problem. Try again shortly."
        else -> "Request failed ($code)"
    }
}
