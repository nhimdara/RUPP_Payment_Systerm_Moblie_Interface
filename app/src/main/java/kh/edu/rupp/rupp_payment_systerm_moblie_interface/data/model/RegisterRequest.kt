package kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model

/**
 * Register request payload matching backend POST /api/v1/auth/register
 * 
 * Rules from API guide:
 * - username: 3-50 chars, required
 * - email: valid email, required
 * - password: min 6 chars, required
 * - firstName: optional
 * - lastName: optional
 * - facultyId: optional
 * - academicYearId: optional
 */
data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val facultyId: Long? = null,
    val academicYearId: Long? = null
)

/**
 * Faculty option for dropdown selection
 */
data class FacultyItem(
    val id: Long,
    val name: String,
    val department: String
)

/**
 * Academic Year option for dropdown selection
 */
data class AcademicYearItem(
    val id: Long,
    val label: String
)

