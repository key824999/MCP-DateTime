package mcp.datetime.tool

import org.springframework.ai.tool.annotation.Tool
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

@Component
class DateTimeFormatTool {

    @Tool(description = "Formats the given LocalDateTime ISO string using the given pattern (e.g., yyyy/MM/dd HH:mm)")
    fun formatLocalDateTime(datetimeIso: String, pattern: String): String {
        val dateTime = LocalDateTime.parse(datetimeIso)

        return dateTime.format(DateTimeFormatter.ofPattern(pattern))
    }

    @Tool(description = "Formats the given LocalDate ISO string using the given pattern (e.g., MM-dd-yyyy)")
    fun formatLocalDate(dateIso: String, pattern: String): String {
        val date = LocalDate.parse(dateIso)

        return date.format(DateTimeFormatter.ofPattern(pattern))
    }

    @Tool(description = "Parses the given date-time string with the given pattern into ISO-8601 format")
    fun parseToIso(datetime: String, pattern: String): String {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        val dateTime = LocalDateTime.parse(datetime, formatter)

        return dateTime.format(DateTimeFormatter.ISO_DATE_TIME)
    }

    @Tool(description = "Safely parses the date-time string using the given pattern. Returns null if parsing fails.")
    fun safeParseToIso(datetime: String, pattern: String): String? {
        return try {
            this.parseToIso(datetime, pattern)
        } catch (e: DateTimeParseException) {
            null
        }
    }

    @Tool(description = "Parses the given ISO date string to a LocalDate and returns the day of week (e.g., MONDAY)")
    fun parseDayOfWeek(dateIso: String): String {
        val date = LocalDate.parse(dateIso)

        return date.dayOfWeek.name
    }

    @Tool(description = "Checks if the given date string is valid with the given pattern (e.g., 2025/04/17 with yyyy/MM/dd)")
    fun isValidDateFormat(dateStr: String, pattern: String): Boolean {
        return try {
            LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern))
            true
        } catch (e: Exception) {
            false
        }
    }

    @Tool(description = "Formats the given ISO date-time string using the given pattern and locale (e.g., en-US, fr-FR)")
    fun formatWithLocale(dateTimeIso: String, pattern: String, localeTag: String): String {
        val locale = Locale.forLanguageTag(localeTag)
        val formatter = DateTimeFormatter.ofPattern(pattern, locale)
        val dateTime = LocalDateTime.parse(dateTimeIso)

        return dateTime.format(formatter)
    }
}
