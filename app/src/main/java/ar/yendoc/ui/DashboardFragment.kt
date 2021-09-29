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
import ar.yendoc.core.Visita
import ar.yendoc.core.VisitaAPI
import ar.yendoc.core.VisitasAdapter
import ar.yendoc.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var listener: DashboardFragment.OnFragmentInteractionListener? = null

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).supportActionBar!!.show()

        val myVisitas = VisitaAPI().getVisitas()

        val viewManager = LinearLayoutManager(this.context)
        val viewAdapter = VisitasAdapter(myVisitas) { itemDto: Visita, position: Int ->
            listener!!.onSelectVisita(itemDto.idVisita)
        }

        recyclerView = binding.recyclerVisitas.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
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


    interface OnFragmentInteractionListener {
        fun onSelectVisita(idVisita: Int)
    }
}