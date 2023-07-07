package com.example.progresscheckapp.Screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Scaffold
import androidx.compose.material.lightColors
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.progresscheckapp.BottomNavigation
import com.example.progresscheckapp.eventList
import com.kizitonwose.calendar.compose.CalendarLayoutInfo
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.core.daysOfWeek
import kotlinx.coroutines.flow.filterNotNull
import java.time.DayOfWeek
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale




@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(
    navController: NavHostController,
    adjacentMonths: Long = 500
) {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(adjacentMonths) }
    val endMonth = remember { currentMonth.plusMonths(adjacentMonths) }
    var selection by rememberSaveable { mutableStateOf<CalendarDay?>(null) }
    val daysOfWeek = remember { daysOfWeek() }
    Scaffold(
        bottomBar = {
            BottomNavigation(navController = navController)
        }
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val state = rememberCalendarState(
                startMonth = startMonth,
                endMonth = endMonth,
                firstVisibleMonth = currentMonth,
                firstDayOfWeek = daysOfWeek.first(),
                outDateStyle = OutDateStyle.EndOfGrid
            )
            val visibleMonth = rememberFirstMostVisibleMonth(state, viewportPercent = 90f)
            LaunchedEffect(visibleMonth) {
                // Clear selection if we scroll to a new month.
                selection = null
            }

            CompositionLocalProvider(LocalContentColor provides lightColors().onSurface) {
                SimpleCalendarTitle(
                    currentMonth = visibleMonth.yearMonth,
                )
                MonthHeader(daysOfWeek = daysOfWeek)
                Spacer(Modifier.size(8.dp))
                Divider(color = Color.LightGray)
                Spacer(Modifier.size(8.dp))
                HorizontalCalendar(
                    modifier = Modifier.wrapContentWidth(),
                    state = state,
                    dayContent = { day ->
                        CompositionLocalProvider(LocalRippleTheme provides ProgressCheckAppTheme) {
                            var title = ""
                            var color = Color.Unspecified
                            if (day.position == DayPosition.MonthDate) {
                                for (event in eventList) {
                                    if (event.kickOff == day.date) {
                                        title = event.title
                                        color = event.color
                                    }
                                    if (event.kickOff!! < day.date && event.codeFreeze!! >= day.date) {
                                        title = ""
                                        color = event.color
                                    }
                                }
                            } else {
                                for (event in eventList) {
                                    if (event.kickOff == day.date) {
                                        title = event.title
                                        color = event.color.copy(alpha = 0.5f)
                                    }
                                    if (event.kickOff!! < day.date && event.codeFreeze!! >= day.date) {
                                        title = ""
                                        color = event.color.copy(alpha = 0.5f)
                                    }
                                }
                            }
                            Day(
                                day = day,
                                isSelected = selection == day,
                                title = title,
                                color = color,
                            ) { clicked ->
                                selection = clicked
                            }
                        }

                    },
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun MonthHeader(daysOfWeek: List<DayOfWeek>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("MonthHeader"),
    ) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                text = dayOfWeek.displayText(),
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun Day(
    day: CalendarDay,
    isSelected: Boolean = false,
    title: String = "",
    color: Color = Color.Unspecified,
    onClick: (CalendarDay) -> Unit = {},
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f) // This is important for square-sizing!
            .border(
                width = if (isSelected) 1.dp else 0.dp,
                color = if (isSelected) Color(0xFF000000) else Color.Transparent,
            )
            .padding(1.dp)
            // Disable clicks on inDates/outDates
            .clickable(
                enabled = day.position == DayPosition.MonthDate,
                onClick = { onClick(day) },
            ),
    ) {
        val textColor = when (day.position) {
            DayPosition.MonthDate -> Color.Unspecified
            DayPosition.InDate, DayPosition.OutDate -> Color(0x80616161)
        }
        Text(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 3.dp),
            text = day.date.dayOfMonth.toString(),
            color = textColor,
            fontSize = 14.sp,
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 8.dp),
        ) {
            Text(
                text = title,
                fontSize = 7.sp,
                fontWeight = FontWeight.Bold,
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .background(color),
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SimpleCalendarTitle(
    currentMonth: YearMonth,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Text(
            modifier = Modifier
                .testTag("MonthTitle"),
            text = currentMonth.year.toString() + "年 " + currentMonth.monthValue.toString() + "月",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xE6000000),
        )
    }
}


@Composable
fun rememberFirstMostVisibleMonth(
    state: CalendarState,
    viewportPercent: Float = 50f,
): CalendarMonth {
    val visibleMonth = remember(state) { mutableStateOf(state.firstVisibleMonth) }
    LaunchedEffect(state) {
        snapshotFlow { state.layoutInfo.firstMostVisibleMonth(viewportPercent) }
            .filterNotNull()
            .collect { month -> visibleMonth.value = month }
    }
    return visibleMonth.value
}

private fun CalendarLayoutInfo.firstMostVisibleMonth(viewportPercent: Float = 50f): CalendarMonth? {
    return if (visibleMonthsInfo.isEmpty()) {
        null
    } else {
        val viewportSize = (viewportEndOffset + viewportStartOffset) * viewportPercent / 100f
        visibleMonthsInfo.firstOrNull { itemInfo ->
            if (itemInfo.offset < 0) {
                itemInfo.offset + itemInfo.size >= viewportSize
            } else {
                itemInfo.size - itemInfo.offset >= viewportSize
            }
        }?.month
    }
}


@RequiresApi(Build.VERSION_CODES.O)
fun DayOfWeek.displayText(uppercase: Boolean = false): String {
    return getDisplayName(TextStyle.SHORT, Locale.JAPAN).let { value ->
        if (uppercase) value.uppercase(Locale.JAPAN) else value
    }
}

private object ProgressCheckAppTheme : RippleTheme {
    @Composable
    override fun defaultColor() = RippleTheme.defaultRippleColor(Color.Black, lightTheme = true)

    @Composable
    override fun rippleAlpha() = RippleTheme.defaultRippleAlpha(Color.Black, lightTheme = true)
}

