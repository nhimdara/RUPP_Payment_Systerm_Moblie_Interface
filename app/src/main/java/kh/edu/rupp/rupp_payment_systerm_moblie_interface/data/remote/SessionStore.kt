package kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.remote

import android.content.Context
import androidx.core.content.edit
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.BuildConfig
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.remote.dto.AuthResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Keeps the JWT pair and a few app preferences in SharedPreferences.
 * The tokens are what the backend issues on login; nothing here is hard-coded user data.
 */
class SessionStore(context: Context) {

    private val prefs = context.getSharedPreferences("rupp_session", Context.MODE_PRIVATE)

    private val _darkMode = MutableStateFlow(prefs.getBoolean(KEY_DARK_MODE, false))
    val darkMode: StateFlow<Boolean> = _darkMode.asStateFlow()

    /** Fires when the refresh token is rejected, so the UI can return to Login. */
    private val _sessionExpired = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val sessionExpired: SharedFlow<Unit> = _sessionExpired.asSharedFlow()

    val accessToken: String? get() = prefs.getString(KEY_ACCESS, null)
    val refreshToken: String? get() = prefs.getString(KEY_REFRESH, null)
    val username: String? get() = prefs.getString(KEY_USERNAME, null)
    val hasSession: Boolean get() = !accessToken.isNullOrBlank()

    /** Remembered login name for the "Remember me" option (never the password). */
    var rememberedLogin: String?
        get() = prefs.getString(KEY_REMEMBERED, null)
        set(value) = prefs.edit { if (value == null) remove(KEY_REMEMBERED) else putString(KEY_REMEMBERED, value) }

    var baseUrl: String
        get() = prefs.getString(KEY_BASE_URL, null) ?: BuildConfig.API_BASE_URL
        set(value) {
            val normalized = value.trim().let { if (it.endsWith("/")) it else "$it/" }
            prefs.edit { putString(KEY_BASE_URL, normalized) }
        }

    fun saveAuth(auth: AuthResponse) {
        prefs.edit {
            putString(KEY_ACCESS, auth.accessToken)
            putString(KEY_REFRESH, auth.refreshToken)
            auth.user?.let {
                putString(KEY_USERNAME, it.username)
                putString(KEY_ROLE, it.role)
            }
        }
    }

    fun clearAuth() {
        prefs.edit {
            remove(KEY_ACCESS)
            remove(KEY_REFRESH)
            remove(KEY_USERNAME)
            remove(KEY_ROLE)
        }
    }

    fun notifySessionExpired() {
        clearAuth()
        _sessionExpired.tryEmit(Unit)
    }

    fun setDarkMode(enabled: Boolean) {
        prefs.edit { putBoolean(KEY_DARK_MODE, enabled) }
        _darkMode.value = enabled
    }

    private companion object {
        const val KEY_ACCESS = "access_token"
        const val KEY_REFRESH = "refresh_token"
        const val KEY_USERNAME = "username"
        const val KEY_ROLE = "role"
        const val KEY_REMEMBERED = "remembered_login"
        const val KEY_BASE_URL = "base_url"
        const val KEY_DARK_MODE = "dark_mode"
    }
}
