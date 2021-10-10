package uk.co.zowenn.localtraintimes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class TimesAdapter(val timesList: MutableList<TrainTime>) : RecyclerView.Adapter<TimesAdapter.TimesViewHolder>(){

    class TimesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val destinationTextView: TextView = itemView.findViewById(R.id.destination)
        private val arrivalTextView: TextView = itemView.findViewById(R.id.arrival_time)
        private val departureTextView: TextView = itemView.findViewById(R.id.departure_time)
        private val platformTextView: TextView = itemView.findViewById(R.id.platform)

        fun bind(times: TrainTime){
            destinationTextView.text = times.destination
            arrivalTextView.text = if (times.arrival == "null" ) "" else "Arriving: "+times.arrival
            departureTextView.text = if (times.departure == "null") "" else "Departing: "+times.departure
            platformTextView.text = if(times.platform == "null") "" else "Platform: "+times.platform
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.times_item, parent, false)
        return TimesViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimesViewHolder, position: Int) {
        holder.bind(timesList[position])
    }

    override fun getItemCount(): Int {
        return timesList.size
    }
}