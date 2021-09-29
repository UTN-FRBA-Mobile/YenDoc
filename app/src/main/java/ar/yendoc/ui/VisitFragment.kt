package ar.yendoc.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ar.yendoc.databinding.FragmentVisitBinding

class VisitFragment : Fragment() {
    private var _binding: FragmentVisitBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVisitBinding.inflate(inflater, container, false)
        return binding.root
    }
}