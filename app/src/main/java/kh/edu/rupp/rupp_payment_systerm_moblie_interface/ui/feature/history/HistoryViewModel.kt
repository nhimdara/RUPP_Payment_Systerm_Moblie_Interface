package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.feature.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.PaymentResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.PaymentStatus
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.repository.ApiResult
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.repository.RuppRepository
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.PayMethod
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class HistoryFilter(val label: String, val statuses: Set<PaymentStatus>?) {
    ALL("All", null),
    CONFIRMED("Confirmed", setOf(PaymentStatus.CONFIRMED, PaymentStatus.COMPLETED)),
    IN_PROGRESS("In progress", setOf(PaymentStatus.PENDING, PaymentStatus.VERIFIED)),
    CLOSED("Rejected", setOf(PaymentStatus.REJECTED, PaymentStatus.CANCELLED, PaymentStatus.REFUNDED, PaymentStatus.FAILED))
}

data class HistoryUiState(
    val items: List<PaymentResponse> = emptyList(),
    val totalElements: Long = 0,
    val page: Int = 0,
    val lastPage: Boolean = false,
    val loading: Boolean = true,
    val loadingMore: Boolean = false,
    val refreshing: Boolean = false,
    val error: String? = null,
    val filter: HistoryFilter = HistoryFilter.ALL,
    val query: String = ""
) {
    val visible: List<PaymentResponse>
        get() {
            val q = query.trim().lowercase()
            return items.filter { p ->
                (filter.statuses == null || p.status in filter.statuses) &&
                    (q.isEmpty() || listOfNotNull(
                        p.remarks,
                        p.receipt?.receiptNumber,
                        p.academicYearLabel,
                        "#${p.id}",
                        p.details.firstOrNull()?.transactionRef,
                        PayMethod.label(p.details.firstOrNull()?.method)
                    ).any { it.lowercase().contains(q) })
            }
        }

    fun count(f: HistoryFilter): Int = items.count { f.statuses == null || it.status in f.statuses }

    val confirmedTotal: Double
        get() = items.filter { it.status == PaymentStatus.CONFIRMED || it.status == PaymentStatus.COMPLETED }.sumOf { it.totalPaid }

    val inProgressTotal: Double
        get() = items.filter { it.status == PaymentStatus.PENDING || it.status == PaymentStatus.VERIFIED }.sumOf { it.totalPaid }
}

/** Paged list from GET /payments/my-payments (newest first), loaded as the student scrolls. */
class HistoryViewModel(private val repo: RuppRepository) : ViewModel() {

    private val _state = MutableStateFlow(HistoryUiState())
    val state: StateFlow<HistoryUiState> = _state.asStateFlow()

    init { refresh(initial = true) }

    fun refresh(initial: Boolean = false) {
        _state.update { it.copy(loading = initial && it.items.isEmpty(), refreshing = !initial, error = null) }
        viewModelScope.launch {
            when (val r = repo.myPayments(page = 0, size = PAGE_SIZE)) {
                is ApiResult.Success -> _state.update {
                    it.copy(
                        items = r.data.content,
                        totalElements = r.data.totalElements,
                        page = 0,
                        lastPage = r.data.last,
                        loading = false,
                        refreshing = false
                    )
                }
                is ApiResult.Failure -> _state.update { it.copy(loading = false, refreshing = false, error = r.message) }
            }
        }
    }

    fun loadMore() {
        val s = _state.value
        if (s.loadingMore || s.lastPage || s.loading) return
        _state.update { it.copy(loadingMore = true) }
        viewModelScope.launch {
            when (val r = repo.myPayments(page = s.page + 1, size = PAGE_SIZE)) {
                is ApiResult.Success -> _state.update {
                    val known = it.items.map { p -> p.id }.toSet()
                    it.copy(
                        items = it.items + r.data.content.filter { p -> p.id !in known },
                        page = s.page + 1,
                        lastPage = r.data.last,
                        loadingMore = false
                    )
                }
                is ApiResult.Failure -> _state.update { it.copy(loadingMore = false, error = r.message) }
            }
        }
    }

    fun setFilter(f: HistoryFilter) = _state.update { it.copy(filter = f) }
    fun setQuery(q: String) = _state.update { it.copy(query = q) }

    private companion object { const val PAGE_SIZE = 20 }
}
