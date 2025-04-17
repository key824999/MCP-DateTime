package mcp.datetime.tool

import org.springframework.ai.tool.annotation.Tool
import org.springframework.stereotype.Component
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.Year
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@Component
class DateTimeCompareTool {

    // ===== Basic date comparisons =====

    @Tool(description = "Returns true if the first ISO date is before the second one")
    fun isBefore(date1: String, date2: String): Boolean {
        return LocalDate.parse(date1).isBefore(LocalDate.parse(date2))
    }

    @Tool(description = "Returns true if the first ISO date is after the second one")
    fun isAfter(date1: String, date2: String): Boolean {
        return LocalDate.parse(date1).isAfter(LocalDate.parse(date2))
    }

    @Tool(description = "Returns true if two ISO dates are the same day")
    fun isSameDay(date1: String, date2: String): Boolean {
        return LocalDate.parse(date1).isEqual(LocalDate.parse(date2))
    }

    @Tool(description = "Returns true if the given ISO date is between start and end (inclusive)")
    fun isBetween(target: String, start: String, end: String): Boolean {
        val t = LocalDate.parse(target)
        val s = LocalDate.parse(start)
        val e = LocalDate.parse(end)

        return !t.isBefore(s) && !t.isAfter(e)
    }

    // ===== Date field comparisons =====

    @Tool(description = "Returns true if two ISO dates are in the same month and year")
    fun isSameMonth(date1: String, date2: String): Boolean {
        val d1 = LocalDate.parse(date1)
        val d2 = LocalDate.parse(date2)

        return d1.year == d2.year && d1.month == d2.month
    }

    @Tool(description = "Returns true if two ISO dates are in the same year")
    fun isSameYear(date1: String, date2: String): Boolean {
        return LocalDate.parse(date1).year == LocalDate.parse(date2).year
    }

    // ===== Current time logic =====

    @Tool(description = "Returns true if the given ISO date is today in UTC")
    fun isToday(dateIso: String): Boolean {
        return LocalDate.parse(dateIso).isEqual(LocalDate.now(ZoneId.of("UTC")))
    }

    @Tool(description = "Returns true if the given ISO date is in the future (after today, UTC)")
    fun isFutureDate(dateIso: String): Boolean {
        return LocalDate.parse(dateIso).isAfter(LocalDate.now(ZoneId.of("UTC")))
    }

    @Tool(description = "Returns true if the given ISO date is in the past (before today, UTC)")
    fun isPastDate(dateIso: String): Boolean {
        return LocalDate.parse(dateIso).isBefore(LocalDate.now(ZoneId.of("UTC")))
    }

    @Tool(description = "Returns true if the current UTC time is before 12:00 (AM)")
    fun isAmNow(): Boolean {
        return LocalTime.now(ZoneId.of("UTC")).hour < 12
    }

    @Tool(description = "Returns true if the current UTC time is 12:00 or later (PM)")
    fun isPmNow(): Boolean {
        return LocalTime.now(ZoneId.of("UTC")).hour >= 12
    }

    // ===== Calendar logic =====

    @Tool(description = "Returns true if the given year is a leap year")
    fun isLeapYear(year: Int): Boolean {
        return Year.of(year).isLeap
    }

    @Tool(description = "Returns true if the given date string is valid according to the given pattern (e.g., yyyy-MM-dd)")
    fun isValidDate(dateStr: String, pattern: String): Boolean {
        return try {
            LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern))
            true
        } catch (e: DateTimeParseException) {
            false
        }
    }

    @Tool(description = "Returns true if the given ISO date is a weekday (Mon-Fri). Currently only supports weekends, not holidays.")
    fun isBusinessDay(dateIso: String): Boolean {
        val day = LocalDate.parse(dateIso).dayOfWeek

        return day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY
    }

    @Tool(description = "Returns true if the given ISO date is a weekend (Saturday or Sunday)")
    fun isWeekend(dateIso: String): Boolean {
        val day = LocalDate.parse(dateIso).dayOfWeek

        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY
    }
}
