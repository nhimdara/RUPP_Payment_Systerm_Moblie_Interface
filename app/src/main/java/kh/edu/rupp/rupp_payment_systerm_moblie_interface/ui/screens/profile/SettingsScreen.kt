package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RUPP_Payment_Systerm_Moblie_InterfaceTheme

enum class AppThemeMode(val label: String) {
    System("System"),
    Light("Light"),
    Dark("Dark")
}

enum class AppLanguage(val label: String) {
    English("English"),
    Khmer("ខ្មែរ")
}

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    appVersion: String = "1.0",
    onBack: () -> Unit = {},
    onEditProfile: () -> Unit = {},
    onChangePassword: () -> Unit = {},
    onThemeChange: (AppThemeMode) -> Unit = {},
    studentName: String? = null,
    // Called only after the student confirms in the logout dialog
    onLogout: () -> Unit = {}
) {
    var showLogoutDialog by rememberSaveable { mutableStateOf(false) }
    var paymentReminders by rememberSaveable { mutableStateOf(true) }
    var paymentReceipts by rememberSaveable { mutableStateOf(true) }
    var announcements by rememberSaveable { mutableStateOf(false) }
    var themeMode by rememberSaveable { mutableStateOf(AppThemeMode.System) }
    var language by rememberSaveable { mutableStateOf(AppLanguage.English) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ProfileTopBar(title = "Settings", onBack = onBack)

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ProfileSection(title = "Account") {
                ProfileMenuRow(
                    title = "Edit Profile",
                    subtitle = "Email, phone and address",
                    onClick = onEditProfile
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                ProfileMenuRow(
                    title = "Change Password",
                    subtitle = "Keep your account secure",
                    onClick = onChangePassword
                )
            }

            ProfileSection(title = "Notifications") {
                SettingSwitchRow(
                    title = "Payment reminders",
                    subtitle = "Before a tuition fee is due",
                    checked = paymentReminders,
                    onCheckedChange = { paymentReminders = it }
                )
                SettingSwitchRow(
                    title = "Payment receipts",
                    subtitle = "When a payment is confirmed",
                    checked = paymentReceipts,
                    onCheckedChange = { paymentReceipts = it }
                )
                SettingSwitchRow(
                    title = "University announcements",
                    checked = announcements,
                    onCheckedChange = { announcements = it }
                )
            }

            ProfileSection(title = "Appearance") {
                Text(
                    text = "Theme",
                    style = MaterialTheme.typography.bodyLarge
                )
                ChoiceChips(
                    options = AppThemeMode.entries,
                    selected = themeMode,
                    label = { it.label },
                    onSelect = {
                        themeMode = it
                        onThemeChange(it)
                    }
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Language",
                    style = MaterialTheme.typography.bodyLarge
                )
                ChoiceChips(
                    options = AppLanguage.entries,
                    selected = language,
                    label = { it.label },
                    onSelect = { language = it }
                )
            }

            ProfileSection(title = "About") {
                ProfileInfoRow("App version", appVersion)
                ProfileMenuRow(title = "Help & Support", onClick = {})
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                ProfileMenuRow(title = "Terms & Privacy Policy", onClick = {})
            }

            ProfileSection(title = "Session") {
                ProfileMenuRow(
                    title = "Log Out",
                    titleColor = MaterialTheme.colorScheme.error,
                    trailing = {},
                    onClick = { showLogoutDialog = true }
                )
            }

            Text(
                text = "RUPP Payment System",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }

    if (showLogoutDialog) {
        LogoutConfirmDialog(
            studentName = studentName,
            onDismiss = { showLogoutDialog = false },
            onConfirm = {
                showLogoutDialog = false
                onLogout()
            }
        )
    }
}

@Composable
private fun SettingSwitchRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    subtitle: String? = null
) {
    ProfileMenuRow(
        title = title,
        subtitle = subtitle,
        trailing = {
            Switch(checked = checked, onCheckedChange = onCheckedChange)
        },
        onClick = { onCheckedChange(!checked) }
    )
}

@Composable
private fun <T> ChoiceChips(
    options: List<T>,
    selected: T,
    label: (T) -> String,
    onSelect: (T) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        options.forEach { option ->
            FilterChip(
                selected = option == selected,
                onClick = { onSelect(option) },
                label = { Text(label(option)) }
            )
        }
    }
}

@Preview(showBackground = true, name = "Settings - Light")
@Composable
private fun SettingsScreenPreview() {
    RUPP_Payment_Systerm_Moblie_InterfaceTheme(darkTheme = false) {
        SettingsScreen()
    }
}

@Preview(showBackground = true, name = "Settings - Dark")
@Composable
private fun SettingsScreenDarkPreview() {
    RUPP_Payment_Systerm_Moblie_InterfaceTheme(darkTheme = true) {
        SettingsScreen()
    }
}
