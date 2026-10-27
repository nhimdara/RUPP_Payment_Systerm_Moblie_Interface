package kh.edu.rupp.rupp_payment_systerm_moblie_interface.viewmodel

import androidx.lifecycle.ViewModel
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.PaymentCategory
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.PaymentHistoryItem
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.PaymentHistorySummary
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.PaymentStatus
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.repository.PaymentHistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class PaymentHistoryUiState(
    val transactions: List<PaymentHistoryItem> = emptyList(),
    val groupedTransactions: Map<String, List<PaymentHistoryItem>> = emptyMap(),
    val selectedCategory: PaymentCategory = PaymentCategory.ALL,
    val selectedStatus: PaymentStatus? = null,
    val searchQuery: String = "",
    val isSearchActive: Boolean = false,
    val summary: PaymentHistorySummary = PaymentHistorySummary(
        totalPaid = 0.0,
        totalTopUp = 0.0,
        transactionCount = 0
    ),
    val selectedTransactionForDetail: PaymentHistoryItem? = null
)

class PaymentHistoryViewModel(
    private val repository: PaymentHistoryRepository = PaymentHistoryRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(PaymentHistoryUiState())
    val uiState: StateFlow<PaymentHistoryUiState> = _uiState.asStateFlow()

    private val allPayments = repository.getAllPayments()

    init {
        loadData()
    }

    private fun loadData() {
        val summary = repository.getSummary()
        _uiState.update { state ->
            val filtered = applyFilters(
                items = allPayments,
                category = state.selectedCategory,
                status = state.selectedStatus,
                query = state.searchQuery
            )
            state.copy(
                transactions = filtered,
                groupedTransactions = groupItems(filtered),
                summary = summary
            )
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { state ->
            val filtered = applyFilters(
                items = allPayments,
                category = state.selectedCategory,
                status = state.selectedStatus,
                query = query
            )
            state.copy(
                searchQuery = query,
                transactions = filtered,
                groupedTransactions = groupItems(filtered)
            )
        }
    }

    fun onCategorySelected(category: PaymentCategory) {
        _uiState.update { state ->
            val newCategory = if (state.selectedCategory == category && category != PaymentCategory.ALL) {
                PaymentCategory.ALL
            } else {
                category
            }
            val filtered = applyFilters(
                items = allPayments,
                category = newCategory,
                status = state.selectedStatus,
                query = state.searchQuery
            )
            state.copy(
                selectedCategory = newCategory,
                transactions = filtered,
                groupedTransactions = groupItems(filtered)
            )
        }
    }

    fun onStatusSelected(status: PaymentStatus?) {
        _uiState.update { state ->
            val newStatus = if (state.selectedStatus == status) null else status
            val filtered = applyFilters(
                items = allPayments,
                category = state.selectedCategory,
                status = newStatus,
                query = state.searchQuery
            )
            state.copy(
                selectedStatus = newStatus,
                transactions = filtered,
                groupedTransactions = groupItems(filtered)
            )
        }
    }

    fun toggleSearch() {
        _uiState.update { state ->
            val active = !state.isSearchActive
            val query = if (!active) "" else state.searchQuery
            val filtered = applyFilters(
                items = allPayments,
                category = state.selectedCategory,
                status = state.selectedStatus,
                query = query
            )
            state.copy(
                isSearchActive = active,
                searchQuery = query,
                transactions = filtered,
                groupedTransactions = groupItems(filtered)
            )
        }
    }

    fun onTransactionClicked(item: PaymentHistoryItem) {
        _uiState.update { it.copy(selectedTransactionForDetail = item) }
    }

    fun onDismissDetail() {
        _uiState.update { it.copy(selectedTransactionForDetail = null) }
    }

    private fun applyFilters(
        items: List<PaymentHistoryItem>,
        category: PaymentCategory,
        status: PaymentStatus?,
        query: String
    ): List<PaymentHistoryItem> {
        return items.filter { item ->
            val matchesCategory = (category == PaymentCategory.ALL || item.category == category)
            val matchesStatus = (status == null || item.status == status)
            val matchesQuery = query.isBlank() ||
                    item.title.contains(query, ignoreCase = true) ||
                    item.description.contains(query, ignoreCase = true) ||
                    item.transactionRef.contains(query, ignoreCase = true) ||
                    item.invoiceNo.contains(query, ignoreCase = true) ||
                    item.paymentMethod.displayName.contains(query, ignoreCase = true)

            matchesCategory && matchesStatus && matchesQuery
        }
    }

    private fun groupItems(items: List<PaymentHistoryItem>): Map<String, List<PaymentHistoryItem>> {
        return items.groupBy { it.monthGroup }
    }
}
