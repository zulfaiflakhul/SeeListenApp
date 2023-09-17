package com.daicov.seelisten.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.daicov.seelisten.MainActivity
import com.daicov.seelisten.R
import com.daicov.seelisten.databinding.FragmentLoginBinding
import com.daicov.seelisten.dataclass.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase


@Suppress("DEPRECATION")
class LoginFragment : Fragment() {

    private var fragmentLoginBinding: FragmentLoginBinding? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private lateinit var googleSignInClient : GoogleSignInClient

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

        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentLoginBinding.bind(view)
        fragmentLoginBinding = binding
        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance("https://see-listen-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("User")

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext() , gso)

        binding.loginBtnMasuk.setOnClickListener {
            val email = binding.loginEtEmail.text.toString()
            val password = binding.loginEtPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent =
                            Intent(this@LoginFragment.requireContext(), MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(requireContext(), "Email/Kata Sandi Salah", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Data Kosong Tidak Diijinkan Masuk", Toast.LENGTH_LONG).show()
            }
        }

        binding.loginBtnGoogle.setOnClickListener {
            signInGoogle()
        }

        binding.loginBtnRegister.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, RegisterFragment())?.commit()
        }

        binding.loginBtnLupaSandi.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, LupaSandiFragment())?.commit()
        }
    }

    private fun signInGoogle(){
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if (result.resultCode == Activity.RESULT_OK){

            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResults(task)
        }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful){
            val account : GoogleSignInAccount? = task.result
            if (account != null){
                updateUI(account)
            }
        }else{
            Toast.makeText(requireContext(), task.exception.toString() , Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken , null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful){
                val uid = FirebaseAuth.getInstance().currentUser?.uid
                dbRef = FirebaseDatabase.getInstance("https://see-listen-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("User")
                dbRef.child(uid.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()){
                            val intent = Intent(this@LoginFragment.requireContext(), MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            val nama = account.displayName
                            val email = account.email
                            val user = User(nama, email)
                            Firebase.auth.currentUser?.let { it1 -> dbRef.child(it1.uid).setValue(user).addOnCompleteListener {
                                Toast.makeText(requireContext(), "Berhasil Login", Toast.LENGTH_SHORT).show()
                            } . addOnFailureListener {
                                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                            }
                            }
                            val intent = Intent(this@LoginFragment.requireContext(), MainActivity::class.java)
                            startActivity(intent)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
            } else {
                Toast.makeText(requireContext(), it.exception.toString() , Toast.LENGTH_SHORT).show()
            }
        }
    }
}