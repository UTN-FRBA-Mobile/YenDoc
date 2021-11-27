package ar.yendoc.ui

import android.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import ar.yendoc.databinding.FragmentTabsBinding
import androidx.viewpager.widget.ViewPager
import ar.yendoc.core.TabsAdapter
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.FragmentActivity

import android.app.Activity
import android.app.AlertDialog
import android.util.Log
import android.view.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.content.DialogInterface
import ar.yendoc.network.ApiServices
import ar.yendoc.network.VisitaAdapt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TabsFragment(val idVisita : Int) : Fragment() {
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
        bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                bottomNavigation.menu.getItem(0).itemId -> {

                    true
                }
                bottomNavigation.menu.getItem(1).itemId -> {

                    true
                }
                bottomNavigation.menu.getItem(2).itemId -> {

                    val builder: AlertDialog.Builder = AlertDialog.Builder(this.context)
                    builder.setTitle(getString(ar.yendoc.R.string.finalizar))
                    builder.setMessage(getString(ar.yendoc.R.string.pudo_atender_correctamente))

                    builder.setPositiveButton(
                        getString(ar.yendoc.R.string.si),
                        DialogInterface.OnClickListener { dialog, which ->
                            val apiInterface = ApiServices.create().updateEstado(idVisita, 1)
                            apiInterface.enqueue( object : Callback<Int> {
                                override fun onResponse(call: Call<Int>, response: Response<Int>) {
                                    if (response?.body() != null){
                                        Log.d("BODY", response.body().toString())
                                        dialog.dismiss()
                                        VolverYActualizar()
                                    }
                                }
                                override fun onFailure(call: Call<Int>, t: Throwable) {
                                    Log.d("ERROR", t.message.toString())
                                }
                            })
                        })

                    builder.setNegativeButton(
                        getString(ar.yendoc.R.string.no),
                        DialogInterface.OnClickListener { dialog, which ->
                            val apiInterface = ApiServices.create().updateEstado(idVisita, 2)
                            apiInterface.enqueue( object : Callback<Int> {
                                override fun onResponse(call: Call<Int>, response: Response<Int>) {
                                    if (response?.body() != null){
                                        Log.d("BODY", response.body().toString())
                                        dialog.dismiss()
                                        VolverYActualizar()
                                    }
                                }
                                override fun onFailure(call: Call<Int>, t: Throwable) {
                                    Log.d("ERROR", t.message.toString())
                                }
                            })
                        })

                    val alert: AlertDialog = builder.create()
                    alert.show()

                    true
                }
                else -> false
            }
        }

        return binding.root
    }

    fun VolverYActualizar() {
        var lastFragment = parentFragmentManager.fragments.last()
        parentFragmentManager.popBackStack()
        parentFragmentManager.beginTransaction().detach(lastFragment).attach(lastFragment).commit()//TODO: Verificar recarga del fragment Dashboard al finalizar visita.
    }

    override fun onResume() {
        super.onResume()
        viewPager.setCurrentItem(0, false)
    }

    override fun onAttach(activity: Activity) {
        myContext = activity as FragmentActivity
        super.onAttach(activity)
    }
}
