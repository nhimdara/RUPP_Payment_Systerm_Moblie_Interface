package kh.edu.rupp.rupp_payment_systerm_moblie_interface.util

import android.util.Patterns

/**
 * Result wrapper for field validation
 */
data class ValidationResult(
    val isValid: Boolean,
    val message: String? = null
)

/**
 * Input validation utility matching RUPP Payment System API guidelines
 */
object ValidationUtils {

    /**
     * Validates Login usernameOrEmail field
     */
    fun validateUsernameOrEmail(input: String): ValidationResult {
        val trimmed = input.trim()
        return when {
            trimmed.isEmpty() -> ValidationResult(false, "Username or email is required")
            trimmed.length < 3 -> ValidationResult(false, "Must be at least 3 characters")
            else -> ValidationResult(true)
        }
    }

    /**
     * Validates username matching backend rule: 3 to 50 characters
     */
    fun validateUsername(username: String): ValidationResult {
        val trimmed = username.trim()
        return when {
            trimmed.isEmpty() -> ValidationResult(false, "Username is required")
            trimmed.length < 3 -> ValidationResult(false, "Username must be between 3 and 50 characters")
            trimmed.length > 50 -> ValidationResult(false, "Username cannot exceed 50 characters")
            !trimmed.matches(Regex("^[a-zA-Z0-9_.-]+$")) -> ValidationResult(false, "Only letters, numbers, dots, and underscores allowed")
            else -> ValidationResult(true)
        }
    }

    /**
     * Validates email address format
     */
    fun validateEmail(email: String): ValidationResult {
        val trimmed = email.trim()
        return when {
            trimmed.isEmpty() -> ValidationResult(false, "Email address is required")
            !Patterns.EMAIL_ADDRESS.matcher(trimmed).matches() -> ValidationResult(false, "Please enter a valid email address (e.g. name@rupp.edu.kh)")
            else -> ValidationResult(true)
        }
    }

    /**
     * Validates password matching backend rule: min 6 characters
     */
    fun validatePassword(password: String): ValidationResult {
        return when {
            password.isEmpty() -> ValidationResult(false, "Password is required")
            password.length < 6 -> ValidationResult(false, "Password must be at least 6 characters")
            else -> ValidationResult(true)
        }
    }

    /**
     * Validates password confirmation match
     */
    fun validateConfirmPassword(password: String, confirm: String): ValidationResult {
        return when {
            confirm.isEmpty() -> ValidationResult(false, "Please confirm your password")
            password != confirm -> ValidationResult(false, "Passwords do not match")
            else -> ValidationResult(true)
        }
    }

    /**
     * Validates 6-digit OTP code for payment verification and password reset
     */
    fun validateOtpCode(code: String): ValidationResult {
        val trimmed = code.trim()
        return when {
            trimmed.isEmpty() -> ValidationResult(false, "Verification code is required")
            trimmed.length != 6 -> ValidationResult(false, "Code must be exactly 6 digits")
            !trimmed.all { it.isDigit() } -> ValidationResult(false, "Verification code must contain digits only")
            else -> ValidationResult(true)
        }
    }

    /**
     * Validates optional name fields (max 50 chars)
     */
    fun validateName(name: String, fieldName: String = "Name"): ValidationResult {
        val trimmed = name.trim()
        return when {
            trimmed.length > 50 -> ValidationResult(false, "$fieldName cannot exceed 50 characters")
            else -> ValidationResult(true)
        }
    }

    /**
     * Helper to map backend error codes to human-readable strings
     */
    fun parseHttpErrorMessage(statusCode: Int, rawMessage: String?): String {
        return when (statusCode) {
            400 -> rawMessage ?: "Validation failed. Please check your inputs."
            401 -> "Invalid username/email or password."
            403 -> "Access denied. You do not have permission."
            404 -> "Account or record not found."
            409 -> "An account with this username or email already exists."
            410 -> "Verification code has expired. Please request a new one."
            422 -> "Invalid operation or conflict."
            423 -> "Account/OTP locked after too many failed attempts. Please contact ICT support."
            429 -> "Too many requests. Please wait a moment and try again."
            500 -> "Server error occurred. Please try again later."
            else -> rawMessage ?: "An unexpected error occurred. Please try again."
        }
    }
}

