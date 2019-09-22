package com.fynes.fronk

import com.fynes.kronk.OpenHoursFormatterService
import com.fynes.kronk.OpenHoursFormatterServiceImpl
import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
open class FronkApplication

fun main(args: Array<String>) {
    runApplication<FronkApplication>(*args) {
        setBannerMode(Banner.Mode.OFF)

        @Bean
        fun getOpenHoursFormatterService(): OpenHoursFormatterService {
            return OpenHoursFormatterServiceImpl()
        }
    }
}
