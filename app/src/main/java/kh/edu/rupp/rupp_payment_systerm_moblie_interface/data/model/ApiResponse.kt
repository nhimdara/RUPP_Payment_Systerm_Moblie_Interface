package kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model

/**
 * Standard backend response envelope matching Section 1.1 of the RUPP API Guide:
 * {
 *   "success": true,
 *   "message": "...",
 *   "data": { ... },
 *   "timestamp": "2026-10-19T23:25:00"
 * }
 */
data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T? = null,
    val timestamp: String? = null
)

/**
 * Validation failure envelope (HTTP 400) where data holds a map of field to error message:
 * {
 *   "success": false,
 *   "message": "Validation failed",
 *   "data": {
 *      "username": "Username must be between 3 and 50 characters",
 *      "email": "Email must be a well-formed email address",
 *      "password": "Password must be at least 6 characters"
 *   },
 *   "timestamp": "..."
 * }
 */
data class ValidationErrorResponse(
    val success: Boolean = false,
    val message: String = "Validation failed",
    val data: Map<String, String>? = null,
    val timestamp: String? = null
)

