package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.feature.pay

import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.repository.ApiResult
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.repository.RuppRepository
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.Fmt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class OtpUiState(
    val visible: Boolean = false,
    val paymentId: Long? = null,
    val challengeId: String? = null,
    val expiresAtMillis: Long? = null,
    /** Earliest time a resend is allowed (backend: once per 60 s, 429 + Retry-After). */
    val resendAvailableAtMillis: Long = 0,
    val code: String = "",
    val requesting: Boolean = false,
    val verifying: Boolean = false,
    val error: String? = null,
    val remainingAttempts: Int? = null,
    val locked: Boolean = false
)

/**
 * Drives the backend OTP flow (section 12.1 of the API guide):
 * request -> verify (payment becomes VERIFIED), with resend and attempt limits.
 * Shared by the Pay flow and the payment detail screen.
 */
class OtpFlow(private val repo: RuppRepository, private val scope: CoroutineScope) {

    private val _state = MutableStateFlow(OtpUiState())
    val state: StateFlow<OtpUiState> = _state.asStateFlow()

    fun start(paymentId: Long) {
        _state.value = OtpUiState(visible = true, paymentId = paymentId, requesting = true)
        scope.launch {
            when (val r = repo.requestOtp(paymentId)) {
                is ApiResult.Success -> _state.update {
                    it.copy(
                        requesting = false,
                        challengeId = r.data.challengeId,
                        expiresAtMillis = Fmt.parseInstantMillis(r.data.expiresAt),
                        resendAvailableAtMillis = System.currentTimeMillis() + 60_000
                    )
                }
                is ApiResult.Failure -> _state.update {
                    it.copy(requesting = false, error = r.message, locked = r.httpCode == 423)
                }
            }
        }
    }

    fun onCodeChange(value: String) {
        val digits = value.filter { it.isDigit() }.take(6)
        _state.update { it.copy(code = digits, error = null) }
    }

    fun resend() {
        val challenge = _state.value.challengeId
        if (challenge == null) {
            _state.value.paymentId?.let { start(it) }
            return
        }
        _state.update { it.copy(requesting = true, error = null, code = "") }
        scope.launch {
            when (val r = repo.resendOtp(challenge)) {
                is ApiResult.Success -> _state.update {
                    it.copy(
                        requesting = false,
                        challengeId = r.data.challengeId,
                        expiresAtMillis = Fmt.parseInstantMillis(r.data.expiresAt),
                        resendAvailableAtMillis = System.currentTimeMillis() + 60_000,
                        remainingAttempts = null
                    )
                }
                is ApiResult.Failure -> _state.update {
                    it.copy(
                        requesting = false,
                        error = r.message,
                        resendAvailableAtMillis = r.retryAfterSeconds
                            ?.let { s -> System.currentTimeMillis() + s * 1000L }
                            ?: it.resendAvailableAtMillis
                    )
                }
            }
        }
    }

    /** Verifies the code; calls [onVerified] with the payment id when the backend accepts it. */
    fun verify(onVerified: (Long) -> Unit) {
        val s = _state.value
        val challenge = s.challengeId ?: return
        if (s.code.length != 6) {
            _state.update { it.copy(error = "Enter all 6 digits.") }
            return
        }
        _state.update { it.copy(verifying = true, error = null) }
        scope.launch {
            when (val r = repo.verifyOtp(challenge, s.code)) {
                is ApiResult.Success -> {
                    _state.update { it.copy(verifying = false, visible = false) }
                    onVerified(r.data.id)
                }
                is ApiResult.Failure -> _state.update {
                    it.copy(
                        verifying = false,
                        code = "",
                        error = when (r.httpCode) {
                            410 -> "This code has expired. Tap Resend to get a new one."
                            423 -> "Too many wrong attempts. Verification is locked for 15 minutes."
                            else -> r.message
                        },
                        remainingAttempts = r.remainingAttempts,
                        locked = r.httpCode == 423
                    )
                }
            }
        }
    }

    fun dismiss() = _state.update { it.copy(visible = false) }
}
