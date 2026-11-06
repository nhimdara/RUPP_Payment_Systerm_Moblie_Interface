package kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model

/**
 * 4 Supported Payment Methods
 */
enum class PaymentMethodType(
    val title: String,
    val subtitle: String
) {
    STUDENT_WALLET(
        title = "RUPP Student Wallet",
        subtitle = "Balance: $120.00"
    ),
    KHQR_BAKONG(
        title = "Bakong / KHQR",
        subtitle = "Scan via any Cambodian mobile banking"
    ),
    ABA_PAY(
        title = "ABA Pay / Mobile App",
        subtitle = "Direct instant debit from ABA account"
    ),
    WING_BANK(
        title = "Wing Bank",
        subtitle = "Wing App & Agent clearing"
    )
}

/**
 * Official Electronic Receipt Model
 * Generated after payment is confirmed
 */
data class PaymentReceipt(
    val receiptNumber: String,
    val studentName: String = "Sokha Chan",
    val studentId: String = "20260142",
    val facultyName: String = "Faculty of Science",
    val majorName: String = "Computer Science & Engineering",
    val feeTitle: String,
    val totalAmount: Double,
    val paymentMethod: PaymentMethodType,
    val transactionRef: String,
    val issueDate: String
) {
    val amountInKhr: Long
        get() = (totalAmount * 4100).toLong()
}

