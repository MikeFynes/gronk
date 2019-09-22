package com.fynes.fronk

import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class FronkApplication

fun main(args: Array<String>) {
    runApplication<FronkApplication>(*args) {
        setBannerMode(Banner.Mode.OFF)
    }
}
