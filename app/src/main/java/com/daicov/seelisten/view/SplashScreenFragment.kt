package com.daicov.seelisten.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.navigation.Navigation
import com.daicov.seelisten.MainActivity
import com.daicov.seelisten.R
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    override fun onStart() {
        super.onStart()

        auth = FirebaseAuth.getInstance()

        Handler(Looper.getMainLooper()).postDelayed({
            if (auth.currentUser != null){
                val intent =
                    Intent(this@SplashScreenFragment.requireContext(), MainActivity::class.java)
                startActivity(intent)
            } else {
                view?.let { Navigation.findNavController(it).navigate(R.id.action_splashScreenFragment_to_loginFragment) }
            }
        }, 2000)
    }
}