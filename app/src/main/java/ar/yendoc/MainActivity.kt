package ar.yendoc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import ar.yendoc.ui.LoginFragment
import ar.yendoc.databinding.ActivityMainBinding
import ar.yendoc.ui.DashboardFragment
import com.google.android.material.navigation.NavigationView

import androidx.drawerlayout.widget.DrawerLayout
import androidx.core.view.GravityCompat
import ar.yendoc.ui.AboutFragment
import java.lang.Exception


class MainActivity : AppCompatActivity(), LoginFragment.OnFragmentInteractionListener {

    private var mDrawer: DrawerLayout? = null
    private val toolbar: Toolbar? = null
    private var drawerTl: ActionBarDrawerToggle? = null

    lateinit var binding: ActivityMainBinding
    private lateinit var loginFragment: Fragment
    private lateinit var dashboardFragment: Fragment
    private var aboutFragment: Fragment = AboutFragment()
    private lateinit var mTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setContentView(R.layout.fragment_login)

        setSupportActionBar(binding.toolbarView.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // This will display an Up icon (<-), we will replace it with hamburger later
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Find our drawer view
        mDrawer = binding.drawerLayout
        drawerTl = setupDrawerToggle()

        // Setup toggle to display hamburger icon with nice animation
        drawerTl?.isDrawerIndicatorEnabled = true
        drawerTl?.syncState()

        // Tie DrawerLayout events to the ActionBarToggle
        drawerTl?.let { mDrawer!!.addDrawerListener(it) }

        mTitle = binding.toolbarView.toolbarTitle
        mTitle.text = getString(R.string.visitas)

        var nvDrawer: NavigationView = findViewById(R.id.nvView)
        setupDrawerContent(nvDrawer)

        if (savedInstanceState == null) {
            loginFragment = LoginFragment()

            supportFragmentManager.beginTransaction()
                .replace(R.id.container, loginFragment)
                .commitNow()
        }
    }

   private fun setupDrawerToggle(): ActionBarDrawerToggle {
       // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
       // and will not render the hamburger icon without it.
       return ActionBarDrawerToggle(
           this,
           mDrawer,
           toolbar,
           R.string.drawer_open,
           R.string.drawer_close
       )
   }

    private fun setupDrawerContent(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            selectDrawerItem(menuItem)
            true
        }
    }

    private fun selectDrawerItem(menuItem: MenuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        var fragment: Fragment? = null
        val fragmentClass: Class<*> = when (menuItem.itemId) {
            R.id.home -> dashboardFragment::class.java
            R.id.about -> aboutFragment::class.java
            R.id.logout -> dashboardFragment::class.java
            else -> loginFragment::class.java
        }
        try {
            fragment = fragmentClass.newInstance() as Fragment
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if(menuItem.itemId == R.id.logout){
            finish()
            startActivity(intent)
        }

        // Insert the fragment by replacing any existing fragment
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.container, fragment!!).commit()

        // Highlight the selected item has been done by NavigationView
        menuItem.isChecked = true
        // Set action bar title
        mTitle.text = menuItem.title
        // Close the navigation drawer
        mDrawer!!.closeDrawers()
    }

    override fun onLogin(username: String, password: String) {
        dashboardFragment = DashboardFragment()

        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

        supportFragmentManager.beginTransaction().remove(loginFragment).add(binding.container.id, dashboardFragment).commitNow()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // The action bar home/up action should open or close the drawer.
        when (item.itemId) {
            android.R.id.home -> {
                mDrawer!!.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}