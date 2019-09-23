package com.fynes.kronk

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import arrow.core.compose
import com.fynes.kronk.model.Time
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*
import java.util.concurrent.TimeUnit

class OpenHoursFormatterServiceImpl : OpenHoursFormatterService {
    private val chunkTwo = 2

    override val parseOpeningHours: (Map<String, List<Time>>) -> Either<FormatterError, String> = {
        val flatten =
            combineOpeningHourStrings compose
                    mapToOpeningHoursOutput compose
                    mapDaysToStringOutput compose
                    mapOpeningToClosingTimes compose
                    flattenDays compose
                    mapInputKeysToDays

        flatten(it)
    }

    val mapInputKeysToDays: (Map<String, List<Time>>) -> Either<FormatterError, Map<DayOfWeek, List<Time>>> = { it ->

        if (it.size < DayOfWeek.values().size) {
            Left(FormatterError.InputValidationError)
        } else {
            Right(it.mapKeys { DayOfWeek.valueOf(it.key.toUpperCase()) })
        }
    }

    val flattenDays: (Either<FormatterError, Map<DayOfWeek, List<Time>>>) -> Either<FormatterError, List<Pair<DayOfWeek, Time>>> =
        { it ->
            when (it) {
                is Either.Left -> Left(it.a)
                is Either.Right -> {
                    Right(it.b.flatMap { entry -> entry.value.map { time -> entry.key to time } })
                }
            }
        }

    val mapOpeningToClosingTimes: (Either<FormatterError, List<Pair<DayOfWeek, Time>>>) -> Either<FormatterError, List<Pair<DayOfWeek, Pair<Time, Time>>>> =
        { it ->
            when (it) {
                is Either.Left -> Left(it.a)
                is Either.Right -> {
                    val pairsOfDaysOfWeekAndTime = it.b
                    val chunkedPairs = pairsOfDaysOfWeekAndTime.chunked(chunkTwo).map {
                        it.first().first to (it.first().second to it.last().second)
                    }

                    if (chunkedPairs.first().second.first.type == "close") {
                        Left(FormatterError.StartingWithCloseError)
                    } else {
                        Right(chunkedPairs)
                    }
                }
            }
        }

    val mapDaysToStringOutput: (Either<FormatterError, List<Pair<DayOfWeek, Pair<Time, Time>>>>) -> Either<FormatterError, List<Pair<DayOfWeek, String>>> =
        { it ->
            when (it) {
                is Either.Left -> Left(it.a)
                is Either.Right -> Right(it.b.map { entry -> entry.first to mapTimesToString(entry.second) })
            }
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

    val mapToOpeningHoursOutput: (Either<FormatterError, List<Pair<DayOfWeek, String>>>) -> Either<FormatterError, List<String>> =
        { it ->
            when (it) {
                is Either.Left -> Left(it.a)
                is Either.Right -> {
                    val openingHoursList = it.b

                    Right(openingHoursList.let {
                        DayOfWeek.values().map { dayOfWeek ->

                            var outputString =
                                "${dayOfWeek.getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault())}: "

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
                    })
                }
            }
        }

    val combineOpeningHourStrings: (Either<FormatterError, List<String>>) -> Either<FormatterError, String> = {
        when (it) {
            is Either.Left -> Left(it.a)
            is Either.Right -> Right(it.b.reduce { previous, current ->
                previous.plus(current)
            })
        }
    }

    sealed class FormatterError {
        object InputValidationError : FormatterError()
        object StartingWithCloseError : FormatterError()

        override fun toString(): String {
            return "Error occured: ${this.javaClass.simpleName}"
        }
    }
}
