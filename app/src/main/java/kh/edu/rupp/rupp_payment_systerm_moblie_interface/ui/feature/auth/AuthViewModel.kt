package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.repository.ApiResult
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.repository.RuppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val login: String = "",
    val password: String = "",
    val remember: Boolean = true,
    val loading: Boolean = false,
    val error: String? = null,
    val serverUrl: String = "",
    val serverCheck: String? = null,
    val checkingServer: Boolean = false
)

class AuthViewModel(private val repo: RuppRepository) : ViewModel() {

    private val _state = MutableStateFlow(
        LoginUiState(
            login = repo.session.rememberedLogin.orEmpty(),
            remember = true,
            serverUrl = repo.session.baseUrl
        )
    )
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    fun onLoginChange(v: String) = _state.update { it.copy(login = v, error = null) }
    fun onPasswordChange(v: String) = _state.update { it.copy(password = v, error = null) }
    fun onRememberChange(v: Boolean) = _state.update { it.copy(remember = v) }
    fun showError(message: String) = _state.update { it.copy(error = message) }

    fun submit(onSuccess: () -> Unit) {
        val s = _state.value
        if (s.login.isBlank() || s.password.isBlank()) {
            _state.update { it.copy(error = "Enter your student ID / email and password.") }
            return
        }
        _state.update { it.copy(loading = true, error = null) }
        viewModelScope.launch {
            when (val r = repo.login(s.login, s.password)) {
                is ApiResult.Success -> {
                    if (r.data.user?.role != null && r.data.user.role != "ROLE_STUDENT") {
                        // This app is the student portal; staff use the web dashboard.
                        repo.logout()
                        _state.update { it.copy(loading = false, error = "This app is for students. Finance and admin staff should use the RUPP web portal.") }
                        return@launch
                    }
                    repo.session.rememberedLogin = if (s.remember) s.login.trim() else null
                    _state.update { it.copy(loading = false, password = "") }
                    onSuccess()
                }
                is ApiResult.Failure -> _state.update { it.copy(loading = false, error = r.message) }
            }
        }
    }

    fun onServerUrlChange(v: String) = _state.update { it.copy(serverUrl = v, serverCheck = null) }

    /** Saves the server address and checks it answers (any HTTP reply, even 401, means it is reachable). */
    fun saveAndTestServer() {
        val url = _state.value.serverUrl.trim()
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            _state.update { it.copy(serverCheck = "The address must start with http:// or https://") }
            return
        }
        repo.session.baseUrl = url
        _state.update { it.copy(checkingServer = true, serverCheck = null, serverUrl = repo.session.baseUrl) }
        viewModelScope.launch {
            val r = repo.me()
            val msg = when {
                r is ApiResult.Success -> "Connected ✓"
                r is ApiResult.Failure && r.httpCode != null -> "Connected ✓ (server replied ${r.httpCode})"
                else -> (r as ApiResult.Failure).message
            }
            _state.update { it.copy(checkingServer = false, serverCheck = msg) }
        }
    }
}
