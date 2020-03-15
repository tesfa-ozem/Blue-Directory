package com.example.servicesforhome.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.servicesforhome.R
import com.example.servicesforhome.models.Category
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.dashboard_card.view.*

class CustomAdapter(private val categoryList: List<Category>,val clickListener: (Category) -> Unit) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val imageItem =
            LayoutInflater.from(parent.context).inflate(R.layout.dashboard_card, parent, false)
        return ViewHolder(imageItem)
    }

    override fun getItemCount(): Int {
        return categoryList.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(categoryList[position], clickListener)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val icon = itemView.category_icon
        private val name = itemView.category_name

        fun bind(category: Category, clickListener: (Category) -> Unit) {
            name.text = category.name
            Picasso.with(itemView.context).load(category.icon).into(icon)
            itemView.setOnClickListener {
                clickListener(category)
            }

        }

    }

}

