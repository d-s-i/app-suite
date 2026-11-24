package app.local.calendar

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.TextStyle
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale

fun monthDates(month: YearMonth): List<LocalDate> {
    val start = month.atDay(1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    return List(42) { start.plusDays(it.toLong()) }
}

fun weekDates(date: LocalDate): List<LocalDate> {
    val start = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    return List(7) { start.plusDays(it.toLong()) }
}

fun hourLabel(hour: Int, is24Hour: Boolean, locale: Locale): String =
    LocalTime.of(hour, 0).format(DateTimeFormatter.ofPattern(if (is24Hour) "HH:mm" else "h a", locale))

fun timeOfDayHours(time: LocalTime): Float = time.hour + time.minute / 60f

fun dayInitialHour(time: LocalTime): Float = if (time.hour == 0) 0f else timeOfDayHours(time) - 1f

fun movePeriod(date: LocalDate, view: CalendarView, direction: Int): LocalDate {
    require(direction == -1 || direction == 1) { "Direction must be -1 or 1" }
    return when (view) {
        CalendarView.Month -> date.plusMonths(direction.toLong())
        CalendarView.Week -> date.plusWeeks(direction.toLong())
        CalendarView.Day -> date.plusDays(direction.toLong())
    }
}

fun monthTitle(month: YearMonth, today: LocalDate, locale: Locale): String {
    val name = month.month.getDisplayName(TextStyle.FULL_STANDALONE, locale)
    return if (month.year == today.year) name else "$name ${month.year}"
}
