package com.daicov.seelisten.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.daicov.seelisten.CameraActivity
import com.daicov.seelisten.R
import com.daicov.seelisten.databinding.FragmentHomeBinding
import com.daicov.seelisten.dataclass.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private var fragmentHomeBinding: FragmentHomeBinding? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                activity?.finishAffinity()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentHomeBinding.bind(view)
        fragmentHomeBinding = binding


        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance("https://see-listen-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("User")

        val user = Firebase.auth.currentUser
        user?.let {
            val uid = it.uid
            if (uid.isNotEmpty()){
                dbRef.child(uid).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val userData = snapshot.getValue(User::class.java)!!
                        binding.homeTvNamaUser.text = userData.nama
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
            }
        }

        binding.homeUserImage.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, AkunFragment())?.commit()
        }

        binding.penerjemahBtn.setOnClickListener {
            val intent = Intent(this@HomeFragment.requireContext(), CameraActivity::class.java)
            startActivity(intent)
        }

        binding.homeBtnCoba.setOnClickListener {
            val intent = Intent(this@HomeFragment.requireContext(), CameraActivity::class.java)
            startActivity(intent)
        }

        binding.belajarBtn.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, BelajarFragment())?.commit()
        }

        binding.permainanBtn.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, GameFragment())?.commit()
        }
    }
}