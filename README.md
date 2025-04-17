# MCP-DateTime

MCP-DateTime is a Kotlin-based [Model Context Protocol (MCP)](https://modelcontextprotocol.io) compatible tool that provides a comprehensive suite of time, date, and timezone utilities.

It is designed to be used by any AI agent or tool host that supports the MCP specification.

### ⚡ Reliable, AI-friendly, and Precise

MCP-DateTime is engineered for:

- **Reliability**: Predictable behavior with full edge case coverage
- **Precision**: ISO-8601 compliant datetime formatting and parsing
- **AI usability**: All methods are well-named and MCP-discoverable for seamless integration

---

## Installation

> Please refer to the [llms-install.md](./llms-install.md) file for step-by-step installation instructions for Claude Desktop, MCP CLI, and more.

---

## Features by Category

### ⏰ DateTimeNowTool
- `nowUtcIso()` – Current UTC timestamp in ISO-8601 with `Z`
- `nowInZone(zoneId)` – Current timestamp in the given timezone
- `todayIso()` – Today's date in `yyyy-MM-dd` format (UTC)
- `todayInZone(zoneId)` – Today's date in given zone
- `nowFormatted(pattern)` – Formatted current datetime
- `nowUtcZoned()` – ZonedDateTime ISO-8601 UTC string
- `currentEpochMillis()` / `currentEpochSeconds()` – Epoch time
- `currentTimePartsUtc()` – Hour, minute, second, dayOfWeek breakdown
- `currentUtcOffset(zoneId)` – UTC offset string (e.g., `+09:00`)
- `weekOfYearUtc()` / `dayOfYearUtc()` – Week/day numbers
- `availableZoneIds()` – List of all zone IDs

### ➖ DateTimeCalcTool
- `addDays(dateIso, days)`, `subtractDays(dateIso, days)`
- `addMonths(dateIso, months)`, `addYears(dateIso, years)`
- `addMinutes(datetimeIso, minutes)`, `subtractHours(datetimeIso, hours)`, `subtractSeconds(datetimeIso, seconds)`
- `withStartOfDay(dateIso)`, `withEndOfDay(dateIso)`
- `daysBetween(startDateIso, endDateIso)`, `hoursBetween(startDateTimeIso, endDateTimeIso)`
- `minutesBetween(startDateTimeIso, endDateTimeIso)`, `secondsBetween(startDateTimeIso, endDateTimeIso)`, `millisBetween(startDateTimeIso, endDateTimeIso)`
- `durationBetween(startDateTimeIso, endDateTimeIso)` – ISO duration string
- `durationBreakdown(startDateTimeIso, endDateTimeIso)` – Map of days, hours, minutes, seconds

### ⚖ DateTimeCompareTool
- `isBefore(date1, date2)`, `isAfter(date1, date2)`
- `isSameDay(date1, date2)`, `isBetween(target, start, end)`
- `isSameMonth(date1, date2)`, `isSameYear(date1, date2)`
- `isToday(dateIso)`, `isFutureDate(dateIso)`, `isPastDate(dateIso)`
- `isLeapYear(year)`
- `isWeekend(dateIso)`, `isWeekday(dateIso)`
- `isWeekendDay(dayName)`, `isBusinessDay(dateIso)`
- `isAmNow()`, `isPmNow()`
- `isValidDateFormat(dateStr, pattern)`

### 📅 DateTimeDayTool
- `getDayOfWeek(dateIso)`, `getTodayDayOfWeek()`
- `getDayOfWeekIndex(dateIso)`, `getTodayDayIndex()`
- `isWeekendDay(dayName)`, `isWeekday(dayName)`
- `isWeekendDate(dateIso)`
- `normalizeDayName(dayName)`, `dayNameToIndex(dayName)`, `indexToDayName(index)`
- `getDayAfterToday(offset)`

### 🗓 DateTimeMonthTool
- `getStartOfMonth(dateIso)`, `getEndOfMonth(dateIso)`
- `getStartOfCurrentMonth()`, `getEndOfCurrentMonth()`
- `getStartOfNextMonth(dateIso)`, `getEndOfPreviousMonth(dateIso)`
- `getSpecificDayInMonth(dateIso, day)`
- `getLengthOfMonth(dateIso)`
- `getWeekCountInMonth(dateIso)`
- `getAllDatesInMonth(dateIso)`
- `isEndOfMonth(dateIso)`
- `isSameMonth(date1, date2)`

### 🧾 DateTimeFormatTool
- `formatLocalDate(dateIso, pattern)`
- `formatLocalDateTime(datetimeIso, pattern)`
- `formatWithLocale(dateTimeIso, pattern, localeTag)`
- `parseToIso(datetime, pattern)`, `safeParseToIso(datetime, pattern)`
- `parseDayOfWeek(dateIso)`
- `isValidDateFormat(dateStr, pattern)`

### 📆 DateTimeWeekTool
- `getStartOfWeek(dateIso)`, `getEndOfWeek(dateIso)`
- `getStartOfWeekByNumber(week, year)`, `getEndOfWeekByNumber(week, year)`
- `getDateFromWeekAndDay(week, year, weekdayIndex)`
- `getAllDatesOfWeek(dateIso)`
- `getWeekOfYear(dateIso)`, `getCurrentWeekOfYear()`
- `getWeekdayOfFirstDay(dateIso)`
- `isEvenWeek(dateIso)`, `isLastWeekOfMonth(dateIso)`
- `isSameWeek(dateIso1, dateIso2)`

### 🌐 DateTimeZoneTool
- `convertZone(datetimeIso, fromZone, toZone)`
- `toUtc(datetimeIso, fromZone)`
- `utcNowToZone(zoneId)`
- `getZoneOffset(zoneId)`, `zoneOffsetDiff(zoneId1, zoneId2)`
- `timeInZone(zoneId)`
- `isDstActive(zoneId)`
- `isValidZoneId(zoneId)`

> ℹ️ All functions are annotated with `@Tool` and can be auto-discovered by any compliant MCP host at runtime.

## Usage Examples

Once installed in an MCP-compatible host:

- `nowUtcIso()` returns `2025-04-17T12:34:56Z`
- `formatLocalDateTime("2025-04-17T10:00:00", "yyyy/MM/dd")` returns `2025/04/17`
- `daysBetween("2025-04-01", "2025-04-11")` returns `10`
- `convertZone("2025-04-17T12:00:00", "Asia/Seoul", "UTC")` returns `2025-04-17T03:00:00Z[UTC]`

## Technical Details

- Built with Kotlin + Spring Boot
- MCP-compatible methods are annotated with `@Tool`
- `manifest.json` is automatically generated using ClassGraph to scan the tool package
- Executable jar is placed under `./libs` for use by MCP hosts

## Build Instructions

```bash
./gradlew clean build
```

This will:
- Build the executable Spring Boot jar
- Generate `manifest.json`
- Copy the jar to `./libs` for publication

## Output

The following files will be generated and should be committed:

- `libs/MCP-DateTime-0.0.1-SNAPSHOT.jar`
- `manifest.json`

## License

This project is licensed under the **MIT License**.  
See the [`LICENSE`](./LICENSE) file for details.

© 2025 JUNG JE KIM  
Original author and maintainer: [JUNG JE KIM](https://github.com/key824999)

## Author

- Email: wjdwo951219@gmail.com
- GitHub: [@key824999](https://github.com/key824999)

