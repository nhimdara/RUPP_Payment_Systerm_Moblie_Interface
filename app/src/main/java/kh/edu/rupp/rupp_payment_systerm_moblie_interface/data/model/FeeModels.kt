package kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model

enum class FeeStatus(val label: String) {
    UNPAID("Unpaid"),
    PAID("Paid"),
    FAILED("Retry"),
    PENDING("Pending")
}

enum class FeeCategory {
    TUITION,
    REGISTRATION,
    MOTO_PARKING,
    LAB_FEE,
    LIBRARY,
    OTHER
}

data class SchoolFeeItem(
    val feeStructureId: Int,
    val facultyId: Int = 1,
    val academicYearId: Int = 1,
    val semesterId: Int = 1,
    val title: String,
    val subtitle: String,
    val category: FeeCategory,
    val totalAmount: Double,
    val dueDate: String,
    val status: FeeStatus,
    val description: String = "",
    val credits: Int = 0
) {
    val amountInKhr: Long
        get() = (totalAmount * 4100).toLong()
}
