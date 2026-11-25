package app.local.calendar

import java.time.LocalDate
import org.junit.Assert.*
import org.junit.Test

class CalendarNavigationTest {
    @Test fun dayPagingCrossesLeapDay() {
        assertEquals(LocalDate.parse("2028-03-01"), movePeriod(LocalDate.parse("2028-02-29"), CalendarView.Day, 1))
        assertEquals(LocalDate.parse("2028-02-29"), movePeriod(LocalDate.parse("2028-03-01"), CalendarView.Day, -1))
    }
    @Test fun weekPagingMovesSevenDaysAcrossTheYear() {
        assertEquals(LocalDate.parse("2027-12-27"), movePeriod(LocalDate.parse("2028-01-03"), CalendarView.Week, -1))
        assertEquals(LocalDate.parse("2028-01-03"), movePeriod(LocalDate.parse("2027-12-27"), CalendarView.Week, 1))
    }
    @Test fun monthPagingDoesNotSkipFebruary() {
        assertEquals(LocalDate.parse("2028-02-01"), movePeriod(LocalDate.parse("2028-01-01"), CalendarView.Month, 1))
    }
    @Test(expected = IllegalArgumentException::class) fun invalidPagingDirectionIsRejected() {
        movePeriod(LocalDate.parse("2028-02-15"), CalendarView.Day, 0)
    }
}
