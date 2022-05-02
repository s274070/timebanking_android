package com.group25.timebanking.ads

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import com.group25.timebanking.R
import com.group25.timebanking.adapters.AdAdapter
import com.group25.timebanking.utils.Database
import java.util.*
import kotlin.collections.ArrayList
import com.group25.timebanking.models.Ads


/**
 * A simple [Fragment] subclass.
 * Use the [AdListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */


class AdListFragment : Fragment() {

    private lateinit var tvEmpty: TextView
    private lateinit var rvAdsList: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ad_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        val addFab = view.findViewById<FloatingActionButton>(R.id.addFab)
        addFab.setOnClickListener {
            findNavController().navigate(R.id.action_ad_list_to_ad_edit, Bundle().apply {
                putBoolean("edit", false)
            })
        }

        tvEmpty = view.findViewById<TextView>(R.id.tvEmpty)
        rvAdsList = view.findViewById<RecyclerView>(R.id.rvAdsList)

        //TODO: set actionbar title

    }

    override fun onResume(){
        super.onResume()

        val adsList: ArrayList<Ads> = Database.getInstance(context).adsList

        if(adsList.count() == 0){
            rvAdsList.visibility = View.GONE
            tvEmpty.visibility = View.VISIBLE
        } else {
            rvAdsList.visibility = View.VISIBLE
            tvEmpty.visibility = View.GONE
        }
        rvAdsList.layoutManager = LinearLayoutManager(context)
        rvAdsList.adapter = AdAdapter(adsList, parentFragmentManager)
    }
}