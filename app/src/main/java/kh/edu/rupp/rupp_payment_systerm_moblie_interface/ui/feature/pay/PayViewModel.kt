package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.feature.pay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.PaymentResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.PaymentStatus
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.remote.dto.ServiceFeeResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.repository.ApiResult
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.repository.PaymentSetup
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.repository.RuppRepository
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.PayMethod
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.round

/** What the student is paying for in the checkout sheet. */
sealed interface PayTarget {
    data object Tuition : PayTarget
    data class Service(val fee: ServiceFeeResponse) : PayTarget
}

enum class AmountOption { FULL, HALF, CUSTOM }

data class PayUiState(
    val loading: Boolean = true,
    val error: String? = null,
    val setup: PaymentSetup? = null,
    /** Checkout sheet target; null = sheet closed. */
    val target: PayTarget? = null,
    val amountOption: AmountOption = AmountOption.FULL,
    val customAmount: String = "",
    val method: PayMethod = PayMethod.KHQR_BAKONG,
    val reference: String = "",
    val qrDeadlineMillis: Long = 0,
    val submitting: Boolean = false,
    val submitError: String? = null,
    /** Payment created on the backend (PENDING until the OTP is verified). */
    val payment: PaymentResponse? = null,
    /** True once the OTP was verified: show the success page. */
    val done: Boolean = false
) {
    val tuitionRemaining: Double get() = setup?.tuition?.remainingBalance ?: 0.0

    val amount: Double
        get() = when (val t = target) {
            is PayTarget.Service -> t.fee.amount
            else -> when (amountOption) {
                AmountOption.FULL -> tuitionRemaining
                AmountOption.HALF -> round(tuitionRemaining * 50) / 100.0
                AmountOption.CUSTOM -> customAmount.toDoubleOrNull() ?: 0.0
            }
        }

    val amountError: String?
        get() = when {
            target !is PayTarget.Tuition || amountOption != AmountOption.CUSTOM || customAmount.isBlank() -> null
            amount < 0.01 -> "Enter an amount above \$0.00"
            amount > tuitionRemaining + 0.0001 -> "You only owe \$${"%.2f".format(tuitionRemaining)}"
            else -> null
        }

    val canPay: Boolean get() = amount >= 0.01 && amountError == null &&
        (target !is PayTarget.Tuition || amount <= tuitionRemaining + 0.0001)

    val title: String
        get() = when (val t = target) {
            is PayTarget.Service -> t.fee.name
            else -> "Tuition Fee ${setup?.fee?.academicYear?.yearLabel ?: ""}".trim()
        }

    /** Tuition payment already waiting for OTP or Finance. */
    val tuitionInProgress: PaymentResponse?
        get() = setup?.tuition?.paymentHistory?.firstOrNull {
            it.status == PaymentStatus.PENDING || it.status == PaymentStatus.VERIFIED
        }
}

class PayViewModel(private val repo: RuppRepository) : ViewModel() {

    private val _state = MutableStateFlow(PayUiState())
    val state: StateFlow<PayUiState> = _state.asStateFlow()

    val otp = OtpFlow(repo, viewModelScope)

    init { load() }

    fun load() {
        _state.update { it.copy(loading = it.setup == null, error = null) }
        viewModelScope.launch {
            when (val r = repo.paymentSetup()) {
                is ApiResult.Success -> _state.update { it.copy(loading = false, setup = r.data) }
                is ApiResult.Failure -> _state.update { it.copy(loading = false, error = r.message) }
            }
        }
    }

    /** Back to the hub with fresh data (after a finished payment). */
    fun reset() {
        _state.update { PayUiState(loading = it.setup == null, setup = it.setup) }
        load()
    }

    fun openTuition() = open(PayTarget.Tuition)
    fun openService(fee: ServiceFeeResponse) = open(PayTarget.Service(fee))

    private fun open(target: PayTarget) {
        _state.update {
            it.copy(
                target = target,
                amountOption = AmountOption.FULL,
                customAmount = "",
                reference = newReference(it.method),
                qrDeadlineMillis = System.currentTimeMillis() + 15 * 60_000,
                submitError = null,
                payment = null,
                done = false
            )
        }
    }

    fun closeCheckout() = _state.update { it.copy(target = null, submitError = null) }

    fun selectAmount(option: AmountOption) = _state.update { it.copy(amountOption = option) }
    fun onCustomAmount(v: String) = _state.update {
        it.copy(customAmount = v.filter { c -> c.isDigit() || c == '.' }.take(9), amountOption = AmountOption.CUSTOM)
    }

    fun selectMethod(m: PayMethod) = _state.update {
        if (it.method == m) it else it.copy(method = m, reference = newReference(m))
    }

    /**
     * Student confirms they paid in their banking app: create the payment (PENDING) and
     * immediately request the OTP. Tuition uses POST /payments; campus services use
     * POST /service-fees/{id}/pay.
     */
    fun submit() {
        val s = _state.value
        val setup = s.setup
        val target = s.target ?: return
        if (s.submitting || !s.canPay) return
        s.payment?.let { otp.start(it.id); return } // retry: never create a duplicate

        _state.update { it.copy(submitting = true, submitError = null) }
        viewModelScope.launch {
            val result = when (target) {
                is PayTarget.Tuition -> {
                    if (setup == null) return@launch
                    val remarks = listOfNotNull(
                        "Tuition ${setup.fee.academicYear.yearLabel}",
                        setup.activeSemester?.semesterName,
                        "via ${s.method.title} (RUPP Pay app)"
                    ).joinToString(" · ")
                    repo.createPayment(setup.fee.id, s.method.code, s.reference, s.amount, remarks)
                }
                is PayTarget.Service -> repo.payServiceFee(
                    target.fee.id, s.method.code, s.reference, "${target.fee.name} via ${s.method.title} (RUPP Pay app)"
                )
            }
            when (result) {
                is ApiResult.Success -> {
                    _state.update { it.copy(submitting = false, payment = result.data, target = null) }
                    otp.start(result.data.id)
                }
                is ApiResult.Failure -> _state.update {
                    it.copy(submitting = false, submitError = result.fieldErrors.values.firstOrNull() ?: result.message)
                }
            }
        }
    }

    fun onVerified(paymentId: Long) {
        viewModelScope.launch {
            val refreshed = (repo.payment(paymentId) as? ApiResult.Success)?.data
            _state.update { it.copy(payment = refreshed ?: it.payment, done = true, target = null) }
        }
    }

    private fun newReference(method: PayMethod): String {
        val prefix = when (method) {
            PayMethod.KHQR_BAKONG -> "KHQR"
            PayMethod.ABA_PAYWAY -> "ABA"
            PayMethod.WING -> "WING"
            PayMethod.ACLEDA -> "ACL"
        }
        val date = SimpleDateFormat("yyyyMMdd", Locale.US).format(Date())
        val chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"
        val random = (1..6).map { chars.random() }.joinToString("")
        return "$prefix-$date-$random"
    }
}
