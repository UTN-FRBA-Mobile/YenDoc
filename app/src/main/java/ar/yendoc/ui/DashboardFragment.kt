package ar.yendoc.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.yendoc.MainActivity
import ar.yendoc.core.VisitasAdapter
import ar.yendoc.databinding.FragmentDashboardBinding
import ar.yendoc.network.ApiServices
import ar.yendoc.network.VisitaAdapt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardFragment() : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var idProfesional: Int = 0

    private var listener: DashboardFragment.OnFragmentInteractionListener? = null

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        val sharedPref = this.activity?.getPreferences(Context.MODE_PRIVATE)
        idProfesional = sharedPref?.getInt("id_profesional",0)!!//Levanta el id del profesional logueado

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).supportActionBar!!.show()

        TraerVisitasByProfesional()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is DashboardFragment.OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    fun TraerVisitasByProfesional(){
        val myVisitas = mutableListOf<VisitaAdapt>()
        val apiInterface = ApiServices.create().getVisitasByProfesionalId(idProfesional)
        apiInterface.enqueue( object: Callback<List<VisitaAdapt>> {
            override fun onResponse(
                call: Call<List<VisitaAdapt>>,
                response: Response<List<VisitaAdapt>>
            ) {
                if(response?.body() != null){
                    Log.d("BODY", response.body().toString())
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
                Log.d("ONFAILURE", t.message.toString())
            }
        })
    }

    interface OnFragmentInteractionListener {
        fun onSelectVisita(visita_id: Int)
    }

}