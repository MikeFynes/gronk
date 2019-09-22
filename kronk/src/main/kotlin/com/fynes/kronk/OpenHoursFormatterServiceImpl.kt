package com.fynes.kronk

import arrow.core.compose
import com.fynes.kronk.model.Time
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*
import java.util.concurrent.TimeUnit

class OpenHoursFormatterServiceImpl : OpenHoursFormatterService {
    private val chunkTwo = 2

    override val parseOpeningHours: (Map<String, List<Time>>) -> String = {
        val flatten = combineOpeningHourStrings compose mapToOpeningHoursOutput compose mapDaysToStringOutput compose mapOpeningToClosingTimes compose flattenDays compose mapInputKeysToDays

        flatten(it)
    }

    val mapInputKeysToDays: (Map<String, List<Time>>) -> Map<DayOfWeek, List<Time>> = { it ->
        it.mapKeys { DayOfWeek.valueOf(it.key.toUpperCase()) }
    }

    val flattenDays: (Map<DayOfWeek, List<Time>>) -> List<Pair<DayOfWeek, Time>> = { it ->
        it.flatMap { entry -> entry.value.map { time -> entry.key to time } }
    }

    val mapOpeningToClosingTimes: (List<Pair<DayOfWeek, Time>>) -> List<Pair<DayOfWeek, Pair<Time, Time>>> = { pairsOfDaysOfWeekAndTime ->
        pairsOfDaysOfWeekAndTime.chunked(chunkTwo).map {
            it.first().first to (it.first().second to it.last().second)
        }
    }

    val mapDaysToStringOutput: (List<Pair<DayOfWeek, Pair<Time, Time>>>) -> List<Pair<DayOfWeek, String>> = {
        it.map { entry -> entry.first to mapTimesToString(entry.second) }
    }

    val parseTimeOfDay: (Int) -> String = { timeInSeconds ->
        val time = parseToDate(TimeUnit.SECONDS.toMillis(timeInSeconds.toLong()))
        time
    }

    val parseToDate: (Long) -> String = {
        val dateStringFormat = if (isWholeHour(it)) {
            "h a"
        } else {
            "hh.mm a"
        }
        val dateFormatter = DateTimeFormatter.ofPattern(dateStringFormat)

        val date = LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneOffset.UTC)

        dateFormatter.format(date)
    }

    val isWholeHour: (Long) -> Boolean = {
        val hours = TimeUnit.MILLISECONDS.toMinutes(it).toDouble() / 60

        (hours - Math.floor(hours)) == 0.0
    }

    val mapTimesToString: (Pair<Time, Time>) -> String = {
        "${parseTimeOfDay(it.first.value)} - ${parseTimeOfDay(it.second.value)}"
    }

    val mapToOpeningHoursOutput: (List<Pair<DayOfWeek, String>>) -> List<String> = { openingHoursList ->
        openingHoursList.let {
            DayOfWeek.values().map { dayOfWeek ->

                var outputString = "${dayOfWeek.getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault())}: "

                val openingHoursForDay = openingHoursList.filter { it.first == dayOfWeek }

                if (openingHoursForDay.size > 0) {
                    openingHoursForDay.forEach { pair ->
                        outputString = outputString.plus(pair.second)
                        outputString = outputString.plus(",")
                        outputString = outputString.plus(" ")
                    }
                } else {
                    outputString = outputString.plus("Closed")
                }

                outputString.trim().trimEnd(',').plus("\n")
            }
        }
    }

    val combineOpeningHourStrings: (List<String>) -> String = {
        it.reduce { previous, current ->
            previous.plus(current)
        }
    }
}
