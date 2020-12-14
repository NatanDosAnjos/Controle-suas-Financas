package com.example.organizze.recycler.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.organizze.R
import com.example.organizze.recycler.adapter.MyAdapter.MyViewHolder
import com.example.organizze.model.FinancialMovement
import com.example.organizze.others.getLocale
import java.text.NumberFormat

class MyAdapter(private val context: Context, private val list: MutableList<FinancialMovement>): RecyclerView.Adapter <MyViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.main_adapater_layout, parent, false) as LinearLayout
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var actualValue = list[position].value
        if(list[position].isExpense() ) {
            actualValue = actualValue.minus(actualValue * 2)
            holder.value.setTextColor(ContextCompat.getColor(holder.context, R.color.expenseAccent))
        } else {
            holder.value.setTextColor(ContextCompat.getColor(holder.context, R.color.colorPrimary))
        }

        holder.description.text = list[position].description
        holder.value.text = showValue(actualValue)
        holder.category.text = list[position].category
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun showValue(value: Double): String {
        val currentLocale = getLocale(context.resources)
        val toFormatValue = NumberFormat.getNumberInstance(currentLocale)
        return toFormatValue.format(value)
    }


    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val description = itemView.findViewById<TextView>(R.id.descriptionAdapter)!!
        val value = itemView.findViewById<TextView>(R.id.valueAdapter)!!
        val category = itemView.findViewById<TextView>(R.id.categoryAdapter)!!
        val context: Context = itemView.context

    }
}