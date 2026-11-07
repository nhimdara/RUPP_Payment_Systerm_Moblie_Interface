package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// Put @DevicePreviews on a screen preview to check it on the sizes we test against:
// small phone, normal phone, dark mode, large font, foldable and tablet.
@Preview(name = "Small phone", device = "spec:width=360dp,height=640dp,dpi=320", showBackground = true)
@Preview(name = "Phone", device = Devices.PIXEL_7, showBackground = true)
@Preview(
    name = "Phone - dark",
    device = Devices.PIXEL_7,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    showBackground = true
)
@Preview(name = "Phone - large font", device = Devices.PIXEL_7, fontScale = 1.5f, showBackground = true)
@Preview(name = "Foldable", device = Devices.PIXEL_FOLD, showBackground = true)
@Preview(name = "Tablet", device = Devices.PIXEL_TABLET, showBackground = true)
annotation class DevicePreviews

// Max width for screen content so cards do not stretch edge to edge on tablets and foldables
val MaxContentWidth = 600.dp

// Fills the width on phones, centers a MaxContentWidth column on wide screens
fun Modifier.readableWidth(): Modifier = this
    .fillMaxWidth()
    .wrapContentWidth(Alignment.CenterHorizontally)
    .widthIn(max = MaxContentWidth)
    .fillMaxWidth()
