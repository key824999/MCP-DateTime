package mcp.datetime.tool

import org.springframework.ai.tool.annotation.Tool
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

@Component
class DateTimeCalcTool {

    // ===== Date arithmetic =====

    /**
     * Adds a number of days to the given ISO-8601 date string.
     */
    @Tool(description = "Adds the given number of days to the ISO date string")
    fun addDays(dateIso: String, days: Long): String {
        return LocalDate.parse(dateIso).plusDays(days).toString()
    }

    /**
     * Adds a number of months to the given ISO-8601 date string.
     */
    @Tool(description = "Adds the given number of months to the ISO date string")
    fun addMonths(dateIso: String, months: Long): String {
        return LocalDate.parse(dateIso).plusMonths(months).toString()
    }

    /**
     * Adds a number of years to the given ISO-8601 date string.
     */
    @Tool(description = "Adds the given number of years to the ISO date string")
    fun addYears(dateIso: String, years: Long): String {
        return LocalDate.parse(dateIso).plusYears(years).toString()
    }

    /**
     * Subtracts a number of days from the given ISO-8601 date string.
     */
    @Tool(description = "Subtracts the given number of days from the ISO date string")
    fun subtractDays(dateIso: String, days: Long): String {
        return LocalDate.parse(dateIso).minusDays(days).toString()
    }

    // ===== Time arithmetic =====

    /**
     * Subtracts hours from the given ISO-8601 date-time string.
     */
    @Tool(description = "Subtracts the given number of hours from the ISO date-time string")
    fun subtractHours(datetimeIso: String, hours: Long): String {
        return LocalDateTime.parse(datetimeIso).minusHours(hours).toString()
    }

    /**
     * Adds minutes to the given ISO-8601 date-time string.
     */
    @Tool(description = "Adds the given number of minutes to the ISO date-time string")
    fun addMinutes(datetimeIso: String, minutes: Long): String {
        return LocalDateTime.parse(datetimeIso).plusMinutes(minutes).toString()
    }

    /**
     * Subtracts seconds from the given ISO-8601 date-time string.
     */
    @Tool(description = "Subtracts the given number of seconds from the ISO date-time string")
    fun subtractSeconds(datetimeIso: String, seconds: Long): String {
        return LocalDateTime.parse(datetimeIso).minusSeconds(seconds).toString()
    }

    // ===== Day start/end =====

    /**
     * Returns the start of the day (00:00:00) for a given ISO-8601 date.
     */
    @Tool(description = "Returns the start of day (00:00:00) for the given ISO date")
    fun withStartOfDay(dateIso: String): String {
        return LocalDate.parse(dateIso).atStartOfDay().toString()
    }

    /**
     * Returns the end of the day (23:59:59.999999999) for a given ISO-8601 date.
     */
    @Tool(description = "Returns the end of day (23:59:59.999999999) for the given ISO date")
    fun withEndOfDay(dateIso: String): String {
        return LocalDate.parse(dateIso).atTime(LocalTime.MAX).toString()
    }

    // ===== Difference calculation =====

    /**
     * Calculates the number of days between two ISO-8601 dates.
     */
    @Tool(description = "Calculates the number of days between two ISO dates")
    fun daysBetween(startDateIso: String, endDateIso: String): Long {
        return ChronoUnit.DAYS.between(LocalDate.parse(startDateIso), LocalDate.parse(endDateIso))
    }

    /**
     * Calculates the number of hours between two ISO-8601 date-time values.
     */
    @Tool(description = "Calculates the number of hours between two ISO date-times")
    fun hoursBetween(startDateTimeIso: String, endDateTimeIso: String): Long {
        return ChronoUnit.HOURS.between(LocalDateTime.parse(startDateTimeIso), LocalDateTime.parse(endDateTimeIso))
    }

    /**
     * Calculates the number of minutes between two ISO-8601 date-time values.
     */
    @Tool(description = "Calculates the number of minutes between two ISO date-times")
    fun minutesBetween(startDateTimeIso: String, endDateTimeIso: String): Long {
        return ChronoUnit.MINUTES.between(LocalDateTime.parse(startDateTimeIso), LocalDateTime.parse(endDateTimeIso))
    }

    /**
     * Calculates the number of seconds between two ISO-8601 date-time values.
     */
    @Tool(description = "Calculates the number of seconds between two ISO date-times")
    fun secondsBetween(startDateTimeIso: String, endDateTimeIso: String): Long {
        return ChronoUnit.SECONDS.between(LocalDateTime.parse(startDateTimeIso), LocalDateTime.parse(endDateTimeIso))
    }

    /**
     * Calculates the number of milliseconds between two ISO-8601 date-time values.
     */
    @Tool(description = "Calculates the number of milliseconds between two ISO date-times")
    fun millisBetween(startDateTimeIso: String, endDateTimeIso: String): Long {
        val start = LocalDateTime.parse(startDateTimeIso).atZone(ZoneId.of("UTC")).toInstant().toEpochMilli()
        val end = LocalDateTime.parse(endDateTimeIso).atZone(ZoneId.of("UTC")).toInstant().toEpochMilli()
        return end - start
    }

    /**
     * Returns the duration between two ISO-8601 date-times as an ISO-8601 duration string (e.g., PT5H30M).
     */
    @Tool(description = "Returns the duration between two ISO date-times as a string (e.g., PT5H30M)")
    fun durationBetween(startDateTimeIso: String, endDateTimeIso: String): String {
        return Duration.between(LocalDateTime.parse(startDateTimeIso), LocalDateTime.parse(endDateTimeIso)).toString()
    }

    /**
     * Returns a breakdown of duration (days, hours, minutes, seconds) between two ISO-8601 date-times.
     */
    @Tool(description = "Returns a breakdown of the duration between two ISO date-times (days, hours, minutes, seconds)")
    fun durationBreakdown(startDateTimeIso: String, endDateTimeIso: String): Map<String, Long> {
        val duration = Duration.between(
            LocalDateTime.parse(startDateTimeIso),
            LocalDateTime.parse(endDateTimeIso)
        )

        return mapOf(
            "days" to duration.toDays(),
            "hours" to duration.toHours() % 24,
            "minutes" to duration.toMinutes() % 60,
            "seconds" to duration.seconds % 60
        )
    }
}
