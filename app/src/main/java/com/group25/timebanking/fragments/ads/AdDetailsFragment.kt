package com.group25.timebanking.fragments.ads

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.group25.timebanking.R
import com.group25.timebanking.models.Request
import com.group25.timebanking.utils.Database

class AdDetailsFragment : Fragment() {

    private var isEditable: Boolean? = true

    private lateinit var tvTitle: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvDateTime: TextView
    private lateinit var tvDuration: TextView
    private lateinit var tvLocation: TextView
    private lateinit var btnUserInfo: Button
    private lateinit var btnSendRequest: Button

    private var adId: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_ad_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        adId = arguments?.getString("id")!!
        isEditable = arguments?.getBoolean("editable",true)

        tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        tvDescription = view.findViewById<TextView>(R.id.tvDescription)
        tvDateTime = view.findViewById<TextView>(R.id.tvDateTime)
        tvDuration = view.findViewById<TextView>(R.id.tvDuration)
        tvLocation = view.findViewById<TextView>(R.id.tvLocation)
        btnUserInfo = view.findViewById<Button>(R.id.btnUserInfo)
        btnSendRequest = view.findViewById<Button>(R.id.btnSendRequest)
        if(isEditable == true){
            btnUserInfo.visibility=View.GONE
            btnSendRequest.visibility=View.GONE
        }
    }

    override fun onResume() {
        super.onResume()

        Database.getInstance(context).getAdById(adId) { ad ->
            if (ad != null) {
                tvTitle.text = ad.title
                tvDescription.text = ad.description
                tvDateTime.text = ad.date + " " + ad.time
                tvDuration.text = ad.duration.toString() + " hours"
                tvLocation.text = ad.location

                btnUserInfo.setOnClickListener {
                    findNavController().navigate(
                        R.id.action_ad_details_to_user_profile,
                        Bundle().apply {
                            putString("userId", ad.createdUser)
                            putBoolean("editable", false)
                        })
                }

                btnSendRequest.setOnClickListener {
                    val builder = AlertDialog.Builder(requireActivity()).create()
                    val view = layoutInflater.inflate(R.layout.dialog_request_session, null)
                    val etDescription = view.findViewById<EditText>(R.id.etDescription)
                    val btnCancel = view.findViewById<Button>(R.id.btnCancel)
                    val btnOK = view.findViewById<Button>(R.id.btnOK)
                    builder.setView(view)
                    btnCancel.setOnClickListener {
                        builder.dismiss()
                    }
                    btnOK.setOnClickListener {
                        val mAuth = FirebaseAuth.getInstance()
                        val request = Request(
                            id = null,
                            AdId = adId,
                            AdTitle = ad.title,
                            AdDateTime = ad.date + " " + ad.time,
                            AdDuration = ad.duration,
                            AdLocation = ad.location,
                            AdUser = ad.createdUser,
                            RequestDescription = etDescription.text.toString(),
                            RequestUser = mAuth.currentUser!!.email!!,
                            RequestUserName = "",
                            Status = 0
                        )
                        Database.getInstance(context).saveUserRequest(request) {
                            builder.dismiss()
                        }
                    }
                    builder.setCanceledOnTouchOutside(false)
                    builder.show()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        if (isEditable == true)
            inflater.inflate(R.menu.fragment_ad_details, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit -> {
                findNavController().navigate(R.id.action_ad_details_to_ad_edit, Bundle().apply {
                    putBoolean("edit", true)
                    putString("id", adId)
                })
                return true
            }
        }

        return false
    }
}