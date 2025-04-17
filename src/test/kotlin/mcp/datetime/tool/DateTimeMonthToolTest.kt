package mcp.datetime.tool

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeParseException

class DateTimeMonthToolTest {

    private val tool = DateTimeMonthTool()

    @Test
    fun `test getStartOfMonth and getEndOfMonth`() {
        assertEquals("2025-04-01", tool.getStartOfMonth("2025-04-17"))
        assertEquals("2025-04-30", tool.getEndOfMonth("2025-04-17"))

        assertEquals("2024-02-01", tool.getStartOfMonth("2024-02-29")) // Leap year
        assertEquals("2024-02-29", tool.getEndOfMonth("2024-02-15"))
    }

    @Test
    fun `test getStartOfCurrentMonth and getEndOfCurrentMonth`() {
        val now = LocalDate.now(ZoneId.of("UTC"))
        assertEquals(now.withDayOfMonth(1).toString(), tool.getStartOfCurrentMonth())
        assertEquals(now.withDayOfMonth(now.lengthOfMonth()).toString(), tool.getEndOfCurrentMonth())
    }

    @Test
    fun `test isEndOfMonth`() {
        assertTrue(tool.isEndOfMonth("2025-04-30"))
        assertFalse(tool.isEndOfMonth("2025-04-29"))
        assertTrue(tool.isEndOfMonth("2024-02-29")) // Leap year
    }

    @Test
    fun `test getAllDatesInMonth`() {
        val dates = tool.getAllDatesInMonth("2025-04-17")
        assertEquals(30, dates.size)
        assertEquals("2025-04-01", dates.first())
        assertEquals("2025-04-30", dates.last())
    }

    @Test
    fun `test getLengthOfMonth`() {
        assertEquals(30, tool.getLengthOfMonth("2025-04-01"))
        assertEquals(28, tool.getLengthOfMonth("2023-02-01"))
        assertEquals(29, tool.getLengthOfMonth("2024-02-01")) // Leap year
    }

    @Test
    fun `test getWeekCountInMonth`() {
        val weeks = tool.getWeekCountInMonth("2025-04-01")
        assertTrue(weeks in 4..6) // ISO 기준 주 수는 달마다 다름
    }

    @Test
    fun `test getEndOfPreviousMonth`() {
        assertEquals("2025-03-31", tool.getEndOfPreviousMonth("2025-04-01"))
        assertEquals("2024-02-29", tool.getEndOfPreviousMonth("2024-03-15")) // Leap year
    }

    @Test
    fun `test getStartOfNextMonth`() {
        assertEquals("2025-05-01", tool.getStartOfNextMonth("2025-04-17"))
        assertEquals("2025-01-01", tool.getStartOfNextMonth("2024-12-31"))
    }

    @Test
    fun `test getSpecificDayInMonth within and out of bounds`() {
        assertEquals("2025-04-15", tool.getSpecificDayInMonth("2025-04-01", 15))
        assertEquals("2025-04-30", tool.getSpecificDayInMonth("2025-04-01", 999)) // Overflow
        assertEquals("2025-04-01", tool.getSpecificDayInMonth("2025-04-01", 0)) // Underflow
    }

    @Test
    fun `test error cases with invalid input`() {
        assertThrows(DateTimeParseException::class.java) {
            tool.getStartOfMonth("invalid-date")
        }
    }

    @Test
    fun `test performance getAllDatesInMonth over multiple months`() {
        repeat(120) { offset ->
            val month = LocalDate.of(2020, 1, 1).plusMonths(offset.toLong())
            val result = tool.getAllDatesInMonth(month.toString())
            assertEquals(month.lengthOfMonth(), result.size)
        }
    }
}
