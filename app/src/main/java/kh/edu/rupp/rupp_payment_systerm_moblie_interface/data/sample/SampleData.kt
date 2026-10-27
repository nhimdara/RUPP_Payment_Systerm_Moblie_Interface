package kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.sample

import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.PaymentDetailResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.PaymentResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.PaymentStatus
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.ReceiptResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.TuitionCalculationResponse

// Fake data shaped like the real API responses, for previews and UI tests
// until the repository layer is connected.
object SampleData {

    val confirmedPayment = PaymentResponse(
        id = 3,
        studentId = 2,
        studentName = "Ayutaya Tab",
        studentEmail = "ayutaya@rupp.edu.kh",
        facultyName = "Computer Science",
        academicYearLabel = "2026-2027",
        feeStructureId = 1,
        feeStructureAmount = 600.0,
        status = PaymentStatus.CONFIRMED,
        paymentDate = "2026-10-05T09:12:00",
        totalPaid = 300.0,
        remarks = "Semester 1",
        details = listOf(PaymentDetailResponse(3, "KHQR_BAKONG", "TXN-KHQR-20261005-001", 300.0)),
        receipt = ReceiptResponse(1, 3, "RUPP-REC-2026-001", "2026-10-05T10:40:00", 300.0)
    )

    val pendingPayment = PaymentResponse(
        id = 4,
        studentId = 2,
        studentName = "Ayutaya Tab",
        studentEmail = "ayutaya@rupp.edu.kh",
        facultyName = "Computer Science",
        academicYearLabel = "2026-2027",
        feeStructureId = 1,
        feeStructureAmount = 600.0,
        status = PaymentStatus.PENDING,
        paymentDate = "2026-10-19T23:20:00",
        totalPaid = 150.0,
        remarks = null,
        details = listOf(PaymentDetailResponse(4, "ABA_PAYWAY", "TXN-ABA-20261019-002", 150.0))
    )

    val partiallyPaid = TuitionCalculationResponse(
        studentId = 2,
        studentName = "Ayutaya Tab",
        studentEmail = "ayutaya@rupp.edu.kh",
        facultyName = "Computer Science",
        academicYear = "2026-2027",
        majorName = "Computer Science",
        totalTuitionFee = 600.0,
        totalPaid = 300.0,
        remainingBalance = 300.0,
        fullyPaid = false,
        paymentStatus = "PARTIAL",
        dueDate = "2026-11-30",
        paymentHistory = listOf(pendingPayment, confirmedPayment)
    )

    val unpaid = partiallyPaid.copy(
        totalPaid = 0.0,
        remainingBalance = 600.0,
        paymentStatus = "UNPAID",
        dueDate = "2026-09-30",
        paymentHistory = emptyList()
    )

    val fullyPaid = partiallyPaid.copy(
        totalPaid = 600.0,
        remainingBalance = 0.0,
        fullyPaid = true,
        paymentStatus = "PAID",
        paymentHistory = listOf(
            confirmedPayment.copy(id = 5, paymentDate = "2026-10-21T08:30:00"),
            confirmedPayment
        )
    )
}
