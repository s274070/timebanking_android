package com.group25.timebanking.fragments.profile

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
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.group25.timebanking.R
import com.group25.timebanking.activities.MainActivity
import com.group25.timebanking.extensions.toString
import com.group25.timebanking.models.Ads
import com.group25.timebanking.models.Users
import com.group25.timebanking.utils.Database
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

    private lateinit var snackBar: Snackbar

    private var isEditable: Boolean? = true
    private var userId: String = ""
    private var userEmail: String = ""


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
    }

    override fun onResume() {
        super.onResume()
        //FirebaseAuth.getInstance().currentUser!!.email
        userEmail = if(arguments?.containsKey("userId") == true)
            arguments?.getString("userId", "")!!
        else{
            FirebaseAuth.getInstance().currentUser!!.email!!
        }
        isEditable = arguments?.getBoolean("editable", true)

        Database.getInstance(context).getUserByEmail(userEmail) { user ->
            if (user != null) {
                userId = user.id
                tvFullName.text = user.fullName
                tvNickName.text = user.nickName
                tvEmail.text = user.email
                tvLocation.text = user.location
                tvSkills.text = user.skills
                tvDescription.text = user.description
            }
        }

        if (isEditable == true) {
            File(context?.filesDir, "profilepic.jpg").let {
                if (it.exists()) imgProfile.setImageBitmap(BitmapFactory.decodeFile(it.absolutePath))
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if (isEditable == true)
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


            val user = Users(
                userId,
                tvEmail.text.toString(),
                tvFullName.text.toString(),
                tvNickName.text.toString(),
                tvSkills.text.toString(),
                tvDescription.text.toString(),
                tvLocation.text.toString()
            )

            snackBar = Snackbar.make(
                requireView().getRootView().findViewById(R.id.coordinatorLayout),
                "Profile updated correctly",
                Snackbar.LENGTH_LONG
            )
            snackBar.setAction("Dismiss") {
                snackBar.dismiss()
            }

            if (isEditable == true) {
                Database.getInstance(context).saveUser(user) {
                    snackBar.show()
                }
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