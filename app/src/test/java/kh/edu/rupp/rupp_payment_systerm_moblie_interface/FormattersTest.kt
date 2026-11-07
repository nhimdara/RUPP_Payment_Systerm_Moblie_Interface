package kh.edu.rupp.rupp_payment_systerm_moblie_interface

import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.formatApiDate
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.formatApiDateTime
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.formatPaymentMethod
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.formatUsd
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.isPastDue
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.paidFraction
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Locale

class FormattersTest {

    private fun day(value: String) = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US).parse(value)!!

    @Test
    fun formatUsd_usesDollarSignAndTwoDecimals() {
        assertEquals("$600.00", formatUsd(600.0))
        assertEquals("$1,234.50", formatUsd(1234.5))
        assertEquals("$0.00", formatUsd(0.0))
    }

    @Test
    fun formatApiDate_readsApiFormat() {
        assertEquals("30 Nov 2024", formatApiDate("2024-11-30"))
    }

    @Test
    fun formatApiDate_handlesMissingAndInvalidValues() {
        assertEquals("-", formatApiDate(null))
        assertEquals("-", formatApiDate(""))
        assertEquals("not a date", formatApiDate("not a date"))
    }

    @Test
    fun formatApiDateTime_readsIsoWithoutTimezone() {
        assertEquals("19 Oct 2026, 23:20", formatApiDateTime("2026-10-19T23:20:00"))
        assertEquals("19 Oct 2026, 23:20", formatApiDateTime("2026-10-19T23:20:00.123"))
    }

    @Test
    fun formatPaymentMethod_knownAndUnknownMethods() {
        assertEquals("KHQR (Bakong)", formatPaymentMethod("KHQR_BAKONG"))
        assertEquals("ABA PayWay", formatPaymentMethod("ABA_PAYWAY"))
        assertEquals("Bank Transfer", formatPaymentMethod("BANK_TRANSFER"))
    }

    @Test
    fun paidFraction_isClamped() {
        assertEquals(0.5f, paidFraction(300.0, 600.0), 0.0001f)
        assertEquals(1f, paidFraction(700.0, 600.0), 0.0001f)
        assertEquals(0f, paidFraction(100.0, 0.0), 0.0001f)
    }

    @Test
    fun isPastDue_comparesWholeDays() {
        assertTrue(isPastDue("2026-10-26", today = day("2026-10-27 09:00")))
        assertFalse(isPastDue("2026-10-27", today = day("2026-10-27 23:59")))
        assertFalse(isPastDue("2026-11-30", today = day("2026-10-27 09:00")))
        assertFalse(isPastDue(null))
    }
}
