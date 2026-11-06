package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components

import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// API formats: dates are yyyy-MM-dd, date-times are ISO strings without timezone
private const val API_DATE = "yyyy-MM-dd"
private const val API_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss"

// 600.0 -> "$600.00"
fun formatUsd(amount: Double): String =
    NumberFormat.getCurrencyInstance(Locale.US).format(amount)

// "2024-11-30" -> "30 Nov 2024"
fun formatApiDate(value: String?): String = reformat(value, API_DATE, "dd MMM yyyy")

// "2026-10-19T23:20:00" -> "19 Oct 2026, 23:20"
fun formatApiDateTime(value: String?): String = reformat(value, API_DATE_TIME, "dd MMM yyyy, HH:mm")

// "KHQR_BAKONG" -> "KHQR (Bakong)". The server upper-cases methods, so unknown ones get title-cased.
fun formatPaymentMethod(method: String): String = when (method.uppercase(Locale.US)) {
    "KHQR_BAKONG" -> "KHQR (Bakong)"
    "ABA_PAYWAY" -> "ABA PayWay"
    "WING" -> "Wing"
    "ACLEDAMOBILE" -> "ACLEDA Mobile"
    "CASH" -> "Cash"
    else -> method.split('_')
        .filter { it.isNotBlank() }
        .joinToString(" ") { word -> word.lowercase(Locale.US).replaceFirstChar { it.titlecase(Locale.US) } }
}

// True when an unpaid fee's due date (yyyy-MM-dd) is before today
fun isPastDue(dueDate: String?, today: Date = Date()): Boolean {
    val due = parse(dueDate, API_DATE) ?: return false
    val todayStart = parse(SimpleDateFormat(API_DATE, Locale.US).format(today), API_DATE) ?: return false
    return due.before(todayStart)
}

// Share of the total that has been paid, clamped to 0..1 for progress bars
fun paidFraction(paid: Double, total: Double): Float =
    if (total <= 0.0) 0f else (paid / total).toFloat().coerceIn(0f, 1f)

private fun reformat(value: String?, inPattern: String, outPattern: String): String {
    if (value.isNullOrBlank()) return "-"
    val date = parse(value, inPattern) ?: return value
    return SimpleDateFormat(outPattern, Locale.US).format(date)
}

private fun parse(value: String?, pattern: String): Date? {
    if (value.isNullOrBlank()) return null
    return try {
        SimpleDateFormat(pattern, Locale.US).apply { isLenient = false }.parse(value)
    } catch (e: ParseException) {
        null
    }
}
