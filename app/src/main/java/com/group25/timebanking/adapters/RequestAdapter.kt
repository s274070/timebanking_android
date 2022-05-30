package com.group25.timebanking.adapters

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.group25.timebanking.R
import com.group25.timebanking.models.Request


class RequestAdapter(private val data: List<Request>, context: Context) :
    RecyclerView.Adapter<RequestAdapter.ViewHolder>() {

    val context: Context = context

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvTitle: TextView = v.findViewById<TextView>(R.id.tvTitle)
        val tvDateTime: TextView = v.findViewById<TextView>(R.id.tvDateTime)
        val tvDuration: TextView = v.findViewById<TextView>(R.id.tvDuration)
        val tvLocation: TextView = v.findViewById<TextView>(R.id.tvLocation)
        val tvRequestUser: TextView = v.findViewById<TextView>(R.id.tvRequestUser)
        val tvRequestDescription: TextView = v.findViewById<TextView>(R.id.tvRequestDescription)
        val cardRequest = v.findViewById<CardView>(R.id.cardRequest)
        val btnReject = v.findViewById<Button>(R.id.btnReject)
        val btnAccept = v.findViewById<Button>(R.id.btnAccept)

        fun bind(req: Request) {
            tvDateTime.text = req.AdDateTime
            tvTitle.text = req.AdTitle
            tvLocation.text = req.AdLocation
            tvDuration.text = req.AdDuration.toString() + " hours"
            tvRequestUser.text = "Request user: " + req.RequestUserName
            if (req.RequestDescription.isNullOrEmpty())
                tvRequestDescription.text = "Request Description: " + req.RequestDescription
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RequestAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.request_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RequestAdapter.ViewHolder, position: Int) {
        holder.bind(data[position])
        holder.cardRequest.setOnClickListener {
            holder.cardRequest.findNavController().navigate(R.id.action_request_list_to_show_user_profile, Bundle().apply {
                putString("userId", data[position].RequestUser)
                putBoolean("editable", false)
            })
        }

        holder.btnAccept.setOnClickListener {
            MaterialAlertDialogBuilder(context)
                .setTitle("Confirmation")
                .setMessage("Are you sure you want to Accept this request?\nBy Accepting this request, all other requests for this advertisement will reject.")
                .setPositiveButton("YES") { dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss()
                }
                .setNegativeButton("NO") { dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss()
                }
                .show()
        }

        holder.btnReject.setOnClickListener {
            MaterialAlertDialogBuilder(context)
                .setTitle("Confirmation")
                .setMessage("Are you sure you want to Reject this request?")
                .setPositiveButton("YES") { dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss()
                }
                .setNegativeButton("NO") { dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss()
                }
                .show()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}