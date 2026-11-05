package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.SampleStudentProfile
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.StudentProfile
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.ActiveMint
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.HeaderOverlay
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RUPP_Payment_Systerm_Moblie_InterfaceTheme

@Composable
fun StudentProfileScreen(
    student: StudentProfile,
    modifier: Modifier = Modifier,
    onEditProfile: () -> Unit = {},
    onOpenSettings: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        ProfileHeader(student)

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ProfileSection(title = "Academic Information") {
                ProfileInfoRow("Student ID", student.studentId)
                ProfileInfoRow("Faculty", student.faculty)
                ProfileInfoRow("Major", student.major)
                ProfileInfoRow("Year / Semester", "Year ${student.year}, Semester ${student.semester}")
                ProfileInfoRow("Academic Year", student.academicYear)
                ProfileInfoRow("Shift", student.shift, showDivider = false)
            }

            ProfileSection(title = "Personal Information") {
                ProfileInfoRow("Khmer Name", student.khmerName)
                ProfileInfoRow("Date of Birth", student.dateOfBirth)
                ProfileInfoRow("Email", student.email)
                ProfileInfoRow("Phone", student.phone)
                ProfileInfoRow("Address", student.address, showDivider = false)
            }

            ProfileSection(title = "Account") {
                ProfileMenuRow(
                    title = "Edit Profile",
                    subtitle = "Update your contact details",
                    onClick = onEditProfile
                )
                ProfileMenuRow(
                    title = "Settings",
                    subtitle = "Password, notifications, theme",
                    onClick = onOpenSettings
                )
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun ProfileHeader(student: StudentProfile) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
            .background(HeaderOverlay)
            .statusBarsPadding()
            .padding(vertical = 28.dp, horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            ProfileAvatar(initials = student.initials)
            Spacer(Modifier.height(12.dp))
            Text(
                text = student.fullName,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = student.khmerName,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.85f)
            )
            Spacer(Modifier.height(8.dp))
            Surface(
                shape = RoundedCornerShape(50),
                color = Color.Black.copy(alpha = 0.25f)
            ) {
                Text(
                    text = "● Active  ·  ${student.studentId}",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = ActiveMint
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Profile - Light")
@Composable
private fun StudentProfileScreenPreview() {
    RUPP_Payment_Systerm_Moblie_InterfaceTheme(darkTheme = false) {
        StudentProfileScreen(student = SampleStudentProfile.student)
    }
}

@Preview(showBackground = true, name = "Profile - Dark")
@Composable
private fun StudentProfileScreenDarkPreview() {
    RUPP_Payment_Systerm_Moblie_InterfaceTheme(darkTheme = true) {
        StudentProfileScreen(student = SampleStudentProfile.student)
    }
}
