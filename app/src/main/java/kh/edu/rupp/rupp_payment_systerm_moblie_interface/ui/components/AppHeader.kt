package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.HeaderOverlay

// Green gradient header used at the top of inner screens
@Composable
fun AppHeader(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    onBack: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(HeaderOverlay)
            .statusBarsPadding()
            .padding(horizontal = 4.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (onBack != null) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.semantics { contentDescription = "Back" }
            ) {
                BackArrow(color = Color.White)
            }
        } else {
            Spacer(modifier = Modifier.width(12.dp))
        }
        Column(modifier = Modifier.padding(end = 16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.8f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun BackArrow(color: Color) {
    Canvas(modifier = Modifier.size(24.dp)) {
        val stroke = 2.dp.toPx()
        val mid = Offset(size.width * 0.2f, size.height / 2)
        drawLine(color, mid, Offset(size.width * 0.8f, size.height / 2), stroke, StrokeCap.Round)
        drawLine(color, mid, Offset(size.width * 0.45f, size.height * 0.25f), stroke, StrokeCap.Round)
        drawLine(color, mid, Offset(size.width * 0.45f, size.height * 0.75f), stroke, StrokeCap.Round)
    }
}
