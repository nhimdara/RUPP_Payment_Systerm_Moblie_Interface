package kh.edu.rupp.rupp_payment_systerm_moblie_interface

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.sample.SampleData
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.UiState
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.screens.AcademicTermFees
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.screens.SemesterFeeScreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RUPP_Payment_Systerm_Moblie_InterfaceTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SemesterFeeScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val data = AcademicTermFees(
        academicYears = listOf(SampleData.year2025, SampleData.year2026),
        fees = SampleData.tuitionFees,
        semesters = SampleData.semesters
    )

    private fun show() {
        composeRule.setContent {
            var selected by remember { mutableStateOf<Long?>(null) }
            RUPP_Payment_Systerm_Moblie_InterfaceTheme {
                SemesterFeeScreen(
                    state = UiState.Success(data),
                    selectedYearId = selected,
                    onYearSelected = { selected = it }
                )
            }
        }
    }

    @Test
    fun defaultsToYearOfActiveSemester() {
        show()

        composeRule.onNodeWithText("2026-2027").assertIsSelected()
        composeRule.onNodeWithText("$600.00").assertIsDisplayed()
        composeRule.onNodeWithText("Active").assertIsDisplayed()
    }

    @Test
    fun selectingAnotherYear_showsThatYearsFee() {
        show()

        composeRule.onNodeWithText("2025-2026").performClick()

        composeRule.onNodeWithText("2025-2026").assertIsSelected()
        composeRule.onNodeWithText("$550.00").assertIsDisplayed()
        composeRule.onNodeWithText("Active").assertDoesNotExist()
    }
}
