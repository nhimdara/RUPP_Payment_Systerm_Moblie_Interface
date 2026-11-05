package kh.edu.rupp.rupp_payment_systerm_moblie_interface

import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.screens.profile.PasswordRules
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.screens.profile.PasswordStrength
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PasswordRulesTest {

    @Test
    fun meetsAll_requiresLengthCaseAndDigit() {
        assertTrue(PasswordRules.meetsAll("Rupp2026"))
        assertFalse(PasswordRules.meetsAll("rupp2026"))
        assertFalse(PasswordRules.meetsAll("RUPP2026"))
        assertFalse(PasswordRules.meetsAll("RuppRupp"))
        assertFalse(PasswordRules.meetsAll("Ru2026"))
    }

    @Test
    fun strength_levels() {
        assertEquals(PasswordStrength.Weak, PasswordRules.strength("abc"))
        assertEquals(PasswordStrength.Medium, PasswordRules.strength("Rupp2026"))
        assertEquals(PasswordStrength.Strong, PasswordRules.strength("Rupp@2026"))
        assertEquals(PasswordStrength.Strong, PasswordRules.strength("RuppStudent2026"))
    }

    @Test
    fun newPassword_mustDifferFromCurrent() {
        assertNotNull(PasswordRules.newError("Rupp2026", "Rupp2026"))
        assertNull(PasswordRules.newError("Rupp2025", "Rupp2026"))
    }

    @Test
    fun confirm_mustMatch() {
        assertNotNull(PasswordRules.confirmError("Rupp2026", ""))
        assertNotNull(PasswordRules.confirmError("Rupp2026", "Rupp2025"))
        assertNull(PasswordRules.confirmError("Rupp2026", "Rupp2026"))
    }
}
