package com.daicov.seelisten.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.daicov.seelisten.R
import com.daicov.seelisten.databinding.FragmentBelajarAngkaBinding
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BelajarAngkaFragment : Fragment() {

    private var fragmentBelajarAngkaBinding : FragmentBelajarAngkaBinding? = null

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
        return inflater.inflate(R.layout.fragment_belajar_angka, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentBelajarAngkaBinding.bind(view)
        fragmentBelajarAngkaBinding = binding
        hideBottomBar()

        binding.satuButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("menu", 1)
            bundle.putInt("delete", 0)
            val fragment = PilihKartuAngkaFragment()
            fragment.arguments = bundle
            fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
        }
        binding.duaButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("menu", 2)
            bundle.putInt("delete", 0)
            val fragment = PilihKartuAngkaFragment()
            fragment.arguments = bundle
            fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
        }
    }

    private fun hideBottomBar(){
        val bottomAppBar = requireActivity().findViewById<BottomAppBar>(R.id.bottomAppBar)
        val fab = requireActivity().findViewById<FloatingActionButton>(R.id.floatingActionButtonCamera)

        bottomAppBar.visibility = View.GONE
        fab.visibility = View.GONE
    }
}