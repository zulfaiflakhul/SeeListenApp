@file:Suppress("UNREACHABLE_CODE")

package com.daicov.seelisten.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.daicov.seelisten.MainActivity
import com.daicov.seelisten.R
import com.daicov.seelisten.databinding.FragmentRegisterBinding
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
class RegisterFragment : Fragment() {

    private var fragmentRegisterBinding : FragmentRegisterBinding? = null
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
                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, LoginFragment())?.commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentRegisterBinding.bind(view)
        fragmentRegisterBinding = binding

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance("https://see-listen-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("User")

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext() , gso)

        // Validasi Data
        emailFocusListener()
        namaFocusListener()
        passwordFocusListener()
        confirmPasswordFocusListener()

        binding.registerBtnSelanjutnya.setOnClickListener {
            val nama = binding.registerEtNama.text.toString()
            val email = binding.registerEtEmail.text.toString()
            val password = binding.registerEtPassword.text.toString()
            val confirmPassword = binding.registerEtPassword2.text.toString()

            signInEmail(nama, email, password, confirmPassword)

        }

        binding.loginBtnGoogle2.setOnClickListener {
            signInGoogle()
        }

        // Go to login page
        binding.registerBtnLogin.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, LoginFragment())?.commit()
        }
    }

    // Sign In Email
    private fun signInEmail(nama: String, email: String, password: String, confirmPassword: String){
        if (nama.isNotEmpty() && email.isNotEmpty() &&
            password.isNotEmpty() && confirmPassword.isNotEmpty()){
            if (password == confirmPassword && Patterns.EMAIL_ADDRESS.matcher(email).matches()){

                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val user = User(nama, email, password)
                        FirebaseAuth.getInstance().currentUser?.let { it1 ->
                            dbRef.child(it1.uid).setValue(user).addOnCompleteListener {
                                Toast.makeText(requireContext(), "Data Behrasil Disimpan", Toast.LENGTH_LONG).show()
                            } .addOnFailureListener {
                                Toast.makeText(requireContext(), "Error", Toast.LENGTH_LONG).show()
                            }
                        }
                        val intent = Intent(this@RegisterFragment.requireContext(), MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(requireContext(), "Gagal Membuat Akun", Toast.LENGTH_LONG).show()
                    }
                }

            } else {
                Toast.makeText(requireContext(), "Password/Email Tidak Valid", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(requireContext(), "Data Tidak Boleh Kosong", Toast.LENGTH_LONG).show()
        }
    }

    // Sign in Google
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
                            val intent = Intent(this@RegisterFragment.requireContext(), MainActivity::class.java)
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
                            val intent = Intent(this@RegisterFragment.requireContext(), MainActivity::class.java)
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

    // VALIDATE
    private fun namaFocusListener(){
        fragmentRegisterBinding?.registerEtNama?.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                fragmentRegisterBinding!!.namaContainer.helperText = validateNama()
            }
        }
    }

    private fun validateNama(): String?{
        val nama = fragmentRegisterBinding?.registerEtNama?.text.toString()
        if (nama.isEmpty()) {
            return "Tidak Boleh Kosong"
        }
        return null
    }

    private fun passwordFocusListener(){
        fragmentRegisterBinding?.registerEtPassword?.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                fragmentRegisterBinding!!.passwordContainer.helperText = validatePassword()
            }
        }
    }

    private fun validatePassword(): String? {
        val password = fragmentRegisterBinding?.registerEtPassword?.text.toString()
        if (password.length < 8) {
            return "Minimal 8 Karakter"
        }
        return null
    }

    private fun confirmPasswordFocusListener(){
        fragmentRegisterBinding?.registerEtPassword2?.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                fragmentRegisterBinding!!.passwordContainer2.helperText = validateConfirmPassword()
            }
        }
    }

    private fun validateConfirmPassword(): String? {
        val password = fragmentRegisterBinding?.registerEtPassword2?.text.toString()
        if (password.length < 8) {
            return "Minimal 8 Karakter"
        }
        return null
    }

    private fun emailFocusListener(){
        fragmentRegisterBinding?.registerEtEmail?.setOnFocusChangeListener { _, focused ->
            if (!focused){
                fragmentRegisterBinding!!.emailContainer.helperText = validateEmail()
            }
        }
    }

    private  fun validateEmail(): String? {
        val email = fragmentRegisterBinding?.registerEtEmail?.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return "Alamat Email Tidak Valid"
        }
        return null
    }
}