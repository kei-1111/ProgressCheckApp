package com.example.progresscheckapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.progresscheckapp.Screens.EventSurface
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId





// 下2つのテキストフィールドのもととなるテキストフィールド
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomTextField(
    value: String,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onValueChange: (String) -> Unit
) {
    BasicTextField(
        value = value,
        modifier = Modifier
            .background(Color.Transparent, TextFieldDefaults.TextFieldShape)
            .fillMaxWidth(),
        onValueChange = { onValueChange(it) },
        enabled = enabled,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        interactionSource = interactionSource,
        singleLine = singleLine,
        textStyle = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        ),
    ) { innerTextField ->
        TextFieldDefaults.TextFieldDecorationBox(
            value = value,
            innerTextField = innerTextField,
            enabled = enabled,
            singleLine = singleLine,
            visualTransformation = visualTransformation,
            interactionSource = interactionSource,
            contentPadding = TextFieldDefaults
                .textFieldWithoutLabelPadding(0.dp),
        )
    }
}

// 短いテキストフィールド
@Composable
fun ShortMyTextField(
    label: String,
    text: String,
    padding: Int = 20,
    spacer: Int,
    onValueChange: (String) -> Unit
) {
    var textSpace = ""
    for (i in 1..spacer / 2) {
        textSpace += "　"
    }
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = padding.dp, end = padding.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label + textSpace,
                fontSize = 16.sp
            )
            Spacer(Modifier.size(padding.dp))
            CustomTextField(
                value = text,
                onValueChange = onValueChange
            )
        }
        Divider(color = Color.LightGray)
    }
}

// 上記のテキストフィールドと同じ感じにリーダーの名前を表示させる
@Composable
fun Name(
    label: String,
    padding: Int = 20,
    spacer: Int,
    name: String,
) {
    var textSpace = ""
    for (i in 1..spacer / 2) {
        textSpace += "　"
    }
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = padding.dp, end = padding.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label + textSpace,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 15.5.dp, bottom = 15.5.dp),
            )
            Spacer(Modifier.size(padding.dp))
            Text(
                text = name,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 15.5.dp, bottom = 15.5.dp)
            )
        }
        Divider(color = Color.LightGray)
    }
}

// 上記のテキストフィールドと同じ感じにメンバーの名前を表示させる
@Composable
fun NameList(
    list: List<String>,
    label: String,
    padding: Int = 20,
    spacer: Int,
) {
    var textSpace = ""
    for (i in 1..spacer / 2) {
        textSpace += "　"
    }
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = padding.dp, end = padding.dp),
        ) {
            Text(
                text = label + textSpace,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 15.5.dp, bottom = 15.5.dp),
            )
            Spacer(Modifier.size(padding.dp))
            Column {
                repeat(list.size) {
                    Text(
                        text = list[it],
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 15.5.dp)
                    )
                }
                Spacer(Modifier.size(15.5.dp))
            }
        }
        Divider(color = Color.LightGray)
    }
}

// 上記のテキストフィールドと同じ感じにハッカソンの名前を表示させる
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventList(
    list: List<Event>,
    label: String,
    padding: Int = 20,
    spacer: Int,
) {
    var textSpace = ""
    for (i in 1..spacer / 2) {
        textSpace += "　"
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = padding.dp, end = padding.dp),
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 15.5.dp, bottom = 7.5.dp),
        )
        Divider(color = Color.LightGray)
        Spacer(modifier = Modifier.size(8.dp))
        Column {
            if (list.size == 0) {
                Text(
                    text = "なし",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 7.5.dp)
                )
            } else {
                repeat(list.size) {
                    EventSurface(event = list[it])
                }
            }
        }
        Spacer(modifier = Modifier.size(8.dp))
    }
    Divider(color = Color.LightGray)
}

@Composable
fun SelfIntroduction(
    label: String,
    selfIntroduction: String,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp),
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 15.5.dp, bottom = 7.5.dp),
        )
        Divider(color = Color.LightGray)
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = selfIntroduction,
            fontSize = 16.sp,
            style = LocalTextStyle.current.merge(
                TextStyle(
                    lineHeight = 2.em,
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    ),
                    lineHeightStyle = LineHeightStyle(
                        alignment = LineHeightStyle.Alignment.Top,
                        trim = LineHeightStyle.Trim.None
                    )
                )
            )
        )
    }
    Spacer(modifier = Modifier.size(8.dp))
    Divider(color = Color.LightGray)
}

// 長いテキストフィールド
/*
LongMyTextField(
            label = "タスクを追加",
            text = text,
            spacer = 1,
            onValueChange = {
                if(it.length <= 60 && it.count{x-> x == '\n'} <= 5) text = it
            }
        )
*/
// 使う際は、onValueChangeのラムダ式の中でif文を使い改行文字列の数を指定して制限しなければならない。
@Composable
fun LongMyTextField(
    label: String,
    text: String,
    padding: Int = 20,
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = padding.dp, end = padding.dp),
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 15.5.dp, bottom = 7.5.dp),
        )
        Divider(color = Color.LightGray)
        CustomTextField(
            value = text,
            singleLine = false,
            onValueChange = onValueChange
        )
    }
    Divider(color = Color.LightGray)
}

@Composable
fun SelectColor(
    label: String,
    padding: Int = 20,
    color: Color,
    spacer: Int,
    onValueChange: (Color) -> Unit
) {
    var textSpace = ""
    for (i in 1..(spacer - 4) / 2) {
        textSpace += "　"
    }
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = padding.dp, top = 15.5.dp, end = padding.dp, bottom = 15.5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label + " ",
                    fontSize = 16.sp,
                )
                Text(
                    text = "●" + textSpace,
                    fontSize = 20.sp,
                    color = color
                )
            }
            Spacer(Modifier.size(padding.dp))
            Button(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(20.dp),
                onClick = { onValueChange(Color(0xfffffacd)) },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xfffffacd)
                )
            ) {}
            Spacer(Modifier.size(20.dp))
            Button(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(20.dp),
                onClick = { onValueChange(Color(0xff4169e1)) },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xff4169e1)
                )
            ) {}
            Spacer(Modifier.size(20.dp))
            Button(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(20.dp),
                onClick = { onValueChange(Color(0xff98fb98)) },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xff98fb98)
                )
            ) {}
            Spacer(Modifier.size(20.dp))
            Button(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(20.dp),
                onClick = { onValueChange(Color(0xffff7f50)) },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xffff7f50)
                )
            ) {}
            Spacer(Modifier.size(20.dp))
        }
        Divider(color = Color.LightGray)
    }
}


// 日付選択をする場所を作る関数
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectDate(
    label: String,
    padding: Int = 8,
    spacer: Int,
    localDate: LocalDate? = null,
    onValueChange: (LocalDate) -> Unit
) {
    var showPicker by remember { mutableStateOf(false) }
    val zoneId: ZoneId = ZoneId.systemDefault()
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = localDate?.atStartOfDay(zoneId)?.toInstant()?.toEpochMilli()
            ?.plus(1000 * 60 * 60 * 24)
    )
    var selectedDate: LocalDate? = datePickerState.selectedDateMillis?.let {
        Instant.ofEpochMilli(it).atZone(
            ZoneId.systemDefault()
        ).toLocalDate()
    }
    var textSpace = ""
    for (i in 1..spacer / 2) {
        textSpace += "　"
    }
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label + textSpace,
                fontSize = 16.sp
            )
            Spacer(Modifier.size(padding.dp))
            TextButton(
                onClick = {
                    showPicker = true
                }
            ) {
                if (selectedDate != null) {
                    Text(
                        text = selectedDate.year.toString() + "年" + selectedDate.monthValue.toString() + "月" + selectedDate.dayOfMonth.toString() + "日",
                        fontSize = 16.sp
                    )
                    onValueChange(selectedDate)
                } else {
                    Text(
                        text = "日付を選択",
                        fontSize = 16.sp
                    )
                }
            }
            if (showPicker) {
                Material3DatePickerDialogComponent(
                    datePickerState = datePickerState,
                    closePicker = { showPicker = false },
                )
            }
        }
        Divider(color = Color.LightGray)
    }
}

// 日付選択ダイアログ
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Material3DatePickerDialogComponent(
    datePickerState: DatePickerState,
    closePicker: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DatePickerDialog(
        onDismissRequest = {
            closePicker()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.setSelection(datePickerState.selectedDateMillis)
                    closePicker()
                }
            ) {
                Text(text = "OK")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    closePicker()
                }
            ) {
                Text(text = "CANCEL")
            }
        },
        modifier = modifier,
    ) {
        DatePicker(state = datePickerState)
    }
}

// 半角の場合＋1、全角の場合＋2と分けて文字数をカウントする
fun getHan1Zen2(str: String): Int {
    var ret = 0

    val c = str.toCharArray()
    for (i in c.indices) {
        ret += if (c[i].toString().toByteArray().size <= 1) {
            1
        } else {
            2
        }
    }
    return ret
}




