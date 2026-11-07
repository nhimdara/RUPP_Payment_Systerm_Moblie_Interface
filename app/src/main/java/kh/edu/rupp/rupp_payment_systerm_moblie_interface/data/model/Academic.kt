package kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model

// Field names match the REST API JSON (API doc sections 5, 6, 8 and 10)

data class FacultyResponse(
    val id: Long,
    val facultyName: String,
    val department: String?
)

data class AcademicYearResponse(
    val id: Long,
    val yearLabel: String, // e.g. "2024-2025"
    val startDate: String,
    val endDate: String
)

data class SemesterResponse(
    val id: Long,
    val semesterName: String,
    val semesterNumber: Int,
    val academicYear: AcademicYearResponse,
    val startDate: String?,
    val endDate: String?,
    val isActive: Boolean
)

// The id is the feeStructureId sent when creating a payment
data class TuitionFeeResponse(
    val id: Long,
    val faculty: FacultyResponse,
    val academicYear: AcademicYearResponse,
    val totalAmount: Double,
    val dueDate: String
)
