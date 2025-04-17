package mcp.datetime.tool

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.format.DateTimeParseException

class DateTimeWeekToolTest {

    private val tool = DateTimeWeekTool()

    @Test
    fun `test getWeekOfYear and getCurrentWeekOfYear`() {
        assertEquals(1, tool.getWeekOfYear("2024-01-01")) // ISO week 1
        val week = tool.getCurrentWeekOfYear()
        assertTrue(week in 1..53)
    }

    @Test
    fun `test getStartOfWeek and getEndOfWeek`() {
        assertEquals("2025-04-14", tool.getStartOfWeek("2025-04-17")) // Monday
        assertEquals("2025-04-20", tool.getEndOfWeek("2025-04-17"))   // Sunday
    }

    @Test
    fun `test isSameWeek true and false`() {
        assertTrue(tool.isSameWeek("2025-04-14", "2025-04-20")) // same ISO week
        assertFalse(tool.isSameWeek("2025-04-14", "2025-04-21")) // next week
    }

    @Test
    fun `test getWeekdayOfFirstDay returns MONDAY`() {
        assertEquals("MONDAY", tool.getWeekdayOfFirstDay("2025-04-17"))
    }

    @Test
    fun `test getAllDatesOfWeek returns 7 days from Monday`() {
        val weekDates = tool.getAllDatesOfWeek("2025-04-17")
        assertEquals(7, weekDates.size)
        assertEquals("2025-04-14", weekDates.first()) // Monday
        assertEquals("2025-04-20", weekDates.last())  // Sunday
    }

    @Test
    fun `test getStartOfWeekByNumber and getEndOfWeekByNumber`() {
        assertEquals("2025-01-06", tool.getStartOfWeekByNumber(2, 2025))
        assertEquals("2025-01-12", tool.getEndOfWeekByNumber(2, 2025))

        assertEquals("2023-12-25", tool.getStartOfWeekByNumber(52, 2023)) // ISO edge case
    }

    @Test
    fun `test isLastWeekOfMonth returns true at month end`() {
        assertTrue(tool.isLastWeekOfMonth("2025-04-30"))
        assertFalse(tool.isLastWeekOfMonth("2025-04-15"))
    }

    @Test
    fun `test isEvenWeek returns correct parity`() {
        assertTrue(tool.isEvenWeek("2025-04-14"))  // week 16
        assertTrue(tool.isEvenWeek("2025-04-15"))  // same ISO week
        assertFalse(tool.isEvenWeek("2025-04-21")) // week 17
    }

    @Test
    fun `test getDateFromWeekAndDay with various indexes`() {
        assertEquals("2025-01-06", tool.getDateFromWeekAndDay(2, 2025, 0)) // Monday
        assertEquals("2025-01-12", tool.getDateFromWeekAndDay(2, 2025, 6)) // Sunday
        assertEquals("2025-01-08", tool.getDateFromWeekAndDay(2, 2025, 2)) // Wednesday
    }

    @Test
    fun `test invalid input throws DateTimeParseException`() {
        assertThrows(DateTimeParseException::class.java) {
            tool.getWeekOfYear("bad-date")
        }
        assertThrows(DateTimeParseException::class.java) {
            tool.getStartOfWeek("2025-13-01")
        }
    }

    @Test
    fun `test performance getDateFromWeekAndDay for many weeks`() {
        repeat(200) { week ->
            val result = tool.getDateFromWeekAndDay(week % 53 + 1, 2025, 0)
            assertTrue(result.matches(Regex("\\d{4}-\\d{2}-\\d{2}")))
        }
    }
}
