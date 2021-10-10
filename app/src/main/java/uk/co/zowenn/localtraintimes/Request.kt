package uk.co.zowenn.localtraintimes

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import java.net.URL

class Request(private val url: String) {

    // Function to carry out the request, and also parse the data for use in the app
    fun run(): MutableList<TrainTime> {
        val travelJson = URL(url).readText()
        return formatJson(travelJson)
    }

    // THIS WILL NEED TESTING AGAIN WHEN RECYCLERVIEW IS IMPLEMENTED
    // - refactoring an even messier function

    /* A somewhat messy implementation to parse the data from the TransportAPI data. Ideally, a
    library would have been found to do this cleaner, but this works to parse the data to a format
    desired for the recyclerView */
    private fun formatJson(response: String): MutableList<TrainTime> {
        var output = mutableListOf<TrainTime>()
        // Extracting the departures array from the API response
        val jsonObject = JSONTokener(response).nextValue() as JSONObject
        val departures = JSONTokener(jsonObject.getString("departures")).nextValue() as JSONObject
        val allDepartures = departures.getJSONArray("all")

        // Checking the array contains any departures
        if (allDepartures.length() > 0) {
            for (i in 0 until allDepartures.length()) {
                var trainTime = TrainTime()
                val departureJson = allDepartures.getJSONObject(i)
                trainTime.destination = departureJson.getString("destination_name")
                trainTime.platform = departureJson.getString("platform")
                /* Handling Arrival times, prioritising expected arrival in case of delays,
                    but this can be missing in some queries. An error needs to be caught before
                    moving onto the aimed arrival time.*/
                val expectedArrival = getValue(departureJson, "expected_arrival_time")
                val aimedArrival = departureJson.getString("aimed_arrival_time")
                when {
                    expectedArrival != null -> trainTime.arrival = expectedArrival
                    else -> trainTime.arrival = aimedArrival
                }
                /* Handling Departure times, prioritising expected departure in case of delays,
                    but this can be missing in some queries. An error needs to be caught before
                    moving onto the aimed departure time.*/
                val expectedDeparture = getValue(departureJson, "expected_departure_time")
                val aimedDeparture = departureJson.getString("aimed_departure_time")
                when {
                    expectedDeparture != null -> trainTime.departure = expectedDeparture
                    else -> trainTime.departure = aimedDeparture
                }
                output.add(trainTime)
            }
        } else {
            // add something for a "no departures found" message
            output.add( TrainTime("No departures found", "null",
                "null", "null") )
        }
        return output
    }

    // Function to try collect a value of the supplied name, in the supplied json object
    // To be used for departure and arrival times, as these are ones that can be missing.
    private fun getValue(json: JSONObject, name: String): String? {
        return try { json.getString(name) } catch (e: JSONException) { null }
    }

}

/*  A simple class to help parse the data collected from the TransportAPI
    While there is more data returned in the request, only this is currently desired to be listed
 */
class TrainTime(var destination: String="No departures found", var platform: String="null",
                var arrival: String="null", var departure: String="null")