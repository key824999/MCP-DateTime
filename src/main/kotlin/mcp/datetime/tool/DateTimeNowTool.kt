package mcp.datetime.tool

import org.springframework.ai.tool.annotation.Tool
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Component
class DateTimeNowTool {

    @Tool(description = "Returns the current time in UTC in ISO-8601 format (e.g., 2025-04-17T02:25:00Z)")
    fun nowUtcIso(): String {
        return OffsetDateTime.now(ZoneOffset.UTC).toString()
    }

    @Tool(description = "Returns the current time in the specified time zone in ISO-8601 format (e.g., Asia/Seoul)")
    fun nowInZone(zoneId: String): String {
        return ZonedDateTime.now(ZoneId.of(zoneId)).format(DateTimeFormatter.ISO_DATE_TIME)
    }

    @Tool(description = "Returns the current date in UTC in ISO format (yyyy-MM-dd)")
    fun todayIso(): String {
        return LocalDate.now(ZoneId.of("UTC")).toString()
    }

    @Tool(description = "Returns the current date in the given time zone in ISO format (yyyy-MM-dd)")
    fun todayInZone(zoneId: String): String {
        return LocalDate.now(ZoneId.of(zoneId)).toString()
    }

    @Tool(description = "Returns the current epoch time in milliseconds")
    fun currentEpochMillis(): Long {
        return Instant.now().toEpochMilli()
    }

    @Tool(description = "Returns the current epoch time in seconds")
    fun currentEpochSeconds(): Long {
        return Instant.now().epochSecond
    }

    @Tool(description = "Returns the current date and time formatted using the given pattern (e.g., yyyy/MM/dd HH:mm)")
    fun nowFormatted(pattern: String): String {
        return LocalDateTime.now(ZoneId.of("UTC")).format(DateTimeFormatter.ofPattern(pattern))
    }

    @Tool(description = "Returns the current UTC ZonedDateTime object as a string")
    fun nowUtcZoned(): String {
        return ZonedDateTime.now(ZoneId.of("UTC")).toString()
    }

    @Tool(description = "Returns current UTC time components (hour, minute, second, dayOfWeek)")
    fun currentTimePartsUtc(): Map<String, Any> {
        val now = ZonedDateTime.now(ZoneId.of("UTC"))

        return mapOf(
            "hour" to now.hour,
            "minute" to now.minute,
            "second" to now.second,
            "dayOfWeek" to now.dayOfWeek.name
        )
    }

    @Tool(description = "Returns the UTC offset for the given zone ID (e.g., +09:00)")
    fun currentUtcOffset(zoneId: String): String {
        val now = ZonedDateTime.now(ZoneId.of(zoneId))

        return now.offset.id
    }

    @Tool(description = "Returns the current ISO week number of the year (UTC)")
    fun weekOfYearUtc(): Int {
        val now = ZonedDateTime.now(ZoneId.of("UTC"))

        return now.get(java.time.temporal.IsoFields.WEEK_OF_WEEK_BASED_YEAR)
    }

    @Tool(description = "Returns the current day of the year (1 to 365 or 366) in UTC")
    fun dayOfYearUtc(): Int {
        return LocalDate.now(ZoneId.of("UTC")).dayOfYear
    }

    @Tool(description = "Returns a list of all available zone IDs supported by the JVM")
    fun availableZoneIds(): List<String> {
        return ZoneId.getAvailableZoneIds().sorted()
    }
}
