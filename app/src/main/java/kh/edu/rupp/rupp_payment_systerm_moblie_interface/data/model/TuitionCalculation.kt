package kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model

// GET /api/v1/tuition-fees/calculate (API doc section 10)
data class TuitionCalculationResponse(
    val studentId: Long,
    val studentName: String,
    val studentEmail: String,
    val facultyName: String?,
    val academicYear: String?,
    val majorName: String?,
    val totalTuitionFee: Double,
    val totalPaid: Double,
    val remainingBalance: Double,
    val fullyPaid: Boolean,
    val paymentStatus: String, // display string PAID, PARTIAL or UNPAID, not the PaymentStatus enum
    val dueDate: String?,
    val paymentHistory: List<PaymentResponse> = emptyList()
)
