package kh.edu.rupp.rupp_payment_systerm_moblie_interface

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.test.ext.junit.runners.AndroidJUnit4
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.TuitionCalculationResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.sample.SampleData
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.UiState
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.screens.FeeBreakdownScreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RUPP_Payment_Systerm_Moblie_InterfaceTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FeeBreakdownScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private fun show(
        state: UiState<TuitionCalculationResponse>,
        onPayNow: () -> Unit = {},
        onRetry: () -> Unit = {}
    ) {
        composeRule.setContent {
            RUPP_Payment_Systerm_Moblie_InterfaceTheme {
                FeeBreakdownScreen(state = state, onPayNow = onPayNow, onRetry = onRetry)
            }
        }
    }

    @Test
    fun partiallyPaid_showsPayButtonWithRemainingBalance() {
        var clicks = 0
        show(UiState.Success(SampleData.partiallyPaid), onPayNow = { clicks++ })

        composeRule.onNodeWithText("Pay $300.00").assertIsDisplayed().performClick()
        assertEquals(1, clicks)
    }

    @Test
    fun fullyPaid_hidesPayButton() {
        show(UiState.Success(SampleData.fullyPaid))

        composeRule.onNodeWithText("All fees paid").assertIsDisplayed()
        composeRule.onNodeWithText("Pay $", substring = true).assertDoesNotExist()
    }

    @Test
    fun noPayments_showsEmptyState() {
        show(UiState.Success(SampleData.unpaid))

        composeRule.onNode(hasScrollAction()).performScrollToNode(hasText("No payments yet"))
        composeRule.onNodeWithText("No payments yet").assertIsDisplayed()
    }

    @Test
    fun pastDueDate_showsOverdue() {
        show(UiState.Success(SampleData.unpaid))

        composeRule.onNodeWithText("Overdue since 30 Sep 2026").assertIsDisplayed()
    }

    @Test
    fun error_showsServerMessageAndRetries() {
        var retries = 0
        show(UiState.Error("An error occurred: timeout"), onRetry = { retries++ })

        composeRule.onNodeWithText("An error occurred: timeout").assertIsDisplayed()
        composeRule.onNodeWithText("Try again").performClick()
        assertEquals(1, retries)
    }
}
