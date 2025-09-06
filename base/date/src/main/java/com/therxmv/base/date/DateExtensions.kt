package com.therxmv.base.date

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn

fun String.toLocalDate(): LocalDate =
    LocalDate.parse(this)

fun LocalDate.toMilliseconds(): Long =
    this.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()