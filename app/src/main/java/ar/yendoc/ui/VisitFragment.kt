package ar.yendoc.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import ar.yendoc.R
import ar.yendoc.databinding.FragmentVisitBinding
import ar.yendoc.network.ApiServices
import ar.yendoc.network.Paciente
import ar.yendoc.network.Profesional
import ar.yendoc.network.Visita
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VisitFragment : Fragment() {
    private var _binding: FragmentVisitBinding? = null
    private val binding get() = _binding!!
    private var visita : Visita? = null
    private var paciente : Paciente? = null
    private var idVisita : Int = 0
    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVisitBinding.inflate(inflater, container, false)

        sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        idVisita = sharedPref.getInt(getString(R.string.id_visita),0)

        getVisita()

        return binding.root
    }

    private fun getVisita() {
        val apiInterface = ApiServices.create().getVisitaById(idVisita)
        apiInterface.enqueue( object : Callback<Visita> {
            override fun onResponse(call: Call<Visita>, response: Response<Visita>
            ) {
                if(response?.body() != null){
                    visita = response.body()!!
                    getPaciente(visita!!.paciente_id)
                }
            }

            override fun onFailure(call: Call<Visita>, t: Throwable) {
                Log.d(getString(R.string.error), t.message.toString())
            }
        })
    }

    private fun getPaciente(idPaciente: Int){
        val apiInterface = ApiServices.create().getPacienteById(idPaciente)
        apiInterface.enqueue( object : Callback<Paciente> {
            override fun onResponse(call: Call<Paciente>, response: Response<Paciente>
            ) {
                if(response?.body() != null){
                    paciente = response.body()!!
                    Log.d("RESPONSEEEEE", response.body().toString())
                    llenarDatos()
                }
            }

            override fun onFailure(call: Call<Paciente>, t: Throwable) {
                Log.d(getString(R.string.error), t.message.toString())
            }
        })
    }

    private fun llenarDatos(){
        binding.namePaciente.setText(paciente?.nombre)
        binding.agePaciente.setText(paciente?.edad.toString() + " años")
        binding.addressPaciente.text = paciente?.direccion_calle + " " +  paciente?.direccion_numero
        binding.symptomPaciente.text = visita?.sintomas
        binding.diagnostic.setText(visita?.diagnostico)
    }
}