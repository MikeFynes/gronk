package com.fynes.fronk

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
        return openHoursFormatterService.parseOpeningHours(openingHoursInput)
    }
}
