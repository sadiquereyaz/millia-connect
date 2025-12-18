package com.reyaz.feature.portal.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyaz.feature.portal.domain.model.DynamicUiClickAction
import com.reyaz.feature.portal.domain.model.PromoCard

@Composable
fun GradientPromoCard(
    promoCard: PromoCard,
    modifier: Modifier = Modifier,
    onActionClick: (DynamicUiClickAction) -> Unit
) {
    val gradientBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFF6B73FF),
            Color(0xFF784BA0)
        )
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(gradientBrush)
            .padding(24.dp)
    ) {
        Column {

            // Title
            promoCard.title?.let {
                Text(
                    text = it,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(16.dp))

            // Body
            promoCard.bodyText?.let {
                Text(
                    text = it,
                    color = Color.White.copy(alpha = 0.95f),
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Justify
                )
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Secondary text
                promoCard.secondaryAction?.let {
                    Text(
                        text = it.text,
                        color = Color.White,
                        textDecoration = TextDecoration.Underline,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .clickable(
                                enabled = it.action != null
                            ) {
                                it.action?.let { it1 -> onActionClick(it1) }
                            }
                    )
                }

                // Primary button
                promoCard.primaryAction?.let {
                    Surface(
                        shape = RoundedCornerShape(100),
                        color = Color.White,
                        contentColor = Color(0xFF784BA0),
                        shadowElevation = 2.dp,
                        tonalElevation = 4.dp,
                        modifier = Modifier.clickable(
                            enabled = it.action != null
                        ) {
                            it.action?.let { it1 -> onActionClick(it1) }

                        }
                    ) {
                        Text(
                            text = it.text,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

/*

@Composable
fun GradientPromoCard(
    modifier: Modifier = Modifier,
    title: String? = null,
    bodyText: String? = null,
    primaryBtnText: String? = null,
    secondaryText: String? = null,
    onPrimaryBtnClick: (() -> Unit)? = null,
    onSecondaryTextClick: (() -> Unit)? = null
) {
    // 1. Define the Gradient Colors
    val gradientBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFF6B73FF), // Top-Left (Lighter Blue-Purple)
            Color(0xFF784BA0)  // Bottom-Right (Darker Violet)
        )
    )

    // 2. Main Container
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(gradientBrush)
            .padding(24.dp)
    ) {
        Column {
            // 3. Title (Only show if not null)
            title?.let {
                Text(
                    text = it,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Spacer(Modifier.height(16.dp))
            // 4. Body Text (Only show if not null)
            bodyText?.let {
                Text(
                    text = it,
                    color = Color.White.copy(alpha = 0.95f),
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Justify,
                )
            }
            Spacer(Modifier.height(16.dp))
            // 5. Footer Row (Only show if at least one footer item exists)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left: Secondary Text
                    if (secondaryText != null) {
                        Text(
                            text = secondaryText,
                            color = Color.White,
                            textDecoration = TextDecoration.Underline,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp)) // distinct ripple shape
                                .clickable(enabled = onSecondaryTextClick != null) {
                                    onSecondaryTextClick?.invoke()
                                }
                        )
                    } else {
                        // Optional: Add a Spacer here if you always want the button
                        // pushed to the right even if secondary text is missing.
                        Spacer(modifier = Modifier.width(1.dp))
                    }

                    // Right: Primary Button
                    primaryBtnText?.let { btnText ->
                        Surface(
                            shape = RoundedCornerShape(100),
                            color = Color.White,
                            contentColor = Color(0xFF784BA0),
                            shadowElevation = 2.dp,
                            tonalElevation = 4.dp,
                            modifier = Modifier
//                                .clip(RoundedCornerShape(100))
//                                .background(color = Color.White)
                                .clickable(enabled = onPrimaryBtnClick != null, onClick = { onPrimaryBtnClick?.invoke() })
                        ) {
                            Text(
                                text = btnText,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color(0xFF784BA0),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
                            )
                        }
                        */
/*Button(
                            onClick = { onPrimaryBtnClick?.invoke() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Color(0xFF784BA0)
                            ),
                            shape = RoundedCornerShape(50),
                            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 0.dp)
                        ) {
                            Text(
                                text = btnText,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }*//*

                    }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GradientPromoCardPreview() {
    MaterialTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            GradientPromoCard(
                title = "This is the Title",
                bodyText = "For mobile app ad banner design, apps like Canva and CapCut offer templates, easy editing, and fonts to create professional visuals.",
                primaryBtnText = "Action",
                secondaryText = "Learn More",
                onPrimaryBtnClick = {},
                onSecondaryTextClick = {}
            )
        }
    }
}*/
