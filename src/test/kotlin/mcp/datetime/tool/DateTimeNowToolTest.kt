package mcp.datetime.tool

import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.format.DateTimeFormatter
import java.util.regex.Pattern

class DateTimeNowToolTest {

    private val tool = DateTimeNowTool()

    @Test
    fun `test nowUtcIso returns valid ISO datetime with Z`() {
        val result = tool.nowUtcIso()
        assertTrue(result.endsWith("Z"))
        assertDoesNotThrow {
            DateTimeFormatter.ISO_DATE_TIME.parse(result)
        }
    }

    @Test
    fun `test nowInZone with valid and invalid zone`() {
        val seoulTime = tool.nowInZone("Asia/Seoul")
        assertTrue(seoulTime.contains("T"))
        assertTrue(seoulTime.endsWith("+09:00") || seoulTime.contains("+09"))

        assertThrows(Exception::class.java) {
            tool.nowInZone("Invalid/Zone")
        }
    }

    @Test
    fun `test todayIso and todayInZone`() {
        val todayUtc = tool.todayIso()
        val todayInKst = tool.todayInZone("Asia/Seoul")

        assertTrue(Pattern.matches("\\d{4}-\\d{2}-\\d{2}", todayUtc))
        assertTrue(Pattern.matches("\\d{4}-\\d{2}-\\d{2}", todayInKst))

        assertThrows(Exception::class.java) {
            tool.todayInZone("Not_A_Zone")
        }
    }

    @Test
    fun `test currentEpochMillis and currentEpochSeconds`() {
        val millis = tool.currentEpochMillis()
        val seconds = tool.currentEpochSeconds()

        assertTrue(millis > 1_000_000_000_000) // after ~2001
        assertTrue(seconds > 1_000_000_000)   // after ~2001
    }

    @Test
    fun `test nowFormatted returns pattern formatted string`() {
        val pattern = "yyyy/MM/dd HH:mm"
        val formatted = tool.nowFormatted(pattern)

        assertTrue(Pattern.matches("\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}", formatted))

        assertThrows(IllegalArgumentException::class.java) {
            tool.nowFormatted("invalid-pattern")
        }
    }

    @Test
    fun `test nowUtcZoned contains zone and datetime`() {
        val result = tool.nowUtcZoned()
        assertTrue(result.contains("T") && result.contains("Z"))
    }

    @Test
    fun `test currentTimePartsUtc contains expected keys`() {
        val parts = tool.currentTimePartsUtc()
        assertTrue(parts.containsKey("hour"))
        assertTrue(parts.containsKey("minute"))
        assertTrue(parts.containsKey("second"))
        assertTrue(parts.containsKey("dayOfWeek"))

        assertTrue(parts["hour"] is Int)
        assertTrue(parts["minute"] is Int)
        assertTrue(parts["second"] is Int)
        assertTrue(parts["dayOfWeek"] is String)
    }

    @Test
    fun `test currentUtcOffset returns valid format`() {
        val offset = tool.currentUtcOffset("Asia/Seoul")
        assertTrue(offset.matches(Regex("[+-]\\d{2}:\\d{2}")))

        assertThrows(Exception::class.java) {
            tool.currentUtcOffset("Fake/Zone")
        }
    }

    @Test
    fun `test weekOfYearUtc returns 1~53`() {
        val week = tool.weekOfYearUtc()
        assertTrue(week in 1..53)
    }

    @Test
    fun `test dayOfYearUtc returns 1~366`() {
        val day = tool.dayOfYearUtc()
        assertTrue(day in 1..366)
    }

    @Test
    fun `test availableZoneIds contains known zones and returns many`() {
        val zones = tool.availableZoneIds()
        assertTrue(zones.contains("UTC"))
        assertTrue(zones.contains("Asia/Seoul"))
        assertTrue(zones.size > 100)
    }

    @Test
    fun `test nowUtcIso multiple calls are unique within millis`() {
        val set = mutableSetOf<String>()
        repeat(10) {
            set.add(tool.nowUtcIso())
            Thread.sleep(1)
        }
        assertTrue(set.size >= 5) // should be at least some variation
    }
}
