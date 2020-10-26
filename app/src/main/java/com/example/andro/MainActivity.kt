package com.example.andro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.andro.Fragments.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadFragment(HomeFragment())

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.search ->{
                    loadFragment(searchFragment())
                    true
                }
                R.id.notification ->{
                    loadFragment(notificationFragment())
                    true
                }
                R.id.profile ->{
                    loadFragment(profileFragment())
                    true
                }
                else -> false
            }
        }
    }


}

fun AppCompatActivity.loadFragment(fragment: Fragment)
{
    supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
}