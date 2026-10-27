package kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model

/**
 * Authentication request payload matching backend JSON schema:
 * {
 *    "usernameOrEmail": "ayutaya",
 *    "password": "student123"
 * }
 */
data class LoginRequest(
    val usernameOrEmail: String,
    val password: String
)

/**
 * Standard login response payload
 */
data class LoginResponse(
    val success: Boolean = true,
    val token: String? = null,
    val message: String? = null,
    val usernameOrEmail: String? = null
)

