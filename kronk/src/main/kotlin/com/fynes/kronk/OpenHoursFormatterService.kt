package com.fynes.kronk

import arrow.core.Either
import com.fynes.kronk.model.Time

interface OpenHoursFormatterService {
    val parseOpeningHours: (Map<String, List<Time>>) -> Either<OpenHoursFormatterServiceImpl.FormatterError, String>
}
