package com.daicov.seelisten.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.daicov.seelisten.R
import com.daicov.seelisten.databinding.FragmentBelajarAlfabetBinding
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton

@Suppress("DEPRECATION")
class BelajarAlfabetFragment : Fragment() {

    private var fragmentBelajarAlfabetBinding : FragmentBelajarAlfabetBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, BelajarFragment())?.commit()
                val bottomAppBar = requireActivity().findViewById<BottomAppBar>(R.id.bottomAppBar)
                val fab = requireActivity().findViewById<FloatingActionButton>(R.id.floatingActionButtonCamera)

                bottomAppBar.visibility = View.VISIBLE
                fab.visibility = View.VISIBLE
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        return inflater.inflate(R.layout.fragment_belajar_alfabet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentBelajarAlfabetBinding.bind(view)
        fragmentBelajarAlfabetBinding = binding
        hideBottomBar()

        binding.aeButton.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, BelajarAeFragment())?.commit()
        }
        binding.fjButton.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, BelajarFjFragment())?.commit()
        }
        binding.koButton.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, BelajarKoFragment())?.commit()
        }
        binding.ptButton.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, BelajarPtFragment())?.commit()
        }
        binding.uzButton.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, BelajarUzFragment())?.commit()
        }
    }

    private fun hideBottomBar(){
        val bottomAppBar = requireActivity().findViewById<BottomAppBar>(R.id.bottomAppBar)
        val fab = requireActivity().findViewById<FloatingActionButton>(R.id.floatingActionButtonCamera)

        bottomAppBar.visibility = View.GONE
        fab.visibility = View.GONE
    }
}