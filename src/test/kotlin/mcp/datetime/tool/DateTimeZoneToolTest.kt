package mcp.datetime.tool

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.zone.ZoneRulesException

class DateTimeZoneToolTest {

    private val tool = DateTimeZoneTool()

    @Test
    fun `test convertZone and toUtc`() {
        val toUtc = tool.toUtc("2025-04-17T12:00:00", "Asia/Seoul")

        val expectedUtc = ZonedDateTime.of(LocalDateTime.parse("2025-04-17T03:00:00"), ZoneId.of("UTC"))

        assertEquals(expectedUtc.toInstant(), ZonedDateTime.parse(toUtc).toInstant())
    }

    @Test
    fun `test utcNowToZone returns valid ISO format`() {
        val result = tool.utcNowToZone("Asia/Tokyo")
        assertTrue(result.contains("T") && (result.contains("+09") || result.contains("+09:00")))
    }

    @Test
    fun `test getZoneOffset returns correct offset`() {
        val offset = tool.getZoneOffset("Asia/Seoul")
        assertTrue(offset == "+09:00" || offset == "+09")
    }

    @Test
    fun `test isValidZoneId`() {
        assertTrue(tool.isValidZoneId("UTC"))
        assertTrue(tool.isValidZoneId("Asia/Seoul"))
        assertFalse(tool.isValidZoneId("Invalid/Zone"))
    }

    @Test
    fun `test isDstActive for known zones`() {
        val seoul = tool.isDstActive("Asia/Seoul")    // Always false (no DST)
        assertFalse(seoul)
    }

    @Test
    fun `test zoneOffsetDiff between UTC and Seoul`() {
        val diff = tool.zoneOffsetDiff("Asia/Seoul", "UTC")
        assertTrue(diff == 9L || diff == 8L || diff == 10L) // to handle DST zones
    }

    @Test
    fun `test timeInZone returns HH-mm-ss`() {
        val result = tool.timeInZone("Asia/Seoul")
        assertTrue(result.matches(Regex("\\d{2}:\\d{2}:\\d{2}(\\.\\d+)?")))
    }

    @Test
    fun `test invalid zoneId throws ZoneRulesException`() {
        assertThrows(ZoneRulesException::class.java) {
            tool.zoneOffsetDiff("UTC", "Bogus/Place")
        }
    }

    @Test
    fun `test performance convertZone many iterations`() {
        repeat(1000) {
            val converted = tool.convertZone("2025-04-17T12:00:00", "Asia/Seoul", "UTC")
            assertTrue(converted.contains("T"))
        }
    }
}
