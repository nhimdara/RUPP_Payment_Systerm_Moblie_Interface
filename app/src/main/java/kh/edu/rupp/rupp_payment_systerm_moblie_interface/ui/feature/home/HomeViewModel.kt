package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.repository.ApiResult
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.repository.DashboardData
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.repository.RuppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val loading: Boolean = true,
    val refreshing: Boolean = false,
    val data: DashboardData? = null,
    val error: String? = null
)

class HomeViewModel(private val repo: RuppRepository) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    init { load() }

    fun load(pullToRefresh: Boolean = false) {
        _state.update {
            it.copy(
                loading = it.data == null && !pullToRefresh,
                refreshing = pullToRefresh,
                error = null
            )
        }
        viewModelScope.launch {
            when (val r = repo.dashboard()) {
                is ApiResult.Success -> _state.update { HomeUiState(loading = false, data = r.data) }
                is ApiResult.Failure -> _state.update { it.copy(loading = false, refreshing = false, error = r.message) }
            }
        }
    }
}
