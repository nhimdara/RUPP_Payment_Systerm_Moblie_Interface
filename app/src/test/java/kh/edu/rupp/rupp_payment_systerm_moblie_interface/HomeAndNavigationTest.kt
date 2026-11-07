package kh.edu.rupp.rupp_payment_systerm_moblie_interface

import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.NotificationCategory
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.SampleDashboardData
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.SampleStudentProfile
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.StudentProfile
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.TuitionPaymentStatus
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.RuppNavTab
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class HomeAndNavigationTest {

    @Test
    fun studentProfile_initialsCalculation_isCorrect() {
        val student = SampleStudentProfile.leangkhai
        assertEquals("TL", student.initials)

        val dara = StudentProfile(
            studentId = "e20230001",
            fullName = "Nhim Dara",
            khmerName = "ញឹម ដារ៉ា",
            email = "dara@rupp.edu.kh",
            phone = "012 345 678",
            faculty = "Computer Science",
            major = "Computer Science",
            year = 3,
            semester = 1,
            academicYear = "2026-2027",
            shift = "Morning"
        )
        assertEquals("ND", dara.initials)
    }

    @Test
    fun tuitionDashboardSummary_sampleData_isFullyPaid() {
        val summary = SampleDashboardData.tuitionSummary
        assertEquals(0.00, summary.remainingBalance, 0.001)
        assertEquals(TuitionPaymentStatus.PAID, summary.paymentStatus)
        assertEquals(summary.totalTuitionFee, summary.totalPaid, 0.001)
    }

    @Test
    fun notifications_filteringAndUnreadCount_isCorrect() {
        val notifications = SampleDashboardData.initialNotifications

        // Check total sample items
        assertEquals(5, notifications.size)

        // Check unread count
        val unread = notifications.filter { !it.isRead }
        assertEquals(3, unread.size)

        // Check category filtering
        val paymentNotifs = notifications.filter { it.category == NotificationCategory.PAYMENTS }
        assertEquals(2, paymentNotifs.size)

        val academicNotifs = notifications.filter { it.category == NotificationCategory.ACADEMIC }
        assertEquals(2, academicNotifs.size)

        val announcementNotifs = notifications.filter { it.category == NotificationCategory.ANNOUNCEMENTS }
        assertEquals(1, announcementNotifs.size)
    }

    @Test
    fun ruppNavTab_hasFourDistinctTabs() {
        val tabs = RuppNavTab.values()
        assertEquals(4, tabs.size)
        assertEquals("Home", tabs[0].label)
        assertEquals("Tuition", tabs[1].label)
        assertEquals("History", tabs[2].label)
        assertEquals("Profile", tabs[3].label)
    }
}
