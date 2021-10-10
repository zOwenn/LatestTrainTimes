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

    private val appId = BuildConfig.APP_ID
    private val appKey = BuildConfig.APP_KEY
    private var response = mutableListOf(TrainTime())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val timesList = findViewById<RecyclerView>(R.id.recycler_view)
        timesList.layoutManager = LinearLayoutManager(this)
        timesList.adapter = TimesAdapter(response)

        requestData("SHF", timesList)

        val refreshButton: FloatingActionButton = findViewById(R.id.refresh_button)
        refreshButton.setOnClickListener {
            toast("Button Pressed!")
            requestData("SHF", timesList)
        }
    }

    private fun requestData(station: String, view: RecyclerView) {
        val url = "https://transportapi.com/v3/uk/train/station/"+station+"/live.json?app_id="+
                appId+"&app_key="+appKey+"&darwin=false&train_status=passenger"

        doAsync {
            response = Request(url).run()
            uiThread {
                toast("Request performed")
                view.adapter = TimesAdapter(response)
            }
        }
    }
}