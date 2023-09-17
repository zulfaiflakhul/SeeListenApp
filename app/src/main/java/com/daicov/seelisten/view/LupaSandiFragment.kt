package com.daicov.seelisten.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.daicov.seelisten.R
import com.daicov.seelisten.databinding.FragmentLupaSandiBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

@Suppress("DEPRECATION")
class LupaSandiFragment : Fragment() {

    private var fragmentLupaSandiBinding : FragmentLupaSandiBinding? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, LoginFragment())?.commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        return inflater.inflate(R.layout.fragment_lupa_sandi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentLupaSandiBinding.bind(view)
        fragmentLupaSandiBinding = binding

        auth = FirebaseAuth.getInstance()

        binding.cekEmailBtn.setOnClickListener {
            val email = binding.lupasandiEtEmail.text.toString()
            auth.sendPasswordResetEmail(email).addOnSuccessListener {
                Toast.makeText(context, "Cek Email untuk mengganti kata sandi", Toast.LENGTH_SHORT).show()
                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, LoginFragment())?.commit()
            } .addOnFailureListener {
                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
}