package com.example.progresscheckapp.Screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.progresscheckapp.BottomNavigation
import com.example.progresscheckapp.Event
import com.example.progresscheckapp.EventList
import com.example.progresscheckapp.Name
import com.example.progresscheckapp.Screen
import com.example.progresscheckapp.SelfIntroduction
import com.example.progresscheckapp.getHan1Zen2
import com.example.progresscheckapp.myAcount


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SettingScreen(
    signOut: () -> Unit,
    navController: NavHostController
) {
    val labels = listOf(
        "名前",
        "ID",
        "参加ハッカソン",
        "自己紹介",
    )
    var openMenu by remember { mutableStateOf(false) }
    var charCounter = 0
    for (label in labels) {
        if (charCounter <= getHan1Zen2(label)) charCounter = getHan1Zen2(label)
    }
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        bottomBar = {
            BottomNavigation(navController = navController)
        }
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Row {
                Image(
                    painter = painterResource(id = myAcount.icon),
                    contentDescription = "icon",
                    modifier = Modifier
                        .size(140.dp)
                        .padding(16.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.TopEnd
                ) {
                    IconButton(
                        onClick = {
                            openMenu = !openMenu
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.MoreVert,
                            contentDescription = null,
                        )
                    }
                    DropdownMenu(
                        expanded = openMenu,
                        onDismissRequest = {
                            openMenu = !openMenu
                        }
                    ) {
                        DropdownMenuItem(
                            onClick = {
                                signOut()
                                navController.navigate(Screen.Auth.screen_route)
                                openMenu = !openMenu
                            }
                        ) {
                            Text(
                                text = "Sign-out"
                            )
                        }
                        DropdownMenuItem(
                            onClick = {
                                navController.navigate(Screen.EditSetting.screen_route)
                                openMenu = !openMenu
                            }
                        ) {
                            Text(
                                text = "編集"
                            )
                        }
                    }
                }
            }
            Divider(color = Color.LightGray)
            Name(
                label = labels[0],
                name = myAcount.name,
                spacer = charCounter - getHan1Zen2(labels[0]),
            )
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
            SelfIntroduction(
                label = labels[3],
                selfIntroduction = myAcount.selfIntroduction,
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventSurface(event: Event) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(0.9f)
        ) {
            Box(
                modifier = Modifier
                    .width(8.dp)
                    .height(34.dp)
                    .background(event.color)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(4.dp)
            ) {
                Text(
                    text = event.title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                )
                if (event.kickOff?.year.toString() == event.codeFreeze?.year.toString()) {
                    Text(
                        text = event.kickOff?.year.toString() + "年" + event.kickOff?.monthValue.toString() + "月" + event.kickOff?.dayOfMonth.toString() + "日"
                                + "～" + event.codeFreeze?.monthValue.toString() + "月" + event.codeFreeze?.dayOfMonth.toString() + "日",
                        fontSize = 6.sp,
                    )
                } else {
                    Text(
                        text = event.kickOff?.year.toString() + "年" + event.kickOff?.monthValue.toString() + "月" + event.kickOff?.dayOfMonth.toString() + "日"
                                + "～" + event.codeFreeze?.year.toString() + "年" + event.codeFreeze?.monthValue.toString() + "月" + event.codeFreeze?.dayOfMonth.toString() + "日",
                        fontSize = 6.sp,
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Text(
                    text = "リーダー",
                    fontSize = 4.5.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = " " + event.leader,
                    fontSize = 4.5.sp,
                )
                Text(
                    text = "メンバー",
                    fontSize = 4.5.sp,
                    fontWeight = FontWeight.Bold,
                )
                Row() {
                    var counter = 0
                    for (member in event.members) {
                        counter = counter + getHan1Zen2(member)
                        if (counter > 25) {
                            Text(
                                text = " . . .",
                                fontSize = 4.5.sp
                            )
                            break
                        }
                        Text(
                            text = " " + member + ",",
                            fontSize = 4.5.sp,
                        )
                    }
                }
            }
        }
    }
}


