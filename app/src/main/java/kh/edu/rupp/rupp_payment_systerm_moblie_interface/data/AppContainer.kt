package kh.edu.rupp.rupp_payment_systerm_moblie_interface.data

import android.content.Context
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.remote.ApiClient
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.remote.SessionStore
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.repository.RuppRepository

/** Tiny manual dependency container: one session, one API client, one repository. */
object AppContainer {
    lateinit var repository: RuppRepository
        private set

    fun init(context: Context) {
        if (::repository.isInitialized) return
        val session = SessionStore(context.applicationContext)
        repository = RuppRepository(ApiClient(session), session)
    }
}
