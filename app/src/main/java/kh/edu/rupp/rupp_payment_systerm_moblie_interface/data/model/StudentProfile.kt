package kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model

/**
 * Student profile information shown on Home Dashboard and Profile screens.
 */
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
    val dateOfBirth: String = "12 March 2005",
    val address: String = "Phnom Penh, Cambodia",
    val userId: String = "#101",
    val facultyCode: String = "FBM",
    val department: String = "Business & Information Systems",
    val motoPlate: String = "1JZ-9988",
    val balance: Double = 145.00
) {
    val initials: String
        get() = fullName.split(" ")
            .filter { it.isNotBlank() }
            .take(2)
            .joinToString("") { it.first().uppercase() }
}

object SampleStudentProfile {
    val sokha = StudentProfile(
        studentId = "20260142",
        fullName = "Sokha Chan",
        khmerName = "ចាន់ សុខា",
        email = "sokha.chan@rupp.edu.kh",
        phone = "012 345 678",
        faculty = "Faculty of Business Management",
        facultyCode = "FBM",
        department = "Business & Information Systems",
        major = "Business & Information Systems",
        year = 3,
        semester = 1,
        academicYear = "2026–2027",
        shift = "Morning",
        dateOfBirth = "15 April 2005",
        address = "Russian Federation Blvd, Phnom Penh",
        userId = "#101",
        motoPlate = "1JZ-9988",
        balance = 145.00
    )

    val chihong = StudentProfile(
        studentId = "e20230001",
        fullName = "Tang Chihong",
        khmerName = "តាំង ជីហុង",
        email = "tang.chihong@rupp.edu.kh",
        phone = "012 345 678",
        faculty = "Faculty of Engineering",
        facultyCode = "FE",
        department = "Information Technology",
        major = "Information Technology Engineering",
        year = 3,
        semester = 1,
        academicYear = "2026 - 2027",
        shift = "Morning",
        dateOfBirth = "12 March 2005",
        address = "Phnom Penh, Cambodia"
    )

    val leangkhai = StudentProfile(
        studentId = "e20230002",
        fullName = "Taing Leangkhai",
        khmerName = "តាំង លាងខាយ",
        email = "leangkhai@rupp.edu.kh",
        phone = "012 889 900",
        faculty = "Faculty of Engineering",
        facultyCode = "FE",
        department = "Telecommunication",
        major = "Telecommunication Engineering",
        year = 3,
        semester = 1,
        academicYear = "2026-2027",
        shift = "Morning",
        dateOfBirth = "07 July 2004",
        address = "Russian Federation Blvd, Phnom Penh"
    )

    val student = sokha
}
