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
import com.google.android.material.bottomnavigation.BottomNavigationView


class TabsFragment : Fragment() {
    private var _binding: FragmentTabsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var myContext: FragmentActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTabsBinding.inflate(inflater, container, false)

        //super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_simple_tab_layout)
        tabLayout = _binding!!.tabLayout
        viewPager = _binding!!.viewPager
        bottomNavigation = _binding!!.bottomNavigationView

        tabLayout.addTab(tabLayout.newTab().setText(getString(ar.yendoc.R.string.detalle)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(ar.yendoc.R.string.mapa)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(ar.yendoc.R.string.fotos)))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        val adapter = TabsAdapter(myContext.applicationContext, myContext.supportFragmentManager, tabLayout.tabCount)

        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
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

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {
                if(tab.position == 0){
                    bottomNavigation.visibility = View.VISIBLE
                }
                else{
                    bottomNavigation.visibility = View.GONE
                }
            }
        })

        return binding.root
    }

    override fun onAttach(activity: Activity) {
        myContext = activity as FragmentActivity
        super.onAttach(activity)
    }
}
