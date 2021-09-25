package ar.yendoc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
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
import android.R
import android.annotation.SuppressLint
import android.view.View
import androidx.core.view.get


class MainActivity : AppCompatActivity(), LoginFragment.OnFragmentInteractionListener {

    private var mDrawer: DrawerLayout? = null
    private val toolbar: Toolbar? = null
    private val nvDrawer: NavigationView? = null
    private var drawerTl: ActionBarDrawerToggle? = null

    // Make sure to be using androidx.appcompat.app.ActionBarDrawerToggle version.
    private val drawerToggle: ActionBarDrawerToggle? = null

    lateinit var binding: ActivityMainBinding
    private lateinit var loginFragment: Fragment
    private lateinit var dashboardFragment: Fragment

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
        drawerTl = setupDrawerToggle();

        // Setup toggle to display hamburger icon with nice animation
        drawerTl?.setDrawerIndicatorEnabled(true);
        drawerTl?.syncState();

        // Tie DrawerLayout events to the ActionBarToggle
        drawerTl?.let { mDrawer!!.addDrawerListener(it) };

        val mTitle = binding.toolbarView.toolbarTitle
        mTitle.text = getString(ar.yendoc.R.string.visitas)

        setupDrawerContent(nvDrawer);

        if (savedInstanceState == null) {
            loginFragment = LoginFragment()

            supportFragmentManager.beginTransaction()
                .replace(binding.container.id, loginFragment)
                .commitNow()
        }
    }
   /* override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_login)
    }*/
   private fun setupDrawerToggle(): ActionBarDrawerToggle? {
       // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
       // and will not render the hamburger icon without it.
       return ActionBarDrawerToggle(
           this,
           mDrawer,
           toolbar,
           R.string.ok,
           R.string.cancel
       )
   }

    private fun setupDrawerContent(navigationView: NavigationView?) {
        navigationView?.setNavigationItemSelectedListener { menuItem ->
            //selectDrawerItem(menuItem)
            true
        }
    }


    override fun onLogin(username: String, password: String) {
        dashboardFragment = DashboardFragment()

        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

        supportFragmentManager.beginTransaction().remove(loginFragment).add(binding.container.id, dashboardFragment).commitNow()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // The action bar home/up action should open or close the drawer.
        when (item.getItemId()) {
            android.R.id.home -> {
                mDrawer!!.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}