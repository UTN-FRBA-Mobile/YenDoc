package ar.yendoc.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.yendoc.MainActivity
import ar.yendoc.core.VisitaAPI
import ar.yendoc.core.VisitasAdapter
import ar.yendoc.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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
        val viewAdapter = VisitasAdapter(myVisitas)

        recyclerView = binding.recyclerVisitas.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }
}