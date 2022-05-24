package com.group25.timebanking.fragments.ads

import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.group25.timebanking.R
import com.group25.timebanking.adapters.AdAdapter
import com.group25.timebanking.adapters.AdSkillsAdapter
import com.group25.timebanking.utils.Database
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [AdSkillsListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */


class AdSkillsListFragment : Fragment() {

    private lateinit var tvEmpty: TextView
    private lateinit var rvAdSkillsList: RecyclerView

    var adapter: AdSkillsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ad_skills_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        tvEmpty = view.findViewById<TextView>(R.id.tvEmpty)
        rvAdSkillsList = view.findViewById<RecyclerView>(R.id.rvAdSkillsList)

    }

    override fun onResume(){
        super.onResume()

        Database.getInstance(context).getAdsSkillsList { dataList ->

            if (dataList.isEmpty()) {
                rvAdSkillsList.visibility = View.GONE
                tvEmpty.visibility = View.VISIBLE
            } else {
                rvAdSkillsList.visibility = View.VISIBLE
                tvEmpty.visibility = View.GONE
            }
            rvAdSkillsList.layoutManager = LinearLayoutManager(context)
            adapter = AdSkillsAdapter(dataList.toMutableList(), "AdSkillsListFragment", parentFragmentManager)
            rvAdSkillsList.adapter = adapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_search, menu)

        val searchItem: MenuItem = menu.findItem(R.id.actionSearch)
        val searchView: SearchView = searchItem.actionView as SearchView

        searchView.imeOptions = EditorInfo.IME_ACTION_DONE
        searchView.queryHint = "Title";

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