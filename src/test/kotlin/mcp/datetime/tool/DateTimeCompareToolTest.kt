package mcp.datetime.tool

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeParseException

class DateTimeCompareToolTest {

    private val tool = DateTimeCompareTool()

    @Test
    fun `test isBefore and isAfter`() {
        assertTrue(tool.isBefore("2024-01-01", "2025-01-01"))
        assertFalse(tool.isBefore("2025-01-01", "2025-01-01"))
        assertTrue(tool.isAfter("2025-01-02", "2025-01-01"))
        assertFalse(tool.isAfter("2025-01-01", "2025-01-01"))
    }

    @Test
    fun `test isSameDay and isBetween`() {
        assertTrue(tool.isSameDay("2025-04-17", "2025-04-17"))
        assertFalse(tool.isSameDay("2025-04-17", "2025-04-18"))

        assertTrue(tool.isBetween("2025-04-17", "2025-04-01", "2025-04-30"))
        assertTrue(tool.isBetween("2025-04-01", "2025-04-01", "2025-04-30")) // inclusive
        assertFalse(tool.isBetween("2025-05-01", "2025-04-01", "2025-04-30"))
    }

    @Test
    fun `test isSameMonth and isSameYear`() {
        assertTrue(tool.isSameMonth("2025-04-01", "2025-04-30"))
        assertFalse(tool.isSameMonth("2025-04-01", "2025-05-01"))

        assertTrue(tool.isSameYear("2025-04-01", "2025-12-31"))
        assertFalse(tool.isSameYear("2025-12-31", "2024-12-31"))
    }

    @Test
    fun `test isToday isFutureDate isPastDate`() {
        val today = LocalDate.now(ZoneId.of("UTC")).toString()
        val tomorrow = LocalDate.now(ZoneId.of("UTC")).plusDays(1).toString()
        val yesterday = LocalDate.now(ZoneId.of("UTC")).minusDays(1).toString()

        assertTrue(tool.isToday(today))
        assertTrue(tool.isFutureDate(tomorrow))
        assertTrue(tool.isPastDate(yesterday))
        assertFalse(tool.isToday(tomorrow))
        assertFalse(tool.isFutureDate(yesterday))
        assertFalse(tool.isPastDate(tomorrow))
    }

    @Test
    fun `test isAmNow and isPmNow return complementary values`() {
        val isAm = tool.isAmNow()
        val isPm = tool.isPmNow()
        assertTrue(isAm || isPm)
        assertFalse(isAm && isPm)
    }

    @Test
    fun `test isLeapYear`() {
        assertTrue(tool.isLeapYear(2024))
        assertFalse(tool.isLeapYear(2023))
    }

    @Test
    fun `test isValidDate`() {
        assertTrue(tool.isValidDate("2025-04-17", "yyyy-MM-dd"))
        assertFalse(tool.isValidDate("2025/04/17", "yyyy-MM-dd"))
        assertFalse(tool.isValidDate("invalid-date", "yyyy-MM-dd"))
    }

    @Test
    fun `test isBusinessDay isWeekend isWeekday`() {
        assertTrue(tool.isBusinessDay("2025-04-17")) // Thursday
        assertFalse(tool.isBusinessDay("2025-04-19")) // Saturday

        assertTrue(tool.isWeekend("2025-04-20")) // Sunday
        assertFalse(tool.isWeekend("2025-04-17")) // Thursday
    }

    @Test
    fun `test invalid inputs throw DateTimeParseException`() {
        assertThrows(DateTimeParseException::class.java) {
            tool.isBefore("bad-date", "2025-01-01")
        }
        assertThrows(DateTimeParseException::class.java) {
            tool.isBetween("2025-04-17", "start", "2025-04-30")
        }
        assertThrows(DateTimeParseException::class.java) {
            tool.isSameDay("2025-13-01", "2025-04-17")
        }
    }
}
