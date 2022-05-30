package com.group25.timebanking.fragments.ads

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.group25.timebanking.R
import com.group25.timebanking.adapters.RequestAdapter
import com.group25.timebanking.utils.Database

class RequestListFragment : Fragment() {

    private lateinit var tvEmpty: TextView
    private lateinit var rvRequestList: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_requests_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        tvEmpty = view.findViewById<TextView>(R.id.tvEmpty)
        rvRequestList = view.findViewById<RecyclerView>(R.id.rvRequestList)

    }

    override fun onResume(){
        super.onResume()

        Database.getInstance(context).getUserRequestList { requestsList ->

            if (requestsList.isEmpty()) {
                rvRequestList.visibility = View.GONE
                tvEmpty.visibility = View.VISIBLE
            } else {
                rvRequestList.visibility = View.VISIBLE
                tvEmpty.visibility = View.GONE
            }
            rvRequestList.layoutManager = LinearLayoutManager(context)
            rvRequestList.adapter = RequestAdapter(requestsList, requireContext())
        }
    }
}