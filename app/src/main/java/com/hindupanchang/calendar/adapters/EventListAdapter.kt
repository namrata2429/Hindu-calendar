package com.hindupanchang.calendar.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hindupanchang.calendar.R
import com.hindupanchang.calendar.models.EventCategory
import com.hindupanchang.calendar.models.HinduEvent
import com.hindupanchang.calendar.utils.AppLanguage
import com.hindupanchang.calendar.utils.CalendarHelper

class EventListAdapter(
    private val language: AppLanguage,
    private val onEventClick: (HinduEvent) -> Unit
) : RecyclerView.Adapter<EventListAdapter.EventViewHolder>() {

    private var events: List<HinduEvent> = emptyList()

    fun updateEvents(newEvents: List<HinduEvent>) {
        events = newEvents
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event_list, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount() = events.size

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.tv_event_name)
        private val tvDate: TextView = itemView.findViewById(R.id.tv_event_date)
        private val tvTithi: TextView = itemView.findViewById(R.id.tv_event_tithi)
        private val tvCategory: TextView = itemView.findViewById(R.id.tv_event_category)
        private val colorBar: View = itemView.findViewById(R.id.color_bar)

        fun bind(event: HinduEvent) {
            tvName.text = CalendarHelper.getEventName(event, language)
            tvDate.text = formatDate(event.dateGregorian)
            tvTithi.text = CalendarHelper.getTithi(event, language)
            tvCategory.text = getCategoryShortLabel(event.category)
            colorBar.setBackgroundColor(
                ContextCompat.getColor(itemView.context, getCategoryColor(event.category))
            )

            itemView.setOnClickListener { onEventClick(event) }
        }

        private fun formatDate(date: String): String {
            if (date.isEmpty()) return ""
            val parts = date.split("-")
            if (parts.size < 3) return date
            val month = parts[1].toIntOrNull() ?: return date
            val day = parts[2]
            val months = arrayOf("Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec")
            return "$day ${months[month - 1]}"
        }

        private fun getCategoryShortLabel(category: EventCategory): String {
            return when (category) {
                EventCategory.MAJOR_FESTIVAL -> "★ Festival"
                EventCategory.EKADASHI -> "Ekadashi"
                EventCategory.PURNIMA -> "Purnima"
                EventCategory.AMAVASYA -> "Amavasya"
                EventCategory.VRAT_UPAVAS -> "Vrat"
                EventCategory.NAVRATRI -> "Navratri"
                EventCategory.SANKRANTI -> "Sankranti"
                EventCategory.CHATURTHI -> "Chaturthi"
                EventCategory.PRADOSH -> "Pradosh"
                EventCategory.SPECIAL -> "Special"
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
