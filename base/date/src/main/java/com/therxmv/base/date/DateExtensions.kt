package com.therxmv.base.date

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime

fun String.toLocalDate(): LocalDate =
    LocalDate.parse(this)

fun LocalDate.toMilliseconds(): Long =
    this.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()

/**
 * Converts Milliseconds to String date ("Sep 2025")
 */
fun Long.formatToMonthAndYear(): String {
    val instant = Instant.fromEpochMilliseconds(this)
    val date = instant.toLocalDateTime(TimeZone.UTC).date

    val pattern = LocalDate.Format {
        monthName(MonthNames.ENGLISH_ABBREVIATED); char(' '); year()
    }

    return date.format(pattern)
}