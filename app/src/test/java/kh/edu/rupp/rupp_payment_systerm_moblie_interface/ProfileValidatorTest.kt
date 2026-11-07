package kh.edu.rupp.rupp_payment_systerm_moblie_interface

import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.screens.profile.ProfileValidator
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class ProfileValidatorTest {

    @Test
    fun validEmail_hasNoError() {
        assertNull(ProfileValidator.emailError("tang.chihong@rupp.edu.kh"))
    }

    @Test
    fun invalidEmail_hasError() {
        assertNotNull(ProfileValidator.emailError(""))
        assertNotNull(ProfileValidator.emailError("tang.chihong"))
        assertNotNull(ProfileValidator.emailError("tang@rupp"))
    }

    @Test
    fun validPhone_hasNoError() {
        assertNull(ProfileValidator.phoneError("012 345 678"))
        assertNull(ProfileValidator.phoneError("0961234567"))
    }

    @Test
    fun invalidPhone_hasError() {
        assertNotNull(ProfileValidator.phoneError(""))
        assertNotNull(ProfileValidator.phoneError("12345678"))
        assertNotNull(ProfileValidator.phoneError("012-345-678"))
        assertNotNull(ProfileValidator.phoneError("01234567890"))
    }

    @Test
    fun blankAddress_hasError() {
        assertNotNull(ProfileValidator.addressError("   "))
        assertNull(ProfileValidator.addressError("Phnom Penh"))
    }
}
