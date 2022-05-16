package com.group25.timebanking.activities

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.group25.timebanking.R
import com.group25.timebanking.utils.Database
import com.group25.timebanking.utils.SharedPrefs
import org.json.JSONObject
import java.io.File

class MainActivity : AppCompatActivity() {
    companion object {
        lateinit var mAuth: FirebaseAuth
    }

    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var tvHeaderName: TextView
    private lateinit var ivHeaderProfileImage: ImageView
    private lateinit var tvHeaderEmail: TextView
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        mAuth = FirebaseAuth.getInstance()


        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.ads_list
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    fun loadData() {
        val drawer = navView.getHeaderView(0)

        tvHeaderName = drawer.findViewById<TextView>(R.id.headerName)
        ivHeaderProfileImage = drawer.findViewById<ImageView>(R.id.headerProfileImage)
        tvHeaderEmail = drawer.findViewById<TextView>(R.id.headermail)


        Database.getInstance(this).getUserByEmail(mAuth.currentUser!!.email!!) { user ->
            if (user != null) {
                tvHeaderName.text = user.fullName
                tvHeaderEmail.text = user.email
            }
        }

        File(filesDir, "profilepic.jpg").let {
            if (it.exists()) ivHeaderProfileImage.setImageBitmap(BitmapFactory.decodeFile(it.absolutePath))
        }
    }

    override fun onResume() {
        super.onResume()

        loadData()
    }

    override fun onSupportNavigateUp(): Boolean {
        loadData()
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


}