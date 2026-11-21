package kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.remote.dto

import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.AcademicYearResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.FacultyResponse

// DTOs for backend responses that the existing data.model package did not cover yet.
// Field names follow the "RUPP Payment System: Frontend API Integration Guide" and were
// checked against the running backend (dev branch).

data class UserResponse(
    val id: Long,
    val username: String,
    val email: String?,
    val role: String? // ROLE_STUDENT, ROLE_ADMIN, ROLE_FINANCE_STAFF
)

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String?,
    val expiresIn: Long?,
    val user: UserResponse?
)

data class RefreshTokenRequest(val refreshToken: String)

data class MajorResponse(
    val id: Long,
    val majorName: String,
    val majorCode: String?,
    val faculty: FacultyResponse?,
    val description: String?
)

/** GET /students/my-profile: nested objects, not flat fields. */
data class StudentResponse(
    val id: Long,
    val user: UserResponse?,
    val firstName: String?,
    val lastName: String?,
    val faculty: FacultyResponse?,
    val academicYear: AcademicYearResponse?,
    val major: MajorResponse?
) {
    val fullName: String
        get() = listOfNotNull(firstName, lastName).joinToString(" ").ifBlank { user?.username ?: "Student" }

    val initials: String
        get() = fullName.split(" ").filter { it.isNotBlank() }.take(2)
            .joinToString("") { it.first().uppercase() }
}

data class PagedResponse<T>(
    val content: List<T> = emptyList(),
    val pageNumber: Int = 0,
    val pageSize: Int = 0,
    val totalElements: Long = 0,
    val totalPages: Int = 0,
    val first: Boolean = true,
    val last: Boolean = true,
    val empty: Boolean = true
)

data class CreatePaymentRequest(
    val feeStructureId: Long,
    val paymentMethod: String,
    val transactionRef: String,
    val amount: Double,
    val remarks: String?
)

/** POST /payments/{id}/verification/otp and /resend. expiresAt is an ISO instant (UTC). */
data class OtpChallengeResponse(
    val challengeId: String,
    val paymentId: Long,
    val expiresAt: String
)

data class OtpVerifyRequest(val code: String)

/** POST /verification/otp/{challengeId}/verify -> { id, statusName: "VERIFIED" } */
data class PaymentStatusResult(
    val id: Long,
    val statusName: String?
)

/** GET /service-fees/my: a campus service (parking, library...) plus my payment status for it. */
data class ServiceFeeResponse(
    val id: Long,
    val code: String,
    val name: String,
    val category: String, // MOTO_PARKING, LIBRARY, LABORATORY, ID_CARD, EXAM, OTHER
    val description: String?,
    val amount: Double,
    val validityLabel: String?,
    val validFrom: String?,
    val validUntil: String?,
    val repeatable: Boolean,
    val active: Boolean,
    val myStatus: String?, // NOT_PAID, PENDING, VERIFIED, PAID
    val myPaymentId: Long?,
    val myReceiptNumber: String?,
    val myPaidAt: String?
) {
    val isPaid: Boolean get() = myStatus == "PAID"
    val inProgress: Boolean get() = myStatus == "PENDING" || myStatus == "VERIFIED"
}

data class PayServiceFeeRequest(
    val paymentMethod: String,
    val transactionRef: String,
    val remarks: String?
)

data class UpdateAcademicInfoRequest(
    val facultyId: Long,
    val academicYearId: Long,
    val majorId: Long?
)
