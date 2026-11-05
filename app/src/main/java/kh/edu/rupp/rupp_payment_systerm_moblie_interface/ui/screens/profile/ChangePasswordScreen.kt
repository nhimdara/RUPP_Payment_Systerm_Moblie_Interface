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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.GreenPrimary
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RUPP_Payment_Systerm_Moblie_InterfaceTheme

enum class PasswordStrength(val label: String, val progress: Float, val color: Color) {
    Weak("Weak", 0.33f, Color(0xFFD32F2F)),
    Medium("Medium", 0.66f, Color(0xFFF9A825)),
    Strong("Strong", 1f, GreenPrimary)
}

object PasswordRules {
    const val MIN_LENGTH = 8

    // Each rule the new password has to pass, with the text shown in the checklist
    val requirements: List<Pair<String, (String) -> Boolean>> = listOf(
        "At least $MIN_LENGTH characters" to { p -> p.length >= MIN_LENGTH },
        "An uppercase letter" to { p -> p.any { it.isUpperCase() } },
        "A lowercase letter" to { p -> p.any { it.isLowerCase() } },
        "A number" to { p -> p.any { it.isDigit() } }
    )

    fun meetsAll(password: String): Boolean = requirements.all { (_, check) -> check(password) }

    fun strength(password: String): PasswordStrength {
        val passed = requirements.count { (_, check) -> check(password) } +
            (if (password.any { !it.isLetterOrDigit() }) 1 else 0)
        return when {
            passed >= 5 || (passed == 4 && password.length >= 12) -> PasswordStrength.Strong
            passed >= 3 -> PasswordStrength.Medium
            else -> PasswordStrength.Weak
        }
    }

    fun currentError(current: String): String? =
        if (current.isEmpty()) "Enter your current password" else null

    fun newError(current: String, new: String): String? = when {
        new.isEmpty() -> "Enter a new password"
        !meetsAll(new) -> "Password does not meet all requirements"
        new == current -> "New password must be different from the current one"
        else -> null
    }

    fun confirmError(new: String, confirm: String): String? = when {
        confirm.isEmpty() -> "Confirm your new password"
        confirm != new -> "Passwords do not match"
        else -> null
    }
}

@Composable
fun ChangePasswordScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onForgotPassword: () -> Unit = {},
    onSubmit: (currentPassword: String, newPassword: String) -> Unit = { _, _ -> }
) {
    var current by rememberSaveable { mutableStateOf("") }
    var new by rememberSaveable { mutableStateOf("") }
    var confirm by rememberSaveable { mutableStateOf("") }
    var showPasswords by rememberSaveable { mutableStateOf(false) }
    var submitted by rememberSaveable { mutableStateOf(false) }

    val currentError = PasswordRules.currentError(current)
    val newError = PasswordRules.newError(current, new)
    val confirmError = PasswordRules.confirmError(new, confirm)
    val transformation = if (showPasswords) VisualTransformation.None else PasswordVisualTransformation()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .imePadding()
    ) {
        ProfileTopBar(title = "Change Password", onBack = onBack)

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Your new password must be different from the one you use now.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            ProfileSection(title = "Password") {
                ProfileTextField(
                    label = "Current password",
                    value = current,
                    onValueChange = { current = it },
                    error = if (submitted) currentError else null,
                    keyboardType = KeyboardType.Password,
                    visualTransformation = transformation
                )
                TextButton(
                    onClick = onForgotPassword,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Forgot password?")
                }
                ProfileTextField(
                    label = "New password",
                    value = new,
                    onValueChange = { new = it },
                    error = if (submitted) newError else null,
                    keyboardType = KeyboardType.Password,
                    visualTransformation = transformation
                )
                if (new.isNotEmpty()) {
                    Spacer(Modifier.height(8.dp))
                    StrengthMeter(PasswordRules.strength(new))
                }
                Spacer(Modifier.height(12.dp))
                ProfileTextField(
                    label = "Confirm new password",
                    value = confirm,
                    onValueChange = { confirm = it },
                    error = if (submitted) confirmError else null,
                    keyboardType = KeyboardType.Password,
                    visualTransformation = transformation
                )
                TextButton(onClick = { showPasswords = !showPasswords }) {
                    Text(if (showPasswords) "Hide passwords" else "Show passwords")
                }
            }

            ProfileSection(title = "Requirements") {
                PasswordRules.requirements.forEach { (text, check) ->
                    RequirementRow(text = text, met = check(new))
                }
            }
        }

        Button(
            onClick = {
                submitted = true
                if (currentError == null && newError == null && confirmError == null) {
                    onSubmit(current, new)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(52.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("Update Password")
        }
    }
}

@Composable
private fun StrengthMeter(strength: PasswordStrength) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        LinearProgressIndicator(
            progress = { strength.progress },
            modifier = Modifier.weight(1f),
            color = strength.color,
            trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = strength.label,
            style = MaterialTheme.typography.labelMedium,
            color = strength.color
        )
    }
}

@Composable
private fun RequirementRow(text: String, met: Boolean) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (met) "✓" else "○",
            color = if (met) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = if (met) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true, name = "Change Password - Light")
@Composable
private fun ChangePasswordScreenPreview() {
    RUPP_Payment_Systerm_Moblie_InterfaceTheme(darkTheme = false) {
        ChangePasswordScreen()
    }
}

@Preview(showBackground = true, name = "Change Password - Dark")
@Composable
private fun ChangePasswordScreenDarkPreview() {
    RUPP_Payment_Systerm_Moblie_InterfaceTheme(darkTheme = true) {
        ChangePasswordScreen()
    }
}
