package com.hindupanchang.calendar.activities

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.hindupanchang.calendar.R
import com.hindupanchang.calendar.models.EventCategory
import com.hindupanchang.calendar.models.HinduEvent
import com.hindupanchang.calendar.utils.AppLanguage
import com.hindupanchang.calendar.utils.CalendarHelper
import com.hindupanchang.calendar.utils.LanguageManager

class EventDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)

        val language = LanguageManager.getLanguage(this)
        val events = intent.getSerializableExtra("events") as? ArrayList<HinduEvent> ?: arrayListOf()
        val date = intent.getStringExtra("date") ?: ""

        setupToolbar(date)

        val container = findViewById<LinearLayout>(R.id.event_details_container)
        container.removeAllViews()

        events.forEach { event ->
            val view = layoutInflater.inflate(R.layout.item_event_detail, container, false)

            val tvName = view.findViewById<TextView>(R.id.tv_event_name)
            val tvDate = view.findViewById<TextView>(R.id.tv_event_date)
            val tvTithi = view.findViewById<TextView>(R.id.tv_event_tithi)
            val tvDescription = view.findViewById<TextView>(R.id.tv_event_description)
            val tvDeity = view.findViewById<TextView>(R.id.tv_event_deity)
            val tvTime = view.findViewById<TextView>(R.id.tv_event_time)
            val tvCategory = view.findViewById<TextView>(R.id.tv_event_category)
            val ivCategoryIcon = view.findViewById<ImageView>(R.id.iv_category_icon)

            tvName.text = CalendarHelper.getEventName(event, language)
            tvDate.text = formatDate(date, language)
            tvTithi.text = buildTithiText(event, language)
            tvDescription.text = CalendarHelper.getEventDescription(event, language)

            if (event.deity.isNotEmpty()) {
                tvDeity.visibility = android.view.View.VISIBLE
                tvDeity.text = when (language) {
                    AppLanguage.HINDI -> "देवता: ${event.deity}"
                    AppLanguage.ODIA -> "ଦେବତା: ${event.deity}"
                    else -> "Deity: ${event.deity}"
                }
            } else {
                tvDeity.visibility = android.view.View.GONE
            }

            if (event.timeInfo.isNotEmpty()) {
                tvTime.visibility = android.view.View.VISIBLE
                tvTime.text = when (language) {
                    AppLanguage.HINDI -> "समय: ${event.timeInfo}"
                    AppLanguage.ODIA -> "ସମୟ: ${event.timeInfo}"
                    else -> "Time: ${event.timeInfo}"
                }
            } else {
                tvTime.visibility = android.view.View.GONE
            }

            tvCategory.text = getCategoryLabel(event.category, language)
            ivCategoryIcon.setImageResource(getCategoryIcon(event.category))
            ivCategoryIcon.setColorFilter(ContextCompat.getColor(this, getCategoryColor(event.category)))

            container.addView(view)
        }
    }

    private fun setupToolbar(date: String) {
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = date
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun formatDate(date: String, language: AppLanguage): String {
        if (date.isEmpty()) return ""
        val parts = date.split("-")
        if (parts.size < 3) return date
        val year = parts[0]
        val month = parts[1].toIntOrNull() ?: return date
        val day = parts[2]
        val months = when (language) {
            AppLanguage.HINDI -> arrayOf("जनवरी","फरवरी","मार्च","अप्रैल","मई","जून","जुलाई","अगस्त","सितंबर","अक्टूबर","नवंबर","दिसंबर")
            AppLanguage.ODIA -> arrayOf("ଜାନୁଆରୀ","ଫେବ୍ରୁଆରୀ","ମାର୍ଚ୍ଚ","ଏପ୍ରିଲ","ମଇ","ଜୁନ","ଜୁଲାଇ","ଅଗଷ୍ଟ","ସେପ୍ଟେମ୍ବର","ଅକ୍ଟୋବର","ନଭେମ୍ବର","ଡିସେମ୍ବର")
            else -> arrayOf("January","February","March","April","May","June","July","August","September","October","November","December")
        }
        return "$day ${months[month - 1]} $year"
    }

    private fun buildTithiText(event: HinduEvent, language: AppLanguage): String {
        val tithi = CalendarHelper.getTithi(event, language)
        val paksha = when (language) {
            AppLanguage.HINDI -> if (event.paksha == "Shukla") "शुक्ल पक्ष" else "कृष्ण पक्ष"
            AppLanguage.ODIA -> if (event.paksha == "Shukla") "ଶୁକ୍ଳ ପକ୍ଷ" else "କୃଷ୍ଣ ପକ୍ଷ"
            else -> "${event.paksha} Paksha"
        }
        val hinduMonth = when (language) {
            AppLanguage.HINDI -> event.hindiMonth
            else -> event.hindiMonth
        }
        return "$tithi • $paksha • $hinduMonth"
    }

    private fun getCategoryLabel(category: EventCategory, language: AppLanguage): String {
        return when (language) {
            AppLanguage.HINDI -> when (category) {
                EventCategory.MAJOR_FESTIVAL -> "प्रमुख त्योहार"
                EventCategory.VRAT_UPAVAS -> "व्रत / उपवास"
                EventCategory.EKADASHI -> "एकादशी"
                EventCategory.PURNIMA -> "पूर्णिमा"
                EventCategory.AMAVASYA -> "अमावस्या"
                EventCategory.PRADOSH -> "प्रदोष"
                EventCategory.SANKRANTI -> "संक्रांति"
                EventCategory.CHATURTHI -> "चतुर्थी"
                EventCategory.NAVRATRI -> "नवरात्रि"
                EventCategory.SPECIAL -> "विशेष दिन"
            }
            AppLanguage.ODIA -> when (category) {
                EventCategory.MAJOR_FESTIVAL -> "ପ୍ରମୁଖ ପର୍ବ"
                EventCategory.VRAT_UPAVAS -> "ବ୍ରତ / ଉପବାସ"
                EventCategory.EKADASHI -> "ଏକାଦଶୀ"
                EventCategory.PURNIMA -> "ପୂର୍ଣ୍ଣiমা"
                EventCategory.AMAVASYA -> "ଅମାବାସ୍ୟା"
                EventCategory.PRADOSH -> "ପ୍ରଦୋଷ"
                EventCategory.SANKRANTI -> "ସଂକ୍ରାନ୍ତି"
                EventCategory.CHATURTHI -> "ଚତୁର୍ଥୀ"
                EventCategory.NAVRATRI -> "ନବରାତ୍ରି"
                EventCategory.SPECIAL -> "ବିଶେଷ ଦିନ"
            }
            else -> when (category) {
                EventCategory.MAJOR_FESTIVAL -> "Major Festival"
                EventCategory.VRAT_UPAVAS -> "Vrat / Upavas"
                EventCategory.EKADASHI -> "Ekadashi"
                EventCategory.PURNIMA -> "Purnima"
                EventCategory.AMAVASYA -> "Amavasya"
                EventCategory.PRADOSH -> "Pradosh"
                EventCategory.SANKRANTI -> "Sankranti"
                EventCategory.CHATURTHI -> "Chaturthi"
                EventCategory.NAVRATRI -> "Navratri"
                EventCategory.SPECIAL -> "Special Day"
            }
        }
    }

    private fun getCategoryIcon(category: EventCategory): Int {
        return when (category) {
            EventCategory.MAJOR_FESTIVAL -> R.drawable.ic_festival
            EventCategory.EKADASHI -> R.drawable.ic_ekadashi
            EventCategory.PURNIMA -> R.drawable.ic_moon
            EventCategory.AMAVASYA -> R.drawable.ic_moon
            EventCategory.VRAT_UPAVAS -> R.drawable.ic_vrat
            else -> R.drawable.ic_calendar
        }
    }

    private fun getCategoryColor(category: EventCategory): Int {
        return when (category) {
            EventCategory.MAJOR_FESTIVAL -> R.color.color_festival
            EventCategory.EKADASHI -> R.color.color_ekadashi
            EventCategory.PURNIMA -> R.color.color_purnima
            EventCategory.AMAVASYA -> R.color.color_amavasya
            EventCategory.VRAT_UPAVAS -> R.color.color_vrat
            EventCategory.NAVRATRI -> R.color.color_navratri
            EventCategory.SANKRANTI -> R.color.color_sankranti
            else -> R.color.color_special
        }
    }
}
