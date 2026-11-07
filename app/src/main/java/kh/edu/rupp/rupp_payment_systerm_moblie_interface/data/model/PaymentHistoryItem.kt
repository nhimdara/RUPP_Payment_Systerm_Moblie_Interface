package kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model

enum class PaymentCategory(val displayName: String) {
    ALL("All"),
    TUITION("Tuition Fee"),
    EXAM("Exam & Retake"),
    SERVICES("Campus Services"),
    CERTIFICATE("Certificate"),
    TOP_UP("Top-up")
}

enum class PaymentStatus(val displayName: String) {
    COMPLETED("Completed"),
    PENDING("Pending"),
    FAILED("Failed")
}

enum class PaymentMethod(val displayName: String) {
    KHQR("Bakong KHQR"),
    ABA_PAY("ABA Mobile"),
    STUDENT_BALANCE("Student Balance"),
    WING_BANK("Wing Bank"),
    ACLEDA("ACLEDA Mobile")
}

data class PaymentHistoryItem(
    val id: String,
    val title: String,
    val description: String,
    val category: PaymentCategory,
    val amount: Double,
    val currency: String = "USD",
    val status: PaymentStatus,
    val paymentMethod: PaymentMethod,
    val transactionRef: String,
    val invoiceNo: String,
    val date: String,
    val time: String,
    val monthGroup: String, // e.g., "October 2026"
    val academicYear: String = "2026–2027",
    val semester: String = "Semester 1",
    val studentName: String = "Sokha Chan",
    val studentId: String = "20260142",
    val faculty: String = "Faculty of Business Management",
    val isDeduction: Boolean = true // true = paid fee, false = top up
)

data class PaymentHistorySummary(
    val totalPaid: Double,
    val totalTopUp: Double,
    val transactionCount: Int,
    val currentBalance: Double = 120.00,
    val academicYear: String = "2026–2027"
)
