package com.hindupanchang.calendar.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hindupanchang.calendar.R
import com.hindupanchang.calendar.adapters.CalendarDayAdapter
import com.hindupanchang.calendar.adapters.EventListAdapter
import com.hindupanchang.calendar.models.CalendarMonth
import com.hindupanchang.calendar.models.EventCategory
import com.hindupanchang.calendar.models.HinduEvent
import com.hindupanchang.calendar.utils.AppLanguage
import com.hindupanchang.calendar.utils.CalendarHelper
import com.hindupanchang.calendar.utils.LanguageManager

class MainActivity : AppCompatActivity() {

    private var currentYear = 2026
    private var currentMonth = 1
    private lateinit var currentLanguage: AppLanguage
    private lateinit var calendarAdapter: CalendarDayAdapter
    private lateinit var eventAdapter: EventListAdapter
    private var currentMonthData: CalendarMonth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        currentLanguage = LanguageManager.getLanguage(this)

        setupToolbar()
        setupCalendarGrid()
        setupEventsList()
        setupMonthNavigation()
        setupFilterButtons()
        loadMonth(currentYear, currentMonth)
        loadUpcomingEvents()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val tvTitle = findViewById<TextView>(R.id.tv_app_title)
        tvTitle.text = when (currentLanguage) {
            AppLanguage.HINDI -> "हिंदू पंचांग 2026"
            AppLanguage.ODIA -> "ହିନ୍ଦୁ ପଞ୍ଚାଙ୍ଗ 2026"
            else -> "Hindu Panchang 2026"
        }

        val tvVikram = findViewById<TextView>(R.id.tv_vikram_samvat)
        tvVikram.text = when (currentLanguage) {
            AppLanguage.HINDI -> "विक्रम संवत 2083"
            AppLanguage.ODIA -> "ବିକ୍ରମ ସମ୍ବତ ୨୦୮୩"
            else -> "Vikram Samvat 2083"
        }
    }

    private fun setupCalendarGrid() {
        val rvCalendar = findViewById<RecyclerView>(R.id.rv_calendar)
        rvCalendar.layoutManager = GridLayoutManager(this, 7)
        calendarAdapter = CalendarDayAdapter(currentLanguage) { day ->
            if (day.events.isNotEmpty()) {
                val intent = Intent(this, EventDetailActivity::class.java)
                intent.putExtra("events", ArrayList(day.events))
                intent.putExtra("date", day.gregorianDate)
                startActivity(intent)
            }
        }
        rvCalendar.adapter = calendarAdapter
    }

    private fun setupEventsList() {
        val rvEvents = findViewById<RecyclerView>(R.id.rv_events)
        rvEvents.layoutManager = LinearLayoutManager(this)
        eventAdapter = EventListAdapter(currentLanguage) { event ->
            val intent = Intent(this, EventDetailActivity::class.java)
            intent.putExtra("events", arrayListOf(event))
            intent.putExtra("date", event.dateGregorian)
            startActivity(intent)
        }
        rvEvents.adapter = eventAdapter
    }

    private fun setupMonthNavigation() {
        val btnPrev = findViewById<ImageButton>(R.id.btn_prev_month)
        val btnNext = findViewById<ImageButton>(R.id.btn_next_month)

        btnPrev.setOnClickListener {
            if (currentMonth > 1) {
                currentMonth--
                loadMonth(currentYear, currentMonth)
            }
        }

        btnNext.setOnClickListener {
            if (currentMonth < 12) {
                currentMonth++
                loadMonth(currentYear, currentMonth)
            }
        }
    }

    private fun setupFilterButtons() {
        val btnAll = findViewById<Button>(R.id.btn_filter_all)
        val btnFestival = findViewById<Button>(R.id.btn_filter_festival)
        val btnVrat = findViewById<Button>(R.id.btn_filter_vrat)
        val btnEkadashi = findViewById<Button>(R.id.btn_filter_ekadashi)

        val upcomingEvents = CalendarHelper.getUpcomingEvents(50)

        btnAll.setOnClickListener {
            updateFilterButtons(0)
            loadUpcomingEvents()
        }
        btnFestival.setOnClickListener {
            updateFilterButtons(1)
            eventAdapter.updateEvents(upcomingEvents.filter { it.isMajorFestival })
        }
        btnVrat.setOnClickListener {
            updateFilterButtons(2)
            eventAdapter.updateEvents(upcomingEvents.filter { it.isVrat })
        }
        btnEkadashi.setOnClickListener {
            updateFilterButtons(3)
            eventAdapter.updateEvents(upcomingEvents.filter { it.isEkadashi })
        }
    }

    private fun updateFilterButtons(activeIndex: Int) {
        val ids = listOf(R.id.btn_filter_all, R.id.btn_filter_festival, R.id.btn_filter_vrat, R.id.btn_filter_ekadashi)
        ids.forEachIndexed { index, id ->
            val btn = findViewById<Button>(id)
            btn.isSelected = (index == activeIndex)
        }
    }

    private fun loadMonth(year: Int, month: Int) {
        currentMonthData = CalendarHelper.buildMonthData(year, month)
        currentMonthData?.let { monthData ->
            val tvMonth = findViewById<TextView>(R.id.tv_month_name)
            tvMonth.text = "${CalendarHelper.getMonthName(monthData, currentLanguage)} $year"

            val tvHinduMonth = findViewById<TextView>(R.id.tv_hindu_month)
            tvHinduMonth.text = when (currentLanguage) {
                AppLanguage.HINDI -> monthData.hinduMonthHi
                AppLanguage.ODIA -> monthData.hinduMonthOr
                else -> monthData.hinduMonthEn
            }

            // Update day headers
            updateDayHeaders()

            calendarAdapter.updateDays(monthData.days)
        }
    }

    private fun updateDayHeaders() {
        val dayHeaderIds = listOf(
            R.id.tv_day_sun, R.id.tv_day_mon, R.id.tv_day_tue, R.id.tv_day_wed,
            R.id.tv_day_thu, R.id.tv_day_fri, R.id.tv_day_sat
        )
        dayHeaderIds.forEachIndexed { index, id ->
            val tv = findViewById<TextView>(id)
            tv.text = CalendarHelper.getDayName(index, currentLanguage)
        }
    }

    private fun loadUpcomingEvents() {
        val upcoming = CalendarHelper.getUpcomingEvents(20)
        eventAdapter.updateEvents(upcoming)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_language -> {
                startActivity(Intent(this, LanguageSelectionActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        currentLanguage = LanguageManager.getLanguage(this)
        setupToolbar()
        loadMonth(currentYear, currentMonth)
        loadUpcomingEvents()
    }
}
