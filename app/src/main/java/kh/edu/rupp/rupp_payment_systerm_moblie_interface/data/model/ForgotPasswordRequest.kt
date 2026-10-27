package kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model

/**
 * Data models for Password Recovery flow
 */
data class ForgotPasswordRequest(
    val emailOrUsername: String
)

data class VerifyOtpRequest(
    val emailOrUsername: String,
    val otpCode: String
)

data class ResetPasswordRequest(
    val emailOrUsername: String,
    val otpCode: String,
    val newPassword: String
)

