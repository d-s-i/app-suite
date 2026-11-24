package app.local.calendar

import java.time.LocalDate
import java.time.LocalTime
import java.util.Locale
import org.junit.Assert.*
import org.junit.Test

class TimedCalendarTest {
    @Test fun weekStartsMondayAndContainsTheSelectedSunday() {
        val dates = weekDates(LocalDate.parse("2028-02-20"))
        assertEquals(LocalDate.parse("2028-02-14"), dates.first())
        assertEquals(LocalDate.parse("2028-02-20"), dates.last())
        assertEquals(7, dates.size)
    }
    @Test fun weekCanSpanLeapDayAndAnotherMonth() {
        assertEquals(LocalDate.parse("2028-03-05"), weekDates(LocalDate.parse("2028-02-29")).last())
    }
    @Test fun weekCanSpanAnotherYear() {
        assertEquals(LocalDate.parse("2027-12-27"), weekDates(LocalDate.parse("2028-01-01")).first())
    }
    @Test fun hourLabelsRespectDeviceFormat() {
        assertEquals("13:00", hourLabel(13, true, Locale.US))
        assertEquals("1 PM", hourLabel(13, false, Locale.US))
        assertEquals("12 AM", hourLabel(0, false, Locale.US))
        assertEquals("12 PM", hourLabel(12, false, Locale.US))
    }
    @Test fun dayInitiallyShowsOneHourBeforeNow() {
        assertEquals(11.5f, dayInitialHour(LocalTime.of(12, 30)), 0f)
    }
    @Test fun dayInitialHourDoesNotGoBeforeMidnight() {
        assertEquals(0f, dayInitialHour(LocalTime.of(0, 45)), 0f)
    }
    @Test fun dayInitialHourPreservesLateMinutes() {
        assertEquals(22f + 59f / 60, dayInitialHour(LocalTime.of(23, 59)), 0.001f)
    }
    @Test fun timePositionIncludesMinutesNotJustHours() {
        assertEquals(12.5f, timeOfDayHours(LocalTime.of(12, 30)), 0f)
        assertEquals(23f + 59f / 60, timeOfDayHours(LocalTime.of(23, 59)), 0.001f)
    }
}
