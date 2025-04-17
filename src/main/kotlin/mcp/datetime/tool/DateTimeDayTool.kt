package mcp.datetime.tool

import org.springframework.ai.tool.annotation.Tool
import org.springframework.stereotype.Component
import java.time.DayOfWeek
import java.time.LocalDate

@Component
class DateTimeDayTool {

    /**
     * Returns the day of the week (e.g., MONDAY) for the given ISO date.
     */
    @Tool(description = "Returns the day of the week (e.g., MONDAY) for the given ISO date")
    fun getDayOfWeek(dateIso: String): String {
        return LocalDate.parse(dateIso).dayOfWeek.name
    }

    /**
     * Returns today's day of the week in UTC (e.g., WEDNESDAY).
     */
    @Tool(description = "Returns the current day of the week (UTC) as a string (e.g., TUESDAY)")
    fun getTodayDayOfWeek(): String {
        return LocalDate.now().dayOfWeek.name
    }

    /**
     * Returns the day index of the given ISO date (0=Monday, 6=Sunday).
     */
    @Tool(description = "Returns the index of the day of week for the given ISO date (0=Monday, 6=Sunday)")
    fun getDayOfWeekIndex(dateIso: String): Int {
        return LocalDate.parse(dateIso).dayOfWeek.ordinal
    }

    /**
     * Returns today's day index (0=Monday, 6=Sunday).
     */
    @Tool(description = "Returns the index of today's day of week (0=Monday, 6=Sunday)")
    fun getTodayDayIndex(): Int {
        return LocalDate.now().dayOfWeek.ordinal
    }

    /**
     * Returns true if the given day name is a weekend (Saturday or Sunday).
     */
    @Tool(description = "Returns true if the given day name is a weekend (SATURDAY or SUNDAY)")
    fun isWeekendDay(dayName: String): Boolean {
        return this.normalizeDayName(dayName) in listOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
    }

    /**
     * Returns true if the given ISO date falls on a weekend (Saturday or Sunday).
     */
    @Tool(description = "Returns true if the given ISO date falls on a weekend (Saturday or Sunday)")
    fun isWeekendDate(dateIso: String): Boolean {
        val day = LocalDate.parse(dateIso).dayOfWeek

        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY
    }

    /**
     * Returns true if the given day name is a weekday (Monday to Friday).
     */
    @Tool(description = "Returns true if the given day name is a weekday (Monday to Friday)")
    fun isWeekday(dayName: String): Boolean {
        val day = normalizeDayName(dayName)

        return day in DayOfWeek.MONDAY..DayOfWeek.FRIDAY
    }

    /**
     * Converts a day name (e.g., Mon, mon, MONDAY) to a DayOfWeek enum.
     */
    @Tool(description = "Normalizes a day name string to a standard DayOfWeek enum (e.g., Mon -> MONDAY)")
    fun normalizeDayName(dayName: String): DayOfWeek {
        return DayOfWeek.valueOf(dayName.trim().uppercase())
    }

    /**
     * Converts a day name (e.g., MONDAY) to its index (0=Monday, 6=Sunday).
     */
    @Tool(description = "Converts a day name to its index (0=Monday, 6=Sunday)")
    fun dayNameToIndex(dayName: String): Int {
        return normalizeDayName(dayName).ordinal
    }

    /**
     * Converts an index (0=Monday, 6=Sunday) to the corresponding day name.
     */
    @Tool(description = "Converts an index (0=Monday, 6=Sunday) to the corresponding day name")
    fun indexToDayName(index: Int): String {
        val normalizedIndex = ((index % 7) + 7) % 7

        return DayOfWeek.of(normalizedIndex + 1).name
    }

    /**
     * Returns the day of the week that is N days after today (e.g., MONDAY).
     */
    @Tool(description = "Returns the day of the week N days after today (e.g., MONDAY)")
    fun getDayAfterToday(offset: Long): String {
        return LocalDate.now().plusDays(offset).dayOfWeek.name
    }
}
