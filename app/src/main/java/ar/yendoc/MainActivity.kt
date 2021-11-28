package ar.yendoc

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import ar.yendoc.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.core.view.GravityCompat
import ar.yendoc.ui.*
import java.lang.Exception
import android.view.View
import ar.yendoc.network.Profesional


class MainActivity : AppCompatActivity(), LoginFragment.OnFragmentInteractionListener, DashboardFragment.OnFragmentInteractionListener {

    private var mDrawer: DrawerLayout? = null
    private val toolbar: Toolbar? = null
    private var drawerTl: ActionBarDrawerToggle? = null
    private var idProfesional: Int = 0
    lateinit var binding: ActivityMainBinding
    private lateinit var sharedPref: SharedPreferences
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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mDrawer = binding.drawerLayout
        drawerTl = setupDrawerToggle()

        drawerTl?.isDrawerIndicatorEnabled = true
        drawerTl?.syncState()

        drawerTl?.let { mDrawer!!.addDrawerListener(it) }

        mTitle = binding.toolbarView.toolbarTitle
        mTitle.text = getString(R.string.visitas)

        var nvDrawer: NavigationView = findViewById(R.id.nvView)
        setupDrawerContent(nvDrawer)

        sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        idProfesional = sharedPref.getInt(getString(R.string.id_profesional),0)//Levanta el id del profesional logueado

        if (idProfesional == 0) {
            loginFragment = LoginFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, loginFragment)
                .commitNow()
        }
        else{
            setNombreProfesional()
            dashboardFragment = DashboardFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, dashboardFragment)
                .commitNow()
        }

        supportFragmentManager.addOnBackStackChangedListener {
            val fragment: Fragment? = getFragment()
            setTitleFromFragment(fragment)
        }
    }

    private fun getFragment(): Fragment? {
        return supportFragmentManager.findFragmentById(R.id.container)
    }

    private fun setNombreProfesional() {
        val navigationView = binding.nvView as NavigationView
        val headerView: View = navigationView.getHeaderView(0)
        headerView.findViewById<TextView>(R.id.doctor_name).text = sharedPref.getString(getString(R.string.nombre_profesional), getString(R.string.nombre_generico))

    }

    private fun setTitleFromFragment(fragment: Fragment?) {
        var nombreTitulo = when (fragment?.javaClass?.simpleName) {
            getString(R.string.dashboard_fragment) -> {
                getString(R.string.visitas)
            }
            getString(R.string.about_fragment) -> {
                getString(R.string.acerca_de)
            }
            getString(R.string.tabs_fragment) -> {
                getString(R.string.visita)
            }
            else ->
                ""
        }
        mTitle.text = nombreTitulo
    }

   private fun setupDrawerToggle(): ActionBarDrawerToggle {
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
                nameFragment = getString(R.string.dashboard_fragment)
            }
            R.id.about -> {
                nameFragment = getString(R.string.about_fragment)
            }
            R.id.logout -> {
                nameFragment = null

                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setTitle(getString(R.string.salir))
                builder.setMessage(getString(R.string.seguro_cerrar_sesion))

                builder.setPositiveButton(
                    getString(R.string.si),
                    DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                        sharedPref?.edit()?.putInt(getString(R.string.id_profesional),0)?.apply()//Limpia el id del profesional que estaba logueado

                        finish()
                        startActivity(intent)
                    })

                builder.setNegativeButton(
                    getString(R.string.no),
                    DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                    })

                val alert: AlertDialog = builder.create()
                alert.show()
            }
        }

        if(nameFragment != null){
            val fragmentManager = supportFragmentManager
            fragmentManager.beginTransaction().replace(R.id.container, fragment!!)
                .addToBackStack(nameFragment)
                .commit()

            mTitle.text = menuItem.title
            mDrawer!!.closeDrawers()
        }
        menuItem.isCheckable = false
    }

    override fun onLogin(profesional : Profesional) {
        sharedPref?.edit()?.putInt(getString(R.string.id_profesional), profesional.profesional_id)?.apply()//Guarda el id del profesional logueado
        sharedPref?.edit()?.putString(getString(R.string.nombre_profesional), profesional.nombre)?.apply()

        setNombreProfesional()

        dashboardFragment = DashboardFragment()
        supportFragmentManager.beginTransaction().remove(loginFragment).add(binding.container.id, dashboardFragment).commitNow()
    }

    override fun onSelectVisita(idVisita: Int) {
        tabsFragment = TabsFragment(idVisita)
        supportFragmentManager.beginTransaction().add(binding.container.id, tabsFragment).addToBackStack(null)
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                mDrawer!!.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}