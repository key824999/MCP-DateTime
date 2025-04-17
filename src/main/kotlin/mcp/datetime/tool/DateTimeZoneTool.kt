package mcp.datetime.tool

import org.springframework.ai.tool.annotation.Tool
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.zone.ZoneRulesException

@Component
class DateTimeZoneTool {

    // ===== Zone Conversion =====

    /**
     * Converts an ISO-8601 date-time string from one time zone to another.
     */
    @Tool(description = "Converts the given ISO date-time from the source time zone to the target time zone")
    fun convertZone(datetimeIso: String, fromZone: String, toZone: String): String {
        val sourceZoned = LocalDateTime.parse(datetimeIso).atZone(ZoneId.of(fromZone))
        return sourceZoned.withZoneSameInstant(ZoneId.of(toZone))
            .format(DateTimeFormatter.ISO_DATE_TIME)
    }

    /**
     * Converts the given ISO date-time from the source zone to UTC.
     */
    @Tool(description = "Converts the given ISO date-time from the source time zone to UTC")
    fun toUtc(datetimeIso: String, fromZone: String): String {
        return convertZone(datetimeIso, fromZone, "UTC")
    }

    /**
     * Converts the current UTC time to the given time zone.
     */
    @Tool(description = "Converts the current UTC time to the given time zone (ISO-8601 format)")
    fun utcNowToZone(zoneId: String): String {
        return ZonedDateTime.now(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of(zoneId))
            .format(DateTimeFormatter.ISO_DATE_TIME)
    }

    // ===== Zone Info =====

    /**
     * Returns the current UTC offset of the given time zone (e.g., +09:00).
     */
    @Tool(description = "Returns the current UTC offset of the given time zone (e.g., +09:00)")
    fun getZoneOffset(zoneId: String): String {
        val now = ZonedDateTime.now(ZoneId.of(zoneId))
        return now.offset.id
    }

    /**
     * Checks whether the given time zone ID is valid and recognized by the system.
     */
    @Tool(description = "Checks whether the given time zone ID is valid")
    fun isValidZoneId(zoneId: String): Boolean {
        return try {
            ZoneId.of(zoneId)
            true
        } catch (e: ZoneRulesException) {
            false
        }
    }

    /**
     * Returns true if the given time zone is currently observing daylight saving time (DST).
     */
    @Tool(description = "Returns true if the given time zone is currently in daylight saving time (DST)")
    fun isDstActive(zoneId: String): Boolean {
        val now = ZonedDateTime.now(ZoneId.of(zoneId))
        return now.zone.rules.isDaylightSavings(now.toInstant())
    }

    /**
     * Returns the hour difference between two time zones at the current moment.
     */
    @Tool(description = "Returns the hour difference between two time zones at the current time")
    fun zoneOffsetDiff(zoneId1: String, zoneId2: String): Long {
        val now = Instant.now()
        val offset1 = ZoneId.of(zoneId1).rules.getOffset(now)
        val offset2 = ZoneId.of(zoneId2).rules.getOffset(now)

        return ((offset1.totalSeconds - offset2.totalSeconds) / 3600).toLong()
    }

    // ===== Current Time Per Zone =====

    /**
     * Returns the current local time (HH:mm:ss) in the given time zone.
     */
    @Tool(description = "Returns the current local time in the given time zone (HH:mm:ss)")
    fun timeInZone(zoneId: String): String {
        return LocalTime.now(ZoneId.of(zoneId)).toString()
    }
}
