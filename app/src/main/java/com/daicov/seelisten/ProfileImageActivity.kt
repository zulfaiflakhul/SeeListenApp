package com.daicov.seelisten

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.daicov.seelisten.databinding.ActivityProfileImageBinding


class ProfileImageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileImageBinding
    private lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_image)

        binding = ActivityProfileImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.editImageButton.setOnClickListener {
            val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            // ******** code for crop image
            // ******** code for crop image
            i.putExtra("crop", "true")
            i.putExtra("aspectX", 100)
            i.putExtra("aspectY", 100)
            i.putExtra("outputX", 500)
            i.putExtra("outputY", 500)

            try {
                i.putExtra("return-data", true)
                startActivityForResult(
                    Intent.createChooser(i, "Select Picture"), 0
                )
            } catch (ex: ActivityNotFoundException) {
                ex.printStackTrace()
            }
        }

        binding.backToAccounut.setOnClickListener {
            val intent = Intent(Intent(this, MainActivity::class.java))
            intent.putExtra("fragmentToLoad", "akunFragment")
            startActivity(intent)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == RESULT_OK) {
            try {
                val bundle = data?.extras
                val bitmap = bundle!!.getParcelable<Bitmap>("data")
                binding.profileImage.setImageBitmap(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}