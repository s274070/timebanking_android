package com.group25.timebanking.ads

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.group25.timebanking.utils.Database
import com.group25.timebanking.R
import java.lang.StringBuilder
import java.util.*
import com.group25.timebanking.extensions.toString
import com.group25.timebanking.models.Ads

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AdEditFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AdEditFragment : Fragment() {
    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_IMAGE_GALLERY = 2

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var etTitle: TextInputLayout
    private lateinit var etLocation: TextInputLayout
    private lateinit var etDateTime: TextInputLayout
    private lateinit var etDuration: TextInputLayout
    private lateinit var etDescription: TextInputLayout

    private lateinit var snackBar: Snackbar;

    private var dateValue: Date = Date()
    private var timeValue: String = "12:10"
    private var dateOk: Boolean = false

    private var edit: Boolean? = false
    private var index = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_ad_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        etTitle = view.findViewById(R.id.etTitle)
        etLocation = view.findViewById(R.id.etLocation)
        etDateTime = view.findViewById(R.id.etDateTime)
        etDuration = view.findViewById(R.id.etDuration)
        etDescription = view.findViewById(R.id.etDescription)


        if (savedInstanceState != null){
            etTitle.editText?.setText(savedInstanceState.getString("title"))
            etLocation.editText?.setText(savedInstanceState.getString("location"))
            dateValue.time = savedInstanceState.getLong("date", Date().time)
            timeValue = savedInstanceState.getString("timeValue","12:10")
            dateOk = savedInstanceState.getBoolean("dateok", false)
            etDuration.editText?.setText(savedInstanceState.getString("duration"))
            etDescription.editText?.setText(savedInstanceState.getString("description"))
            if (dateOk)
                etDateTime.editText?.setText(dateValue.toString("dd/MM/yyyy")+" - "+timeValue)
        }
    }

    override fun onResume(){
        super.onResume()

        edit = arguments?.getBoolean("edit")
        if (edit!!){
            index = arguments?.getInt("index")!!

            val ad = Database.getInstance(context).adsList[index]
            etTitle.editText?.setText(ad.title)
            etLocation.editText?.setText(ad.location)
            etDateTime.editText?.setText(ad.date.toString("dd/MM/yyyy")+" - "+ad.time)
            etDuration.editText?.setText(ad.duration.toString())
            etDescription.editText?.setText(ad.description)

            dateValue = ad.date
            timeValue = ad.time
            dateOk = true
        }

        val datePicker =
            MaterialDatePicker.Builder.datePicker().setTitleText("Select date").setSelection(dateValue.time).build()

        val timePicker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(timeValue.split(":")[0].toInt())
                .setMinute(timeValue.split(":")[1].toInt())
                .setTitleText("Select time")
                .build()

        etDateTime.editText?.setOnClickListener {
            dateOk = false
            activity?.supportFragmentManager?.let {
                    it1 -> datePicker.show(it1, "selectdate")
            }
        }

        datePicker.addOnPositiveButtonClickListener {
            dateValue.time = it
            activity?.supportFragmentManager?.let {
                    it1 -> timePicker.show(it1, "selecttime")
            }
        }
        timePicker.addOnPositiveButtonClickListener {
            dateOk = true
            timeValue = timePicker.hour.toString()+":"+timePicker.minute.toString()
            etDateTime.editText?.setText(dateValue.toString("dd/MM/yyyy")+" - "+timeValue)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("title", etTitle.editText?.text.toString())
        outState.putString("location", etLocation.editText?.text.toString())
        outState.putLong("date", dateValue.time)
        outState.putString("timeValue", timeValue)
        outState.putBoolean("dateok", dateOk)
        outState.putString("duration", etDuration.editText?.text.toString())
        outState.putString("description", etDescription.editText?.text.toString())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.fragment_ad_edit, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.save -> {
                //save data
                if (validateForm()){

                    val ad = Ads(
                        etTitle.editText?.text.toString(),
                        etDescription.editText?.text.toString(),
                        dateValue,
                        timeValue,
                        etDuration.editText?.text.toString().toInt(),
                                etLocation.editText?.text.toString())

                    if (edit!!)
                        Database.getInstance(context).adsList[index] = ad
                    else
                        Database.getInstance(context).adsList.add(ad)

                    //save
                    Database.getInstance(context).save()
                    snackBar = Snackbar.make( requireView().getRootView().findViewById(R.id.coordinatorLayout), "Ad updated correctly", Snackbar.LENGTH_LONG)
                    snackBar.setAction("Dismiss"){
                        snackBar.dismiss()
                    }
                    snackBar.show()

                    findNavController().navigate(R.id.action_ad_edit_to_ads_list)
                }
                return true
            }
            android.R.id.home -> {
                //Handling the toolbar back button
                findNavController().navigate(R.id.action_ad_edit_to_ads_list)
                return true
            }
        }
        return false
    }

    private fun validateForm(): Boolean{
        var res = true

        if (etTitle.editText?.text.toString() == ""){
            etTitle.error = "Provide a value"
            res = false
        }
        if (etLocation.editText?.text.toString() == ""){
            etLocation.error = "Provide a value"
            res = false
        }
        if (etDuration.editText?.text.toString() == ""){
            etDuration.error = "Provide a value"
            res = false
        }
        if (!dateOk) {
            etDateTime.error = "Provide a value"
            res = false
        }
        return res
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        activity?.menuInflater?.inflate(R.menu.activity_edit_profile_photo_menu, menu)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AdEditFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AdEditFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}