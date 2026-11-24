package app.local.calendar

import java.time.LocalDate
import java.time.YearMonth
import java.util.Locale
import org.junit.Assert.*
import org.junit.Test

class CalendarDatesTest {
    @Test fun leapFebruaryHasSixConsecutiveMondayFirstRows() {
        val dates = monthDates(YearMonth.of(2028, 2))
        assertEquals(42, dates.size)
        assertEquals(LocalDate.parse("2028-01-31"), dates.first())
        assertEquals(LocalDate.parse("2028-03-12"), dates.last())
        assertEquals(1, dates.count { it == LocalDate.parse("2028-02-29") })
        dates.zipWithNext().forEach { (a, b) -> assertEquals(a.plusDays(1), b) }
    }
    @Test fun nonLeapFebruaryDoesNotInventADate() {
        val dates = monthDates(YearMonth.of(2027, 2))
        assertEquals(LocalDate.parse("2027-02-01"), dates.first())
        assertEquals(LocalDate.parse("2027-03-14"), dates.last())
        assertEquals(28, dates.count { it.monthValue == 2 })
    }
    @Test fun sundayMonthStartIncludesThePreviousSixDays() {
        assertEquals(LocalDate.parse("2026-02-23"), monthDates(YearMonth.of(2026, 3)).first())
    }
    @Test fun yearBoundaryDatesKeepTheirYear() {
        val dates = monthDates(YearMonth.of(2028, 1))
        assertEquals(LocalDate.parse("2027-12-27"), dates.first())
        assertEquals(LocalDate.parse("2028-02-06"), dates.last())
    }
    @Test fun titleUsesLocaleAndOnlyIncludesAnotherYear() {
        val today = LocalDate.parse("2028-02-15")
        assertEquals("February", monthTitle(YearMonth.of(2028, 2), today, Locale.US))
        assertEquals("février", monthTitle(YearMonth.of(2028, 2), today, Locale.FRANCE))
        assertEquals("décembre 2027", monthTitle(YearMonth.of(2027, 12), today, Locale.FRANCE))
    }
}
