package com.example.card_recharge_demo.ui.page

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.card_recharge_demo.ui.widgets.Display
import com.example.card_recharge_demo.defaultElevation
import com.example.card_recharge_demo.ui.theme.AppColors
import com.example.card_recharge_demo.ui.theme.Card_recharge_demoTheme
import com.example.card_recharge_demo.ui.widgets.LoadingIndicator
import com.example.card_recharge_demo.vm.MainViewModel
import com.example.card_recharge_demo.vm.StatusCode
import com.gandiva.neumorphic.LightSource
import com.gandiva.neumorphic.neu
import com.gandiva.neumorphic.shape.Flat
import com.gandiva.neumorphic.shape.Pressed
import com.gandiva.neumorphic.shape.RoundedCorner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun MainPage(vm: MainViewModel) {
    val _balance = vm.balance.observeAsState()
    val _loading = vm.loading.observeAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp)
    ) {
        Display("余额: ${_balance.value}")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 30.dp)
        ) {
            Button(
                onClick = { vm.topUp(10 * 100) },
                shape = btnShape,
                modifier = Modifier
                    .defaultMinSize(minHeight = 80.dp)
                    .padding(btnPadding)
                    .neu(
                        lightShadowColor = AppColors.lightShadow(),
                        darkShadowColor = AppColors.darkShadow(),
                        shadowElevation = defaultElevation,
                        lightSource = LightSource.LEFT_TOP,
                        shape = Flat(RoundedCorner(6.dp))
                    )
                    .weight(1f, fill = true),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),

            ) {
                Text(
                    text = "充10",
                    fontSize = fontSize
                )
            }
            Button(
                onClick = { vm.topUp(30 * 100) },
                shape = btnShape,
                modifier = Modifier
                    .defaultMinSize(minHeight = 80.dp)
                    .padding(btnPadding)
                    .neu(
                        lightShadowColor = AppColors.lightShadow(),
                        darkShadowColor = AppColors.darkShadow(),
                        shadowElevation = defaultElevation,
                        lightSource = LightSource.LEFT_TOP,
                        shape = Flat(RoundedCorner(12.dp))
                    )
                    .weight(1f, fill = true),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
            ) {
                Text(
                    text = "充30",
                    fontSize = fontSize
                )
            }
            Button(
                onClick = { vm.topUp(50 * 100) },
                shape = btnShape,
                modifier = Modifier
                    .defaultMinSize(minHeight = 80.dp)
                    .padding(btnPadding)
                    .neu(
                        lightShadowColor = AppColors.lightShadow(),
                        darkShadowColor = AppColors.darkShadow(),
                        shadowElevation = defaultElevation,
                        lightSource = LightSource.LEFT_TOP,
                        shape = Flat(RoundedCorner(12.dp))
                    )
                    .weight(1f, fill = true),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
            ) {
                Text(
                    text = "充50",
                    fontSize = fontSize
                )
            }
        }

        LaunchedEffect( Unit ) {
            vm.statusCode.collect { status ->
                var text = ""
                when(status) {
                    StatusCode.NO_CARD -> {
                        text = "找不到卡片"
                    }
                    StatusCode.READ_SUCCESS -> {
                        text = "读卡成功"
                    }
                    StatusCode.READ_FAILED -> {
                        text = "读卡失败, 可能是卡片不受支持"
                    }
                    StatusCode.READ_EXCEPTION -> {
                        text = "读卡异常"
                    }
                    StatusCode.WRITE_SUCCESS -> {
                        text = "充值成功"
                    }
                    StatusCode.WRITE_FAILED -> {
                        text = "写卡失败, 可能是密码错误"
                    }
                    StatusCode.WRITE_EXCEPTION -> {
                        text = "写卡异常"
                    }
                    StatusCode.TIMEOUT -> {
                        text = "操作超时"
                    }
                }
                Toast.makeText(
                        context,
                        text,
                        Toast.LENGTH_SHORT,
                    ).show()
                }
        }

        if(_loading.value == true) {
            Dialog(
                onDismissRequest = {  },
                DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
            ) {
                Box(
                    contentAlignment= Center,
                    modifier = Modifier
                        .size(100.dp)
                        .background(White, shape = RoundedCornerShape(8.dp))
                ) {
                    LoadingIndicator()
                }
            }
        }
    }
}

val fontSize = 15.sp
val btnPadding = 10.dp
val btnShape = RoundedCornerShape(12.dp)

@Preview
@Composable
fun MainPagePreview() {
    Card_recharge_demoTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp)
        ) {
            Display("余额: 0.12")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 30.dp)
            ) {
                Button(
                    onClick = {  },
                    shape = btnShape,
                    modifier = Modifier
                        .defaultMinSize(minHeight = 80.dp)
                        .padding(btnPadding)
                        .neu(
                            lightShadowColor = AppColors.lightShadow(),
                            darkShadowColor = AppColors.darkShadow(),
                            shadowElevation = defaultElevation,
                            lightSource = LightSource.LEFT_TOP,
                            shape = Flat(RoundedCorner(6.dp))
                        )
                        .weight(1f, fill = true),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),

                    ) {
                    Text(
                        text = "充10",
                        fontSize = fontSize
                    )
                }
                Button(
                    onClick = { },
                    shape = btnShape,
                    modifier = Modifier
                        .defaultMinSize(minHeight = 80.dp)
                        .padding(btnPadding)
                        .neu(
                            lightShadowColor = AppColors.lightShadow(),
                            darkShadowColor = AppColors.darkShadow(),
                            shadowElevation = defaultElevation,
                            lightSource = LightSource.LEFT_TOP,
                            shape = Flat(RoundedCorner(12.dp))
                        )
                        .weight(1f, fill = true),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                ) {
                    Text(
                        text = "充30",
                        fontSize = fontSize
                    )
                }
                Button(
                    onClick = {  },
                    shape = btnShape,
                    modifier = Modifier
                        .defaultMinSize(minHeight = 80.dp)
                        .padding(btnPadding)
                        .neu(
                            lightShadowColor = AppColors.lightShadow(),
                            darkShadowColor = AppColors.darkShadow(),
                            shadowElevation = defaultElevation,
                            lightSource = LightSource.LEFT_TOP,
                            shape = Flat(RoundedCorner(12.dp))
                        )
                        .weight(1f, fill = true),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                ) {
                    Text(
                        text = "充50",
                        fontSize = fontSize
                    )
                }
            }
            Divider()
            Text(
                text = "其它操作"
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 40.dp)
                    .neu(
                        lightShadowColor = AppColors.lightShadow(),
                        darkShadowColor = AppColors.darkShadow(),
                        shadowElevation = defaultElevation,
                        lightSource = LightSource.LEFT_TOP,
                        shape = Pressed(RoundedCorner(6.dp))
                    )
            ) {
                Button(
                    onClick = {  },
                    shape = btnShape,
                    modifier = Modifier
                        .defaultMinSize(minHeight = 80.dp)
                        .padding(btnPadding)
                        .neu(
                            lightShadowColor = AppColors.lightShadow(),
                            darkShadowColor = AppColors.darkShadow(),
                            shadowElevation = defaultElevation,
                            lightSource = LightSource.LEFT_TOP,
                            shape = Flat(RoundedCorner(6.dp))
                        )
                        .weight(1f, fill = true),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),

                    ) {
                    Text(
                        text = "充10",
                        fontSize = fontSize
                    )
                }
                Button(
                    onClick = { },
                    shape = btnShape,
                    modifier = Modifier
                        .defaultMinSize(minHeight = 80.dp)
                        .padding(btnPadding)
                        .neu(
                            lightShadowColor = AppColors.lightShadow(),
                            darkShadowColor = AppColors.darkShadow(),
                            shadowElevation = defaultElevation,
                            lightSource = LightSource.LEFT_TOP,
                            shape = Flat(RoundedCorner(12.dp))
                        )
                        .weight(1f, fill = true),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                ) {
                    Text(
                        text = "充30",
                        fontSize = fontSize
                    )
                }
            }
        }
    }
}
