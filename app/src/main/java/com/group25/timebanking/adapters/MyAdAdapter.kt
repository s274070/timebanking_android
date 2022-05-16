package com.group25.timebanking.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.group25.timebanking.R
import com.group25.timebanking.extensions.toString
import com.group25.timebanking.models.Ads

class MyAdAdapter(private val data: List<Ads>, fragmentManager: FragmentManager?) :
    RecyclerView.Adapter<MyAdAdapter.ViewHolder>() {

    var fm: FragmentManager? = fragmentManager

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvTitle: TextView = v.findViewById<TextView>(R.id.tvTitle)
        val tvDateTime: TextView = v.findViewById<TextView>(R.id.tvDateTime)
        val tvDuration: TextView = v.findViewById<TextView>(R.id.tvDuration)
        val tvLocation: TextView = v.findViewById<TextView>(R.id.tvLocation)
        val cardAd = v.findViewById<CardView>(R.id.cardAd)
        val btnEdit = v.findViewById<Button>(R.id.btnEdit)

        fun bind(ad: Ads) {
            tvDateTime.text = ad.date+"  "+ad.time.toString()
            tvTitle.text = ad.title
            tvLocation.text = ad.location
            tvDuration.text = ad.duration.toString() + " hours"
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyAdAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.my_ad_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyAdAdapter.ViewHolder, position: Int) {
        holder.bind(data[position])
        holder.cardAd.setOnClickListener {
            holder.cardAd.findNavController().navigate(R.id.action_my_ads_list_to_ad_details, Bundle().apply {
                putString("id", data[position].id)
                putBoolean("editable", true)
            })
        }

        holder.btnEdit.setOnClickListener {
            holder.cardAd.findNavController().navigate(R.id.action_my_ads_list_to_ad_edit, Bundle().apply {
                putString("id", data[position].id)
                putBoolean("edit", true)
            })
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}