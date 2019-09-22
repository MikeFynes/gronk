package com.fynes.kronk

import com.fynes.kronk.model.Time
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class OpenHoursFormatterServiceImplTest {

    private lateinit var formatterService: OpenHoursFormatterService

    @BeforeAll
    fun setup() {
        this.formatterService = OpenHoursFormatterServiceImpl()
    }

    @Test
    fun parseOpeningHoursHappyPath() {
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

        val output = formatterService.parseOpeningHours(input)

        assertEquals(expected, output)
    }

    @Test
    fun parseOpeningHoursMultipleOpenClose() {
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

        val output = formatterService.parseOpeningHours(input)

        assertEquals(expected, output)
    }
}
