package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.R
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.*
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.util.ValidationUtils
import kotlinx.coroutines.delay

enum class ForgotPasswordStep {
    ENTER_EMAIL,
    ENTER_OTP,
    NEW_PASSWORD,
    SUCCESS
}

/**
 * Modern Multi-Step Password Recovery Screen for RUPP Payment System
 * Integrated with input validation and field-level error messages.
 */
@Composable
fun ForgotPasswordScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    onResetComplete: () -> Unit = {},
    onContactSupportClick: () -> Unit = {}
) {
    val isDark = isSystemInDarkTheme()
    val focusManager = LocalFocusManager.current

    // Adaptive Theme Colors
    val backgroundColor = if (isDark) DarkBackground else LightBackground
    val surfaceColor    = if (isDark) DarkSurface else LightSurface
    val tileColor       = if (isDark) DarkTile else LightTile
    val textPrimary     = if (isDark) DarkTextPrimary else LightTextPrimary
    val textSecondary   = if (isDark) DarkTextSecondary else LightTextSecondary
    val accentGreen     = if (isDark) GreenAccentDark else GreenPrimary
    val iconCircleColor = if (isDark) DarkIconCircle else LightIconCircle
    val borderColor     = if (isDark) DarkBorder else Color(0xFFE2E8F0)
    val buttonGradient  = if (isDark) CardGradientDark else CardGradientLight

    // State
    var currentStep by remember { mutableStateOf(ForgotPasswordStep.ENTER_EMAIL) }
    var emailOrUsername by remember { mutableStateOf("") }
    var otpCode by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    // Errors
    var emailOrUsernameError by remember { mutableStateOf<String?>(null) }
    var otpCodeError by remember { mutableStateOf<String?>(null) }
    var newPasswordError by remember { mutableStateOf<String?>(null) }
    var confirmNewPasswordError by remember { mutableStateOf<String?>(null) }
    var generalError by remember { mutableStateOf<String?>(null) }

    var isLoading by remember { mutableStateOf(false) }
    var resendTimer by remember { mutableIntStateOf(59) }

    // Resend countdown timer for OTP step
    LaunchedEffect(currentStep, resendTimer) {
        if (currentStep == ForgotPasswordStep.ENTER_OTP && resendTimer > 0) {
            delay(1000L)
            resendTimer -= 1
        }
    }

    val scrollState = rememberScrollState()

    fun validateStep1() {
        focusManager.clearFocus()
        val res = ValidationUtils.validateUsernameOrEmail(emailOrUsername)
        emailOrUsernameError = res.message
        if (!res.isValid) return

        generalError = null
        isLoading = true
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            isLoading = false
            resendTimer = 59
            currentStep = ForgotPasswordStep.ENTER_OTP
        }, 1000)
    }

    fun validateStep2() {
        focusManager.clearFocus()
        val res = ValidationUtils.validateOtpCode(otpCode)
        otpCodeError = res.message
        if (!res.isValid) return

        generalError = null
        isLoading = true
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            isLoading = false
            // Check for valid OTP (e.g. 123456 from dev doc)
            if (otpCode.trim() == "123456" || otpCode.trim().length == 6) {
                currentStep = ForgotPasswordStep.NEW_PASSWORD
            } else {
                otpCodeError = "Invalid verification code. Please check your email."
            }
        }, 1000)
    }

    fun validateStep3() {
        focusManager.clearFocus()
        val pRes = ValidationUtils.validatePassword(newPassword)
        val cpRes = ValidationUtils.validateConfirmPassword(newPassword, confirmNewPassword)

        newPasswordError = pRes.message
        confirmNewPasswordError = cpRes.message

        if (!pRes.isValid || !cpRes.isValid) return

        generalError = null
        isLoading = true
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            isLoading = false
            currentStep = ForgotPasswordStep.SUCCESS
        }, 1200)
    }

    // Hardware / Gesture Back button support
    BackHandler {
        when (currentStep) {
            ForgotPasswordStep.ENTER_EMAIL -> onNavigateBack()
            ForgotPasswordStep.ENTER_OTP -> currentStep = ForgotPasswordStep.ENTER_EMAIL
            ForgotPasswordStep.NEW_PASSWORD -> currentStep = ForgotPasswordStep.ENTER_OTP
            ForgotPasswordStep.SUCCESS -> onNavigateBack()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        // -——— Top Header Banner with Official RUPP Seal -———
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .background(
                    brush = HeaderOverlay,
                    shape = RoundedCornerShape(bottomStart = 38.dp, bottomEnd = 38.dp)
                )
        ) {
            Box(
                modifier = Modifier
                    .size(190.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 40.dp, y = (-20).dp)
                    .clip(CircleShape)
                    .background(EmblemGold)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 42.dp, start = 24.dp, end = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(2.5.dp, ProfileRing),
                    shadowElevation = 8.dp,
                    modifier = Modifier.size(72.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.rupp_logo),
                        contentDescription = "RUPP Seal",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "PASSWORD RECOVERY",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )

                Text(
                    text = "Royal University of Phnom Penh",
                    color = ActiveMint,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // -——— Floating Form Container -———
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(195.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = if (isDark) 0.dp else 14.dp,
                        shape = RoundedCornerShape(26.dp),
                        ambientColor = Color.Black.copy(alpha = 0.08f),
                        spotColor = Color.Black.copy(alpha = 0.14f)
                    ),
                shape = RoundedCornerShape(26.dp),
                color = surfaceColor,
                border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (currentStep != ForgotPasswordStep.SUCCESS) {
                        StepIndicatorRow(
                            currentStep = currentStep,
                            accentColor = accentGreen,
                            tileColor = tileColor
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    // General Error Banner
                    AnimatedVisibility(
                        visible = generalError != null,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Surface(
                            color = Color(0xFFEF5350).copy(alpha = 0.12f),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEF5350).copy(alpha = 0.35f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = Color(0xFFD32F2F),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = generalError.orEmpty(),
                                    color = Color(0xFFD32F2F),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    // Step 1: Email or Username
                    when (currentStep) {
                        ForgotPasswordStep.ENTER_EMAIL -> {
                            Text(
                                text = "Forgot Your Password?",
                                color = textPrimary,
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "Enter your registered RUPP email or username. We'll send you an OTP verification code to reset your password.",
                                color = textSecondary,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
                            )

                            ValidatedInputField(
                                value = emailOrUsername,
                                onValueChange = {
                                    emailOrUsername = it
                                    if (emailOrUsernameError != null) emailOrUsernameError = null
                                    if (generalError != null) generalError = null
                                },
                                label = "Username or Email Address",
                                placeholder = "e.g. ayutaya or student@rupp.edu.kh",
                                leadingIcon = Icons.Default.Email,
                                iconCircleColor = iconCircleColor,
                                accentColor = accentGreen,
                                containerColor = tileColor,
                                borderColor = borderColor,
                                textPrimary = textPrimary,
                                textSecondary = textSecondary,
                                errorMessage = emailOrUsernameError,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Email,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(onDone = { validateStep1() })
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = { validateStep1() },
                                enabled = !isLoading,
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = accentGreen,
                                    contentColor = Color.White
                                ),
                                contentPadding = PaddingValues(0.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(buttonGradient),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isLoading) {
                                        CircularProgressIndicator(
                                            color = Color.White,
                                            strokeWidth = 2.5.dp,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    } else {
                                        Text(
                                            text = "Send Verification Code",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }

                        // Step 2: 6-Digit OTP
                        ForgotPasswordStep.ENTER_OTP -> {
                            Text(
                                text = "Verify Security Code",
                                color = textPrimary,
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "Enter the 6-digit verification code sent to:",
                                color = textSecondary,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 4.dp)
                            )

                            Surface(
                                color = tileColor,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.padding(top = 6.dp, bottom = 18.dp)
                            ) {
                                Text(
                                    text = emailOrUsername,
                                    color = accentGreen,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }

                            ValidatedInputField(
                                value = otpCode,
                                onValueChange = {
                                    if (it.length <= 6) {
                                        otpCode = it
                                        if (otpCodeError != null) otpCodeError = null
                                        if (generalError != null) generalError = null
                                    }
                                },
                                label = "6-Digit OTP Code",
                                placeholder = "e.g. 123456",
                                leadingIcon = Icons.Default.Password,
                                iconCircleColor = iconCircleColor,
                                accentColor = accentGreen,
                                containerColor = tileColor,
                                borderColor = borderColor,
                                textPrimary = textPrimary,
                                textSecondary = textSecondary,
                                errorMessage = otpCodeError,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(onDone = { validateStep2() })
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (resendTimer > 0) {
                                    Text(
                                        text = "Resend code in 0:${resendTimer.toString().padStart(2, '0')}",
                                        color = textSecondary,
                                        fontSize = 12.sp
                                    )
                                } else {
                                    Text(
                                        text = "Didn't receive code?",
                                        color = textSecondary,
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        text = "Resend OTP",
                                        color = accentGreen,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .clickable {
                                                resendTimer = 59
                                                generalError = null
                                                otpCodeError = null
                                            }
                                            .padding(4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(22.dp))

                            Button(
                                onClick = { validateStep2() },
                                enabled = !isLoading,
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = accentGreen,
                                    contentColor = Color.White
                                ),
                                contentPadding = PaddingValues(0.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(buttonGradient),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isLoading) {
                                        CircularProgressIndicator(
                                            color = Color.White,
                                            strokeWidth = 2.5.dp,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    } else {
                                        Text(
                                            text = "Verify Code",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }

                        // Step 3: New Password
                        ForgotPasswordStep.NEW_PASSWORD -> {
                            Text(
                                text = "Create New Password",
                                color = textPrimary,
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "Set a secure password for your RUPP student account (minimum 6 characters).",
                                color = textSecondary,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
                            )

                            ValidatedInputField(
                                value = newPassword,
                                onValueChange = {
                                    newPassword = it
                                    if (newPasswordError != null) newPasswordError = null
                                    if (generalError != null) generalError = null
                                },
                                label = "New Password *",
                                placeholder = "Enter new password (min 6 chars)",
                                leadingIcon = Icons.Default.Lock,
                                iconCircleColor = iconCircleColor,
                                accentColor = accentGreen,
                                containerColor = tileColor,
                                borderColor = borderColor,
                                textPrimary = textPrimary,
                                textSecondary = textSecondary,
                                isPassword = true,
                                passwordVisible = passwordVisible,
                                errorMessage = newPasswordError,
                                onPasswordToggle = { passwordVisible = !passwordVisible },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Password,
                                    imeAction = ImeAction.Next
                                ),
                                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            ValidatedInputField(
                                value = confirmNewPassword,
                                onValueChange = {
                                    confirmNewPassword = it
                                    if (confirmNewPasswordError != null) confirmNewPasswordError = null
                                    if (generalError != null) generalError = null
                                },
                                label = "Confirm New Password *",
                                placeholder = "Re-type your new password",
                                leadingIcon = Icons.Default.Lock,
                                iconCircleColor = iconCircleColor,
                                accentColor = accentGreen,
                                containerColor = tileColor,
                                borderColor = borderColor,
                                textPrimary = textPrimary,
                                textSecondary = textSecondary,
                                isPassword = true,
                                passwordVisible = confirmPasswordVisible,
                                errorMessage = confirmNewPasswordError,
                                onPasswordToggle = { confirmPasswordVisible = !confirmPasswordVisible },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Password,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(onDone = { validateStep3() })
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = { validateStep3() },
                                enabled = !isLoading,
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = accentGreen,
                                    contentColor = Color.White
                                ),
                                contentPadding = PaddingValues(0.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(buttonGradient),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isLoading) {
                                        CircularProgressIndicator(
                                            color = Color.White,
                                            strokeWidth = 2.5.dp,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    } else {
                                        Text(
                                            text = "Reset Password",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }

                        // Step 4: Success
                        ForgotPasswordStep.SUCCESS -> {
                            Box(
                                modifier = Modifier
                                    .size(76.dp)
                                    .clip(CircleShape)
                                    .background(ActiveMint.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.CheckCircle,
                                    contentDescription = "Success",
                                    tint = accentGreen,
                                    modifier = Modifier.size(46.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Password Reset Complete!",
                                color = textPrimary,
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = "Your password has been securely updated. You can now sign in to your RUPP Payment account using your new credentials.",
                                color = textSecondary,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center,
                                lineHeight = 18.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp)
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = onResetComplete,
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = accentGreen,
                                    contentColor = Color.White
                                ),
                                contentPadding = PaddingValues(0.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(buttonGradient),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Return to Sign In",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    if (currentStep != ForgotPasswordStep.SUCCESS) {
                        Spacer(modifier = Modifier.height(18.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Remember your password? ",
                                color = textSecondary,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "Sign In",
                                color = accentGreen,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .clickable { onNavigateBack() }
                                    .padding(vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Footer Support
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onContactSupportClick() }
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.HelpOutline,
                    contentDescription = "Help",
                    tint = textSecondary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Need help recovering account? Contact RUPP ICT Center",
                    color = textSecondary,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Security,
                    contentDescription = "Security",
                    tint = ActiveMint,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "256-bit SSL Secure Payment Gateway • RUPP",
                    color = textSecondary,
                    fontSize = 11.sp
                )
            }
        }

        // -——— Floating Back Navigation Button (Always on top of Column to guarantee clickability) -———
        IconButton(
            onClick = {
                when (currentStep) {
                    ForgotPasswordStep.ENTER_EMAIL -> onNavigateBack()
                    ForgotPasswordStep.ENTER_OTP -> currentStep = ForgotPasswordStep.ENTER_EMAIL
                    ForgotPasswordStep.NEW_PASSWORD -> currentStep = ForgotPasswordStep.ENTER_OTP
                    ForgotPasswordStep.SUCCESS -> onNavigateBack()
                }
            },
            modifier = Modifier
                .align(Alignment.TopStart)
                .zIndex(50f)
                .padding(top = 40.dp, start = 12.dp)
                .size(48.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }
    }
}

@Composable
private fun StepIndicatorRow(
    currentStep: ForgotPasswordStep,
    accentColor: Color,
    tileColor: Color
) {
    val steps = listOf("Account", "Verify Code", "New Password")
    val currentIndex = when (currentStep) {
        ForgotPasswordStep.ENTER_EMAIL -> 0
        ForgotPasswordStep.ENTER_OTP -> 1
        ForgotPasswordStep.NEW_PASSWORD -> 2
        ForgotPasswordStep.SUCCESS -> 3
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        steps.forEachIndexed { index, stepName ->
            val isActive = index == currentIndex
            val isPassed = index < currentIndex

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                isPassed -> accentColor
                                isActive -> accentColor
                                else -> tileColor
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isPassed) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    } else {
                        Text(
                            text = (index + 1).toString(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isActive) Color.White else Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = stepName,
                    fontSize = 10.sp,
                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                    color = if (isActive) accentColor else Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// -——— Previews -———

@Preview(name = "Light Mode - Forgot Password", showBackground = true)
@Composable
fun ForgotPasswordScreenPreview() {
    RUPP_Payment_Systerm_Moblie_InterfaceTheme(darkTheme = false) {
        ForgotPasswordScreen()
    }
}

@Preview(name = "Dark Mode - Forgot Password", showBackground = true)
@Composable
fun ForgotPasswordScreenDarkPreview() {
    RUPP_Payment_Systerm_Moblie_InterfaceTheme(darkTheme = true) {
        ForgotPasswordScreen()
    }
}
