package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.screens.profile

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RUPP_Payment_Systerm_Moblie_InterfaceTheme

// Asks the student to confirm before signing out
@Composable
fun LogoutConfirmDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    studentName: String? = null
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        title = {
            Text(text = "Log out?", fontWeight = FontWeight.SemiBold)
        },
        text = {
            val who = studentName?.let { " as $it" } ?: ""
            Text(
                text = "You are signed in$who. You will need to log in again " +
                    "to view your fees and make payments.",
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Text("Log Out")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Preview(name = "Logout Dialog - Light")
@Composable
private fun LogoutConfirmDialogPreview() {
    RUPP_Payment_Systerm_Moblie_InterfaceTheme(darkTheme = false) {
        LogoutConfirmDialog(onConfirm = {}, onDismiss = {}, studentName = "Tang Chihong")
    }
}

@Preview(name = "Logout Dialog - Dark")
@Composable
private fun LogoutConfirmDialogDarkPreview() {
    RUPP_Payment_Systerm_Moblie_InterfaceTheme(darkTheme = true) {
        LogoutConfirmDialog(onConfirm = {}, onDismiss = {}, studentName = "Tang Chihong")
    }
}
