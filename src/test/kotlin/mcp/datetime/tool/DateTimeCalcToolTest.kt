package mcp.datetime.tool

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.format.DateTimeParseException

class DateTimeCalcToolTest {

    private val tool = DateTimeCalcTool()

    @Test
    fun `test add subtract days months years`() {
        assertEquals("2025-04-20", tool.addDays("2025-04-17", 3))
        assertEquals("2025-04-14", tool.subtractDays("2025-04-17", 3))
        assertEquals("2025-07-17", tool.addMonths("2025-04-17", 3))
        assertEquals("2027-04-17", tool.addYears("2025-04-17", 2))
        assertEquals("2021-02-28", tool.addYears("2020-02-29", 1)) // leap year
    }

    @Test
    fun `test addMinutes subtractHours subtractSeconds`() {
        assertEquals("2025-04-17T01:30", tool.addMinutes("2025-04-17T01:00:00", 30))
        assertEquals("2025-04-17T00:00", tool.subtractHours("2025-04-17T01:00:00", 1))
        assertEquals("2025-04-17T00:59:30", tool.subtractSeconds("2025-04-17T01:00:00", 30))
    }

    @Test
    fun `test withStartOfDay withEndOfDay`() {
        assertEquals("2025-04-17T00:00", tool.withStartOfDay("2025-04-17"))
        assertEquals("2025-04-17T23:59:59.999999999", tool.withEndOfDay("2025-04-17"))
    }

    @Test
    fun `test daysBetween hoursBetween minutesBetween secondsBetween`() {
        assertEquals(10, tool.daysBetween("2025-04-01", "2025-04-11"))
        assertEquals(24, tool.hoursBetween("2025-04-17T00:00:00", "2025-04-18T00:00:00"))
        assertEquals(60, tool.minutesBetween("2025-04-17T00:00:00", "2025-04-17T01:00:00"))
        assertEquals(30, tool.secondsBetween("2025-04-17T00:00:00", "2025-04-17T00:00:30"))
    }

    @Test
    fun `test millisBetween with large range`() {
        val millis = tool.millisBetween("2000-01-01T00:00:00", "2025-01-01T00:00:00")

        assertTrue(millis > 0)
        assertTrue(millis > 1000L * 60 * 60 * 24 * 365 * 10)
    }

    @Test
    fun `test durationBetween ISO format`() {
        assertEquals("PT2H", tool.durationBetween("2025-04-17T10:00:00", "2025-04-17T12:00:00"))
        assertEquals("PT-2H", tool.durationBetween("2025-04-17T12:00:00", "2025-04-17T10:00:00"))
        assertEquals("PT0S", tool.durationBetween("2025-04-17T10:00:00", "2025-04-17T10:00:00"))
    }

    @Test
    fun `test durationBreakdown normal and negative`() {
        val positive = tool.durationBreakdown("2025-04-16T10:00:00", "2025-04-17T13:45:20")

        assertEquals(1, positive["days"])
        assertEquals(3, positive["hours"])
        assertEquals(45, positive["minutes"])
        assertEquals(20, positive["seconds"])

        val zero = tool.durationBreakdown("2025-04-17T10:00:00", "2025-04-17T10:00:00")

        assertEquals(0, zero["days"])
        assertEquals(0, zero["hours"])
        assertEquals(0, zero["minutes"])
        assertEquals(0, zero["seconds"])
    }

    @Test
    fun `test invalid format throws exception`() {
        assertThrows(DateTimeParseException::class.java) {
            tool.addDays("not-a-date", 1)
        }

        assertThrows(DateTimeParseException::class.java) {
            tool.subtractHours("2025-13-01T10:00:00", 1)
        }

        assertThrows(DateTimeParseException::class.java) {
            tool.durationBetween("bad", "2025-04-17T10:00:00")
        }

        assertThrows(DateTimeParseException::class.java) {
            tool.durationBreakdown("2025-04-17T10:00:00", "broken")
        }
    }
}
