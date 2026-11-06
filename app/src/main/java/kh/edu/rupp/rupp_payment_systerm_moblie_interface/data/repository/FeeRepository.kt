package kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.repository

import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.FeeCategory
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.FeeStatus
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.SchoolFeeItem

object FeeRepository {
    private val feeList = mutableListOf(
        SchoolFeeItem(
            feeStructureId = 1,
            title = "Tuition balance",
            subtitle = "Semester 1 · Due 30 Nov",
            category = FeeCategory.TUITION,
            totalAmount = 450.0,
            dueDate = "30 Nov 2026",
            status = FeeStatus.UNPAID,
            description = "Year 4 Semester 1 Academic Tuition - Faculty of Science (18 Credits)",
            credits = 18
        ),
        SchoolFeeItem(
            feeStructureId = 2,
            title = "Registration fee",
            subtitle = "Last payment failed",
            category = FeeCategory.REGISTRATION,
            totalAmount = 50.0,
            dueDate = "Overdue",
            status = FeeStatus.FAILED,
            description = "Semester 1 Course Registration & Examination Administration Fee",
            credits = 0
        ),
        SchoolFeeItem(
            feeStructureId = 3,
            title = "Moto Parking Semester Pass",
            subtitle = "Due 10 Dec 2026",
            category = FeeCategory.MOTO_PARKING,
            totalAmount = 15.0,
            dueDate = "10 Dec 2026",
            status = FeeStatus.UNPAID,
            description = "Campus 1 & Campus 2 Motorcycle RFID Gate Access Permit",
            credits = 0
        ),
        SchoolFeeItem(
            feeStructureId = 4,
            title = "Laboratory & Library Fee",
            subtitle = "Due 20 Dec 2026",
            category = FeeCategory.LAB_FEE,
            totalAmount = 25.0,
            dueDate = "20 Dec 2026",
            status = FeeStatus.UNPAID,
            description = "Computer Science Laboratory Maintenance & Hun Sen Digital Library Access",
            credits = 0
        ),
        SchoolFeeItem(
            feeStructureId = 5,
            title = "Student Health & Safety Insurance",
            subtitle = "Paid on 15 Oct 2026",
            category = FeeCategory.OTHER,
            totalAmount = 20.0,
            dueDate = "15 Oct 2026",
            status = FeeStatus.PAID,
            description = "Annual On-Campus Medical Insurance Coverage",
            credits = 0
        ),
        SchoolFeeItem(
            feeStructureId = 6,
            title = "Student Activity & Sports Fund",
            subtitle = "Paid on 10 Oct 2026",
            category = FeeCategory.OTHER,
            totalAmount = 10.0,
            dueDate = "10 Oct 2026",
            status = FeeStatus.PAID,
            description = "RUPP University Sports Facilities and Student Council Support",
            credits = 0
        )
    )

    fun getAllFees(): List<SchoolFeeItem> = feeList.toList()

    fun getToPayFees(): List<SchoolFeeItem> =
        feeList.filter { it.status == FeeStatus.UNPAID || it.status == FeeStatus.FAILED }

    fun getTotalToPayAmount(): Double =
        getToPayFees().sumOf { it.totalAmount }
}
