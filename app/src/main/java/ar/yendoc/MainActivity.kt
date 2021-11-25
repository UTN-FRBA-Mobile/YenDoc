package ar.yendoc

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import ar.yendoc.ui.LoginFragment
import ar.yendoc.databinding.ActivityMainBinding
import ar.yendoc.ui.DashboardFragment
import com.google.android.material.navigation.NavigationView

import androidx.drawerlayout.widget.DrawerLayout
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import ar.yendoc.ui.AboutFragment
import ar.yendoc.ui.TabsFragment
import java.lang.Exception


class MainActivity : AppCompatActivity(), LoginFragment.OnFragmentInteractionListener, DashboardFragment.OnFragmentInteractionListener {

    private var mDrawer: DrawerLayout? = null
    private val toolbar: Toolbar? = null
    private var drawerTl: ActionBarDrawerToggle? = null
    private var idProfesional: Int = 0

    lateinit var binding: ActivityMainBinding
    private lateinit var loginFragment: Fragment
    private lateinit var dashboardFragment: Fragment
    private lateinit var tabsFragment: Fragment
    private var aboutFragment: Fragment = AboutFragment()
    private lateinit var mTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        idProfesional = sharedPref.getInt("id_profesional",0)//Levanta el id del profesional logueado

        if (idProfesional == 0) {
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
        var nameFragment: String? = null
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

        when (menuItem.itemId) {
            R.id.home -> {
                nameFragment = "DashboardFragment"
            }
            R.id.about -> {
                nameFragment = "AboutFragment"
            }
            R.id.logout -> {
                nameFragment = null
            }
        }

        if(menuItem.itemId == R.id.logout){
            val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
            sharedPref?.edit()?.putInt("id_profesional",0)?.apply()//Limpia el id del profesional que estaba logueado

            finish()
            startActivity(intent)
        }

        // Insert the fragment by replacing any existing fragment
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.container, fragment!!)
            .addToBackStack(nameFragment)
            .commit()

        // Highlight the selected item has been done by NavigationView
        menuItem.isChecked = true
        // Set action bar title
        mTitle.text = menuItem.title
        // Close the navigation drawer
        mDrawer!!.closeDrawers()
    }

    override fun onLogin(username: String, password: String) {

        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        val id_obtenido_profesional = 1
        sharedPref?.edit()?.putInt("id_profesional",id_obtenido_profesional)?.apply()//Guarda el id del profesional logueado

        dashboardFragment = DashboardFragment()
        supportFragmentManager.beginTransaction().remove(loginFragment).add(binding.container.id, dashboardFragment).commitNow()
    }

    override fun onSelectVisita(idVisita: Int) {
        tabsFragment = TabsFragment()//Pasar el id de visita
        supportFragmentManager.beginTransaction().add(binding.container.id, tabsFragment).addToBackStack(null)
            .commit()
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

    override fun onStart() {
        super.onStart()
        if(idProfesional > 0){//TODO: Verificar cuándo se necesita recargar la lista al levantar de nuevo la app.
            dashboardFragment = DashboardFragment()
            supportFragmentManager.beginTransaction().remove(dashboardFragment).add(binding.container.id, dashboardFragment).commitNow()
        }
    }
}