package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.feature.pay

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material.icons.rounded.VerifiedUser
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.ErrorBanner
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.Fmt
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.GradientButton
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.IconBadge
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.Rupp
import kotlinx.coroutines.delay

/** Bottom sheet for the 6-digit payment verification code (Figma screen 2, wired to the backend). */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpSheet(flow: OtpFlow, amountLabel: String?, onVerified: (Long) -> Unit) {
    val s by flow.state.collectAsState()
    if (!s.visible) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var now by remember { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(s.challengeId) {
        while (true) { now = System.currentTimeMillis(); delay(1000) }
    }
    val secondsLeft = s.expiresAtMillis?.let { (it - now) / 1000 }
    val resendIn = ((s.resendAvailableAtMillis - now) / 1000).coerceAtLeast(0)
    val focus = remember { FocusRequester() }
    LaunchedEffect(s.challengeId) { if (s.challengeId != null) runCatching { focus.requestFocus() } }

    ModalBottomSheet(onDismissRequest = flow::dismiss, sheetState = sheetState) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .navigationBarsPadding()
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconBadge(Icons.Rounded.VerifiedUser, MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primaryContainer, size = 60.dp, iconSize = 30.dp)
            Spacer(Modifier.height(12.dp))
            Text("Security Verification", style = MaterialTheme.typography.headlineSmall)
            Text("RUPP SECURE PAYMENT GATEWAY", style = MaterialTheme.typography.labelSmall, color = Rupp.colors.gold)
            Spacer(Modifier.height(8.dp))
            Text(
                "Enter the 6-digit code sent to your registered contact to authorize" +
                    (amountLabel?.let { " your $it payment." } ?: " this payment."),
                style = MaterialTheme.typography.bodyMedium,
                color = Rupp.colors.muted,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(20.dp))

            if (s.requesting && s.challengeId == null) {
                CircularProgressIndicator(Modifier.size(32.dp))
                Spacer(Modifier.height(8.dp))
                Text("Requesting a code…", style = MaterialTheme.typography.bodySmall, color = Rupp.colors.muted)
            } else {
                OtpBoxes(s.code, flow::onCodeChange, focus, enabled = !s.locked && !s.verifying)
            }

            Spacer(Modifier.height(14.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (secondsLeft != null) {
                    Icon(Icons.Rounded.Timer, null, tint = if (secondsLeft < 60) Rupp.colors.danger else Rupp.colors.muted, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(
                        if (secondsLeft > 0) "Code expires in ${Fmt.mmss(secondsLeft)}" else "Code expired",
                        style = MaterialTheme.typography.labelMedium,
                        color = if (secondsLeft < 60) Rupp.colors.danger else Rupp.colors.muted
                    )
                }
                Spacer(Modifier.weight(1f))
                TextButton(onClick = flow::resend, enabled = resendIn == 0L && !s.requesting && !s.locked) {
                    Text(if (resendIn > 0) "Resend in ${resendIn}s" else "Resend code")
                }
            }

            s.remainingAttempts?.let {
                Text(
                    "Attempts remaining: $it of 5",
                    style = MaterialTheme.typography.labelMedium,
                    color = Rupp.colors.warning
                )
                Spacer(Modifier.height(6.dp))
            }
            s.error?.let { ErrorBanner(it, Modifier.padding(bottom = 8.dp)) }

            Spacer(Modifier.height(8.dp))
            GradientButton(
                text = "Verify & Authorize Payment",
                icon = Icons.Rounded.Lock,
                loading = s.verifying,
                enabled = s.code.length == 6 && !s.locked && (secondsLeft ?: 1) > 0,
                onClick = { flow.verify(onVerified) }
            )
            Spacer(Modifier.height(10.dp))
            Text(
                "5 attempts per code · codes last 5 minutes",
                style = MaterialTheme.typography.bodySmall,
                color = Rupp.colors.muted
            )
            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
private fun OtpBoxes(code: String, onChange: (String) -> Unit, focus: FocusRequester, enabled: Boolean) {
    BasicTextField(
        value = code,
        onValueChange = onChange,
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        modifier = Modifier.focusRequester(focus),
        decorationBox = {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                repeat(6) { i ->
                    val char = code.getOrNull(i)
                    val active = i == code.length
                    val borderColor = when {
                        char != null -> MaterialTheme.colorScheme.secondary
                        active -> MaterialTheme.colorScheme.primary
                        else -> Rupp.colors.cardBorder
                    }
                    Box(
                        Modifier
                            .size(width = 44.dp, height = 54.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .border(if (active || char != null) 2.dp else 1.dp, borderColor, RoundedCornerShape(14.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(char?.toString() ?: "", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    )
}
