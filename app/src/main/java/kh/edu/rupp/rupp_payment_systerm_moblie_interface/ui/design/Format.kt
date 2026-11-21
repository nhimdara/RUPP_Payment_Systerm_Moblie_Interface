package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.roundToLong

/**
 * Formatting helpers for backend values. Uses SimpleDateFormat rather than java.time because
 * minSdk is 24 and java.time needs API 26 without desugaring.
 */
object Fmt {
    /** Display-only USD to KHR conversion shown as an approximation, like in the Figma. */
    private const val KHR_PER_USD = 4100

    private val usd: NumberFormat = NumberFormat.getCurrencyInstance(Locale.US)

    fun usd(amount: Double?): String = usd.format(amount ?: 0.0)

    fun khr(amount: Double?): String {
        val riel = ((amount ?: 0.0) * KHR_PER_USD).roundToLong()
        return "≈ " + NumberFormat.getIntegerInstance(Locale.US).format(riel) + " ៛"
    }

    private fun localDateTime(): SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
    private fun localDate(): SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    /** "2026-11-17T09:24:58.450962" (server local time, no zone) -> Date */
    fun parseDateTime(value: String?): Date? {
        if (value.isNullOrBlank()) return null
        val trimmed = value.substringBefore('.').removeSuffix("Z").take(19)
        return runCatching { localDateTime().parse(trimmed) }.getOrNull()
            ?: parseDate(value)
    }

    /** "2025-11-30" -> Date */
    fun parseDate(value: String?): Date? {
        if (value.isNullOrBlank()) return null
        return runCatching { localDate().parse(value.take(10)) }.getOrNull()
    }

    /** ISO instant in UTC ("2026-11-20T02:30:00.123Z") -> epoch millis */
    fun parseInstantMillis(value: String?): Long? {
        if (value.isNullOrBlank()) return null
        val f = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US).apply { timeZone = TimeZone.getTimeZone("UTC") }
        return runCatching { f.parse(value.substringBefore('.').removeSuffix("Z").take(19))?.time }.getOrNull()
    }

    fun dateLong(value: String?): String =
        parseDateTime(value)?.let { SimpleDateFormat("d MMM yyyy", Locale.US).format(it) } ?: "—"

    fun dateTime(value: String?): String =
        parseDateTime(value)?.let { SimpleDateFormat("d MMM yyyy · h:mm a", Locale.US).format(it) } ?: "—"

    fun time(value: String?): String =
        parseDateTime(value)?.let { SimpleDateFormat("h:mm a", Locale.US).format(it) } ?: ""

    fun monthGroup(value: String?): String =
        parseDateTime(value)?.let { SimpleDateFormat("MMMM yyyy", Locale.US).format(it) } ?: "Earlier"

    /** Whole days from today until [dateValue]; negative when overdue. */
    fun daysUntil(dateValue: String?): Int? {
        val due = parseDate(dateValue) ?: return null
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
        }
        return ((due.time - today.timeInMillis) / 86_400_000L).toInt()
    }

    fun greeting(): String = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
        in 5..11 -> "Good morning"
        in 12..16 -> "Good afternoon"
        else -> "Good evening"
    }

    fun mmss(totalSeconds: Long): String {
        val s = totalSeconds.coerceAtLeast(0)
        return "%02d:%02d".format(s / 60, s % 60)
    }
}

/** Payment channels the backend accepts (free text, upper-cased server side). */
enum class PayMethod(val code: String, val title: String, val subtitle: String, val badge: String?) {
    KHQR_BAKONG("KHQR_BAKONG", "Bakong KHQR", "Scan with any Cambodian banking app", "Instant"),
    ABA_PAYWAY("ABA_PAYWAY", "ABA PayWay", "Pay from your ABA Mobile account", null),
    WING("WING", "Wing Bank", "Wing app or any Wing agent", null),
    ACLEDA("ACLEDAMOBILE", "ACLEDA Mobile", "ACLEDA Unity ToanChet app", null);

    companion object {
        /** Human label for any method string the backend returns (including seeded ones like ABA_PAY). */
        fun label(code: String?): String {
            val c = code?.uppercase().orEmpty()
            return entries.firstOrNull { it.code == c }?.title ?: when {
                c.startsWith("ABA") -> "ABA PayWay"
                c.startsWith("WING") -> "Wing Bank"
                c.startsWith("ACLEDA") -> "ACLEDA Mobile"
                c.contains("KHQR") || c.contains("BAKONG") -> "Bakong KHQR"
                c == "CASH" -> "Cash at Finance Office"
                c.isBlank() -> "—"
                else -> c.replace('_', ' ').lowercase().replaceFirstChar { it.uppercase() }
            }
        }
    }
}
