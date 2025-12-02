package com.example.core_data.util

import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * Приводит ввод пользователя формата dd.MM.yyyy к OffsetDateTime-строке.
 * Если распарсить не удалось — возвращает исходную строку, чтобы не ломать запрос.
 */
fun normalizeDateToOffsetString(
    input: String,
    zoneId: ZoneId = ZoneOffset.UTC
): String {
    val trimmed = input.trim()
    if (trimmed.isEmpty()) return trimmed
    return try {
        val localDate = LocalDate.parse(trimmed, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        localDate
            .atStartOfDay(zoneId)
            .toOffsetDateTime()
            .toString()
    } catch (_: DateTimeParseException) {
        trimmed
    }
}
