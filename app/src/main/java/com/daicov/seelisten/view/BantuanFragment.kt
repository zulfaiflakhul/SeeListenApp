package com.daicov.seelisten.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.daicov.seelisten.R
import com.daicov.seelisten.databinding.FragmentBantuanBinding

@Suppress("DEPRECATION")
class BantuanFragment : Fragment() {

    private var fragmentBantuanBinding : FragmentBantuanBinding? = null

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

        return inflater.inflate(R.layout.fragment_bantuan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentBantuanBinding.bind(view)
        fragmentBantuanBinding = binding

        binding.tentangAppBtn.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, AboutFragment())?.commit()
        }

        binding.kebijakanPrivasibtn.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, KebijakanFragment())?.commit()
        }

        binding.syaratKetentuanBtn.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, SyaratFragment())?.commit()
        }
    }
}