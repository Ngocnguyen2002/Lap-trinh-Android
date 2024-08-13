package com.example.renthouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val navController: NavController = findNavController(R.id.fragmentContainerView)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.setupWithNavController(navController)

        if (intent.getBooleanExtra("SHOW_HISTORY_FRAGMENT", false)) {
            navController.navigate(R.id.historyFragment)
            val newIntent = Intent(this, HomeActivity::class.java)
            newIntent.putExtra("SHOW_HISTORY_FRAGMENT", false)
            startActivity(newIntent)
        }
    }

}