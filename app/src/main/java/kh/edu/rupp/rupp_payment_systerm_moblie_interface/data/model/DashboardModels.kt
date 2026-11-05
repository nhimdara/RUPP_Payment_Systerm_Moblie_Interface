package kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Status of the student's tuition fee payment for the current term.
 */
enum class TuitionPaymentStatus {
    PAID,
    PARTIAL,
    UNPAID
}

/**
 * Summary data shown in the Student Profile Summary & Tuition Card.
 */
data class TuitionDashboardSummary(
    val totalTuitionFee: Double,
    val totalPaid: Double,
    val remainingBalance: Double,
    val paymentStatus: TuitionPaymentStatus,
    val dueDate: String,
    val academicYear: String,
    val semesterName: String,
    val invoiceNumber: String = "INV-2026-T01"
)

/**
 * Quick action shortcuts shown in the Dashboard hub.
 */
data class DashboardQuickAction(
    val id: String,
    val title: String,
    val subtitle: String,
    val badgeText: String? = null
)

/**
 * Recent payment transactions displayed on the Home Dashboard.
 */
data class RecentPaymentActivity(
    val id: String,
    val title: String,
    val receiptNumber: String,
    val amount: Double,
    val date: String,
    val paymentMethod: String,
    val status: String // CONFIRMED, PENDING, FAILED
)

/**
 * Announcements and important campus notices for students.
 */
data class CampusAnnouncement(
    val id: String,
    val title: String,
    val description: String,
    val date: String,
    val badgeTag: String,
    val isUrgent: Boolean = false
)

/**
 * Notification categories for filtering and iconography.
 */
enum class NotificationCategory(val label: String) {
    ALL("All"),
    PAYMENTS("Payments"),
    ACADEMIC("Academic"),
    ANNOUNCEMENTS("Announcements")
}

/**
 * Notification model for the Notifications screen.
 */
data class AppNotification(
    val id: String,
    val title: String,
    val message: String,
    val timeAgo: String,
    val category: NotificationCategory,
    val isRead: Boolean = false,
    val amount: Double? = null,
    val receiptNumber: String? = null
)

/**
 * Sample dataset providing authentic RUPP tuition and activity records.
 */
object SampleDashboardData {
    val tuitionSummary = TuitionDashboardSummary(
        totalTuitionFee = 600.00,
        totalPaid = 600.00,
        remainingBalance = 0.00,
        paymentStatus = TuitionPaymentStatus.PAID,
        dueDate = "30 November 2026",
        academicYear = "2026 - 2027",
        semesterName = "Semester 1",
        invoiceNumber = "RUPP-INV-2026-089"
    )

    val recentPayments = listOf(
        RecentPaymentActivity(
            id = "tx1",
            title = "Semester 1 Tuition Fee",
            receiptNumber = "RUPP-REC-2026-002",
            amount = 600.00,
            date = "Today, 10:15 AM",
            paymentMethod = "KHQR Bakong",
            status = "CONFIRMED"
        ),
        RecentPaymentActivity(
            id = "tx2",
            title = "Lab & Technology Access Fee",
            receiptNumber = "RUPP-REC-2026-001",
            amount = 45.00,
            date = "28 Oct 2026",
            paymentMethod = "ABA PayWay",
            status = "CONFIRMED"
        ),
        RecentPaymentActivity(
            id = "tx3",
            title = "Student ID Card Replacement",
            receiptNumber = "RUPP-REC-2026-000",
            amount = 5.00,
            date = "15 Sep 2026",
            paymentMethod = "Wing Bank",
            status = "CONFIRMED"
        )
    )

    val announcements = listOf(
        CampusAnnouncement(
            id = "ann1",
            title = "Academic Year 2026-2027 Semester 1 Enrollment",
            description = "Tuition payment via KHQR Bakong and ABA PayWay is open until 30 November 2026. Official receipts will be generated automatically upon bank clearance.",
            date = "Nov 02, 2026",
            badgeTag = "Important",
            isUrgent = true
        ),
        CampusAnnouncement(
            id = "ann2",
            title = "Department of Telecommunication Lab Upgrades",
            description = "Room 304 STEM Building is scheduled for fiber optic and network spectrum analyzer maintenance from Friday to Sunday.",
            date = "Oct 25, 2026",
            badgeTag = "Notice",
            isUrgent = false
        )
    )

    val initialNotifications = listOf(
        AppNotification(
            id = "notif1",
            title = "Tuition Payment Confirmed",
            message = "Your tuition payment of $600.00 via KHQR Bakong was successfully verified. Receipt #RUPP-REC-2026-002 is ready for download.",
            timeAgo = "10m ago",
            category = NotificationCategory.PAYMENTS,
            isRead = false,
            amount = 600.00,
            receiptNumber = "RUPP-REC-2026-002"
        ),
        AppNotification(
            id = "notif2",
            title = "Semester 1 Class Schedule Released",
            message = "The timetable for Year 3 Telecommunication Engineering (Shift: Morning) has been posted by the Faculty of Engineering.",
            timeAgo = "2h ago",
            category = NotificationCategory.ACADEMIC,
            isRead = false
        ),
        AppNotification(
            id = "notif3",
            title = "Bakong KHQR Instant Payment Ready",
            message = "You can now make fee payments with zero fees across all local mobile banking apps using Bakong KHQR QR scan.",
            timeAgo = "1d ago",
            category = NotificationCategory.ANNOUNCEMENTS,
            isRead = false
        ),
        AppNotification(
            id = "notif4",
            title = "Lab Fee Payment Recorded",
            message = "Payment of $45.00 for STEM Lab & Technology Access has been acknowledged and stamped by the RUPP Finance Office.",
            timeAgo = "5d ago",
            category = NotificationCategory.PAYMENTS,
            isRead = true,
            amount = 45.00,
            receiptNumber = "RUPP-REC-2026-001"
        ),
        AppNotification(
            id = "notif5",
            title = "Academic Term Registration Complete",
            message = "Your academic standing for Semester 1 (2026-2027) is Active and in good standing.",
            timeAgo = "1w ago",
            category = NotificationCategory.ACADEMIC,
            isRead = true
        )
    )
}
