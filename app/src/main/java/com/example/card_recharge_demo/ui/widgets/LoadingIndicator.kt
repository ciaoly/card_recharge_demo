package com.example.card_recharge_demo.ui.widgets

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.card_recharge_demo.defaultElevation
import com.example.card_recharge_demo.ui.theme.AppColors
import com.example.card_recharge_demo.ui.theme.Card_recharge_demoTheme
import com.gandiva.neumorphic.LightSource
import com.gandiva.neumorphic.neu
import com.gandiva.neumorphic.shape.Flat
import com.gandiva.neumorphic.shape.Pressed
import com.gandiva.neumorphic.shape.RoundedCorner

@Composable
fun LoadingIndicator() {
    val infiniteTransition = rememberInfiniteTransition()
    val primaryColor = MaterialTheme.colorScheme.primary

    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 600
            }
        )
    )

    Card(
        modifier = Modifier
            .neu(
                lightShadowColor = AppColors.lightShadow(),
                darkShadowColor = AppColors.darkShadow(),
                shadowElevation = defaultElevation,
                lightSource = LightSource.LEFT_TOP,
                shape = Flat(RoundedCorner(24.dp)),
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(24.dp),
    ) {
        Box(
            modifier = Modifier
                .rotate(progress)
                .padding(30.dp)
                .background(Color.Transparent),
            contentAlignment = Alignment.TopCenter
        ) {
            var radius = 100.dp
            var ringWidth = 10.dp
            Canvas(modifier = Modifier.size(radius)) {
                this.drawArc( // 画圆
                    startAngle = 0f,
                    sweepAngle = 36f,
                    color = primaryColor,
                    useCenter = false,
                    style = Stroke(
                        width = ringWidth.toPx(),
                        cap = StrokeCap.Round
                    )
                )
            }
        }
    }
}

@Preview
@Composable
fun LoadingIndicatorPreview() {
    Card_recharge_demoTheme {
        LoadingIndicator()
    }
}
