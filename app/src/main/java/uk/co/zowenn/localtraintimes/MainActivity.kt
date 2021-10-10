package uk.co.zowenn.localtraintimes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

class MainActivity : AppCompatActivity() {

    // accessing the API keys to form the request
    private val appId = BuildConfig.APP_ID
    private val appKey = BuildConfig.APP_KEY
    // defaulting to Sheffield Station, could extend to base this on a search
    private val station = "SHF"

    /* initialising response, defaulting to the default TrainTime, to show something when a search
        is yet to be carried out     */
    private var response = mutableListOf(TrainTime())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val timesList = findViewById<RecyclerView>(R.id.recycler_view)
        timesList.layoutManager = LinearLayoutManager(this)
        timesList.adapter = TimesAdapter(response)
        updateData(timesList)

        // Setting up the refresh button
        val refreshButton: FloatingActionButton = findViewById(R.id.refresh_button)
        refreshButton.setOnClickListener {
            toast("Refreshing Data")
            updateData(timesList)
        }
    }

    /* Calling fresh data from TransportAPI, and refreshing the recyclerView with new data
        Should be updated for offline-checking to avoid sending a request + display a warning */
    private fun updateData(view: RecyclerView) {
        val url = "https://transportapi.com/v3/uk/train/station/"+station+"/live.json?app_id="+
                appId+"&app_key="+appKey+"&darwin=false&train_status=passenger"

        doAsync {
            response = Request(url).run()
            uiThread {
                view.adapter = TimesAdapter(response)
                toast("Data loaded")
            }
        }
    }
}