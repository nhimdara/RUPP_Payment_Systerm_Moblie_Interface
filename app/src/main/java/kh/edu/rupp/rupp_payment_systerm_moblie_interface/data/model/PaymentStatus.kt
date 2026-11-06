package kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model

// PaymentStatus enum from the REST API (section 1.6) and Payment History UI
enum class PaymentStatus(val displayName: String) {
    PENDING("Pending"),
    VERIFIED("Verified"),
    CONFIRMED("Confirmed"),
    COMPLETED("Completed"),
    FAILED("Failed"),
    REJECTED("Rejected"),
    CANCELLED("Cancelled"),
    REFUNDED("Refunded")
}
