package com.group25.timebanking.adapters

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.group25.timebanking.R
import com.group25.timebanking.models.Ad
import com.group25.timebanking.utils.LoadingScreen
import java.util.*

class AdAdapter() :
    RecyclerView.Adapter<AdAdapter.ViewHolder>(), Filterable{

    private lateinit var context: Context
    var data: MutableList<Ad> = ArrayList()
    var dataFull: MutableList<Ad> = ArrayList()
    var caller: String = ""

    constructor(
        allData: MutableList<Ad>,
        caller: String,
        contex: Context
    ) : this() {
        this.data = allData
        this.dataFull.addAll(data)
        this.context = contex
        this.caller = caller
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvTitle: TextView = v.findViewById<TextView>(R.id.tvTitle)
        val tvDateTime: TextView = v.findViewById<TextView>(R.id.tvDateTime)
        val tvDuration: TextView = v.findViewById<TextView>(R.id.tvDuration)
        val tvLocation: TextView = v.findViewById<TextView>(R.id.tvLocation)
        val cardAd = v.findViewById<CardView>(R.id.cardAd)
        val btnUser = v.findViewById<Button>(R.id.btnUser)
        val btnSendRequest = v.findViewById<Button>(R.id.btnSendRequest)

        fun bind(ad: Ad) {
            tvDateTime.text = ad.date+"  "+ad.time.toString()
            tvTitle.text = ad.title
            tvLocation.text = ad.location
            tvDuration.text = ad.duration.toString() + " hours"
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.ad_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AdAdapter.ViewHolder, position: Int) {
        holder.bind(data[position])
        holder.cardAd.setOnClickListener {
            holder.cardAd.findNavController().navigate(R.id.action_ad_list_to_ad_details, Bundle().apply {
                putString("id", data[position].id)
                putBoolean("editable", false)
            })
        }

        holder.btnUser.setOnClickListener {
            holder.cardAd.findNavController().navigate(R.id.action_ad_list_to_user_profile, Bundle().apply {
                putString("userId", data[position].createdUser)
                putBoolean("editable", false)
            })
        }

        holder.btnSendRequest.setOnClickListener {
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
            val dataFiltered = mutableListOf<Ad>()
            if (constraint == null || constraint.isEmpty()) {
                dataFiltered.addAll(dataFull)
            } else {
                val filterPattern = constraint.toString().toLowerCase().trim()
                for (item: Ad in dataFull) {
                    if (item.title.lowercase(Locale.ROOT).contains(filterPattern) ||
                        item.location.lowercase(Locale.ROOT).contains(filterPattern) ||
                        item.date.contains(filterPattern)
                    ) {
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
            data.addAll(results?.values as MutableList<Ad>)
            notifyDataSetChanged()
        }
    }
}