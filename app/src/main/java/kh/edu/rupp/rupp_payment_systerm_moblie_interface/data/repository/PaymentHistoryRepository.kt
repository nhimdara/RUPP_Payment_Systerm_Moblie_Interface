package kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.repository

import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.PaymentCategory
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.PaymentHistoryItem
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.PaymentHistorySummary
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.PaymentMethod
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.PaymentStatus

class PaymentHistoryRepository {

    private val sampleHistory = listOf(
        PaymentHistoryItem(
            id = "TXN-20261026-001",
            title = "Semester 1 Tuition Fee",
            description = "Year 3 Bachelor of Business Management",
            category = PaymentCategory.TUITION,
            amount = 450.00,
            status = PaymentStatus.COMPLETED,
            paymentMethod = PaymentMethod.KHQR,
            transactionRef = "RUPP-KHQR-98241",
            invoiceNo = "INV-2026-10-942",
            date = "Oct 26, 2026",
            time = "09:30 AM",
            monthGroup = "October 2026",
            academicYear = "2026–2027",
            semester = "Semester 1",
            isDeduction = true
        ),
        PaymentHistoryItem(
            id = "TXN-20261024-002",
            title = "Top-up Student Balance",
            description = "Wallet balance reload via Bakong KHQR",
            category = PaymentCategory.TOP_UP,
            amount = 50.00,
            status = PaymentStatus.COMPLETED,
            paymentMethod = PaymentMethod.KHQR,
            transactionRef = "RUPP-TOPUP-45129",
            invoiceNo = "INV-2026-10-891",
            date = "Oct 24, 2026",
            time = "02:15 PM",
            monthGroup = "October 2026",
            academicYear = "2026–2027",
            semester = "Semester 1",
            isDeduction = false
        ),
        PaymentHistoryItem(
            id = "TXN-20261018-003",
            title = "Retake Exam - Business Law",
            description = "FBM Module 302 Re-examination Fee",
            category = PaymentCategory.EXAM,
            amount = 25.00,
            status = PaymentStatus.COMPLETED,
            paymentMethod = PaymentMethod.ABA_PAY,
            transactionRef = "RUPP-ABA-77192",
            invoiceNo = "INV-2026-10-754",
            date = "Oct 18, 2026",
            time = "11:20 AM",
            monthGroup = "October 2026",
            academicYear = "2026–2027",
            semester = "Semester 1",
            isDeduction = true
        ),
        PaymentHistoryItem(
            id = "TXN-20261012-004",
            title = "Campus Smart ID Card Reissue",
            description = "Replacement fee for student RFID card",
            category = PaymentCategory.SERVICES,
            amount = 10.00,
            status = PaymentStatus.PENDING,
            paymentMethod = PaymentMethod.STUDENT_BALANCE,
            transactionRef = "RUPP-BAL-33819",
            invoiceNo = "INV-2026-10-610",
            date = "Oct 12, 2026",
            time = "04:45 PM",
            monthGroup = "October 2026",
            academicYear = "2026–2027",
            semester = "Semester 1",
            isDeduction = true
        ),
        PaymentHistoryItem(
            id = "TXN-20260928-005",
            title = "Official Transcript Certificate",
            description = "Department of Academic Affairs certification",
            category = PaymentCategory.CERTIFICATE,
            amount = 15.00,
            status = PaymentStatus.COMPLETED,
            paymentMethod = PaymentMethod.ABA_PAY,
            transactionRef = "RUPP-ABA-66281",
            invoiceNo = "INV-2026-09-543",
            date = "Sep 28, 2026",
            time = "10:10 AM",
            monthGroup = "September 2026",
            academicYear = "2026–2027",
            semester = "Semester 1",
            isDeduction = true
        ),
        PaymentHistoryItem(
            id = "TXN-20260915-006",
            title = "Top-up Student Balance",
            description = "Wallet balance reload via Wing Bank",
            category = PaymentCategory.TOP_UP,
            amount = 70.00,
            status = PaymentStatus.COMPLETED,
            paymentMethod = PaymentMethod.WING_BANK,
            transactionRef = "RUPP-WING-88123",
            invoiceNo = "INV-2026-09-411",
            date = "Sep 15, 2026",
            time = "01:05 PM",
            monthGroup = "September 2026",
            academicYear = "2026–2027",
            semester = "Semester 1",
            isDeduction = false
        ),
        PaymentHistoryItem(
            id = "TXN-20260905-007",
            title = "English Proficiency Test (EPT)",
            description = "Institute of Foreign Languages (IFL) test fee",
            category = PaymentCategory.EXAM,
            amount = 30.00,
            status = PaymentStatus.FAILED,
            paymentMethod = PaymentMethod.ACLEDA,
            transactionRef = "RUPP-ACL-19482",
            invoiceNo = "INV-2026-09-290",
            date = "Sep 05, 2026",
            time = "03:50 PM",
            monthGroup = "September 2026",
            academicYear = "2026–2027",
            semester = "Semester 1",
            isDeduction = true
        ),
        PaymentHistoryItem(
            id = "TXN-20260714-008",
            title = "Semester 2 Tuition Fee (Previous Year)",
            description = "Year 2 Bachelor of Business Management",
            category = PaymentCategory.TUITION,
            amount = 450.00,
            status = PaymentStatus.COMPLETED,
            paymentMethod = PaymentMethod.KHQR,
            transactionRef = "RUPP-KHQR-33910",
            invoiceNo = "INV-2026-07-104",
            date = "Jul 14, 2026",
            time = "08:15 AM",
            monthGroup = "July 2026",
            academicYear = "2025–2026",
            semester = "Semester 2",
            isDeduction = true
        ),
        PaymentHistoryItem(
            id = "TXN-20260702-009",
            title = "Library Caution & Security Fee",
            description = "RUPP Hun Sen Library annual membership",
            category = PaymentCategory.SERVICES,
            amount = 20.00,
            status = PaymentStatus.COMPLETED,
            paymentMethod = PaymentMethod.STUDENT_BALANCE,
            transactionRef = "RUPP-BAL-99120",
            invoiceNo = "INV-2026-07-045",
            date = "Jul 02, 2026",
            time = "11:00 AM",
            monthGroup = "July 2026",
            academicYear = "2025–2026",
            semester = "Semester 2",
            isDeduction = true
        )
    )

    fun getAllPayments(): List<PaymentHistoryItem> = sampleHistory

    fun getSummary(): PaymentHistorySummary {
        val completedPayments = sampleHistory.filter { it.status == PaymentStatus.COMPLETED }
        val totalPaid = completedPayments.filter { it.isDeduction }.sumOf { it.amount }
        val totalTopUp = completedPayments.filter { !it.isDeduction }.sumOf { it.amount }
        return PaymentHistorySummary(
            totalPaid = totalPaid,
            totalTopUp = totalTopUp,
            transactionCount = sampleHistory.size,
            currentBalance = 120.00,
            academicYear = "2026–2027"
        )
    }
}
