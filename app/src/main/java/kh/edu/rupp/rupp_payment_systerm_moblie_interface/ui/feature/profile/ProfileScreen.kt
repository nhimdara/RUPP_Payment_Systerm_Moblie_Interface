package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.feature.profile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.Dns
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.Badge
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.AvatarInitials
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.ErrorBanner
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.ErrorState
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.GradientHeader
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.HeaderBar
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.IconBadge
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.InfoRow
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.Rupp
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.RuppCard
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.RuppPalette
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.SectionTitle
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.Skeleton
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.SoftButton
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.CAMPUS_PHOTO_CREDIT
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.StatusBarScrim

@Composable
fun ProfileScreen(vm: ProfileViewModel, onLoggedOut: () -> Unit) {
    val s by vm.state.collectAsState()
    val dark by vm.darkMode.collectAsState()
    val context = LocalContext.current
    var confirmLogout by remember { mutableStateOf(false) }

    LaunchedEffect(s.toast) {
        s.toast?.let { Toast.makeText(context, it, Toast.LENGTH_SHORT).show(); vm.clearToast() }
    }

    val scroll = rememberScrollState()
    Box(Modifier.fillMaxSize()) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
    ) {
        GradientHeader {
            Spacer(Modifier.statusBarsPadding())
            HeaderBar(title = "My Profile", subtitle = "STUDENT ACCOUNT")
            Spacer(Modifier.height(18.dp))
            val p = s.profile
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (p != null) AvatarInitials(p.initials, size = 64.dp) else Skeleton(Modifier.size(64.dp), radius = 32.dp)
                Spacer(Modifier.width(14.dp))
                Column(Modifier.weight(1f)) {
                    Text(p?.fullName ?: "…", color = Color.White, style = MaterialTheme.typography.headlineSmall)
                    Text(
                        p?.user?.email ?: "",
                        color = Color.White.copy(alpha = 0.75f),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        (p?.user?.role ?: "ROLE_STUDENT").removePrefix("ROLE_").replace('_', ' '),
                        color = RuppPalette.Forest900,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.clip(RoundedCornerShape(50)).background(RuppPalette.Gold).padding(horizontal = 10.dp, vertical = 3.dp)
                    )
                }
            }
        }

        Column(
            Modifier.padding(16.dp).navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            val p = s.profile
            when {
                p != null -> {
                    RuppCard {
                        SectionTitle("Account")
                        Spacer(Modifier.height(6.dp))
                        InfoRow("Username", "@${p.user?.username ?: "—"}")
                        InfoRow("Email", p.user?.email ?: "—")
                        InfoRow("Student record", "#${p.id}")
                    }
                    RuppCard {
                        SectionTitle("Academic Information", action = "Edit", onAction = vm::openEditor)
                        Spacer(Modifier.height(6.dp))
                        InfoRow("Faculty", p.faculty?.facultyName ?: "Not set")
                        p.faculty?.department?.let { InfoRow("Department", it) }
                        InfoRow("Major", p.major?.majorName ?: "Not set")
                        p.major?.majorCode?.let { InfoRow("Major code", it) }
                        InfoRow("Academic year", p.academicYear?.yearLabel ?: "Not set")
                    }
                }
                s.loading -> Skeleton(Modifier.fillMaxWidth().height(240.dp), radius = 20.dp)
                else -> ErrorState(s.error ?: "Unknown error", onRetry = vm::load)
            }

            RuppCard {
                SectionTitle("Preferences")
                Spacer(Modifier.height(4.dp))
                SettingRow(Icons.Rounded.DarkMode, "Dark mode", "Navy night theme") {
                    Switch(
                        checked = dark,
                        onCheckedChange = vm::setDarkMode,
                        colors = SwitchDefaults.colors(checkedTrackColor = MaterialTheme.colorScheme.secondary)
                    )
                }
                SettingRow(Icons.Rounded.Dns, "Server", vm.serverUrl) {}
            }

            SoftButton("Sign out", onClick = { confirmLogout = true }, icon = Icons.AutoMirrored.Rounded.Logout)
            Text(
                CAMPUS_PHOTO_CREDIT,
                style = MaterialTheme.typography.labelSmall,
                color = Rupp.colors.muted,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                "RUPP Pay · Royal University of Phnom Penh",
                style = MaterialTheme.typography.bodySmall,
                color = Rupp.colors.muted,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(Modifier.height(8.dp))
        }
    }
    StatusBarScrim(visible = scroll.value > 40)
    }

    if (confirmLogout) {
        AlertDialog(
            onDismissRequest = { confirmLogout = false },
            title = { Text("Sign out?") },
            text = { Text("You'll need your username and password to sign in again.") },
            confirmButton = {
                TextButton(onClick = { confirmLogout = false; vm.logout(); onLoggedOut() }) {
                    Text("Sign out", color = Rupp.colors.danger)
                }
            },
            dismissButton = { TextButton(onClick = { confirmLogout = false }) { Text("Cancel") } }
        )
    }

    if (s.editor.open) AcademicEditorDialog(vm, s.editor)
}

@Composable
private fun SettingRow(icon: ImageVector, title: String, subtitle: String, trailing: @Composable () -> Unit) {
    Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        IconBadge(icon, MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primaryContainer, size = 38.dp, iconSize = 20.dp)
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleSmall)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Rupp.colors.muted)
        }
        trailing()
    }
}

@Composable
private fun AcademicEditorDialog(vm: ProfileViewModel, e: AcademicEditorState) {
    AlertDialog(
        onDismissRequest = vm::closeEditor,
        icon = { Icon(Icons.Rounded.School, null) },
        title = { Text("Academic information") },
        text = {
            if (e.loading) {
                CircularProgressIndicator()
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Picker(
                        label = "Faculty",
                        options = e.faculties.map { it.id to it.facultyName },
                        selected = e.facultyId,
                        onSelect = { vm.selectFaculty(it) }
                    )
                    Picker(
                        label = "Academic year",
                        options = e.years.map { it.id to it.yearLabel },
                        selected = e.yearId,
                        onSelect = { vm.selectYear(it) }
                    )
                    Picker(
                        label = "Major (optional)",
                        options = e.majors.map { it.id to (it.majorName + (it.majorCode?.let { c -> " · $c" } ?: "")) },
                        selected = e.majorId,
                        onSelect = { vm.selectMajor(it) }
                    )
                    e.error?.let { ErrorBanner(it) }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { vm.saveEditor {} }, enabled = !e.loading && !e.saving) {
                Text(if (e.saving) "Saving…" else "Save")
            }
        },
        dismissButton = { TextButton(onClick = vm::closeEditor) { Text("Cancel") } }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Picker(label: String, options: List<Pair<Long, String>>, selected: Long?, onSelect: (Long) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(
            value = options.firstOrNull { it.first == selected }?.second ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable)
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            if (options.isEmpty()) {
                DropdownMenuItem(text = { Text("Nothing available") }, onClick = { expanded = false }, enabled = false)
            }
            options.forEach { (id, name) ->
                DropdownMenuItem(text = { Text(name) }, onClick = { onSelect(id); expanded = false })
            }
        }
    }
}
