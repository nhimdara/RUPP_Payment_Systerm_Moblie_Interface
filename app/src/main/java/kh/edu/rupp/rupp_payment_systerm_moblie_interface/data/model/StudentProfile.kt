package kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model

// Student information shown on the profile screen
data class StudentProfile(
    val studentId: String,
    val fullName: String,
    val khmerName: String,
    val email: String,
    val phone: String,
    val faculty: String,
    val major: String,
    val year: Int,
    val semester: Int,
    val academicYear: String,
    val shift: String,
    val dateOfBirth: String,
    val address: String
) {
    // "Tang Chihong" -> "TC"
    val initials: String
        get() = fullName.split(" ")
            .filter { it.isNotBlank() }
            .take(2)
            .joinToString("") { it.first().uppercase() }
}

object SampleStudentProfile {
    val student = StudentProfile(
        studentId = "e20230001",
        fullName = "Tang Chihong",
        khmerName = "តាំង ជីហុង",
        email = "tang.chihong@rupp.edu.kh",
        phone = "012 345 678",
        faculty = "Faculty of Engineering",
        major = "Information Technology Engineering",
        year = 3,
        semester = 1,
        academicYear = "2026 - 2027",
        shift = "Morning",
        dateOfBirth = "12 March 2005",
        address = "Phnom Penh, Cambodia"
    )
}
