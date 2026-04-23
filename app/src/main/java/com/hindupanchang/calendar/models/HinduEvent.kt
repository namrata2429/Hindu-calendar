package com.hindupanchang.calendar.models

import java.io.Serializable

data class HinduEvent(
    val id: String,
    val dateGregorian: String,          // "2026-01-14"
    val dayOfMonth: Int,
    val month: Int,                      // 1-12
    val year: Int,
    val hindiMonth: String,              // Purnimanta month
    val tithiEn: String,                 // e.g. "Dwadashi"
    val tithiHi: String,
    val tithiOr: String,
    val paksha: String,                  // "Shukla" or "Krishna"
    val nameEn: String,
    val nameHi: String,
    val nameOr: String,
    val descriptionEn: String,
    val descriptionHi: String,
    val descriptionOr: String,
    val timeInfo: String = "",           // Timing if applicable e.g. "Sunrise to Sunset"
    val category: EventCategory,
    val isMajorFestival: Boolean = false,
    val isVrat: Boolean = false,
    val isPurnima: Boolean = false,
    val isAmavasya: Boolean = false,
    val isEkadashi: Boolean = false,
    val deity: String = ""
) : Serializable

enum class EventCategory {
    MAJOR_FESTIVAL,
    VRAT_UPAVAS,
    EKADASHI,
    PURNIMA,
    AMAVASYA,
    PRADOSH,
    SANKRANTI,
    CHATURTHI,
    NAVRATRI,
    SPECIAL
}

data class CalendarDay(
    val gregorianDate: String,
    val dayOfMonth: Int,
    val month: Int,
    val year: Int,
    val dayOfWeek: Int,
    val hinduDate: String,              // "Chaitra 5, Shukla Paksha"
    val tithiEn: String,
    val tithiHi: String,
    val tithiOr: String,
    val nakshatraEn: String = "",
    val events: List<HinduEvent> = emptyList(),
    val isToday: Boolean = false,
    val isCurrentMonth: Boolean = true
)

data class CalendarMonth(
    val monthNumber: Int,
    val year: Int,
    val nameEn: String,
    val nameHi: String,
    val nameOr: String,
    val hinduMonthEn: String,
    val hinduMonthHi: String,
    val hinduMonthOr: String,
    val days: List<CalendarDay>
)
