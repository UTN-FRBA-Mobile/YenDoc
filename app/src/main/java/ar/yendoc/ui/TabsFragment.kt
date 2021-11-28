package ar.yendoc.ui

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import ar.yendoc.databinding.FragmentTabsBinding
import androidx.viewpager.widget.ViewPager
import ar.yendoc.core.TabsAdapter
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.FragmentActivity
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import ar.yendoc.network.ApiServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.util.*
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat


class TabsFragment(val idVisita : Int) : Fragment() {
    private var _binding: FragmentTabsBinding? = null
    private val binding get() = _binding!!

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var myContext: FragmentActivity
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var visitFragment: Fragment
    private lateinit var sharedPref: SharedPreferences

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

        if (ContextCompat.checkSelfPermission(
                myContext,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            bottomNavigation.menu.getItem(0).setEnabled(false)
            ActivityCompat.requestPermissions(
                myContext,
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                0
            )
        }

        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    handleCameraImage(result.data)
                }
            }

        bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                bottomNavigation.menu.getItem(0).itemId -> {
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    resultLauncher.launch(cameraIntent)
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
                                        volverYActualizar()
                                    }
                                }
                                override fun onFailure(call: Call<Int>, t: Throwable) {
                                    Log.d("ERROR", t.message.toString())
                                    dialog.dismiss()
                                    volverYActualizar()
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
                                        volverYActualizar()
                                    }
                                }
                                override fun onFailure(call: Call<Int>, t: Throwable) {
                                    Log.d("ERROR", t.message.toString())
                                    dialog.dismiss()
                                    volverYActualizar()
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

        sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        sharedPref?.edit()?.putInt(getString(ar.yendoc.R.string.id_visita), idVisita)?.apply()

        return binding.root
    }

    private fun handleCameraImage(intent: Intent?) {
        val bitmap = intent?.extras?.get("data") as Bitmap

        var outStream: FileOutputStream? = null
        val sdCard = Environment.getExternalStorageDirectory()
        val dir = File(sdCard.absolutePath + "/camtest")
        Log.d("RUTAAA", dir.toString())
        dir.mkdirs()
        val fileName = String.format("%d.jpg", System.currentTimeMillis())
        val outFile = File(dir, fileName)
        outStream = FileOutputStream(outFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
        outStream.flush()
        outStream.close()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 0) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                bottomNavigation.menu.getItem(0).setEnabled(true)
            }
        }
    }

    fun volverYActualizar() {
        parentFragmentManager.popBackStack()
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
