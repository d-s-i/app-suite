package app.local.calendar

import app.suite.design.SuiteColors as CalendarColors
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle as DateTextStyle
import java.util.Locale
import kotlin.math.abs

enum class CalendarView { Month, Week, Day }

@Composable
fun CalendarApp(environment: CalendarEnvironment) {
    val now = calendarNow(environment.clock)
    val today = now.toLocalDate()
    var selected by remember { mutableStateOf(today) }
    var month by remember { mutableStateOf(YearMonth.from(today)) }
    var view by remember { mutableStateOf(CalendarView.Month) }
    var drawer by remember { mutableStateOf(false) }
    var todayRequest by remember { mutableIntStateOf(0) }
    fun chooseDate(date: LocalDate) { selected = date; month = YearMonth.from(date); view = CalendarView.Day }
    BackHandler(enabled = drawer || view == CalendarView.Day) {
        if (drawer) {
            drawer = false
        } else {
            month = YearMonth.from(selected)
            view = CalendarView.Month
        }
    }
    Box(Modifier.fillMaxSize().background(Color.White).windowInsetsPadding(WindowInsets.safeDrawing).testTag("calendar-app")) {
        Column(Modifier.fillMaxSize().testTag("view-${view.name}")) {
            Row(Modifier.fillMaxWidth().height(64.dp).padding(horizontal = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(48.dp).testTag("open-drawer").semantics { contentDescription = "Calendar views" }.clickable { drawer = true }, contentAlignment = Alignment.Center) {
                    Canvas(Modifier.size(22.dp)) {
                        for (i in 0..2) drawLine(CalendarColors.Ink, Offset(0f, size.height * (0.2f + i * 0.3f)), Offset(size.width, size.height * (0.2f + i * 0.3f)), 1.6.dp.toPx())
                    }
                }
                BasicText(monthTitle(month, today, environment.locale), Modifier.weight(1f).padding(start = 12.dp).testTag("month-title").semantics { heading() }, style = TextStyle(color = CalendarColors.Ink, fontSize = 23.sp, fontWeight = FontWeight.Medium))
                Box(Modifier.size(48.dp).testTag("today").semantics { contentDescription = "Today" }.clickable { selected = today; month = YearMonth.from(today); todayRequest++ }, contentAlignment = Alignment.Center) {
                    Box(Modifier.size(25.dp).drawBehind { drawRoundRect(CalendarColors.Ink, cornerRadius = CornerRadius(5.dp.toPx()), style = Stroke(1.5.dp.toPx())) }, contentAlignment = Alignment.Center) {
                        BasicText(today.dayOfMonth.toString(), style = TextStyle(color = CalendarColors.Ink, fontSize = 12.sp, fontWeight = FontWeight.Medium))
                    }
                }
            }
            if (view == CalendarView.Month) {
                MonthGrid(month, today, environment.locale, Modifier.weight(1f).horizontalPaging(month) { direction -> month = YearMonth.from(movePeriod(month.atDay(1), CalendarView.Month, direction)); selected = month.atDay(1) }, ::chooseDate)
            } else {
                TimedCalendar(view, selected, now, environment,
                    onDayHeader = ::chooseDate,
                    onPeriod = { selected = movePeriod(selected, view, it); month = YearMonth.from(selected) },
                    todayRequest = todayRequest,
                    modifier = Modifier.weight(1f),
                )
            }
        }
        if (drawer) {
            Box(Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.18f)).testTag("drawer-scrim").clickable { drawer = false })
            Column(Modifier.fillMaxHeight().width(280.dp).background(Color.White).padding(20.dp).testTag("view-drawer")) {
                BasicText("Calendar", Modifier.padding(vertical = 24.dp), style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Medium, color = CalendarColors.Ink))
                CalendarView.entries.forEach { item ->
                    BasicText(item.name, Modifier.fillMaxWidth().background(if (view == item) CalendarColors.Soft else Color.Transparent).clickable { view = item; month = YearMonth.from(selected); drawer = false }.padding(18.dp).testTag("choose-${item.name}"), style = TextStyle(fontSize = 16.sp, color = if (view == item) CalendarColors.Accent else CalendarColors.Ink))
                }
            }
        }
    }
}

internal fun Modifier.horizontalPaging(key: Any, onPage: (Int) -> Unit): Modifier = pointerInput(key) {
    var distance = 0f
    detectHorizontalDragGestures(
        onDragStart = { distance = 0f },
        onDragCancel = { distance = 0f },
        onDragEnd = { if (abs(distance) > 48.dp.toPx()) onPage(if (distance < 0) 1 else -1) },
        onHorizontalDrag = { change, amount -> change.consume(); distance += amount },
    )
}

@Composable
internal fun MonthGrid(month: YearMonth, today: LocalDate, locale: Locale, modifier: Modifier = Modifier, onDate: (LocalDate) -> Unit) {
    val dates = remember(month) { monthDates(month) }
    Column(modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth().height(24.dp).testTag("weekday-headers"), verticalAlignment = Alignment.CenterVertically) {
            DayOfWeek.entries.forEach { day ->
                BasicText(day.getDisplayName(DateTextStyle.SHORT, locale).take(1), Modifier.weight(1f).testTag("weekday-${day.value}").semantics { contentDescription = day.getDisplayName(DateTextStyle.FULL, locale) }, style = TextStyle(color = CalendarColors.Muted, fontSize = 12.sp, textAlign = TextAlign.Center))
            }
        }
        Column(Modifier.weight(1f).fillMaxWidth().testTag("month-grid").drawBehind {
            for (row in 0..5) drawLine(CalendarColors.Line, Offset(0f, size.height * row / 6), Offset(size.width, size.height * row / 6), 1.dp.toPx())
            for (col in 1..6) drawLine(CalendarColors.Line, Offset(size.width * col / 7, 0f), Offset(size.width * col / 7, size.height), 1.dp.toPx())
        }) {
            dates.chunked(7).forEach { week ->
                Row(Modifier.weight(1f).fillMaxWidth()) {
                    week.forEach { date ->
                        val isToday = date == today
                        Box(Modifier.weight(1f).fillMaxHeight().testTag("date-$date").semantics { contentDescription = date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(locale)) }.clickable { onDate(date) }.drawBehind {
                            if (isToday) drawRoundRect(CalendarColors.Accent, topLeft = Offset((size.width - 21.dp.toPx()) / 2, 4.dp.toPx()), size = Size(21.dp.toPx(), 20.dp.toPx()), cornerRadius = CornerRadius(5.dp.toPx()), style = Stroke(1.dp.toPx()))
                        }) {
                            BasicText(date.dayOfMonth.toString(), Modifier.align(Alignment.TopCenter).paddingFromBaseline(top = 18.dp).testTag("number-$date"), style = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Medium, fontSize = 12.sp, color = when { isToday -> CalendarColors.Accent; date.month != month.month -> CalendarColors.Muted; else -> CalendarColors.Ink }))
                        }
                    }
                }
            }
        }
    }
}
