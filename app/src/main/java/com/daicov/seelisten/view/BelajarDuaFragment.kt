package com.daicov.seelisten.view

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.daicov.seelisten.R
import com.daicov.seelisten.databinding.FragmentBelajarSatuBinding
import com.daicov.seelisten.view.adapter.ViewPagerImageAdapter
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton

@Suppress("DEPRECATION")
class BelajarDuaFragment : Fragment() {

    private var belajarSatuBinding : FragmentBelajarSatuBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, BelajarAngkaFragment())?.commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val args = this.arguments
        val menu = args?.getInt("menu")
        if (menu == null){
            sharedPreferences.edit().clear().apply()
        }
        return inflater.inflate(R.layout.fragment_belajar_satu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentBelajarSatuBinding.bind(view)
        belajarSatuBinding = binding

        val args = this.arguments
        val angka = listOf("6","7","8","9","10")
        val image = listOf(
            R.drawable.img_sibi_6,
            R.drawable.img_sibi_7,
            R.drawable.img_sibi_8,
            R.drawable.img_sibi_9,
            R.drawable.img_sibi_10
        )

        val viewPagerImage : ViewPager2 = binding.viewPager2
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        var count = sharedPreferences.getInt("count", 1)

        val pilihan = args?.getInt("PILIHAN")
        if (pilihan != null){
            viewPagerImage.isUserInputEnabled = false
            viewPagerImage.adapter = ViewPagerImageAdapter(angka, image)
            count++
            binding.seekBar.progress = count
            when (pilihan) {
                7 -> {
                    viewPagerImage.setCurrentItem(4, false)
                }
                6 -> {
                    viewPagerImage.setCurrentItem(2, false)
                }
                8 -> {
                    viewPagerImage.setCurrentItem(0, false)
                }
                10 -> {
                    viewPagerImage.setCurrentItem(3, false)
                }
                9 -> {
                    viewPagerImage.setCurrentItem(1, false)
                }
            }

            binding.selanjutnyaButton2.setOnClickListener {
                sharedPreferences.edit().putInt("count", count).apply()
                if (count == 6) {
                    binding.scrollView2.visibility = View.GONE
                    binding.selanjutnyaButton2.visibility = View.GONE
                    binding.finishLayout.visibility = View.VISIBLE
                    binding.titleKeteranganFinishTv.text = "Anda telah belajar angka 6-10"
                    binding.deskripsiFinishTv.text = "Uji ingatanmu dengan permainan seru bersama SeeListen"
                    binding.mulaiLatihanBtn.text = "Mulai bermain"
                    binding.kembaliMengingatBtn.text = "Kembali belajar"

                    binding.mulaiLatihanBtn.setOnClickListener {
                        fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, GameFragment())?.commit()
                        val bottomAppBar = requireActivity().findViewById<BottomAppBar>(R.id.bottomAppBar)
                        val fab = requireActivity().findViewById<FloatingActionButton>(R.id.floatingActionButtonCamera)

                        bottomAppBar.visibility = View.VISIBLE
                        fab.visibility = View.VISIBLE
                    }

                    binding.kembaliMengingatBtn.setOnClickListener {
                        fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, BelajarAngkaFragment())?.commit()
                    }
                } else {
                    val bundle = Bundle()
                    bundle.putInt("menu", 2)
                    bundle.putInt("delete", 1)
                    val fragment = PilihKartuAngkaFragment()
                    fragment.arguments = bundle
                    fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
                }
            }
        }
        binding.closeButton.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, BelajarAngkaFragment())?.commit()
        }
    }
}