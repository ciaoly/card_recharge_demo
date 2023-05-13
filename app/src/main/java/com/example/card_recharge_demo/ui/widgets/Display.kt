package com.example.card_recharge_demo.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.card_recharge_demo.defaultElevation
import com.example.card_recharge_demo.ui.theme.AppColors
import com.example.card_recharge_demo.ui.theme.Card_recharge_demoTheme
import com.gandiva.neumorphic.LightSource
import com.gandiva.neumorphic.neu
import com.gandiva.neumorphic.shape.Pressed
import com.gandiva.neumorphic.shape.RoundedCorner

@Composable
fun Display(value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .neu(
                lightShadowColor = AppColors.lightShadow(),
                darkShadowColor = AppColors.darkShadow(),
                shadowElevation = defaultElevation,
                lightSource = LightSource.LEFT_TOP,
                shape = Pressed(RoundedCorner(24.dp)),
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(24.dp),
    ) {
        // Add child components here.
        Box(
            modifier= Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.Transparent)
                .padding(15.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = value,
                modifier = modifier,
                fontSize = 39.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DisplayPreview() {
    Card_recharge_demoTheme {
        Display("Android")
    }
}
