package kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model

// Field names match the REST API JSON (API doc section 11). Dates are API strings.

data class PaymentResponse(
    val id: Long,
    val studentId: Long,
    val studentName: String,
    val studentEmail: String,
    val facultyName: String?,
    val academicYearLabel: String?,
    val feeStructureId: Long,
    val feeStructureAmount: Double,
    val status: PaymentStatus,
    val paymentDate: String,
    val totalPaid: Double,
    val remarks: String?,
    val details: List<PaymentDetailResponse> = emptyList(),
    val receipt: ReceiptResponse? = null // filled once the payment is CONFIRMED
)

data class PaymentDetailResponse(
    val id: Long,
    val method: String,
    val transactionRef: String,
    val amount: Double
)

data class ReceiptResponse(
    val id: Long,
    val paymentId: Long,
    val receiptNumber: String,
    val issueDate: String,
    val totalAmount: Double
)
