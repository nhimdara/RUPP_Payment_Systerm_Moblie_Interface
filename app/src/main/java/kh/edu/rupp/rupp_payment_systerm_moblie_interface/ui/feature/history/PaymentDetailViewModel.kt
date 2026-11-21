package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.feature.history

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.PaymentResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.PaymentStatus
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.ReceiptResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.repository.ApiResult
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.repository.RuppRepository
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.feature.pay.OtpFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

data class PaymentDetailUiState(
    val loading: Boolean = true,
    val error: String? = null,
    val payment: PaymentResponse? = null,
    val receipt: ReceiptResponse? = null,
    val downloading: Boolean = false,
    val message: String? = null
)

class PaymentDetailViewModel(private val repo: RuppRepository, private val paymentId: Long) : ViewModel() {

    private val _state = MutableStateFlow(PaymentDetailUiState())
    val state: StateFlow<PaymentDetailUiState> = _state.asStateFlow()

    val otp = OtpFlow(repo, viewModelScope)

    init { load() }

    fun load() {
        _state.update { it.copy(loading = it.payment == null, error = null) }
        viewModelScope.launch {
            when (val r = repo.payment(paymentId)) {
                is ApiResult.Success -> {
                    val p = r.data
                    // Receipts exist only for CONFIRMED payments; students must use /receipts/payment/{id}.
                    val receipt = p.receipt ?: if (p.status == PaymentStatus.CONFIRMED) {
                        (repo.receipt(p.id) as? ApiResult.Success)?.data
                    } else null
                    _state.update { it.copy(loading = false, payment = p, receipt = receipt) }
                }
                is ApiResult.Failure -> _state.update { it.copy(loading = false, error = r.message) }
            }
        }
    }

    fun startVerification() = otp.start(paymentId)

    fun onVerified() {
        _state.update { it.copy(message = "Payment verified. Waiting for Finance Office confirmation.") }
        load()
    }

    fun downloadReceipt(context: Context, onReady: (File) -> Unit) {
        val receipt = _state.value.receipt ?: return
        _state.update { it.copy(downloading = true, message = null) }
        viewModelScope.launch {
            when (val r = repo.downloadReceiptPdf(context, paymentId, receipt.receiptNumber)) {
                is ApiResult.Success -> {
                    _state.update { it.copy(downloading = false) }
                    onReady(r.data)
                }
                is ApiResult.Failure -> _state.update { it.copy(downloading = false, message = r.message) }
            }
        }
    }

    fun clearMessage() = _state.update { it.copy(message = null) }
}
