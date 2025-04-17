package mcp.datetime.tool

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.format.DateTimeParseException
import java.util.Locale

class DateTimeFormatToolTest {

    private val tool = DateTimeFormatTool()

    @Test
    fun `test formatLocalDateTime`() {
        assertEquals("2025/04/17 10:00", tool.formatLocalDateTime("2025-04-17T10:00:00", "yyyy/MM/dd HH:mm"))
        assertThrows(DateTimeParseException::class.java) {
            tool.formatLocalDateTime("invalid", "yyyy/MM/dd")
        }
    }

    @Test
    fun `test formatLocalDate`() {
        assertEquals("04-17-2025", tool.formatLocalDate("2025-04-17", "MM-dd-yyyy"))
        assertThrows(DateTimeParseException::class.java) {
            tool.formatLocalDate("2025/04/17", "yyyy-MM-dd")
        }
    }

    @Test
    fun `test parseToIso`() {
        assertEquals("2025-04-17T10:00:00", tool.parseToIso("17-04-2025 10:00:00", "dd-MM-yyyy HH:mm:ss"))
        assertThrows(DateTimeParseException::class.java) {
            tool.parseToIso("17/04/2025", "yyyy-MM-dd")
        }
    }

    @Test
    fun `test safeParseToIso`() {
        assertEquals("2025-04-17T10:00:00", tool.safeParseToIso("17-04-2025 10:00:00", "dd-MM-yyyy HH:mm:ss"))
        assertNull(tool.safeParseToIso("invalid-date", "yyyy-MM-dd"))
    }

    @Test
    fun `test parseDayOfWeek`() {
        assertEquals("THURSDAY", tool.parseDayOfWeek("2025-04-17"))
        assertThrows(DateTimeParseException::class.java) {
            tool.parseDayOfWeek("2025/04/17")
        }
    }

    @Test
    fun `test isValidDateFormat`() {
        assertTrue(tool.isValidDateFormat("2025/04/17", "yyyy/MM/dd"))
        assertFalse(tool.isValidDateFormat("17-04-2025", "yyyy/MM/dd"))
        assertFalse(tool.isValidDateFormat("not-a-date", "yyyy-MM-dd"))
    }

    @Test
    fun `test formatWithLocale`() {
        val us = tool.formatWithLocale("2025-04-17T00:00:00", "MMMM", "en-US")
        val fr = tool.formatWithLocale("2025-04-17T00:00:00", "MMMM", "fr-FR")

        assertEquals("April", us)
        assertEquals("avril", fr.lowercase(Locale.getDefault())) // locale-sensitive
    }

    @Test
    fun `test performance with formatLocalDateTime and large input`() {
        val pattern = "yyyy-MM-dd'T'HH:mm:ss"
        val datetime = "2025-04-17T23:59:59"
        repeat(100_000) {
            val formatted = tool.formatLocalDateTime(datetime, pattern)
            assertEquals(datetime, formatted)
        }
    }
}
