package com.group25.timebanking.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toBitmap
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.group25.timebanking.R
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream


class ShowProfileFragment : Fragment() {
    private lateinit var tvFullName: TextView
    private lateinit var tvNickName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvLocation: TextView
    private lateinit var tvSkills: TextView
    private lateinit var tvDescription: TextView
    private lateinit var imgProfile: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // enable option edit
        setHasOptionsMenu(true)


        setFragmentResultListener("requestKeyEditToShow") { requestKey, bundle ->
            tvFullName.text = bundle.getString("group25.timebanking.fullName")
            tvNickName.text = bundle.getString("group25.timebanking.nickName")
            tvEmail.text = bundle.getString("group25.timebanking.email")
            tvLocation.text = bundle.getString("group25.timebanking.location")
            tvSkills.text = bundle.getString("group25.timebanking.skills")
            tvDescription.text = bundle.getString("group25.timebanking.description")
            val fileName: String = bundle.getString("group25.timebanking.profile") ?: ""
            File(context?.filesDir, fileName).let {
                if (it.exists()) imgProfile
                    .setImageBitmap(BitmapFactory.decodeFile(it.absolutePath))
            }

            val profileData = JSONObject().also {
                it.put("fullName", tvFullName.text)
                it.put("nickName", tvNickName.text)
                it.put("email", tvEmail.text)
                it.put("location", tvLocation.text)
                it.put("skills", tvSkills.text)
                it.put("description", tvDescription.text)
            }

            //store data persistently
            val sharedPref = requireActivity().getSharedPreferences(
                getString(R.string.preference_file_key),
                Context.MODE_PRIVATE
            )

            with(sharedPref.edit()) {
                putString("profile", profileData.toString())
                apply()
            }
        }


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_activity_show_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        tvFullName = view.findViewById(R.id.tvFullName)
        tvNickName = view.findViewById(R.id.tvNickName)
        tvEmail = view.findViewById(R.id.tvEmail)
        tvLocation = view.findViewById(R.id.tvLocation)
        tvSkills = view.findViewById(R.id.tvSkills)
        tvDescription = view.findViewById(R.id.tvDescription)
        imgProfile = view.findViewById(R.id.imgProfile)

        val sharedPref = requireActivity().getSharedPreferences(
            getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )
//        val sharedPref = getSharedPreferences(
//            getString(R.string.preference_file_key), Context.MODE_PRIVATE
//        )
        val data = sharedPref.getString("profile", null)
        if (data != null)
            with(JSONObject(data)) {
                tvFullName.text = getString("fullName")
                tvNickName.text = getString("nickName")
                tvEmail.text = getString("email")
                tvLocation.text = getString("location")
                tvSkills.text = getString("skills")
                tvDescription.text = getString("description")
            }
        File(context?.filesDir, "profilepic.jpg").let {
            if (it.exists()) imgProfile.setImageBitmap(BitmapFactory.decodeFile(it.absolutePath))
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.activity_show_profile_menu, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.editMenu -> {
                editProfile()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            tvFullName.text = data?.getStringExtra("group25.timebanking.fullName")
            tvNickName.text = data?.getStringExtra("group25.timebanking.nickName")
            tvEmail.text = data?.getStringExtra("group25.timebanking.email")
            tvLocation.text = data?.getStringExtra("group25.timebanking.location")
            tvSkills.text = data?.getStringExtra("group25.timebanking.skills")
            tvDescription.text = data?.getStringExtra("group25.timebanking.description")
            File(context?.filesDir, "profilepic.jpg").let {
                if (it.exists()) {
                    imgProfile.setImageBitmap(BitmapFactory.decodeFile(it.absolutePath))
                    val file = File(context?.filesDir, "profilepic.jpg")
                    val out = FileOutputStream(file)
                    imgProfile.drawable.toBitmap().compress(Bitmap.CompressFormat.JPEG, 85, out)
                    out.flush()
                    out.close()
                }
            }
            //serialize data into a JSON object
            val profileData = JSONObject().also {
                it.put("fullName", tvFullName.text)
                it.put("nickName", tvNickName.text)
                it.put("email", tvEmail.text)
                it.put("location", tvLocation.text)
                it.put("skills", tvSkills.text)
                it.put("description", tvDescription.text)
            }

            //store data persistently
            val sharedPref = requireActivity().getSharedPreferences(
                getString(R.string.preference_file_key),
                Context.MODE_PRIVATE
            )

            with(sharedPref.edit()) {
                putString("profile", profileData.toString())
                apply()
            }


        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("group25.timebanking.fullName", tvFullName.text.toString())
        outState.putString("group25.timebanking.nickName", tvNickName.text.toString())
        outState.putString("group25.timebanking.email", tvEmail.text.toString())
        outState.putString("group25.timebanking.location", tvLocation.text.toString())
        outState.putString("group25.timebanking.skills", tvSkills.text.toString())
        outState.putString("group25.timebanking.description", tvDescription.text.toString())
        outState.putString("group25.timebanking.profile", "profilepic.jpg")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            tvFullName.text = savedInstanceState.getString("group25.timebanking.fullName")
            tvNickName.text = savedInstanceState.getString("group25.timebanking.nickName")
            tvEmail.text = savedInstanceState.getString("group25.timebanking.email")
            tvLocation.text = savedInstanceState.getString("group25.timebanking.location")
            tvSkills.text = savedInstanceState.getString("group25.timebanking.skills")
            tvDescription.text = savedInstanceState.getString("group25.timebanking.description")
            File(
                context?.filesDir,
                savedInstanceState.getString("group25.timebanking.profile") ?: ""
            ).let {
                if (it.exists()) imgProfile.setImageBitmap(BitmapFactory.decodeFile(it.absolutePath))
            }
        }
    }


    private fun editProfile() {
        setFragmentResult(
            "requestKeyShowToEdit", bundleOf(
                "group25.timebanking.fullName" to tvFullName.text.toString(),
                "group25.timebanking.nickName" to tvNickName.text.toString(),
                "group25.timebanking.email" to tvEmail.text.toString(),
                "group25.timebanking.location" to tvLocation.text.toString(),
                "group25.timebanking.skills" to tvSkills.text.toString(),
                "group25.timebanking.description" to tvDescription.text.toString(),
                "group25.timebanking.profile" to "profilepic.jpg"
            )

        )
        findNavController().navigate(R.id.action_showProfileActivity_to_editProfileActivity)
    }
}