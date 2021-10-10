package uk.co.zowenn.testapplication

import android.service.autofill.Validators.or
import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import java.net.URL

class Request(private val url: String) {

    fun run(): MutableList<TrainTime> {
        Log.d("TravelURL", url)
        val travelJson = URL(url).readText()
        Log.d("TravelData", travelJson)
        return formatJSON(travelJson)
    }

    /* A messy function to parse the data collected from TransportAPI. Ideally, an library would
    * have been found to do this a cleaner - but it works. */
    private fun formatJSON(response: String): MutableList<TrainTime> {
        var output = mutableListOf<TrainTime>()
        val jsonObject = JSONTokener(response).nextValue() as JSONObject
        val departures = JSONTokener(jsonObject.getString("departures")).nextValue() as JSONObject
        val allDepartures = departures.getJSONArray("all")

        if (allDepartures.length() > 0) {
            for (i in 0 until allDepartures.length()) {
                var trainTime = TrainTime()
                trainTime.destination = allDepartures.getJSONObject(i).getString("destination_name")
                trainTime.platform = allDepartures.getJSONObject(i).getString("platform")
                // prioritising expected arrival to take into account potential delays
                val expectedArrival =
                    try { allDepartures.getJSONObject(i).getString("expected_arrival_time") }
                    catch (e: JSONException) {null}
                val aimedArrival =
                    try { allDepartures.getJSONObject(i).getString("aimed_arrival_time") }
                    catch (e: JSONException) {null}
                when {
                    expectedArrival != null -> trainTime.arrival = expectedArrival
                    aimedArrival != null -> trainTime.arrival = aimedArrival
                    else -> trainTime.arrival = "N/A"
                }
                // prioritising expected departure to take into account potential delays
                val expectedDeparture =
                    try { allDepartures.getJSONObject(i).getString("expected_departure_time") }
                    catch (e: JSONException) { null }
                val aimedDeparture =
                    try { allDepartures.getJSONObject(i).getString("aimed_departure_time")
                    } catch (e: JSONException) { null }

                when {
                    expectedDeparture != null -> trainTime.departure = expectedDeparture
                    aimedDeparture != null -> trainTime.departure = aimedDeparture
                    else -> trainTime.departure = "N/A"
                }
                if (trainTime.departure == null) trainTime.departure = "N/A"
                output.add(trainTime)
            }
        }
        return output
    }
}

class TrainTime(var destination: String="", var platform: String="", var arrival: String="", var departure: String="")