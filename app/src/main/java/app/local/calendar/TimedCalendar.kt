package app.local.calendar

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.Clock
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.TextStyle as DateTextStyle

@Composable
internal fun calendarNow(clock: Clock): ZonedDateTime {
    var now by remember(clock) { mutableStateOf(ZonedDateTime.now(clock)) }
    DisposableEffect(clock) {
        val handler = Handler(Looper.getMainLooper())
        val tick = object : Runnable {
            override fun run() {
                now = ZonedDateTime.now(clock)
                handler.postDelayed(this, 60_000L - Math.floorMod(clock.millis(), 60_000L))
            }
        }
        handler.postDelayed(tick, 60_000L - Math.floorMod(clock.millis(), 60_000L))
        onDispose { handler.removeCallbacks(tick) }
    }
    return now
}

@Composable
internal fun TimedCalendar(
    view: CalendarView,
    selected: LocalDate,
    now: ZonedDateTime,
    environment: CalendarEnvironment,
    onDayHeader: (LocalDate) -> Unit,
    onPeriod: (Int) -> Unit,
    todayRequest: Int,
    modifier: Modifier = Modifier,
) {
    val dayView = view == CalendarView.Day
    val dates = if (dayView) listOf(selected) else weekDates(selected)
    val density = LocalDensity.current
    val measurer = rememberTextMeasurer(cacheSize = 32)
    val hourStyle = TextStyle(color = if (dayView) CalendarColors.Ink else CalendarColors.Muted, fontSize = if (dayView) 12.sp else 11.sp)
    val hourLayouts = (1..23).map { measurer.measure(AnnotatedString(hourLabel(it, environment.is24Hour, environment.locale)), hourStyle) }
    val axisWidth = if (dayView) with(density) { hourLayouts.maxOf { it.size.width }.toDp() + 20.dp } else 52.dp
    val hourLabelHeight = with(density) { hourLayouts.maxOf { it.size.height }.toDp() }
    val headerDividers = Modifier.drawBehind {
        val stroke = 1.dp.toPx()
        val axis = axisWidth.toPx() - stroke / 2
        val bottom = size.height - stroke / 2
        drawLine(CalendarColors.Line, Offset(axis, 0f), Offset(axis, size.height), stroke)
        drawLine(CalendarColors.Line, Offset(0f, bottom), Offset(size.width, bottom), stroke)
    }
    Column(modifier.fillMaxWidth()) {
        if (dayView) {
            val today = selected == now.toLocalDate()
            val weekday = selected.dayOfWeek.getDisplayName(DateTextStyle.SHORT, environment.locale)
            val weekdayStyle = TextStyle(color = if (today) CalendarColors.Accent else CalendarColors.Muted, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            val dateStyle = TextStyle(color = if (today) CalendarColors.Accent else CalendarColors.Ink, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            val weekdayHeight = with(density) { measurer.measure(AnnotatedString(weekday), weekdayStyle).size.height.toDp() }
            val dateHeight = with(density) { measurer.measure(AnnotatedString(selected.dayOfMonth.toString()), dateStyle).size.height.toDp() }
            val squareTop = 12.dp + weekdayHeight + 2.dp
            val headerHeight = weekdayHeight + dateHeight + 27.dp
            Box(Modifier.fillMaxWidth().height(headerHeight).testTag("date-headers").then(headerDividers)) {
                Box(Modifier.width(axisWidth).fillMaxHeight().testTag("day-header-$selected").clickable { onDayHeader(selected) }.drawBehind {
                    if (today) drawRoundRect(CalendarColors.Accent,
                        topLeft = Offset((size.width - 24.dp.toPx()) / 2, squareTop.toPx()),
                        size = Size(24.dp.toPx(), 24.dp.toPx()), cornerRadius = CornerRadius(5.dp.toPx()), style = Stroke(1.5.dp.toPx()))
                }) {
                    BasicText(weekday, Modifier.align(Alignment.TopCenter).offset(y = 12.dp).testTag("day-weekday"), style = weekdayStyle)
                    BasicText(selected.dayOfMonth.toString(), Modifier.align(Alignment.TopCenter).offset(y = squareTop + 12.dp - dateHeight / 2).testTag("day-number"), style = dateStyle)
                }
            }
        } else {
            Row(Modifier.fillMaxWidth().height(56.dp).testTag("date-headers").then(headerDividers), verticalAlignment = Alignment.CenterVertically) {
                Spacer(Modifier.width(axisWidth))
                dates.forEach { date ->
                    val today = date == now.toLocalDate()
                    Column(Modifier.weight(1f).fillMaxHeight().testTag("day-header-$date").clickable { onDayHeader(date) }, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        BasicText(date.dayOfWeek.getDisplayName(DateTextStyle.SHORT, environment.locale), style = TextStyle(color = CalendarColors.Muted, fontSize = 11.sp))
                        BasicText(date.dayOfMonth.toString(), Modifier.padding(top = 3.dp), style = TextStyle(color = if (today) CalendarColors.Accent else CalendarColors.Ink, fontSize = 17.sp, fontWeight = FontWeight.Medium))
                    }
                }
            }
        }
        TimeGrid(dates, now, environment, todayRequest, onPeriod, dayView, axisWidth, hourStyle, hourLabelHeight, Modifier.weight(1f))
    }
}

@Composable
private fun TimeGrid(dates: List<LocalDate>, now: ZonedDateTime, environment: CalendarEnvironment, todayRequest: Int, onPeriod: (Int) -> Unit, dayView: Boolean, axisWidth: Dp, hourStyle: TextStyle, hourLabelHeight: Dp, modifier: Modifier) {
    val scroll = rememberScrollState()
    val density = LocalDensity.current
    val hourHeight = with(density) { 60.dp.toPx() }
    LaunchedEffect(dates, todayRequest) {
        if (now.toLocalDate() in dates) {
            val startHour = if (dayView) dayInitialHour(now.toLocalTime()) else (timeOfDayHours(now.toLocalTime()) - 2).coerceAtLeast(0f)
            scroll.scrollTo((startHour * hourHeight).toInt())
        }
    }
    Box(modifier.fillMaxWidth().horizontalPaging(dates, onPeriod).testTag("time-viewport")) {
        BoxWithConstraints(Modifier.fillMaxWidth().verticalScroll(scroll).testTag("time-grid")) {
            val columnWidth = (maxWidth - axisWidth) / dates.size
            Box(Modifier.fillMaxWidth().height(1440.dp)) {
                Spacer(Modifier.width(axisWidth).fillMaxHeight().testTag("time-axis"))
                Canvas(Modifier.fillMaxSize()) {
                    val gutter = axisWidth.toPx()
                    val width = (size.width - gutter) / dates.size
                    for (hour in 1..23) {
                        drawLine(CalendarColors.Line, Offset(gutter, hour * hourHeight), Offset(size.width, hour * hourHeight), 1.dp.toPx())
                        if (dayView) drawLine(CalendarColors.Line, Offset(gutter - 10.dp.toPx(), hour * hourHeight), Offset(gutter, hour * hourHeight), 1.dp.toPx())
                    }
                    for (day in dates.indices) {
                        val x = if (day == 0) gutter - 0.5.dp.toPx() else gutter + day * width
                        drawLine(CalendarColors.Line, Offset(x, 0f), Offset(x, size.height), 1.dp.toPx())
                    }
                }
                for (hour in 1..23) {
                    val top = 60.dp * hour - if (dayView) hourLabelHeight / 2 else 8.dp
                    val padding = if (dayView) Modifier.padding(horizontal = 10.dp) else Modifier.padding(end = 9.dp)
                    BasicText(hourLabel(hour, environment.is24Hour, environment.locale), Modifier.width(axisWidth).offset(y = top).then(padding).testTag("hour-$hour"), style = hourStyle.copy(textAlign = TextAlign.End))
                }
                val todayIndex = dates.indexOf(now.toLocalDate())
                if (todayIndex >= 0) {
                    Canvas(Modifier.offset(x = axisWidth + columnWidth * todayIndex, y = 60.dp * timeOfDayHours(now.toLocalTime())).width(columnWidth).height(2.dp).testTag("now-indicator").semantics { contentDescription = "Current time ${now.toLocalTime()}" }) {
                        drawLine(CalendarColors.Accent, Offset.Zero, Offset(size.width, 0f), 2.dp.toPx())
                        drawCircle(CalendarColors.Accent, (if (dayView) 5.dp else 4.dp).toPx(), Offset.Zero)
                    }
                }
            }
        }
    }
}
