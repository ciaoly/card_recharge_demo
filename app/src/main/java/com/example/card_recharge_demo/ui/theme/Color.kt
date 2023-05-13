package com.example.card_recharge_demo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)



object AppColors {
    val Blue600 = Color(0xff138ffa)
    val Blue700 = Color(0xff147ce6)

    object Light {
        val Background = Color(0xFFDCDCDC)
        val LightShadow = Color(0xFFFFFFFF)
        val DarkShadow = Color(0xFFA8B5C7)
        val TextColor = Color.Black
    }

    object Dark {
        val Background = Color(0xFF303234)
        val LightShadow = Color(0x66494949)
        val DarkShadow = Color(0x66000000)
        val TextColor = Color.White
    }

    @Composable
    fun lightShadow() = if (!isSystemInDarkTheme()) Light.LightShadow else Dark.LightShadow

    @Composable
    fun darkShadow() = if (!isSystemInDarkTheme()) Light.DarkShadow else Dark.DarkShadow
}