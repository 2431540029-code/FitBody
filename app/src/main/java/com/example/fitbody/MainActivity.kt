package com.example.fitbody

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.fitbody.ui.fragments.FavoriteFragment
import com.example.fitbody.ui.fragments.HomeFragment
import com.example.fitbody.ui.fragments.ProfileFragment
import com.example.fitbody.utils.ScheduleNotification
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        ScheduleNotification.scheduleMorning(this)
        ScheduleNotification.scheduleEvening(this)

        bottomNav = findViewById(R.id.bottomNav)

        replaceFragment(HomeFragment())

        bottomNav.setOnItemSelectedListener {

            when (it.itemId) {

                R.id.nav_home -> {
                    replaceFragment(HomeFragment())
                    true
                }

                R.id.nav_favorite -> {
                    replaceFragment(FavoriteFragment())
                    true
                }

                R.id.nav_profile -> {
                    replaceFragment(ProfileFragment())
                    true
                }

                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .commit()
    }
}