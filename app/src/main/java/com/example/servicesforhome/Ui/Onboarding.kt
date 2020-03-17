package com.example.servicesforhome.Ui



import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.servicesforhome.R
import com.toptoche.searchablespinnerlibrary.SearchableSpinner
import kotlinx.android.synthetic.main.activity_onboarding.*
import java.util.*
import kotlin.collections.ArrayList


class Onboarding : AppCompatActivity() {

    var spcountries: SearchableSpinner? = null
    var arraycountires: ArrayList<String>? = null
    var adapter: ArrayAdapter<*>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        setSupportActionBar(toolbar)
        spcountries=findViewById(R.id.search_service_category)
        arraycountires = ArrayList(
            Arrays.asList(
                "Cleaning",
                "Plumbing",
                "Electrician",
                "Exterminator",
                "Car Wash"
            )
        )

        adapter = ArrayAdapter<String?>(
            this,
            R.layout.simple_list_item_1,
            arraycountires!! as List<String?>
        )
        spcountries!!.setAdapter(adapter)
        spcountries!!.setTitle("Select Countries")
        spcountries!!.setPositiveButton("Done")

    }


}
