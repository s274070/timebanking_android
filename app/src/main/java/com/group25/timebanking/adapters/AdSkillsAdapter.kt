package com.group25.timebanking.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.group25.timebanking.R
import java.util.*

class AdSkillsAdapter() :
    RecyclerView.Adapter<AdSkillsAdapter.ViewHolder>(), Filterable{

    var fm: FragmentManager? = null
    var data: MutableList<String> = ArrayList()
    var dataFull: MutableList<String> = ArrayList()
    var caller: String = ""

    constructor(
        allData: MutableList<String>,
        caller: String,
        fragmentManager: FragmentManager?
    ) : this() {
        this.data = allData
        this.dataFull.addAll(data)
        this.fm = fragmentManager
        this.caller = caller
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvTitle: TextView = v.findViewById<TextView>(R.id.tvTitle)
        val cardAd = v.findViewById<CardView>(R.id.cardAd)

        fun bind(title: String) {
            tvTitle.text = title
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdSkillsAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.ad_skills_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AdSkillsAdapter.ViewHolder, position: Int) {
        holder.bind(data[position])
        holder.cardAd.setOnClickListener {
            holder.cardAd.findNavController().navigate(R.id.action_ad_skills_list_to_ad_list, Bundle().apply {
                putString("skill", data[position])
            })
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getFilter(): Filter {
        return myFilter;
    }

    private var myFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val dataFiltered = mutableListOf<String>()
            if (constraint == null || constraint.isEmpty()) {
                dataFiltered.addAll(dataFull)
            } else {
                val filterPattern = constraint.toString().toUpperCase().trim()
                for (item in dataFull) {
                    if (item.contains(filterPattern)){
                        dataFiltered.add(item)
                    }
                }
            }

            val results = FilterResults()
            results.values = dataFiltered
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            data.clear()
            data.addAll(results?.values as MutableList<String>)
            notifyDataSetChanged()
        }
    }
}