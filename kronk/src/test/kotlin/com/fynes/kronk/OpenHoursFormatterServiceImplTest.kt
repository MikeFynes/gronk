package com.fynes.kronk

import arrow.core.Either
import com.fynes.kronk.model.Time
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class OpenHoursFormatterServiceImplTest {

    private lateinit var formatterService: OpenHoursFormatterService

    @BeforeAll
    fun setup() {
        this.formatterService = OpenHoursFormatterServiceImpl()
    }

    @Test
    fun testOpeningHoursHappyPath() {
        val expected = "Monday: Closed\n" +
                "Tuesday: 10 AM - 6 PM\n" +
                "Wednesday: Closed\n" +
                "Thursday: 10 AM - 6 PM\n" +
                "Friday: 10 AM - 1 AM\n" +
                "Saturday: 10 AM - 1 AM\n" +
                "Sunday: 12 PM - 9 PM\n";

        val input = mapOf(
            "monday" to emptyList(),
            "tuesday" to listOf(
                Time(type = "open", value = 36000),
                Time(type = "close", value = 64800)
            ),
            "wednesday" to emptyList(),
            "thursday" to listOf(
                Time(type = "open", value = 36000),
                Time(type = "close", value = 64800)
            ),
            "friday" to listOf(
                Time(type = "open", value = 36000)
            ),
            "saturday" to listOf(
                Time(type = "close", value = 3600),
                Time(type = "open", value = 36000)
            ),
            "sunday" to listOf(
                Time(type = "close", value = 3600),
                Time(type = "open", value = 43200),
                Time(type = "close", value = 75600)
            )
        )

        when (val output = formatterService.parseOpeningHours(input)) {
            is Either.Left -> fail<OpenHoursFormatterServiceImpl.FormatterError>()
            is Either.Right -> assertEquals(expected, output.b)
        }
    }

    @Test
    fun testOpeningHoursWithHalfHours() {
        val expected = "Monday: Closed\n" +
                "Tuesday: 10.30 AM - 6 PM\n" +
                "Wednesday: Closed\n" +
                "Thursday: 10 AM - 6 PM\n" +
                "Friday: 10 AM - 1 AM\n" +
                "Saturday: 10 AM - 1 AM\n" +
                "Sunday: 12 PM - 9 PM\n";

        val input = mapOf(
            "monday" to emptyList(),
            "tuesday" to listOf(
                Time(type = "open", value = 37800),
                Time(type = "close", value = 64800)
            ),
            "wednesday" to emptyList(),
            "thursday" to listOf(
                Time(type = "open", value = 36000),
                Time(type = "close", value = 64800)
            ),
            "friday" to listOf(
                Time(type = "open", value = 36000)
            ),
            "saturday" to listOf(
                Time(type = "close", value = 3600),
                Time(type = "open", value = 36000)
            ),
            "sunday" to listOf(
                Time(type = "close", value = 3600),
                Time(type = "open", value = 43200),
                Time(type = "close", value = 75600)
            )
        )

        when (val output = formatterService.parseOpeningHours(input)) {
            is Either.Left -> fail<OpenHoursFormatterServiceImpl.FormatterError>()
            is Either.Right -> assertEquals(expected, output.b)
        }
    }

    @Test
    fun testOpeningHoursMultipleOpenClose() {
        val expected = "Monday: Closed\n" +
                "Tuesday: 10 AM - 1 PM, 2 PM - 2 AM\n" +
                "Wednesday: Closed\n" +
                "Thursday: 10 AM - 6 PM\n" +
                "Friday: 10 AM - 1 AM\n" +
                "Saturday: 10 AM - 1 AM\n" +
                "Sunday: 12 PM - 9 PM\n";

        val input = mapOf(
            "monday" to emptyList(),
            "tuesday" to listOf(
                Time(type = "open", value = 36000),
                Time(type = "close", value = 46800),
                Time(type = "open", value = 50400)
            ),
            "wednesday" to listOf(
                Time(type = "close", value = 7200)
            ),
            "thursday" to listOf(
                Time(type = "open", value = 36000),
                Time(type = "close", value = 64800)
            ),
            "friday" to listOf(
                Time(type = "open", value = 36000)
            ),
            "saturday" to listOf(
                Time(type = "close", value = 3600),
                Time(type = "open", value = 36000)
            ),
            "sunday" to listOf(
                Time(type = "close", value = 3600),
                Time(type = "open", value = 43200),
                Time(type = "close", value = 75600)
            )
        )

        when (val output = formatterService.parseOpeningHours(input)) {
            is Either.Left -> fail<OpenHoursFormatterServiceImpl.FormatterError>()
            is Either.Right -> assertEquals(expected, output.b)
        }
    }

    @Test
    fun testOpeningHoursFirstEntryClose() {
        val input = mapOf(
            "monday" to emptyList(),
            "tuesday" to listOf(
                Time(type = "close", value = 3600),
                Time(type = "open", value = 36000),
                Time(type = "close", value = 46800),
                Time(type = "open", value = 50400)
            ),
            "wednesday" to listOf(
                Time(type = "close", value = 7200)
            ),
            "thursday" to listOf(
                Time(type = "open", value = 36000),
                Time(type = "close", value = 64800)
            ),
            "friday" to listOf(
                Time(type = "open", value = 36000)
            ),
            "saturday" to listOf(
                Time(type = "close", value = 3600),
                Time(type = "open", value = 36000)
            ),
            "sunday" to listOf(
                Time(type = "close", value = 3600),
                Time(type = "open", value = 43200),
                Time(type = "close", value = 75600)
            )
        )

        val output = formatterService.parseOpeningHours(input)

        assert(output.isLeft())
    }

    @Test
    fun testOpeningHoursMissingDays() {
        val input = mapOf(
            "tuesday" to listOf(
                Time(type = "close", value = 3600),
                Time(type = "open", value = 36000),
                Time(type = "close", value = 46800),
                Time(type = "open", value = 50400)
            ),
            "wednesday" to listOf(
                Time(type = "close", value = 7200)
            ),
            "thursday" to listOf(
                Time(type = "open", value = 36000),
                Time(type = "close", value = 64800)
            ),
            "friday" to listOf(
                Time(type = "open", value = 36000)
            ),
            "sunday" to listOf(
                Time(type = "close", value = 3600),
                Time(type = "open", value = 43200),
                Time(type = "close", value = 75600)
            )
        )

        val output = formatterService.parseOpeningHours(input)

        assert(output.isLeft())
    }
}
