package com.group25.timebanking.fragments.ads

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.group25.timebanking.utils.Database
import com.group25.timebanking.R
import com.group25.timebanking.activities.MainActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [AdDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AdDetailsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var isEditable: Boolean? = true

    private lateinit var tvTitle: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvDateTime: TextView
    private lateinit var tvDuration: TextView
    private lateinit var tvLocation: TextView

    private var adId: String = ""

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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AdDetailsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AdDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}