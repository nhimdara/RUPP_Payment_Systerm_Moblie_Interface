package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.zIndex
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.R
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.AcademicYearItem
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.FacultyItem
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.RegisterRequest
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.*
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.util.ValidationUtils

/**
 * Modern Student Registration Screen for RUPP Payment System
 * Includes field-level validation and backend HTTP 400 error mapping.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    onRegisterSuccess: (request: RegisterRequest) -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
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

    // Form Field States
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    // Field-level error messages
    var firstNameError by remember { mutableStateOf<String?>(null) }
    var lastNameError by remember { mutableStateOf<String?>(null) }
    var usernameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
    var generalError by remember { mutableStateOf<String?>(null) }

    // Dropdown options
    val faculties = remember {
        listOf(
            FacultyItem(1L, "Computer Science", "Faculty of Science"),
            FacultyItem(2L, "Information Tech Engineering", "Faculty of Engineering"),
            FacultyItem(3L, "Telecomm & Electronics", "Faculty of Engineering"),
            FacultyItem(4L, "Development Studies", "Faculty of Development"),
            FacultyItem(5L, "Institute of Foreign Languages", "IFL")
        )
    }
    val academicYears = remember {
        listOf(
            AcademicYearItem(2L, "2024-2025 (Current)"),
            AcademicYearItem(3L, "2025-2026 (Upcoming)"),
            AcademicYearItem(1L, "2023-2024 (Past)")
        )
    }

    var selectedFaculty by remember { mutableStateOf<FacultyItem?>(faculties[0]) }
    var facultyDropdownExpanded by remember { mutableStateOf(false) }

    var selectedYear by remember { mutableStateOf<AcademicYearItem?>(academicYears[0]) }
    var yearDropdownExpanded by remember { mutableStateOf(false) }

    var agreeTerms by remember { mutableStateOf(true) }
    var isLoading by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    fun validateAndSubmit() {
        focusManager.clearFocus()

        val fnRes = ValidationUtils.validateName(firstName, "First name")
        val lnRes = ValidationUtils.validateName(lastName, "Last name")
        val uRes  = ValidationUtils.validateUsername(username)
        val eRes  = ValidationUtils.validateEmail(email)
        val pRes  = ValidationUtils.validatePassword(password)
        val cpRes = ValidationUtils.validateConfirmPassword(password, confirmPassword)

        firstNameError = fnRes.message
        lastNameError = lnRes.message
        usernameError = uRes.message
        emailError = eRes.message
        passwordError = pRes.message
        confirmPasswordError = cpRes.message

        if (!agreeTerms) {
            generalError = "Please agree to the RUPP Payment System terms of service"
            return
        }

        if (!fnRes.isValid || !lnRes.isValid || !uRes.isValid || !eRes.isValid || !pRes.isValid || !cpRes.isValid) {
            generalError = "Please correct the highlighted errors before submitting"
            return
        }

        generalError = null
        val request = RegisterRequest(
            username = username.trim(),
            email = email.trim(),
            password = password,
            firstName = firstName.trim().ifBlank { null },
            lastName = lastName.trim().ifBlank { null },
            facultyId = selectedFaculty?.id,
            academicYearId = selectedYear?.id
        )

        isLoading = true
        // Simulated API call (or call to repository POST /api/v1/auth/register)
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            isLoading = false
            // Simulate duplicate check (409) or success
            if (request.username == "admin" || request.username == "test_duplicate") {
                usernameError = "Username is already taken (HTTP 409)"
                generalError = "Registration failed: Duplicate username"
            } else {
                onRegisterSuccess(request)
            }
        }, 1200)
    }

    // Hardware / Gesture Back button support
    BackHandler {
        onNavigateToLogin()
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
                    text = "STUDENT REGISTRATION",
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

        // -——— Main Form Body -———
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
                    Text(
                        text = "Create Student Account",
                        color = textPrimary,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Register to manage tuition payments & digital student services",
                        color = textSecondary,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp, bottom = 18.dp)
                    )

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

                    // First Name & Last Name (Row with individual validation)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ValidatedInputField(
                            value = firstName,
                            onValueChange = {
                                firstName = it
                                if (firstNameError != null) firstNameError = null
                                if (generalError != null) generalError = null
                            },
                            label = "First Name",
                            placeholder = "e.g. Ayutaya",
                            leadingIcon = Icons.Default.Person,
                            iconCircleColor = iconCircleColor,
                            accentColor = accentGreen,
                            containerColor = tileColor,
                            borderColor = borderColor,
                            textPrimary = textPrimary,
                            textSecondary = textSecondary,
                            errorMessage = firstNameError,
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Right) })
                        )

                        ValidatedInputField(
                            value = lastName,
                            onValueChange = {
                                lastName = it
                                if (lastNameError != null) lastNameError = null
                                if (generalError != null) generalError = null
                            },
                            label = "Last Name",
                            placeholder = "e.g. Tab",
                            leadingIcon = Icons.Default.Person,
                            iconCircleColor = iconCircleColor,
                            accentColor = accentGreen,
                            containerColor = tileColor,
                            borderColor = borderColor,
                            textPrimary = textPrimary,
                            textSecondary = textSecondary,
                            errorMessage = lastNameError,
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Username (Required, 3-50 chars)
                    ValidatedInputField(
                        value = username,
                        onValueChange = {
                            username = it
                            if (usernameError != null) usernameError = null
                            if (generalError != null) generalError = null
                        },
                        label = "Username * (3-50 characters)",
                        placeholder = "e.g. ayutaya",
                        leadingIcon = Icons.Default.Badge,
                        iconCircleColor = iconCircleColor,
                        accentColor = accentGreen,
                        containerColor = tileColor,
                        borderColor = borderColor,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary,
                        errorMessage = usernameError,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Email (Required, valid email)
                    ValidatedInputField(
                        value = email,
                        onValueChange = {
                            email = it
                            if (emailError != null) emailError = null
                            if (generalError != null) generalError = null
                        },
                        label = "Email Address *",
                        placeholder = "e.g. ayutaya@rupp.edu.kh",
                        leadingIcon = Icons.Default.Email,
                        iconCircleColor = iconCircleColor,
                        accentColor = accentGreen,
                        containerColor = tileColor,
                        borderColor = borderColor,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary,
                        errorMessage = emailError,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Password (Required, min 6 chars)
                    ValidatedInputField(
                        value = password,
                        onValueChange = {
                            password = it
                            if (passwordError != null) passwordError = null
                            if (generalError != null) generalError = null
                        },
                        label = "Password * (min 6 characters)",
                        placeholder = "Create a strong password",
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
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Confirm Password
                    ValidatedInputField(
                        value = confirmPassword,
                        onValueChange = {
                            confirmPassword = it
                            if (confirmPasswordError != null) confirmPasswordError = null
                            if (generalError != null) generalError = null
                        },
                        label = "Confirm Password *",
                        placeholder = "Re-enter your password",
                        leadingIcon = Icons.Default.Lock,
                        iconCircleColor = iconCircleColor,
                        accentColor = accentGreen,
                        containerColor = tileColor,
                        borderColor = borderColor,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary,
                        isPassword = true,
                        passwordVisible = confirmPasswordVisible,
                        errorMessage = confirmPasswordError,
                        onPasswordToggle = { confirmPasswordVisible = !confirmPasswordVisible },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = { validateAndSubmit() })
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Faculty Dropdown Selection
                    Text(
                        text = "Faculty / Department",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = textPrimary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp)
                    )
                    ExposedDropdownMenuBox(
                        expanded = facultyDropdownExpanded,
                        onExpandedChange = { facultyDropdownExpanded = !facultyDropdownExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = selectedFaculty?.name ?: "Select Faculty",
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = facultyDropdownExpanded) },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = accentGreen,
                                unfocusedBorderColor = borderColor,
                                focusedTextColor = textPrimary,
                                unfocusedTextColor = textPrimary,
                                focusedContainerColor = tileColor,
                                unfocusedContainerColor = tileColor
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = facultyDropdownExpanded,
                            onDismissRequest = { facultyDropdownExpanded = false },
                            modifier = Modifier.background(surfaceColor)
                        ) {
                            faculties.forEach { faculty ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(text = faculty.name, fontWeight = FontWeight.Medium, color = textPrimary)
                                            Text(text = faculty.department, fontSize = 11.sp, color = textSecondary)
                                        }
                                    },
                                    onClick = {
                                        selectedFaculty = faculty
                                        facultyDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Academic Year Dropdown Selection
                    Text(
                        text = "Academic Year",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = textPrimary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp)
                    )
                    ExposedDropdownMenuBox(
                        expanded = yearDropdownExpanded,
                        onExpandedChange = { yearDropdownExpanded = !yearDropdownExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = selectedYear?.label ?: "Select Academic Year",
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = yearDropdownExpanded) },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = accentGreen,
                                unfocusedBorderColor = borderColor,
                                focusedTextColor = textPrimary,
                                unfocusedTextColor = textPrimary,
                                focusedContainerColor = tileColor,
                                unfocusedContainerColor = tileColor
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = yearDropdownExpanded,
                            onDismissRequest = { yearDropdownExpanded = false },
                            modifier = Modifier.background(surfaceColor)
                        ) {
                            academicYears.forEach { year ->
                                DropdownMenuItem(
                                    text = { Text(text = year.label, color = textPrimary) },
                                    onClick = {
                                        selectedYear = year
                                        yearDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Terms and Conditions agreement
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                agreeTerms = !agreeTerms
                                if (agreeTerms && generalError?.contains("terms") == true) generalError = null
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = agreeTerms,
                            onCheckedChange = {
                                agreeTerms = it
                                if (it && generalError?.contains("terms") == true) generalError = null
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = accentGreen,
                                uncheckedColor = textSecondary,
                                checkmarkColor = Color.White
                            ),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "I agree to the RUPP Payment System terms of service and academic integrity policy",
                            fontSize = 11.sp,
                            color = textSecondary,
                            lineHeight = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Submit Registration Button
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
                                    text = "Create Student Account",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Already have an account link
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Already have an account? ",
                            color = textSecondary,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Sign In",
                            color = accentGreen,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clickable { onNavigateToLogin() }
                                .padding(vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Footer
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
                    text = "Need help registering? Contact RUPP ICT Center",
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
            onClick = onNavigateToLogin,
            modifier = Modifier
                .align(Alignment.TopStart)
                .zIndex(50f)
                .padding(top = 40.dp, start = 12.dp)
                .size(48.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                contentDescription = "Back to Login",
                tint = Color.White
            )
        }
    }
}

// -——— Compose Previews -———

@Preview(name = "Light Mode - Register", showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RUPP_Payment_Systerm_Moblie_InterfaceTheme(darkTheme = false) {
        RegisterScreen()
    }
}

@Preview(name = "Dark Mode - Register", showBackground = true)
@Composable
fun RegisterScreenDarkPreview() {
    RUPP_Payment_Systerm_Moblie_InterfaceTheme(darkTheme = true) {
        RegisterScreen()
    }
}
