package ar.yendoc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import ar.yendoc.ui.LoginFragment
import ar.yendoc.databinding.ActivityMainBinding
import ar.yendoc.ui.DashboardFragment


class MainActivity : AppCompatActivity(), LoginFragment.OnFragmentInteractionListener {

    lateinit var binding: ActivityMainBinding
    private lateinit var loginFragment: Fragment
    private lateinit var dashboardFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        getSupportActionBar()?.setDisplayShowTitleEnabled(false);

        val mTitle = binding.toolbarTitle
        mTitle.setText("Visitas")

        //setContentView(R.layout.fragment_login)

        if (savedInstanceState == null) {
            loginFragment = LoginFragment()

            supportFragmentManager.beginTransaction()
                .replace(R.id.container, loginFragment)
                .commitNow()
        }
    }
   /* override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_login)
    }*/

    override fun onLogin(username: String, password: String) {
        dashboardFragment = DashboardFragment()

        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

        supportFragmentManager.beginTransaction().remove(loginFragment).add(R.id.container, dashboardFragment).commitNow()
    }
}