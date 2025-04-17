package mcp.datetime.tool

import org.springframework.ai.tool.annotation.Tool
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.WeekFields

@Component
class DateTimeWeekTool {

    private val isoWeekFields = WeekFields.ISO

    /**
     * Returns the ISO week number (1–53) of the year for the given ISO date.
     */
    @Tool(description = "Returns the ISO week number (1–53) of the year for the given ISO date")
    fun getWeekOfYear(dateIso: String): Int {
        val date = LocalDate.parse(dateIso)

        return date.get(isoWeekFields.weekOfWeekBasedYear())
    }

    /**
     * Returns the ISO week number (1–53) for today's date (UTC).
     */
    @Tool(description = "Returns the current ISO week number of the year")
    fun getCurrentWeekOfYear(): Int {
        return LocalDate.now(ZoneId.of("UTC")).get(isoWeekFields.weekOfWeekBasedYear())
    }

    /**
     * Returns the start date (Monday) of the week for the given ISO date.
     */
    @Tool(description = "Returns the start date (Monday) of the week for the given ISO date")
    fun getStartOfWeek(dateIso: String): String {
        val date = LocalDate.parse(dateIso)

        return date.with(isoWeekFields.dayOfWeek(), 1).toString()
    }

    /**
     * Returns the end date (Sunday) of the week for the given ISO date.
     */
    @Tool(description = "Returns the end date (Sunday) of the week for the given ISO date")
    fun getEndOfWeek(dateIso: String): String {
        val date = LocalDate.parse(dateIso)

        return date.with(isoWeekFields.dayOfWeek(), 7).toString()
    }

    /**
     * Returns true if the two given ISO dates fall in the same ISO week.
     */
    @Tool(description = "Returns true if the two dates fall in the same ISO week of the year")
    fun isSameWeek(dateIso1: String, dateIso2: String): Boolean {
        val d1 = LocalDate.parse(dateIso1)
        val d2 = LocalDate.parse(dateIso2)

        return d1.get(isoWeekFields.weekOfWeekBasedYear()) == d2.get(isoWeekFields.weekOfWeekBasedYear())
                && d1.get(isoWeekFields.weekBasedYear()) == d2.get(isoWeekFields.weekBasedYear())
    }

    /**
     * Returns the day of the week for the first day of the week that includes the given ISO date.
     */
    @Tool(description = "Returns the day of the week for the first day of the week that includes the given ISO date")
    fun getWeekdayOfFirstDay(dateIso: String): String {
        val date = LocalDate.parse(dateIso).with(isoWeekFields.dayOfWeek(), 1)

        return date.dayOfWeek.name
    }

    /**
     * Returns a list of ISO date strings representing all 7 days in the week of the given date.
     */
    @Tool(description = "Returns a list of ISO date strings for all days in the week that includes the given ISO date")
    fun getAllDatesOfWeek(dateIso: String): List<String> {
        val monday = LocalDate.parse(dateIso).with(isoWeekFields.dayOfWeek(), 1)

        return (0..6).map { monday.plusDays(it.toLong()).toString() }
    }

    /**
     * Returns the start date of a given ISO week and year.
     */
    @Tool(description = "Returns the start date (Monday) of the given ISO week number and year")
    fun getStartOfWeekByNumber(week: Int, year: Int): String {
        val jan4 = LocalDate.of(year, 1, 4)

        return jan4.with(isoWeekFields.weekOfWeekBasedYear(), week.toLong())
            .with(isoWeekFields.dayOfWeek(), 1)
            .toString()
    }

    /**
     * Returns the end date (Sunday) of a given ISO week and year.
     */
    @Tool(description = "Returns the end date (Sunday) of the given ISO week number and year")
    fun getEndOfWeekByNumber(week: Int, year: Int): String {
        val jan4 = LocalDate.of(year, 1, 4)

        return jan4.with(isoWeekFields.weekOfWeekBasedYear(), week.toLong())
            .with(isoWeekFields.dayOfWeek(), 7)
            .toString()
    }

    /**
     * Returns true if the given date is in the last ISO week of its month.
     */
    @Tool(description = "Returns true if the given ISO date falls in the last ISO week of its month")
    fun isLastWeekOfMonth(dateIso: String): Boolean {
        val date = LocalDate.parse(dateIso)
        val week = date.get(isoWeekFields.weekOfMonth())
        val lastWeek = date.withDayOfMonth(date.lengthOfMonth()).get(isoWeekFields.weekOfMonth())

        return week == lastWeek
    }

    /**
     * Returns true if the ISO week number of the given date is even.
     */
    @Tool(description = "Returns true if the ISO week number of the given ISO date is even")
    fun isEvenWeek(dateIso: String): Boolean {
        val weekNum = LocalDate.parse(dateIso).get(isoWeekFields.weekOfWeekBasedYear())

        return weekNum % 2 == 0
    }

    /**
     * Returns the ISO date string for a given ISO week number, year, and weekday index (0=Monday, 6=Sunday).
     */
    @Tool(description = "Returns the ISO date for a given ISO week number, year, and weekday index (0=Monday, 6=Sunday)")
    fun getDateFromWeekAndDay(week: Int, year: Int, weekdayIndex: Int): String {
        val base = LocalDate.of(year, 1, 4)

        return base.with(isoWeekFields.weekOfWeekBasedYear(), week.toLong())
            .with(isoWeekFields.dayOfWeek(), (weekdayIndex + 1).toLong())
            .toString()
    }
}
