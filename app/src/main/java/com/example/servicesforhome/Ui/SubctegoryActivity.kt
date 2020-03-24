package com.example.servicesforhome.Ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.servicesforhome.Gps
import com.example.servicesforhome.R
import com.example.servicesforhome.adapters.SubcategoryAdapter
import com.example.servicesforhome.models.Category
import com.example.servicesforhome.models.Subcategories
import kotlinx.android.synthetic.main.activity_subctegory.*


class SubctegoryActivity : AppCompatActivity() {

    lateinit var subCategoryRecyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subctegory)
        setSupportActionBar(toolbar)
        try {

            val intent = intent
            val bundle = intent.getBundleExtra("myBundle")
            var category = bundle!!.getParcelable<Category>("category")
            subCategoryRecyclerView = findViewById(R.id.sub_category_list)
            subCategoryRecyclerView.layoutManager = GridLayoutManager(applicationContext, 1)
            if (category != null) {
                subCategoryRecyclerView.adapter =
                    SubcategoryAdapter(category.subcategories) { subcategories: Subcategories ->
                        SubClicked(
                            subcategories
                        )
                    }
            }



        } catch (ex: Exception) {
            Log.e("Recycler view", ex.toString())
        }


    }

    fun SubClicked(sub: Subcategories) {
        val intent = Intent(this, Gps::class.java)
        startActivity(intent)
    }
}
