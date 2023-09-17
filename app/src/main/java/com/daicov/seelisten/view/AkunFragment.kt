package com.daicov.seelisten.view

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.ContentResolver
import android.content.Intent
import android.content.Intent.getIntent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.daicov.seelisten.AuthActivity
import com.daicov.seelisten.MainActivity
import com.daicov.seelisten.ProfileImageActivity
import com.daicov.seelisten.R
import com.daicov.seelisten.databinding.FragmentAkunBinding
import com.daicov.seelisten.dataclass.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import java.io.IOException


@Suppress("NAME_SHADOWING", "DEPRECATION")
class AkunFragment : Fragment() {

    private var fragmentAkunBinding: FragmentAkunBinding? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private lateinit var storageRef: StorageReference
    private lateinit var profilePicRef : StorageReference
    private lateinit var googleSignInClient : GoogleSignInClient
    private val PICK_IMAGE = 1
    private var imageUri: Uri? = null
    private var imageBitmap: Bitmap? = null

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

        return inflater.inflate(R.layout.fragment_akun, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentAkunBinding.bind(view)
        fragmentAkunBinding = binding

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance("https://see-listen-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("User")

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext() , gso)

        val user = Firebase.auth.currentUser
        user?.let {
            val uid = it.uid
            if (uid.isNotEmpty()){
                dbRef.child(uid).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val user = snapshot.getValue(User::class.java)!!
                        binding.profileUserNameTv.text = user.nama
                        binding.profileUserEmailTv.text = user.email
                        if (user.noHp != null){
                            binding.profileUserPhoneTv.text = user.noHp
                        } else {
                            binding.profileUserPhoneTv.text = binding.profileUserPhoneTv.text
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
            }
        }

//        binding.profileUserImg.setOnClickListener {
//            val intent = Intent(Intent(requireContext(), ProfileImageActivity::class.java))
//            startActivity(intent)
//        }
//
//        if (getIntent(imageUri.toString()) != null){
//            imageUri = getIntent(imageUri.toString()).data
//            fragmentAkunBinding!!.profileUserImg.setImageURI(imageUri)
//        }
//
//        binding.editFotoProfilBtn.setOnClickListener {
//            dialogEditMenu(R.layout.dialog_edit_nama, 1)
//        }
//
//        binding.icEditGenderBtn.setOnClickListener {
//            dialogEditMenu(R.layout.dialog_edit_gender, 2)
//        }
//
//        binding.icEditEmailBtn.setOnClickListener {
//            dialogEditMenu(R.layout.dialog_edit_profile, 3)
//        }
//
//        binding.icEditNoTelpBtn.setOnClickListener {
//            dialogEditMenu(R.layout.dialog_edit_profile, 4)
//        }
//
//        binding.icEditPasswordBtn.setOnClickListener {
//            dialogEditMenu(R.layout.dialog_edit_profile, 5)
//        }
//
//        binding.icEditTtlBtn.setOnClickListener {
//            dialogEditMenu(R.layout.dialog_edit_tanggal_lahir, 6)
//        }
//
//        binding.icEditKotaBtn.setOnClickListener {
//            dialogEditMenu(R.layout.dialog_edit_kota, 7)
//        }

        binding.icKeluarAkunBtn.setOnClickListener {
            dialogKeluar()
        }
    }

    private fun dialogEditMenu(layout: Int, id: Int){
        val dialog = Dialog(requireContext())
        dialog.setContentView(layout)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.setCancelable(false)

        val closeButton = dialog.findViewById<ImageButton>(R.id.dialogCloseBtn)

        when (id) {
            1 -> {
                editNama(dialog)
            }
            3 -> {
                editEmail(dialog)
            }
            4 -> {
                editNoTelp(dialog)
            }
            5 -> {
                editPassword(dialog)
            }
            7 -> {
                editKota(dialog)
            }
        }

        closeButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun editNama(dialog: Dialog){
        val dialogImg = dialog.findViewById<ImageView>(R.id.dialogImg)
        val editFotoButton = dialog.findViewById<TextView>(R.id.editFotoBtn)
        val dataEditText = dialog.findViewById<EditText>(R.id.editNamaEt)
        val dataNama = fragmentAkunBinding?.profileUserNameTv?.text

        dataEditText.setText(dataNama)
    }

    private fun editFoto(){
        val dialogEdit = Dialog(requireContext())
        dialogEdit.setContentView(R.layout.dialog_edit_foto)
        dialogEdit.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogEdit.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogEdit.setCancelable(false)

        val closeDialog = dialogEdit.findViewById<TextView>(R.id.closeDialogEditFotoBtn)
        val fromAlbumBtn = dialogEdit.findViewById<TextView>(R.id.fromAlbumBtn)
        val deleteBtn = dialogEdit.findViewById<TextView>(R.id.hapusFotoBtn)

        fromAlbumBtn.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE)
            dialogEdit.dismiss()
        }

        closeDialog.setOnClickListener {
            dialogEdit.dismiss()
        }
        dialogEdit.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null){
            imageUri = data.data!!
            val intent = Intent(Intent(requireContext(), MainActivity::class.java))
            intent.data = imageUri
            startActivity(intent)
        }
    }

    private fun getImage(){
        var bitmap: Bitmap? = null
        try {
            bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imageUri)
        } catch (e: IOException){
            e.printStackTrace()
        }
        fragmentAkunBinding?.profileUserImg?.setImageBitmap(bitmap)
    }

    private fun editEmail(dialog: Dialog){
        val titleTv = dialog.findViewById<TextView>(R.id.editProfileTitleTv)
        val dataEditText = dialog.findViewById<EditText>(R.id.editDataEt)
        val dataEmail = fragmentAkunBinding?.profileUserEmailTv?.text

        dataEditText.setText(dataEmail)
        titleTv.text = "Email"
    }

    private fun editNoTelp(dialog: Dialog){
        val titleTv = dialog.findViewById<TextView>(R.id.editProfileTitleTv)
        titleTv.text = "Nomor Telepon"
    }

    private fun editPassword(dialog: Dialog){
        val titleTv = dialog.findViewById<TextView>(R.id.editProfileTitleTv)
        titleTv.text = "Kata Sandi"
    }

    private fun editKota(dialog: Dialog){
        val listView = dialog.findViewById<ListView>(R.id.kotaList)
        val query: ArrayList<String> = ArrayList()

        val adapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, query)
        listView.adapter = adapter
    }

    private fun dialogKeluar(){
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.custom_dialog_pilihan)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.setCancelable(false)
        val btnYa = dialog.findViewById<RadioButton>(R.id.dialogYaBtn)
        val btnTidak = dialog.findViewById<RadioButton>(R.id.dialogTidakbtn)
        val ketTv = dialog.findViewById<TextView>(R.id.hapusKeluarTv)

        ketTv.text = getString(R.string.dialog_signout)

        btnYa.setOnClickListener {
            Firebase.auth.signOut()
            googleSignInClient.signOut()
            val intent = Intent(this@AkunFragment.requireContext(), AuthActivity::class.java)
            startActivity(intent)
            dialog.dismiss()
        }
        btnTidak.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun dialogHapus(){
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.custom_dialog_pilihan)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.setCancelable(false)
        val btnYa = dialog.findViewById<RadioButton>(R.id.dialogYaBtn)
        val btnTidak = dialog.findViewById<RadioButton>(R.id.dialogTidakbtn)
        val ketTv = dialog.findViewById<TextView>(R.id.hapusKeluarTv)

        ketTv.text = getString(R.string.dialog_hapusakun)

        btnYa.setOnClickListener {
            dialog.dismiss()
        }
        btnTidak.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun dialogEditData(keterangan: String, dataTv: TextView){
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_edit_profile)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(true)

        val keteranganData = dialog.findViewById<TextView>(R.id.editProfileTitleTv)
        val dataEditText = dialog.findViewById<EditText>(R.id.editDataEt)
        val editButton = dialog.findViewById<Button>(R.id.editDataBtn)

        keteranganData.text = keterangan
        dataEditText.setText(dataTv.text)

        editPhoneNumber(editButton,dataEditText, dataTv, dialog)

        dialog.show()
    }

    private fun editPhoneNumber(editButton: Button, dataEditText: EditText, dataTv: TextView, dialog: Dialog){
        editButton.setOnClickListener {
            val data = dataEditText.text.toString()
            val user = mapOf(
                "noHp" to data
            )
            Firebase.auth.currentUser?.let { it1 -> dbRef.child(it1.uid).updateChildren(user).addOnCompleteListener {
                Toast.makeText(context, "Berhasil ditambahkan", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(context, "Gagal", Toast.LENGTH_SHORT).show()
            } }
            dataTv.text = data
            dialog.dismiss()
        }
    }
}