package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.SampleStudentProfile
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.StudentProfile
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RUPP_Payment_Systerm_Moblie_InterfaceTheme

// Validation for the editable profile fields
object ProfileValidator {
    private val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    fun emailError(email: String): String? = when {
        email.isBlank() -> "Email is required"
        !emailRegex.matches(email.trim()) -> "Enter a valid email address"
        else -> null
    }

    // Cambodian numbers: 9 or 10 digits starting with 0, spaces allowed
    fun phoneError(phone: String): String? {
        val digits = phone.filter { !it.isWhitespace() }
        return when {
            digits.isEmpty() -> "Phone number is required"
            !digits.all { it.isDigit() } -> "Only digits are allowed"
            !digits.startsWith("0") || digits.length !in 9..10 -> "Enter a valid phone number, e.g. 012 345 678"
            else -> null
        }
    }

    fun addressError(address: String): String? =
        if (address.isBlank()) "Address is required" else null
}

/**
 * Students can only change contact details. Name, ID and academic
 * information come from the university and are shown read-only.
 */
@Composable
fun EditProfileScreen(
    student: StudentProfile,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onSave: (StudentProfile) -> Unit = {}
) {
    var email by rememberSaveable { mutableStateOf(student.email) }
    var phone by rememberSaveable { mutableStateOf(student.phone) }
    var address by rememberSaveable { mutableStateOf(student.address) }
    var submitted by rememberSaveable { mutableStateOf(false) }

    val emailError = ProfileValidator.emailError(email)
    val phoneError = ProfileValidator.phoneError(phone)
    val addressError = ProfileValidator.addressError(address)
    val hasChanges = email != student.email || phone != student.phone || address != student.address

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .imePadding()
    ) {
        ProfileTopBar(title = "Edit Profile", onBack = onBack)

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfileAvatar(initials = student.initials, size = 88.dp)
                TextButton(onClick = { /* Photo upload is not part of this sprint */ }) {
                    Text("Change photo")
                }
            }

            ProfileSection(title = "Read-only") {
                ProfileInfoRow("Full Name", student.fullName)
                ProfileInfoRow("Student ID", student.studentId)
                ProfileInfoRow("Major", student.major, showDivider = false)
                Text(
                    text = "Contact the registrar's office to change these details.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            ProfileSection(title = "Contact Details") {
                ProfileTextField(
                    label = "Email",
                    value = email,
                    onValueChange = { email = it },
                    error = if (submitted) emailError else null,
                    keyboardType = KeyboardType.Email
                )
                Spacer(Modifier.height(12.dp))
                ProfileTextField(
                    label = "Phone",
                    value = phone,
                    onValueChange = { phone = it },
                    error = if (submitted) phoneError else null,
                    keyboardType = KeyboardType.Phone
                )
                Spacer(Modifier.height(12.dp))
                ProfileTextField(
                    label = "Address",
                    value = address,
                    onValueChange = { address = it },
                    error = if (submitted) addressError else null,
                    singleLine = false
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Cancel")
            }
            Spacer(Modifier.width(12.dp))
            Button(
                onClick = {
                    submitted = true
                    if (emailError == null && phoneError == null && addressError == null) {
                        onSave(
                            student.copy(
                                email = email.trim(),
                                phone = phone.trim(),
                                address = address.trim()
                            )
                        )
                    }
                },
                enabled = hasChanges,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Save Changes")
            }
        }
    }
}

@Composable
fun ProfileTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null,
    singleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: (@Composable () -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        label = { Text(label) },
        isError = error != null,
        supportingText = error?.let { { Text(it) } },
        singleLine = singleLine,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        shape = RoundedCornerShape(14.dp)
    )
}

@Preview(showBackground = true, name = "Edit Profile - Light")
@Composable
private fun EditProfileScreenPreview() {
    RUPP_Payment_Systerm_Moblie_InterfaceTheme(darkTheme = false) {
        EditProfileScreen(student = SampleStudentProfile.student)
    }
}

@Preview(showBackground = true, name = "Edit Profile - Dark")
@Composable
private fun EditProfileScreenDarkPreview() {
    RUPP_Payment_Systerm_Moblie_InterfaceTheme(darkTheme = true) {
        EditProfileScreen(student = SampleStudentProfile.student)
    }
}
