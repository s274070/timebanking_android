package com.group25.timebanking.fragments.ads

import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.group25.timebanking.R
import com.group25.timebanking.adapters.AdAdapter
import com.group25.timebanking.utils.Database
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [AdListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */


class AdListFragment : Fragment() {

    private lateinit var tvEmpty: TextView
    private lateinit var rvAdsList: RecyclerView

    var adapter: AdAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ad_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        tvEmpty = view.findViewById<TextView>(R.id.tvEmpty)
        rvAdsList = view.findViewById<RecyclerView>(R.id.rvAdsList)

        //TODO: set actionbar title

    }

    override fun onResume(){
        super.onResume()

        Database.getInstance(context).getAdsList { adsList ->

            if (adsList.isEmpty()) {
                rvAdsList.visibility = View.GONE
                tvEmpty.visibility = View.VISIBLE
            } else {
                rvAdsList.visibility = View.VISIBLE
                tvEmpty.visibility = View.GONE
            }
            rvAdsList.layoutManager = LinearLayoutManager(context)
            adapter = AdAdapter(adsList, "AdListFragment", parentFragmentManager)
            rvAdsList.adapter = adapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_search, menu)

        val searchItem: MenuItem = menu.findItem(R.id.actionSearch)
        val searchView: SearchView = searchItem.actionView as SearchView

        searchView.imeOptions = EditorInfo.IME_ACTION_DONE
        searchView.queryHint = "Title, Location, Date";

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter?.filter?.filter(newText)
                return false
            }
        })
    }
}