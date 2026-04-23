package com.hindupanchang.calendar.adapters

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hindupanchang.calendar.R
import com.hindupanchang.calendar.models.CalendarDay
import com.hindupanchang.calendar.models.EventCategory
import com.hindupanchang.calendar.utils.AppLanguage

class CalendarDayAdapter(
    private val language: AppLanguage,
    private val onDayClick: (CalendarDay) -> Unit
) : RecyclerView.Adapter<CalendarDayAdapter.DayViewHolder>() {

    private var days: List<CalendarDay> = emptyList()

    fun updateDays(newDays: List<CalendarDay>) {
        days = newDays
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day, parent, false)
        return DayViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        holder.bind(days[position])
    }

    override fun getItemCount() = days.size

    inner class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDay: TextView = itemView.findViewById(R.id.tv_day_number)
        private val tvTithi: TextView = itemView.findViewById(R.id.tv_tithi)
        private val dotContainer: LinearLayout = itemView.findViewById(R.id.dot_container)
        private val dayRoot: View = itemView.findViewById(R.id.day_root)

        fun bind(day: CalendarDay) {
            if (day.dayOfMonth == 0 || !day.isCurrentMonth) {
                itemView.visibility = View.INVISIBLE
                return
            }
            itemView.visibility = View.VISIBLE

            tvDay.text = day.dayOfMonth.toString()
            tvTithi.text = when (language) {
                AppLanguage.HINDI -> day.tithiHi
                AppLanguage.ODIA -> day.tithiOr
                else -> day.tithiEn
            }

            // Today highlight
            if (day.isToday) {
                dayRoot.setBackgroundResource(R.drawable.bg_today)
                tvDay.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
                tvDay.setTypeface(null, Typeface.BOLD)
            } else {
                dayRoot.setBackgroundResource(0)
                tvDay.setTextColor(ContextCompat.getColor(itemView.context, R.color.text_primary))
                tvDay.setTypeface(null, Typeface.NORMAL)
            }

            // Event dots
            dotContainer.removeAllViews()
            val categories = day.events.map { it.category }.distinct().take(3)
            categories.forEach { category ->
                val dot = View(itemView.context)
                val size = itemView.context.resources.getDimensionPixelSize(R.dimen.event_dot_size)
                val params = LinearLayout.LayoutParams(size, size)
                params.setMargins(2, 0, 2, 0)
                dot.layoutParams = params
                dot.setBackgroundResource(R.drawable.dot_circle)
                dot.background.setTint(ContextCompat.getColor(itemView.context, getCategoryColor(category)))
                dotContainer.addView(dot)
            }

            // Major festival highlight
            if (day.events.any { it.isMajorFestival }) {
                tvDay.setTypeface(null, Typeface.BOLD)
                if (!day.isToday) {
                    tvDay.setTextColor(ContextCompat.getColor(itemView.context, R.color.color_festival))
                }
            }

            itemView.setOnClickListener {
                if (day.events.isNotEmpty()) onDayClick(day)
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
}
