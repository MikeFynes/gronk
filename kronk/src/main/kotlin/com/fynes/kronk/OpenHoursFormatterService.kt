package com.fynes.kronk

import com.fynes.kronk.model.Time

interface OpenHoursFormatterService {
    val parseOpeningHours: (Map<String, List<Time>>) -> String
}
