package ar.yendoc.ui

import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ar.yendoc.databinding.FragmentTabsBinding
import androidx.viewpager.widget.ViewPager
import ar.yendoc.core.TabsAdapter
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.FragmentActivity

import android.app.Activity
import android.util.Log
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.widget.TextView





class TabsFragment : Fragment() {
    private var _binding: FragmentTabsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var myContext: FragmentActivity

    private lateinit var visitFragment: Fragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTabsBinding.inflate(inflater, container, false)
        //super.onCreate(savedInstanceState)

        //setContentView(R.layout.activity_simple_tab_layout)
        tabLayout = _binding!!.tabLayout
        viewPager = _binding!!.viewPager
        bottomNavigation = _binding!!.bottomNavigationView

        tabLayout.addTab(tabLayout.newTab().setText(getString(ar.yendoc.R.string.detalle)), 0, true)
        tabLayout.addTab(tabLayout.newTab().setText(getString(ar.yendoc.R.string.mapa)), 1)
        tabLayout.addTab(tabLayout.newTab().setText(getString(ar.yendoc.R.string.fotos)), 2)
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        val adapter = TabsAdapter(myContext.applicationContext, getChildFragmentManager(), tabLayout.tabCount)

        viewPager.adapter = adapter
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
                if(tab.position == 0){
                    bottomNavigation.visibility = View.VISIBLE
                }
                else{
                    bottomNavigation.visibility = View.GONE
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) { }
            override fun onTabReselected(tab: TabLayout.Tab) { }
        })
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewPager.setCurrentItem(0, false)
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onAttach(activity: Activity) {
        myContext = activity as FragmentActivity
        super.onAttach(activity)
    }
}
