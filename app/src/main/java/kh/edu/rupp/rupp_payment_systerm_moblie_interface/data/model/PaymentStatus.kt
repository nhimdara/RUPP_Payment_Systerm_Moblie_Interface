package kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model

// PaymentStatus enum from the REST API (section 1.6)
enum class PaymentStatus {
    PENDING,
    VERIFIED,
    CONFIRMED,
    REJECTED,
    CANCELLED,
    REFUNDED
}
