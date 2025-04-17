package mcp.datetime.tool

import org.springframework.ai.tool.annotation.Tool
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.WeekFields

@Component
class DateTimeMonthTool {

    @Tool(description = "Returns the first day of the month for the given ISO date")
    fun getStartOfMonth(dateIso: String): String {
        return LocalDate.parse(dateIso).withDayOfMonth(1).toString()
    }

    @Tool(description = "Returns the last day of the month for the given ISO date")
    fun getEndOfMonth(dateIso: String): String {
        val date = LocalDate.parse(dateIso)

        return date.withDayOfMonth(date.lengthOfMonth()).toString()
    }

    @Tool(description = "Returns the first day of the current month (UTC)")
    fun getStartOfCurrentMonth(): String {
        return LocalDate.now(ZoneId.of("UTC")).withDayOfMonth(1).toString()
    }

    @Tool(description = "Returns the last day of the current month (UTC)")
    fun getEndOfCurrentMonth(): String {
        val today = LocalDate.now(ZoneId.of("UTC"))

        return today.withDayOfMonth(today.lengthOfMonth()).toString()
    }

    @Tool(description = "Returns true if the given ISO date is the last day of its month")
    fun isEndOfMonth(dateIso: String): Boolean {
        val date = LocalDate.parse(dateIso)

        return date.dayOfMonth == date.lengthOfMonth()
    }

    @Tool(description = "Returns a list of all ISO dates in the month of the given date")
    fun getAllDatesInMonth(dateIso: String): List<String> {
        val date = LocalDate.parse(dateIso)
        val length = date.lengthOfMonth()
        val firstDay = date.withDayOfMonth(1)

        return (0 until length).map { firstDay.plusDays(it.toLong()).toString() }
    }

    @Tool(description = "Returns the number of days in the month of the given ISO date")
    fun getLengthOfMonth(dateIso: String): Int {
        return LocalDate.parse(dateIso).lengthOfMonth()
    }

    @Tool(description = "Returns the number of ISO weeks in the month of the given ISO date")
    fun getWeekCountInMonth(dateIso: String): Int {
        val date = LocalDate.parse(dateIso)
        val startWeek = date.withDayOfMonth(1).get(WeekFields.ISO.weekOfWeekBasedYear())
        val endWeek = date.withDayOfMonth(date.lengthOfMonth()).get(WeekFields.ISO.weekOfWeekBasedYear())

        return endWeek - startWeek + 1
    }

    @Tool(description = "Returns the last day of the previous month for the given ISO date")
    fun getEndOfPreviousMonth(dateIso: String): String {
        val date = LocalDate.parse(dateIso).minusMonths(1)

        return date.withDayOfMonth(date.lengthOfMonth()).toString()
    }

    @Tool(description = "Returns the first day of the next month for the given ISO date")
    fun getStartOfNextMonth(dateIso: String): String {
        return LocalDate.parse(dateIso).plusMonths(1).withDayOfMonth(1).toString()
    }

    @Tool(description = "Returns the ISO date of a specific day in the same month as the given date (e.g., 15th)")
    fun getSpecificDayInMonth(dateIso: String, day: Int): String {
        val date = LocalDate.parse(dateIso)
        val validDay = day.coerceIn(1, date.lengthOfMonth())

        return date.withDayOfMonth(validDay).toString()
    }
}
