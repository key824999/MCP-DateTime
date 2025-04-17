package mcp.datetime.tool

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.DayOfWeek
import java.time.LocalDate

class DateTimeDayToolTest {

    private val tool = DateTimeDayTool()

    @Test
    fun `test getDayOfWeek and getTodayDayOfWeek`() {
        assertEquals("MONDAY", tool.getDayOfWeek("2025-04-14")) // Known Monday
        val today = LocalDate.now().dayOfWeek.name
        assertEquals(today, tool.getTodayDayOfWeek())
    }

    @Test
    fun `test getDayOfWeekIndex and getTodayDayIndex`() {
        assertEquals(0, tool.getDayOfWeekIndex("2025-04-14")) // MONDAY
        val expected = LocalDate.now().dayOfWeek.ordinal
        assertEquals(expected, tool.getTodayDayIndex())
    }

    @Test
    fun `test isWeekendDay and isWeekday`() {
        assertTrue(tool.isWeekendDay("SATURDAY"))
        assertTrue(tool.isWeekendDay("sunday"))
        assertFalse(tool.isWeekendDay("MONDAY"))

        assertTrue(tool.isWeekday("monday"))
        assertFalse(tool.isWeekday("sunday"))
    }

    @Test
    fun `test isWeekendDate`() {
        assertTrue(tool.isWeekendDate("2025-04-20")) // Sunday
        assertFalse(tool.isWeekendDate("2025-04-18")) // Friday
    }

    @Test
    fun `test normalizeDayName with variants`() {
        assertEquals(DayOfWeek.MONDAY, tool.normalizeDayName("monday"))
        assertEquals(DayOfWeek.FRIDAY, tool.normalizeDayName(" Friday "))
        assertThrows(IllegalArgumentException::class.java) {
            tool.normalizeDayName("funday")
        }
    }

    @Test
    fun `test dayNameToIndex and indexToDayName`() {
        assertEquals(0, tool.dayNameToIndex("monday"))
        assertEquals(6, tool.dayNameToIndex("sunday"))
        assertEquals("MONDAY", tool.indexToDayName(0))
        assertEquals("SUNDAY", tool.indexToDayName(6))
        assertEquals("TUESDAY", tool.indexToDayName(1002)) // 1002 % 7 = 1
        assertEquals("SUNDAY", tool.indexToDayName(-1))    // handled with modulo logic
    }

    @Test
    fun `test getDayAfterToday with offsets`() {
        val today = LocalDate.now().dayOfWeek
        val next = LocalDate.now().plusDays(1).dayOfWeek
        val prev = LocalDate.now().minusDays(1).dayOfWeek

        assertEquals(today.name, tool.getDayAfterToday(0))
        assertEquals(next.name, tool.getDayAfterToday(1))
        assertEquals(prev.name, tool.getDayAfterToday(-1))
    }

    @Test
    fun `test getDayAfterToday with large offset values`() {
        val result1 = tool.getDayAfterToday(10_000)
        val result2 = tool.getDayAfterToday(-10_000)
        val validDays = DayOfWeek.values().map { it.name }

        assertTrue(result1 in validDays)
        assertTrue(result2 in validDays)
    }

    @Test
    fun `test indexToDayName with extreme values`() {
        assertEquals("MONDAY", tool.indexToDayName(0))
        assertEquals("SUNDAY", tool.indexToDayName(6))
        assertEquals("WEDNESDAY", tool.indexToDayName(16))   // 16 % 7 = 2
        assertEquals("SUNDAY", tool.indexToDayName(-8))    // handled as (-8 % 7 + 7) % 7 = 6
    }
}
