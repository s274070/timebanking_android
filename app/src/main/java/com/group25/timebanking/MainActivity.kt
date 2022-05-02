package com.group25.timebanking

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.group25.timebanking.utils.Database
import org.json.JSONObject
import java.io.File

class MainActivity : AppCompatActivity() {

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

        Database.getInstance(this).load()

//        val fab: FloatingActionButton = findViewById(R.id.fab)
//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }


        val coordinatorLayout = findViewById<CoordinatorLayout>(R.id.coordinatorLayout)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        loadData()

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

    /*override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }*/

    fun loadData() {
        val drawer = navView.getHeaderView(0)

        tvHeaderName = drawer.findViewById<TextView>(R.id.headerName)
        ivHeaderProfileImage = drawer.findViewById<ImageView>(R.id.headerProfileImage)
        tvHeaderEmail = drawer.findViewById<TextView>(R.id.headermail)

        val sharedPref = getSharedPreferences(
            getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )

        val data = sharedPref.getString("profile", null)

        if (data != null)
            with(JSONObject(data)) {
                tvHeaderName.text = getString("fullName")
                tvHeaderEmail.text = getString("email")
            }

        File(filesDir, "profilepic.jpg").let {
            if (it.exists()) ivHeaderProfileImage.setImageBitmap(BitmapFactory.decodeFile(it.absolutePath))
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        loadData()
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


}