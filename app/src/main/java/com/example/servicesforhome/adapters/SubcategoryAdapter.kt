package com.example.servicesforhome.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.servicesforhome.R
import com.example.servicesforhome.models.Subcategories
import kotlinx.android.synthetic.main.subcategory_layout.view.*

class SubcategoryAdapter(private val mValues: List<Subcategories>, val clickListener: (Subcategories) -> Unit) : RecyclerView.Adapter<SubcategoryAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val imageItem =
            LayoutInflater.from(parent.context).inflate(R.layout.subcategory_layout, parent, false)
        return ViewHolder(imageItem)
    }

    override fun getItemCount(): Int {
        return mValues.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mValues[position],clickListener)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bind(subcategories: Subcategories,clickListener: (Subcategories) -> Unit){
            itemView.sub_category_name.text = subcategories.name
            itemView.setOnClickListener {
                clickListener(subcategories)
            }
        }
    }


}
