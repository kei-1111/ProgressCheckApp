package com.example.progresscheckapp.Screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.progresscheckapp.EventList
import com.example.progresscheckapp.LongMyTextField
import com.example.progresscheckapp.Name
import com.example.progresscheckapp.Screen
import com.example.progresscheckapp.ShortMyTextField
import com.example.progresscheckapp.User
import com.example.progresscheckapp.getHan1Zen2
import com.example.progresscheckapp.myAcount

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditSettingScreen(navController: NavController) {
    val labels = listOf(
        "名前",
        "ID",
        "参加ハッカソン",
        "自己紹介",
    )
    var name by remember { mutableStateOf(myAcount.name) }
    var selfIntroduction by remember { mutableStateOf(myAcount.selfIntroduction) }
    var charCounter = 0
    for (label in labels) {
        if (charCounter <= getHan1Zen2(label)) charCounter = getHan1Zen2(label)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        ProfileSetBar(
            navController = navController,
            route = Screen.Setting.screen_route,
            user = myAcount
        )
        Image(
            painter = painterResource(id = myAcount.icon),
            contentDescription = "icon",
            modifier = Modifier
                .size(140.dp)
                .padding(16.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Divider(color = Color.LightGray)
        ShortMyTextField(
            label = labels[0],
            text = name,
            spacer = charCounter - getHan1Zen2(labels[0]),
            onValueChange = {
                if (getHan1Zen2(it) <= 14) name = it
            }
        )
        myAcount.name = name
        Name(
            label = labels[1],
            name = myAcount.id,
            spacer = charCounter - getHan1Zen2(labels[1]),
        )
        EventList(
            label = labels[2],
            list = myAcount.joinEvent,
            spacer = charCounter - getHan1Zen2(labels[2]),
        )
        LongMyTextField(
            label = labels[3],
            text = selfIntroduction,
            onValueChange = {
                if (it.length <= 120 && it.count { x -> x == '\n' } <= 5) selfIntroduction = it
            }
        )
        myAcount.selfIntroduction = selfIntroduction
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(67.dp)
        )
    }
}

// イベントを追加するためのボタン
@Composable
fun ProfileSetBar(
    navController: NavController,
    route: String,
    user: User
) {
    Column() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            TextButton(
                onClick = {
                    navController.navigate(route)
                }
            ) {
                Text(
                    text = "キャンセル",
                    fontSize = 16.sp
                )
            }
            Spacer(Modifier.weight(1f))
            TextButton(
                onClick = {
                    navController.navigate(route)
                }
            ) {
                Text(
                    text = "保存",
                    fontSize = 16.sp
                )
            }
        }
        androidx.compose.material3.Divider(color = Color.LightGray)
    }
}
