package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.localization.LocalStrings
import com.example.ui.theme.AppVisualTheme
import com.example.ui.theme.LocalAppThemeColors
import com.example.ui.viewmodel.ScreenDestination

data class NavItemData(
    val destination: ScreenDestination,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

/**
 * Modern, Floating, Rounded, Premium Bottom Navigation Bar.
 * - Purple: Dark/purple surface + subtle violet gradient + fine geometric texture + soft glowing active pill.
 * - Gold: Dark/gold/bronze surface + subtle gold gradient + fine luxury texture + soft golden glowing active pill.
 * - Black & White: Completely static flat monochrome pill, zero glow, zero motion, high-contrast clear distinction.
 */
@Composable
fun FloatingCurvedBottomBar(
    currentDestination: ScreenDestination,
    onNavigate: (ScreenDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    val strings = LocalStrings.current

    val navItems = listOf(
        NavItemData(
            destination = ScreenDestination.HOME,
            label = strings.navHome,
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            testTag = "nav_item_home"
        ),
        NavItemData(
            destination = ScreenDestination.PURCHASES,
            label = strings.navPurchases,
            selectedIcon = Icons.Filled.ShoppingCart,
            unselectedIcon = Icons.Outlined.ShoppingCart,
            testTag = "nav_item_purchases"
        ),
        NavItemData(
            destination = ScreenDestination.STATEMENTS,
            label = strings.navStatements,
            selectedIcon = Icons.Filled.Assessment,
            unselectedIcon = Icons.Outlined.Assessment,
            testTag = "nav_item_statements"
        ),
        NavItemData(
            destination = ScreenDestination.DATABASE,
            label = strings.navDatabase,
            selectedIcon = Icons.Filled.Folder,
            unselectedIcon = Icons.Outlined.Folder,
            testTag = "nav_item_database"
        ),
        NavItemData(
            destination = ScreenDestination.SETTINGS,
            label = strings.navSettings,
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
            testTag = "nav_item_settings"
        )
    )

    val themeColors = LocalAppThemeColors.current

    val barBrush = remember(themeColors.visualTheme, themeColors.isDark) {
        when (themeColors.visualTheme) {
            AppVisualTheme.PURPLE -> {
                if (themeColors.isDark) {
                    Brush.horizontalGradient(
                        listOf(Color(0xFF220835), Color(0xFF140321), Color(0xFF220835))
                    )
                } else {
                    Brush.horizontalGradient(
                        listOf(Color(0xFF4C1D95), Color(0xFF3B0764), Color(0xFF4C1D95))
                    )
                }
            }
            AppVisualTheme.GOLD -> {
                if (themeColors.isDark) {
                    Brush.horizontalGradient(
                        listOf(Color(0xFF2E2108), Color(0xFF191204), Color(0xFF2E2108))
                    )
                } else {
                    Brush.horizontalGradient(
                        listOf(Color(0xFF5C4712), Color(0xFF3E2F0B), Color(0xFF5C4712))
                    )
                }
            }
            AppVisualTheme.BLACK_AND_WHITE -> {
                val c = if (themeColors.isDark) Color(0xFF141414) else Color(0xFF1F1F1F)
                Brush.linearGradient(listOf(c, c))
            }
        }
    }

    val barBorderColor = remember(themeColors.visualTheme) {
        when (themeColors.visualTheme) {
            AppVisualTheme.PURPLE -> Color(0xFFA855F7).copy(alpha = 0.25f)
            AppVisualTheme.GOLD -> Color(0xFFD4AF37).copy(alpha = 0.25f)
            AppVisualTheme.BLACK_AND_WHITE -> Color.White.copy(alpha = 0.12f)
        }
    }

    val barElevation = if (themeColors.visualTheme == AppVisualTheme.BLACK_AND_WHITE) 4.dp else 16.dp
    val barSpotColor = when (themeColors.visualTheme) {
        AppVisualTheme.PURPLE -> Color(0xFFA855F7).copy(alpha = 0.4f)
        AppVisualTheme.GOLD -> Color(0xFFD4AF37).copy(alpha = 0.4f)
        AppVisualTheme.BLACK_AND_WHITE -> Color.Black.copy(alpha = 0.25f)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = barElevation,
                    shape = RoundedCornerShape(26.dp),
                    spotColor = barSpotColor
                ),
            shape = RoundedCornerShape(26.dp),
            color = Color.Transparent
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(26.dp))
                    .background(barBrush)
                    .border(1.dp, barBorderColor, RoundedCornerShape(26.dp))
            ) {
                // Subtle Canvas Pattern inside Floating Bottom Bar for Purple and Gold
                if (themeColors.visualTheme != AppVisualTheme.BLACK_AND_WHITE) {
                    Canvas(modifier = Modifier.matchParentSize()) {
                        val w = size.width
                        val h = size.height

                        when (themeColors.visualTheme) {
                            AppVisualTheme.PURPLE -> {
                                val strokeColor = Color(0xFFD8B4FE).copy(alpha = 0.06f)
                                var x = 0f
                                while (x < w + h) {
                                    drawLine(
                                        color = strokeColor,
                                        start = Offset(x, 0f),
                                        end = Offset(x - h, h),
                                        strokeWidth = 1f
                                    )
                                    x += 40f
                                }
                            }
                            AppVisualTheme.GOLD -> {
                                val strokeColor = Color(0xFFFFE082).copy(alpha = 0.06f)
                                var x = 0f
                                while (x < w + 50f) {
                                    val path = Path().apply {
                                        moveTo(x, 0f)
                                        lineTo(x + 20f, h / 2)
                                        lineTo(x, h)
                                    }
                                    drawPath(path, strokeColor, style = Stroke(1f))
                                    x += 50f
                                }
                            }
                            AppVisualTheme.BLACK_AND_WHITE -> {
                                // Static flat - no Canvas
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(68.dp)
                        .padding(horizontal = 6.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    navItems.forEach { item ->
                        val isSelected = currentDestination == item.destination

                        val targetPillColor = if (isSelected) {
                            when (themeColors.visualTheme) {
                                AppVisualTheme.PURPLE -> Color(0xFFA855F7).copy(alpha = 0.28f)
                                AppVisualTheme.GOLD -> Color(0xFFD4AF37).copy(alpha = 0.28f)
                                AppVisualTheme.BLACK_AND_WHITE -> Color.White.copy(alpha = 0.18f)
                            }
                        } else {
                            Color.Transparent
                        }

                        val activePillColor by animateColorAsState(
                            targetValue = targetPillColor,
                            animationSpec = if (themeColors.isMotionEnabled) spring(stiffness = Spring.StiffnessMediumLow) else snap(),
                            label = "pill_color"
                        )

                        val targetContentColor = if (isSelected) {
                            when (themeColors.visualTheme) {
                                AppVisualTheme.PURPLE -> Color(0xFFF3E8FF)
                                AppVisualTheme.GOLD -> Color(0xFFFFE082)
                                AppVisualTheme.BLACK_AND_WHITE -> Color.White
                            }
                        } else {
                            Color.White.copy(alpha = 0.65f)
                        }

                        val contentColor by animateColorAsState(
                            targetValue = targetContentColor,
                            animationSpec = if (themeColors.isMotionEnabled) spring(stiffness = Spring.StiffnessMediumLow) else snap(),
                            label = "content_color"
                        )

                        val iconScale by animateFloatAsState(
                            targetValue = if (isSelected && themeColors.isMotionEnabled) 1.08f else 1f,
                            animationSpec = if (themeColors.isMotionEnabled) spring(stiffness = Spring.StiffnessMediumLow) else snap(),
                            label = "icon_scale"
                        )

                        val interactionSource = remember { MutableInteractionSource() }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(18.dp))
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    onNavigate(item.destination)
                                }
                                .minimumInteractiveComponentSize()
                                .padding(vertical = 4.dp)
                                .testTag(item.testTag),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            val pillBorderColor = if (isSelected && themeColors.visualTheme != AppVisualTheme.BLACK_AND_WHITE) {
                                when (themeColors.visualTheme) {
                                    AppVisualTheme.PURPLE -> Color(0xFFC084FC).copy(alpha = 0.4f)
                                    AppVisualTheme.GOLD -> Color(0xFFE5C158).copy(alpha = 0.4f)
                                    else -> Color.Transparent
                                }
                            } else Color.Transparent

                            Box(
                                modifier = Modifier
                                    .scale(iconScale)
                                    .clip(CircleShape)
                                    .background(activePillColor)
                                    .border(if (isSelected && themeColors.visualTheme != AppVisualTheme.BLACK_AND_WHITE) 1.dp else 0.dp, pillBorderColor, CircleShape)
                                    .padding(horizontal = 14.dp, vertical = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.label,
                                    tint = contentColor,
                                    modifier = Modifier.size(22.dp)
                                )
                            }

                            Text(
                                text = item.label,
                                color = contentColor,
                                fontSize = 10.5.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
