package com.group25.timebanking.fragments.profile

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.group25.timebanking.R
import java.io.File
import java.io.FileOutputStream

class EditProfileFragment : Fragment() {
    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_IMAGE_GALLERY = 2
    private var profileChanged = false

    private lateinit var etFullName: EditText
    private lateinit var etNickName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etLocation: EditText
    private lateinit var imgProfile: ImageView
    private lateinit var etSkills: EditText
    private lateinit var etDescription: EditText
    private lateinit var snackBar: Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this,
            object: OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.action_editProfileActivity_to_showProfileActivity)
                }
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_activity_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val btnImageEdit = view.findViewById<ImageButton>(R.id.imageButtonEdit)
        // open context menu by clicking on btnImage
        btnImageEdit?.setOnClickListener {
            registerForContextMenu(btnImageEdit)
            activity?.openContextMenu(btnImageEdit)
            unregisterForContextMenu(btnImageEdit);
        }

        etFullName = view.findViewById(R.id.etFullName)
        etNickName = view.findViewById(R.id.etNickName)
        etEmail = view.findViewById(R.id.etEmail)
        etLocation = view.findViewById(R.id.etLocation)
        imgProfile = view.findViewById(R.id.imgProfile)
        etSkills = view.findViewById(R.id.etSkills)
        etDescription = view.findViewById(R.id.etDescription)

        setFragmentResultListener("requestKeyShowToEdit") { _, bundle ->
            etFullName.setText(bundle.getString("group25.timebanking.fullName"))
            etNickName.setText(bundle.getString("group25.timebanking.nickName"))
            etEmail.setText(bundle.getString("group25.timebanking.email"))
            etLocation.setText(bundle.getString("group25.timebanking.location"))
            etSkills.setText(bundle.getString("group25.timebanking.skills"))
            etDescription.setText(bundle.getString("group25.timebanking.description"))
            val fileName: String = bundle.getString("group25.timebanking.profile") ?: ""
            File(context?.filesDir, fileName).let {
                if (it.exists()) imgProfile
                    .setImageBitmap(BitmapFactory.decodeFile(it.absolutePath))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.activity_edit_save, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        activity?.menuInflater?.inflate(R.menu.activity_edit_profile_photo_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        imgProfile.invalidate()
        return when (item.itemId) {
            R.id.addGallery -> {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                startActivityForResult(intent, REQUEST_IMAGE_GALLERY)
                true
            }
            R.id.addCamera -> {
                dispatchTakePictureIntent()
                true
            }
            else -> false
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.saveEdit -> {
                if (profileChanged){
                    saveImageOnStorage(imgProfile.drawable.toBitmap())
                }
                setFragmentResult(
                    "requestKeyEditToShow", bundleOf(
                        "group25.timebanking.fullName" to etFullName.text.toString(),
                        "group25.timebanking.nickName" to etNickName.text.toString(),
                        "group25.timebanking.email" to etEmail.text.toString(),
                        "group25.timebanking.location" to etLocation.text.toString(),
                        "group25.timebanking.skills" to etSkills.text.toString(),
                        "group25.timebanking.description" to etDescription.text.toString(),
                        "group25.timebanking.profile" to "profilepic.jpg"
                    )
                )

                findNavController().navigate(R.id.action_editProfileActivity_to_showProfileActivity)
                true
            }
            android.R.id.home -> { // the back button on action bar
                findNavController().navigate(R.id.action_editProfileActivity_to_showProfileActivity)
                true
            }

            else -> false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("group25.timebanking.fullName", etFullName.text.toString())
        outState.putString("group25.timebanking.nickName", etNickName.text.toString())
        outState.putString("group25.timebanking.email", etEmail.text.toString())
        outState.putString("group25.timebanking.location", etLocation.text.toString())
        outState.putString("group25.timebanking.skills", etSkills.text.toString())
        outState.putString("group25.timebanking.description", etDescription.text.toString())
        outState.putString("group25.timebanking.image", "profilepictemp.jpg")
        outState.putBoolean("group25.timebanking.profileChanged", profileChanged)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            etFullName.setText(savedInstanceState.getString("group25.timebanking.fullName"))
            etNickName.setText(savedInstanceState.getString("group25.timebanking.nickName"))
            etEmail.setText(savedInstanceState.getString("group25.timebanking.email"))
            etLocation.setText(savedInstanceState.getString("group25.timebanking.location"))
            etSkills.setText(savedInstanceState.getString("group25.timebanking.skills"))
            etDescription.setText(savedInstanceState.getString("group25.timebanking.description"))
            profileChanged = savedInstanceState.getBoolean("group25.timebanking.profileChanged")
//            if (profileChanged)
            File(context?.filesDir, savedInstanceState.getString("group25.timebanking.image") ?: "").let {
                if (it.exists()) imgProfile.setImageBitmap(BitmapFactory.decodeFile(it.absolutePath))
            }
        }
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
            Toast.makeText(context, "error $e", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == AppCompatActivity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imgProfile.setImageBitmap(imageBitmap)
            profileChanged = true
            saveImageOnStorage(imageBitmap, "profilepictemp.jpg")
        } else if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == AppCompatActivity.RESULT_OK) {
            imgProfile.setImageURI(data?.data)
            profileChanged = true
            saveImageOnStorage(imgProfile.drawable.toBitmap(), "profilepictemp.jpg")
        }
    }

    private fun saveImageOnStorage(bitmap: Bitmap, tempName: String = "profilepic.jpg") {
        val file = File(context?.filesDir, tempName)
        val out = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out)
        out.flush()
        out.close()
    }

}


