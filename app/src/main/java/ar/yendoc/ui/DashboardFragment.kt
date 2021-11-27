package ar.yendoc.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.yendoc.MainActivity
import ar.yendoc.R
import ar.yendoc.core.VisitasAdapter
import ar.yendoc.databinding.FragmentDashboardBinding
import ar.yendoc.network.ApiServices
import ar.yendoc.network.VisitaAdapt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DashboardFragment() : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private var idProfesional: Int = 0
    private val myVisitas = mutableListOf<VisitaAdapt>()

    private var doRefresh : Boolean = false
    private var listener: DashboardFragment.OnFragmentInteractionListener? = null
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        val sharedPref = this.activity?.getPreferences(Context.MODE_PRIVATE)
        idProfesional = sharedPref?.getInt(getString(R.string.id_profesional),0)!!//Levanta el id del profesional logueado

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).supportActionBar!!.show()

        traerVisitasByProfesional()

        parentFragmentManager!!.addOnBackStackChangedListener(
            object : FragmentManager.OnBackStackChangedListener {
                override fun onBackStackChanged() {
                    val currentFragment: Fragment? = parentFragmentManager.findFragmentById(ar.yendoc.R.id.container)
                    if (currentFragment?.javaClass?.simpleName.toString() ==  DashboardFragment().javaClass.simpleName.toString()){
                        doRefresh = true
                        currentFragment?.onResume()
                    }
                }
            })
    }

    override fun onResume() {
        super.onResume()
        if(doRefresh){
            traerVisitasByProfesional()
            doRefresh = false
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is DashboardFragment.OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString())
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    private fun traerVisitasByProfesional(){
        val apiInterface = ApiServices.create().getVisitasByProfesionalId(idProfesional)
        apiInterface.enqueue( object: Callback<List<VisitaAdapt>> {
            override fun onResponse(
                call: Call<List<VisitaAdapt>>,
                response: Response<List<VisitaAdapt>>
            ) {
                if(response?.body() != null){
                    Log.d(getString(R.string.body), response.body().toString())
                    myVisitas.clear()
                    for (i in 0 until (response.body()!!.size)){
                        myVisitas.add(i, response.body()!![i])
                    }

                    val viewManager = LinearLayoutManager(context)
                    val viewAdapter = VisitasAdapter(myVisitas) {
                            itemDto: VisitaAdapt, position: Int ->
                        listener!!.onSelectVisita(itemDto.visita_id)
                    }

                    recyclerView = binding.recyclerVisitas.apply {
                        layoutManager = viewManager
                        adapter = viewAdapter
                    }
                }
            }

            override fun onFailure(call: Call<List<VisitaAdapt>>, t: Throwable) {
                Log.d(getString(R.string.error), t.message.toString())
            }
        })
    }

    interface OnFragmentInteractionListener {
        fun onSelectVisita(visita_id: Int)
    }
}