package com.example.friends.bookstore.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.widget.FrameLayout
import com.example.friends.bookstore.*
import com.example.friends.bookstore.fragment.AboutAppFragment
import com.example.friends.bookstore.fragment.DashboardFragment
import com.example.friends.bookstore.fragment.FavouritesFragment
import com.example.friends.bookstore.fragment.ProfileFragment

class MainActivity : AppCompatActivity() {
    lateinit var drawerLayout : DrawerLayout
    lateinit var coordinatorLayout : CoordinatorLayout
    lateinit var toolbar: android.support.v7.widget.Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView: NavigationView
    var previousMenuItem: MenuItem?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawerLayout)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        toolbar = findViewById(R.id.toolbar)
        frameLayout = findViewById(R.id.frameLayout)
        navigationView = findViewById(R.id.navigation_view)

        setUpToolbar()
        openDashboard()


        val actionBarDrawerToggle =ActionBarDrawerToggle(this@MainActivity,drawerLayout,
                R.string.open_drawer, R.string.close_drawer)

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener {

            if(previousMenuItem!=null){
                previousMenuItem?.isChecked=false
            }
            it.isCheckable=true
            it.isChecked=true
            previousMenuItem=it
            when(it.itemId){
                R.id.dashboard ->{
                    openDashboard()

                    drawerLayout.closeDrawers()
                }

                R.id.favourites ->{
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, FavouritesFragment())
                            .commit()

                    supportActionBar?.title="Favourites"

                    drawerLayout.closeDrawers()
                }

                R.id.profile ->{
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, ProfileFragment())
                            .commit()

                    supportActionBar?.title="Profile"

                    drawerLayout.closeDrawers()
                }

                R.id.about_app ->{
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, AboutAppFragment())
                            .commit()

                    supportActionBar?.title="About App"

                    drawerLayout.closeDrawers()
                }


            }
            return@setNavigationItemSelectedListener true
        }

    }

    fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title="Toolbar Title"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if(id==android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START)
        }

        return super.onOptionsItemSelected(item)
    }

    fun openDashboard(){
        val fragment = DashboardFragment()
        val transaction= supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.commit()
        navigationView.setCheckedItem(R.id.dashboard)

        supportActionBar?.title="Dashboard"

    }

    override fun onBackPressed() {
        val frag=supportFragmentManager.findFragmentById(R.id.frameLayout)
        when(frag){
            !is DashboardFragment -> openDashboard()
            else -> super.onBackPressed()
        }
    }
}
