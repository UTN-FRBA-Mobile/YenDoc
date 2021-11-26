package ar.yendoc.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ar.yendoc.MainActivity
import ar.yendoc.R
import ar.yendoc.databinding.FragmentLoginBinding
import ar.yendoc.network.ApiServices
import ar.yendoc.network.Profesional
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment: Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).supportActionBar!!.hide()
        binding.botonIniciarSesion.setOnClickListener {

            val usuario = binding.inputUsuario.text.toString()
            val contrasenia = binding.inputContrasena.text.toString()

            when {
                usuario.isEmpty() or contrasenia.isEmpty() -> {
                    val toast = Toast.makeText(activity, getString(R.string.usuario_contrasenia_obligatorio), Toast.LENGTH_SHORT)
                    toast.setGravity(Gravity.BOTTOM, 0, 0)
                    toast.show()
                }
                else -> {
                    val apiInterface = ApiServices.create().getProfesionalByName(usuario)
                    apiInterface.enqueue( object : Callback<Profesional> {
                        override fun onResponse(
                            call: Call<Profesional>,
                            response: Response<Profesional>
                        ) {
                            if(response.body() != null){
                                val profesional : Profesional = response.body()!!
                                listener!!.onLogin(profesional.profesional_id)
                            }
                        }

                        override fun onFailure(call: Call<Profesional>, t: Throwable) {
                            Log.d(getString(R.string.error), t.message.toString())

                            val toast = Toast.makeText(activity, getString(R.string.usuario_contrasenia_incorrecto), Toast.LENGTH_SHORT)
                            toast.setGravity(Gravity.BOTTOM, 0, 0)
                            toast.show()
                        }
                    })
                }

            }

        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString())
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onLogin(profesional_id : Int)
    }
}