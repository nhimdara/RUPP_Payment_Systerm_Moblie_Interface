package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.Badge
import androidx.compose.material.icons.rounded.Computer
import androidx.compose.material.icons.rounded.EditNote
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.TwoWheeler
import androidx.compose.material.icons.rounded.Payments
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.R

const val CAMPUS_PHOTO_CREDIT = "Campus 1 photo: Royal University of Phnom Penh (rupp.edu.kh)"

/**
 * Header with the RUPP Campus 1 photo under a green wash, the emblem and the bilingual
 * university name (like the official RUPP app). [content] is drawn below the name.
 */
@Composable
fun CampusHeader(
    modifier: Modifier = Modifier,
    bottomRadius: Dp = 0.dp,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    Box(
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = bottomRadius, bottomEnd = bottomRadius))
    ) {
        Image(
            painter = painterResource(R.drawable.rupp_campus),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center,
            modifier = Modifier.matchParentSize()
        )
        Box(
            Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            RuppPalette.Forest900.copy(alpha = 0.72f),
                            RuppPalette.Forest800.copy(alpha = 0.80f),
                            RuppPalette.Forest700.copy(alpha = 0.94f)
                        )
                    )
                )
        )
        Column(
            Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
                .padding(top = 12.dp, bottom = 18.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier.size(52.dp).clip(CircleShape).background(Color.White).padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(painterResource(R.drawable.rupp_logo), contentDescription = "RUPP emblem")
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        "សាកលវិទ្យាល័យភូមិន្ទភ្នំពេញ",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text("Royal University of Phnom Penh", color = Color.White.copy(alpha = 0.85f), style = MaterialTheme.typography.bodySmall)
                }
            }
            content()
        }
    }
}

/** Large faded RUPP emblem in the corner of a card, like the official ID card. */
@Composable
fun BoxScope.EmblemWatermark(size: Dp = 170.dp) {
    Image(
        painter = painterResource(R.drawable.rupp_logo),
        contentDescription = null,
        modifier = Modifier
            .align(Alignment.CenterEnd)
            .offset(x = size * 0.28f)
            .size(size)
            .alpha(0.10f)
    )
}

/** Card colours per kind of campus card. */
enum class CardSkin(val brush: Brush) {
    STUDENT(Brush.linearGradient(listOf(Color(0xFF1F5A3A), Color(0xFF123D2C), Color(0xFF0A2A1E)))),
    PARKING(Brush.linearGradient(listOf(Color(0xFF3B4A1E), Color(0xFF263515), Color(0xFF16200C)))),
    LIBRARY(Brush.linearGradient(listOf(Color(0xFF16504F), Color(0xFF0F3A39), Color(0xFF082424)))),
    LAB(Brush.linearGradient(listOf(Color(0xFF233F63), Color(0xFF172C47), Color(0xFF0D1B2E)))),
    INACTIVE(Brush.linearGradient(listOf(Color(0xFF59615C), Color(0xFF3E4541), Color(0xFF2A2F2C))))
}

/**
 * Smart campus card in the RUPP style: emblem, university name, card title, status chip,
 * holder name and a footer line. Used for the student ID, moto parking and library cards.
 */
@Composable
fun SmartCard(
    title: String,
    holder: String,
    subtitle: String,
    footerLeft: String,
    footerRight: String,
    statusLabel: String,
    statusActive: Boolean,
    skin: CardSkin,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    extra: @Composable ColumnScope.() -> Unit = {}
) {
    val shape = RoundedCornerShape(24.dp)
    Box(
        modifier
            .fillMaxWidth()
            .clip(shape)
            .background(skin.brush)
            .border(1.dp, Color.White.copy(alpha = 0.10f), shape)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
    ) {
        EmblemWatermark()
        Column(Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier.size(36.dp).clip(CircleShape).background(Color.White).padding(3.dp),
                    contentAlignment = Alignment.Center
                ) { Image(painterResource(R.drawable.rupp_logo), contentDescription = null) }
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        "ROYAL UNIVERSITY OF PHNOM PENH",
                        color = Color.White.copy(alpha = 0.75f),
                        fontSize = 9.sp,
                        letterSpacing = 1.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                    Text(title, color = Color.White, style = MaterialTheme.typography.labelLarge, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                CardStatusChip(statusLabel, statusActive)
            }
            Spacer(Modifier.height(22.dp))
            Text(holder, color = Color.White, style = MaterialTheme.typography.headlineSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(subtitle, color = Color.White.copy(alpha = 0.75f), style = MaterialTheme.typography.bodySmall, maxLines = 2)
            extra()
            Spacer(Modifier.height(16.dp))
            Box(Modifier.fillMaxWidth().height(1.dp).background(Color.White.copy(alpha = 0.18f)))
            Spacer(Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(footerLeft, color = Color.White.copy(alpha = 0.85f), fontFamily = FontFamily.Monospace, fontSize = 12.sp, modifier = Modifier.weight(1f))
                Text(footerRight, color = Color.White.copy(alpha = 0.85f), style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

@Composable
fun CardStatusChip(label: String, active: Boolean) {
    Row(
        Modifier
            .clip(RoundedCornerShape(50))
            .background(Color.White.copy(alpha = 0.14f))
            .border(1.dp, if (active) RuppPalette.Green400.copy(alpha = 0.6f) else Color.White.copy(alpha = 0.25f), RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(7.dp).clip(CircleShape).background(if (active) RuppPalette.Green400 else RuppPalette.Gold))
        Spacer(Modifier.width(6.dp))
        Text(label, color = Color.White, style = MaterialTheme.typography.labelMedium)
    }
}

/** Icon, colour and short label for each backend service fee category. */
data class ServiceVisual(val icon: ImageVector, val color: Color, val label: String, val skin: CardSkin)

fun serviceVisual(category: String?): ServiceVisual = when (category?.uppercase()) {
    "MOTO_PARKING" -> ServiceVisual(Icons.Rounded.TwoWheeler, Color(0xFF5B7A1E), "Moto Parking", CardSkin.PARKING)
    "LIBRARY" -> ServiceVisual(Icons.AutoMirrored.Rounded.MenuBook, Color(0xFF13706C), "Library", CardSkin.LIBRARY)
    "LABORATORY" -> ServiceVisual(Icons.Rounded.Computer, Color(0xFF2F5C93), "Lab Access", CardSkin.LAB)
    "ID_CARD" -> ServiceVisual(Icons.Rounded.Badge, Color(0xFFA97C00), "ID Card", CardSkin.STUDENT)
    "EXAM" -> ServiceVisual(Icons.Rounded.EditNote, Color(0xFFB4532A), "Exam", CardSkin.STUDENT)
    "TUITION" -> ServiceVisual(Icons.Rounded.School, RuppPalette.Forest700, "Tuition", CardSkin.STUDENT)
    else -> ServiceVisual(Icons.Rounded.Payments, RuppPalette.Forest600, "Service", CardSkin.STUDENT)
}

/** Groups digits like a card number: 1 -> "2026 0000 0001". */
fun cardNumber(prefix: Int, id: Long): String {
    val digits = "%04d%08d".format(prefix, id)
    return digits.chunked(4).joinToString(" ")
}

@Composable
fun PillTabs(options: List<Pair<ImageVector, String>>, selected: Int, onSelect: (Int) -> Unit, modifier: Modifier = Modifier) {
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        options.forEachIndexed { i, (icon, label) ->
            val on = i == selected
            Row(
                Modifier
                    .clip(RoundedCornerShape(50))
                    .background(if (on) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface)
                    .border(1.dp, if (on) Color.Transparent else Rupp.colors.cardBorder, RoundedCornerShape(50))
                    .clickable { onSelect(i) }
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(icon, null, tint = if (on) Color.White else MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
                Text(label, style = MaterialTheme.typography.labelLarge, color = if (on) Color.White else MaterialTheme.colorScheme.onSurface, maxLines = 1)
            }
        }
    }
}
