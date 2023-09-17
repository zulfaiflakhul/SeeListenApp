package com.daicov.seelisten.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.daicov.seelisten.R
import com.daicov.seelisten.databinding.FragmentBelajarBinding

@Suppress("DEPRECATION")
class BelajarFragment : Fragment() {

    private var fragmentBelajarBinding : FragmentBelajarBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, HomeFragment())?.commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        return inflater.inflate(R.layout.fragment_belajar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentBelajarBinding.bind(view)
        fragmentBelajarBinding = binding

        binding.belajarAlfabetBtn.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, BelajarAlfabetFragment())?.commit()
        }
        binding.belajarAngkaBtn.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, BelajarAngkaFragment())?.commit()
        }
        binding.belajarAnggotaBtn.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, BelajarKataFragment())?.commit()
        }
    }
}