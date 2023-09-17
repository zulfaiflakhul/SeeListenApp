package com.daicov.seelisten.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.daicov.seelisten.R
import com.daicov.seelisten.SusunkataCameraActivity
import com.daicov.seelisten.TebakGambarCameraActivity
import com.daicov.seelisten.databinding.FragmentGameBinding

class GameFragment : Fragment() {

    private var fragmentGameBinding : FragmentGameBinding? = null

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
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentGameBinding.bind(view)
        fragmentGameBinding = binding

        binding.permainanTebakHurufBtn.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, MenuTebakHurufFragment())?.commit()
        }
        binding.permainanTebakGambarBtn.setOnClickListener {
            val intent = Intent(Intent(requireContext(), TebakGambarCameraActivity::class.java))
            startActivity(intent)
        }
        binding.permainanSusunKataBtn.setOnClickListener {
            val intent = Intent(Intent(requireContext(), SusunkataCameraActivity::class.java))
            startActivity(intent)
        }
    }
}