package com.fynes.fronk

import arrow.core.Either
import com.fynes.kronk.OpenHoursFormatterService
import com.fynes.kronk.OpenHoursFormatterServiceImpl
import com.fynes.kronk.model.Time
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class KronkController {

    val openHoursFormatterService: OpenHoursFormatterService = OpenHoursFormatterServiceImpl()

    @PostMapping("/parseHours")
    fun parseOpeningHours(@RequestBody openingHoursInput: Map<String, List<Time>>): String {
        val output = openHoursFormatterService.parseOpeningHours(openingHoursInput)
        return when (output) {
            is Either.Left -> output.a.toString()
            is Either.Right -> output.b
        }
    }
}
