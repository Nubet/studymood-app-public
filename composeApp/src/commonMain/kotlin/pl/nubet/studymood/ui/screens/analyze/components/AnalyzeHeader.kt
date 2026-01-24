package pl.nubet.studymood.ui.screens.analyze.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.nubet.studymood.presentation.analyze.YearMonth
import pl.nubet.studymood.ui.theme.LocalAppTypography
import pl.nubet.studymood.ui.theme.LocalDimens

@Composable
fun AnalyzeHeader() {
    val dimens = LocalDimens.current
    val typography = LocalAppTypography.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "ANALYZE",
            style = typography.caption.copy(fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Text(
            text = "Last 30 days",
            style = typography.caption,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
        )
    }
}

@Composable
fun MonthSelector(
    availableMonths: List<YearMonth>,
    selectedMonth: YearMonth,
    onMonthSelected: (YearMonth) -> Unit,
) {
    val dimens = LocalDimens.current
    val typography = LocalAppTypography.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dimens.x12),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimens.x8),
        ) {
            Text(
                text = "Monthly",
                style = typography.h2,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = "›",
                style = typography.h2.copy(fontWeight = FontWeight.Light),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            )
        }

        if (availableMonths.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dimens.x12),
            ) {
                availableMonths.forEach { month ->
                    MonthTab(
                        month = month,
                        isSelected = month == selectedMonth,
                        onClick = { onMonthSelected(month) },
                    )
                }
            }
        }
    }
}

@Composable
private fun MonthTab(month: YearMonth, isSelected: Boolean, onClick: () -> Unit) {
    val dimens = LocalDimens.current
    val typography = LocalAppTypography.current

    Column(
        modifier =
            Modifier.clip(RoundedCornerShape(8.dp))
                .clickable(onClick = onClick)
                .padding(vertical = dimens.x8, horizontal = dimens.x12),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = month.toDisplayString(),
            style =
                typography.body2.copy(
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                ),
            color =
                if (isSelected) MaterialTheme.colorScheme.onBackground
                else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
        )

        if (isSelected) {
            Spacer(modifier = Modifier.height(dimens.x4))
            Box(
                modifier =
                    Modifier.width(24.dp)
                        .height(2.dp)
                        .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(1.dp))
            )
        }
    }
}
