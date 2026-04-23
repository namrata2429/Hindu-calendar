package com.hindupanchang.calendar.utils

import com.hindupanchang.calendar.data.HinduCalendarData2026
import com.hindupanchang.calendar.models.CalendarDay
import com.hindupanchang.calendar.models.CalendarMonth
import com.hindupanchang.calendar.models.HinduEvent
import java.util.Calendar

object CalendarHelper {

    fun buildMonthData(year: Int, month: Int): CalendarMonth {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, 1)

        val totalDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1 // 0 = Sunday

        val today = Calendar.getInstance()
        val todayYear = today.get(Calendar.YEAR)
        val todayMonth = today.get(Calendar.MONTH) + 1
        val todayDay = today.get(Calendar.DAY_OF_MONTH)

        val allEvents = HinduCalendarData2026.getAllEvents()
        val monthEvents = allEvents.filter { it.month == month && it.year == year }

        val days = mutableListOf<CalendarDay>()

        // Add blank days for alignment
        repeat(firstDayOfWeek) {
            days.add(CalendarDay(
                gregorianDate = "",
                dayOfMonth = 0,
                month = month,
                year = year,
                dayOfWeek = 0,
                hinduDate = "",
                tithiEn = "",
                tithiHi = "",
                tithiOr = "",
                isCurrentMonth = false
            ))
        }

        // Add actual days
        for (day in 1..totalDays) {
            val dateStr = "$year-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}"
            val dayEvents = monthEvents.filter { it.dayOfMonth == day }
            val tithi = dayEvents.firstOrNull()

            days.add(CalendarDay(
                gregorianDate = dateStr,
                dayOfMonth = day,
                month = month,
                year = year,
                dayOfWeek = ((firstDayOfWeek + day - 1) % 7),
                hinduDate = getHinduDateString(month, day),
                tithiEn = tithi?.tithiEn ?: "",
                tithiHi = tithi?.tithiHi ?: "",
                tithiOr = tithi?.tithiOr ?: "",
                events = dayEvents,
                isToday = (year == todayYear && month == todayMonth && day == todayDay),
                isCurrentMonth = true
            ))
        }

        return CalendarMonth(
            monthNumber = month,
            year = year,
            nameEn = HinduCalendarData2026.GREGORIAN_MONTHS_EN[month - 1],
            nameHi = HinduCalendarData2026.GREGORIAN_MONTHS_HI[month - 1],
            nameOr = HinduCalendarData2026.GREGORIAN_MONTHS_OR[month - 1],
            hinduMonthEn = HinduCalendarData2026.HINDU_MONTHS_EN[month - 1],
            hinduMonthHi = HinduCalendarData2026.HINDU_MONTHS_HI[month - 1],
            hinduMonthOr = HinduCalendarData2026.HINDU_MONTHS_OR[month - 1],
            days = days
        )
    }

    private fun getHinduDateString(month: Int, day: Int): String {
        // Simplified Hindu date display
        val hinduMonthIdx = (month - 1) % 12
        return HinduCalendarData2026.HINDU_MONTHS_EN[hinduMonthIdx]
    }

    fun getUpcomingEvents(count: Int = 5): List<HinduEvent> {
        val today = Calendar.getInstance()
        val todayYear = today.get(Calendar.YEAR)
        val todayMonth = today.get(Calendar.MONTH) + 1
        val todayDay = today.get(Calendar.DAY_OF_MONTH)

        return HinduCalendarData2026.getAllEvents()
            .filter { event ->
                val eYear = event.year
                val eMonth = event.month
                val eDay = event.dayOfMonth
                (eYear > todayYear) ||
                (eYear == todayYear && eMonth > todayMonth) ||
                (eYear == todayYear && eMonth == todayMonth && eDay >= todayDay)
            }
            .take(count)
    }

    fun getMajorFestivals(): List<HinduEvent> {
        return HinduCalendarData2026.getAllEvents().filter { it.isMajorFestival }
    }

    fun getEventsByCategory(category: com.hindupanchang.calendar.models.EventCategory): List<HinduEvent> {
        return HinduCalendarData2026.getAllEvents().filter { it.category == category }
    }

    fun getEventName(event: HinduEvent, language: AppLanguage): String {
        return when (language) {
            AppLanguage.HINDI -> event.nameHi
            AppLanguage.ODIA -> event.nameOr
            else -> event.nameEn
        }
    }

    fun getEventDescription(event: HinduEvent, language: AppLanguage): String {
        return when (language) {
            AppLanguage.HINDI -> event.descriptionHi
            AppLanguage.ODIA -> event.descriptionOr
            else -> event.descriptionEn
        }
    }

    fun getTithi(event: HinduEvent, language: AppLanguage): String {
        return when (language) {
            AppLanguage.HINDI -> event.tithiHi
            AppLanguage.ODIA -> event.tithiOr
            else -> event.tithiEn
        }
    }

    fun getMonthName(month: CalendarMonth, language: AppLanguage): String {
        return when (language) {
            AppLanguage.HINDI -> month.nameHi
            AppLanguage.ODIA -> month.nameOr
            else -> month.nameEn
        }
    }

    fun getDayName(dayOfWeek: Int, language: AppLanguage): String {
        val en = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
        val hi = listOf("रवि", "सोम", "मंगल", "बुध", "गुरु", "शुक्र", "शनि")
        val or = listOf("ରବି", "ସୋମ", "ମଙ୍ଗଳ", "ବୁଧ", "ଗୁରୁ", "ଶୁକ୍ର", "ଶନି")
        return when (language) {
            AppLanguage.HINDI -> hi[dayOfWeek % 7]
            AppLanguage.ODIA -> or[dayOfWeek % 7]
            else -> en[dayOfWeek % 7]
        }
    }
}
