package com.group25.timebanking.fragments.ads

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.group25.timebanking.R
import com.group25.timebanking.adapters.SessionAdapter
import com.group25.timebanking.utils.Database

class SessionListFragment : Fragment() {

    private lateinit var tvEmpty: TextView
    private lateinit var rvSessionsList: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sessions_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        tvEmpty = view.findViewById<TextView>(R.id.tvEmpty)
        rvSessionsList = view.findViewById<RecyclerView>(R.id.rvSessionsList)

    }

    override fun onResume(){
        super.onResume()
        loadData()
    }

    fun loadData(){

        Database.getInstance(context).getUserSessionList { sessionList ->

            if (sessionList.isEmpty()) {
                rvSessionsList.visibility = View.GONE
                tvEmpty.visibility = View.VISIBLE
            } else {
                rvSessionsList.visibility = View.VISIBLE
                tvEmpty.visibility = View.GONE
            }
            rvSessionsList.layoutManager = LinearLayoutManager(context)
            rvSessionsList.adapter = SessionAdapter(sessionList, requireContext(), layoutInflater)
        }
    }
}