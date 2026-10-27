package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.AcademicYearResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.SemesterResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.TuitionFeeResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.sample.SampleData
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.ActiveBadge
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.AppHeader
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.DevicePreviews
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.EmptyState
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.InfoRow
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.SectionCard
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.UiState
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.UiStateContent
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.formatApiDate
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.formatUsd
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.readableWidth
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RUPP_Payment_Systerm_Moblie_InterfaceTheme

// Everything the academic term screen needs, loaded from:
// GET /academic-years, GET /tuition-fees?facultyId={student faculty}, GET /semesters
data class AcademicTermFees(
    val academicYears: List<AcademicYearResponse>,
    val fees: List<TuitionFeeResponse>,
    val semesters: List<SemesterResponse>
) {
    // Year of the active semester, otherwise the latest year
    fun defaultYearId(): Long? =
        semesters.firstOrNull { it.isActive }?.academicYear?.id
            ?: academicYears.maxByOrNull { it.startDate }?.id
}

// Academic term / semester fee screen. Tuition is set per academic year (and faculty)
// by the backend, so this shows the year's fee and the semesters it covers.
@Composable
fun SemesterFeeScreen(
    state: UiState<AcademicTermFees>,
    selectedYearId: Long?,
    onYearSelected: (Long) -> Unit,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    onRetry: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        AppHeader(title = "Semester Fees", subtitle = "Academic terms and tuition", onBack = onBack)
        UiStateContent(state = state, modifier = Modifier.fillMaxSize(), onRetry = onRetry) { data ->
            SemesterFeeContent(data, selectedYearId ?: data.defaultYearId(), onYearSelected)
        }
    }
}

@Composable
private fun SemesterFeeContent(
    data: AcademicTermFees,
    yearId: Long?,
    onYearSelected: (Long) -> Unit
) {
    if (data.academicYears.isEmpty()) {
        EmptyState(
            title = "No academic years yet",
            message = "Fees will appear once the university sets up the academic year.",
            modifier = Modifier.fillMaxSize()
        )
        return
    }
    val year = data.academicYears.firstOrNull { it.id == yearId }
    val fees = data.fees.filter { it.academicYear.id == yearId }
    val semesters = data.semesters
        .filter { it.academicYear.id == yearId }
        .sortedBy { it.semesterNumber }

    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .readableWidth(),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            YearSelector(
                years = data.academicYears.sortedByDescending { it.startDate },
                selectedId = yearId,
                onYearSelected = onYearSelected
            )
        }
        item {
            FeeSection(
                year = year,
                fees = fees,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        item {
            SemestersSection(
                semesters = semesters,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        item {
            Text(
                text = "Tuition is charged once per academic year. The semesters above are the terms it covers.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }
    }
}

@Composable
private fun YearSelector(
    years: List<AcademicYearResponse>,
    selectedId: Long?,
    onYearSelected: (Long) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        years.forEach { year ->
            FilterChip(
                selected = year.id == selectedId,
                onClick = { onYearSelected(year.id) },
                label = { Text(year.yearLabel) }
            )
        }
    }
}

@Composable
private fun FeeSection(
    year: AcademicYearResponse?,
    fees: List<TuitionFeeResponse>,
    modifier: Modifier = Modifier
) {
    SectionCard(title = "Tuition fee", modifier = modifier) {
        if (fees.isEmpty()) {
            EmptyState(
                title = "No fee set",
                message = "There is no tuition fee for ${year?.yearLabel ?: "this year"} yet.",
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            fees.forEachIndexed { index, fee ->
                if (index > 0) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 6.dp),
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                    )
                }
                InfoRow(label = "Faculty", value = fee.faculty.facultyName)
                fee.faculty.department?.let { InfoRow(label = "Department", value = it) }
                InfoRow(label = "Annual tuition", value = formatUsd(fee.totalAmount), emphasize = true)
                InfoRow(label = "Due date", value = formatApiDate(fee.dueDate))
            }
        }
        if (year != null) {
            InfoRow(
                label = "Academic year",
                value = "${formatApiDate(year.startDate)} – ${formatApiDate(year.endDate)}"
            )
        }
    }
}

@Composable
private fun SemestersSection(
    semesters: List<SemesterResponse>,
    modifier: Modifier = Modifier
) {
    SectionCard(title = "Semesters", modifier = modifier) {
        if (semesters.isEmpty()) {
            Text(
                text = "No semesters have been added for this year.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        } else {
            semesters.forEachIndexed { index, semester ->
                if (index > 0) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
                }
                SemesterRow(semester)
            }
        }
    }
}

@Composable
private fun SemesterRow(semester: SemesterResponse) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = semester.semesterNumber.toString(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = semester.semesterName,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = if (semester.startDate == null && semester.endDate == null) {
                    "Dates not set"
                } else {
                    "${formatApiDate(semester.startDate)} – ${formatApiDate(semester.endDate)}"
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        if (semester.isActive) {
            Spacer(modifier = Modifier.width(8.dp))
            ActiveBadge()
        }
    }
}

private val sampleTermFees = AcademicTermFees(
    academicYears = listOf(SampleData.year2025, SampleData.year2026),
    fees = SampleData.tuitionFees,
    semesters = SampleData.semesters
)

@DevicePreviews
@Composable
private fun SemesterFeePreview() {
    RUPP_Payment_Systerm_Moblie_InterfaceTheme {
        SemesterFeeScreen(
            state = UiState.Success(sampleTermFees),
            selectedYearId = null,
            onYearSelected = {},
            onBack = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SemesterFeeDarkPreview() {
    RUPP_Payment_Systerm_Moblie_InterfaceTheme(darkTheme = true) {
        SemesterFeeScreen(
            state = UiState.Success(sampleTermFees),
            selectedYearId = SampleData.year2025.id,
            onYearSelected = {},
            onBack = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SemesterFeeLoadingPreview() {
    RUPP_Payment_Systerm_Moblie_InterfaceTheme {
        SemesterFeeScreen(state = UiState.Loading, selectedYearId = null, onYearSelected = {})
    }
}
