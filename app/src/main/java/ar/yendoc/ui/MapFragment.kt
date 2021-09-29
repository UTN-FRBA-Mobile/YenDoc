package ar.yendoc.ui

import androidx.fragment.app.Fragment
import android.R

import android.os.Bundle

import android.view.ViewGroup

import android.view.LayoutInflater
import android.view.View
import ar.yendoc.databinding.FragmentMapBinding


class MapFragment : Fragment() {
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }
}