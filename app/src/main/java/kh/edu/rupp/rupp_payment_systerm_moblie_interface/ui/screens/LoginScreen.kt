package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.R
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.LoginRequest
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.*
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.util.ValidationUtils

/**
 * Modern Login Screen for RUPP Payment System
 * Designed with inline field validation, error messaging, and light/dark theme support.
 */
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLoginSuccess: (request: LoginRequest) -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    onBiometricClick: () -> Unit = {},
    onContactSupportClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {}
) {
    val isDark = isSystemInDarkTheme()
    val focusManager = LocalFocusManager.current

    // Adaptive Brand Palette
    val backgroundColor   = if (isDark) DarkBackground else LightBackground
    val surfaceColor      = if (isDark) DarkSurface else LightSurface
    val tileColor         = if (isDark) DarkTile else LightTile
    val textPrimary       = if (isDark) DarkTextPrimary else LightTextPrimary
    val textSecondary     = if (isDark) DarkTextSecondary else LightTextSecondary
    val accentGreen       = if (isDark) GreenAccentDark else GreenPrimary
    val iconCircleColor   = if (isDark) DarkIconCircle else LightIconCircle
    val borderColor       = if (isDark) DarkBorder else Color(0xFFE2E8F0)
    val buttonGradient    = if (isDark) CardGradientDark else CardGradientLight

    // Form & Validation States
    var selectedPortal by remember { mutableIntStateOf(0) } // 0: Student, 1: Staff / Faculty
    var usernameOrEmail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(true) }

    // Errors
    var usernameError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var generalError by remember { mutableStateOf<String?>(null) }

    var isLoading by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    fun validateAndSubmit() {
        focusManager.clearFocus()
        val uResult = ValidationUtils.validateUsernameOrEmail(usernameOrEmail)
        val pResult = ValidationUtils.validatePassword(password)

        usernameError = uResult.message
        passwordError = pResult.message

        if (!uResult.isValid || !pResult.isValid) {
            return
        }

        generalError = null
        val request = LoginRequest(
            usernameOrEmail = usernameOrEmail.trim(),
            password = password
        )

        isLoading = true
        // Simulated API authentication call (or call to your repository)
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            isLoading = false
            // Check for valid credentials
            if (request.password == "student123" || request.password.length >= 6) {
                onLoginSuccess(request)
            } else {
                generalError = "Invalid username/email or password (HTTP 401)"
            }
        }, 1200)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        // -——— Top Branded Curved Header -———
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(270.dp)
                .background(
                    brush = HeaderOverlay,
                    shape = RoundedCornerShape(bottomStart = 38.dp, bottomEnd = 38.dp)
                )
        ) {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 45.dp, y = (-25).dp)
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
                    modifier = Modifier.size(76.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.rupp_logo),
                        contentDescription = "Royal University of Phnom Penh Logo",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "RUPP PAYMENT SYSTEM",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.4.sp
                )

                Text(
                    text = "Royal University of Phnom Penh",
                    color = ActiveMint,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(6.dp))

                Surface(
                    color = Color.Black.copy(alpha = 0.22f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(ActiveMint)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Secure Student Gateway",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // -——— Floating Main Form Body -———
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(205.dp))

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
                    LoginRoleSwitcher(
                        selectedRole = selectedPortal,
                        onRoleChange = {
                            selectedPortal = it
                            generalError = null
                            usernameError = null
                            passwordError = null
                        },
                        isDark = isDark,
                        tileColor = tileColor
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    Text(
                        text = if (selectedPortal == 0) "Student Portal Sign In" else "Faculty & Staff Sign In",
                        color = textPrimary,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = if (selectedPortal == 0)
                            "Access tuition fees, digital ID & transaction history"
                        else
                            "Access staff payroll, department & student fees verification",
                        color = textSecondary,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp, bottom = 18.dp)
                    )

                    // General Server Error Banner (401, 403, 500)
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

                    // Username or Email Input Field with Inline Error
                    ValidatedInputField(
                        value = usernameOrEmail,
                        onValueChange = {
                            usernameOrEmail = it
                            if (usernameError != null) usernameError = null
                            if (generalError != null) generalError = null
                        },
                        label = if (selectedPortal == 0) "Username or Email" else "Staff ID / University Email",
                        placeholder = if (selectedPortal == 0) "e.g. ayutaya or student@rupp.edu.kh" else "e.g. name@rupp.edu.kh",
                        leadingIcon = Icons.Default.Person,
                        iconCircleColor = iconCircleColor,
                        accentColor = accentGreen,
                        containerColor = tileColor,
                        borderColor = borderColor,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary,
                        errorMessage = usernameError,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = if (selectedPortal == 0) KeyboardType.Text else KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Password Input Field with Inline Error
                    ValidatedInputField(
                        value = password,
                        onValueChange = {
                            password = it
                            if (passwordError != null) passwordError = null
                            if (generalError != null) generalError = null
                        },
                        label = "Password",
                        placeholder = "Enter your password",
                        leadingIcon = Icons.Default.Lock,
                        iconCircleColor = iconCircleColor,
                        accentColor = accentGreen,
                        containerColor = tileColor,
                        borderColor = borderColor,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary,
                        isPassword = true,
                        passwordVisible = passwordVisible,
                        errorMessage = passwordError,
                        onPasswordToggle = { passwordVisible = !passwordVisible },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { validateAndSubmit() }
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Remember Me & Forgot Password
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { rememberMe = !rememberMe }
                        ) {
                            Checkbox(
                                checked = rememberMe,
                                onCheckedChange = { rememberMe = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = accentGreen,
                                    uncheckedColor = textSecondary,
                                    checkmarkColor = Color.White
                                ),
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Remember me",
                                fontSize = 12.sp,
                                color = textSecondary
                            )
                        }

                        Text(
                            text = "Forgot password?",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = accentGreen,
                            modifier = Modifier
                                .clickable { onForgotPasswordClick() }
                                .padding(vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    // Sign In Button
                    Button(
                        onClick = { validateAndSubmit() },
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
                                    text = "Sign In",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Biometric Option
                    OutlinedButton(
                        onClick = onBiometricClick,
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = textPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Fingerprint,
                            contentDescription = "Biometric Login",
                            tint = accentGreen,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Quick Login with Biometrics",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "First time here? ",
                            color = textSecondary,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "Activate Student Account",
                            color = accentGreen,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clickable { onRegisterClick() }
                                .padding(vertical = 4.dp)
                        )
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
                    text = "Need help? Contact RUPP ICT Center",
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
    }
}

// -——— Reusable Validated Input Field -———

@Composable
fun ValidatedInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    leadingIcon: ImageVector,
    iconCircleColor: Color,
    accentColor: Color,
    containerColor: Color,
    borderColor: Color,
    textPrimary: Color,
    textSecondary: Color,
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onPasswordToggle: () -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    val isError = errorMessage != null
    val errorColor = Color(0xFFD32F2F)

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isError) errorColor else textPrimary,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = isError,
            placeholder = {
                Text(
                    text = placeholder,
                    color = textSecondary.copy(alpha = 0.7f),
                    fontSize = 13.sp
                )
            },
            leadingIcon = {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(if (isError) errorColor.copy(alpha = 0.12f) else iconCircleColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = label,
                        tint = if (isError) errorColor else accentColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
            },
            trailingIcon = if (isPassword) {
                {
                    IconButton(onClick = onPasswordToggle) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password",
                            tint = if (isError) errorColor else textSecondary
                        )
                    }
                }
            } else null,
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            shape = RoundedCornerShape(12.dp),
            supportingText = if (isError) {
                {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Error",
                            tint = errorColor,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = errorMessage,
                            color = errorColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            } else null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = accentColor,
                unfocusedBorderColor = borderColor,
                errorBorderColor = errorColor,
                errorLeadingIconColor = errorColor,
                focusedTextColor = textPrimary,
                unfocusedTextColor = textPrimary,
                focusedContainerColor = containerColor,
                unfocusedContainerColor = containerColor,
                errorContainerColor = containerColor,
                cursorColor = accentColor
            )
        )
    }
}

@Composable
private fun LoginRoleSwitcher(
    selectedRole: Int,
    onRoleChange: (Int) -> Unit,
    isDark: Boolean,
    tileColor: Color
) {
    Surface(
        color = tileColor,
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(4.dp)) {
            listOf("Student", "Staff & Faculty").forEachIndexed { index, title ->
                val isSelected = selectedRole == index
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (isSelected) {
                                if (isDark) DarkNavSelected else LightNavSelected
                            } else Color.Transparent
                        )
                        .clickable { onRoleChange(index) }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) {
                            if (isDark) GreenAccentDark else GreenPrimary
                        } else {
                            if (isDark) DarkTextSecondary else LightNavUnselected
                        }
                    )
                }
            }
        }
    }
}

// -——— Interactive Previews -———

@Preview(name = "Light Mode - RUPP Login", showBackground = true)
@Composable
fun LoginScreenPreview() {
    RUPP_Payment_Systerm_Moblie_InterfaceTheme(darkTheme = false) {
        LoginScreen()
    }
}

@Preview(name = "Dark Mode - RUPP Login", showBackground = true)
@Composable
fun LoginScreenDarkPreview() {
    RUPP_Payment_Systerm_Moblie_InterfaceTheme(darkTheme = true) {
        LoginScreen()
    }
}
