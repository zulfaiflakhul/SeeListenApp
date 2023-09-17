@file:Suppress("DEPRECATION")

package com.daicov.seelisten.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.daicov.seelisten.R
import com.daicov.seelisten.databinding.FragmentBelajarAeBinding
import com.daicov.seelisten.view.adapter.ViewPagerImageAdapter
import com.daicov.seelisten.view.adapter.ViewPagerVideoAdapter
import com.google.android.gms.common.api.GoogleApi
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton

@Suppress("DEPRECATION")
class BelajarUzFragment : Fragment() {

    private var belajarAeFragment : FragmentBelajarAeBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, BelajarAlfabetFragment())?.commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val args = this.arguments
        val menu = args?.getInt("menu")
        if (menu == null){
            sharedPreferences.edit().clear().apply()
        }
        return inflater.inflate(R.layout.fragment_belajar_ae, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideBottomBar()
        val binding = FragmentBelajarAeBinding.bind(view)
        belajarAeFragment = binding
        binding.titleBelajarAlfabetTv.text = "Huruf U - Z"

        val args = this.arguments

        val alfabet = listOf("V","W","X","Y","Z")
        val image = listOf(
            R.drawable.img_sibi_v,
            R.drawable.img_sibi_w,
            R.drawable.img_sibi_x,
            R.drawable.img_sibi_y,
            R.drawable.img_sibi_z)
        val video = listOf(
            "android.resource://${activity?.packageName}/${R.raw.u}" ,
            "android.resource://${activity?.packageName}/${R.raw.v}" ,
            "android.resource://${activity?.packageName}/${R.raw.w}" ,
            "android.resource://${activity?.packageName}/${R.raw.x}" ,
            "android.resource://${activity?.packageName}/${R.raw.y}" ,
        )

        val viewPagerVideo : ViewPager2 = binding.viewPager
        val viewPagerImage : ViewPager2 = binding.viewPager2

        viewPagerVideo.isUserInputEnabled = false
        viewPagerVideo.adapter = ViewPagerVideoAdapter(alfabet, video)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        var count = sharedPreferences.getInt("count", 1)
        binding.seekBar.progress = count
        binding.seekBar.isEnabled = false
        binding.selanjutnyaButton.setOnClickListener {
            count++
            sharedPreferences.edit().putInt("count", count).apply()
            binding.seekBar.progress = count
            viewPagerVideo.setCurrentItem(getItem(+1, viewPagerVideo), true)
            if (count == 6){
                count = 5
                sharedPreferences.edit().putInt("count", count).apply()
                binding.seekBar.progress = count
                binding.scrollView.visibility = View.GONE
                binding.selanjutnyaButton.visibility = View.GONE
                binding.finishLayout.visibility = View.VISIBLE
                binding.titleKeteranganFinishTv.text = "Anda telah mengenal huruf U-Z"
                binding.deskripsiFinishTv.text = "Lanjutkan proses belajarmu dengan mulai mengingat huruf bersama SeeListen"
                binding.mulaiLatihanBtn.text = "Mulai mengingat"
                binding.kembaliMengingatBtn.text = "Kembali pengenalan"
                binding.icDot.setColorFilter(ContextCompat.getColor(requireContext(), R.color.see_listen_secondary))
                binding.mulaiLatihanBtn.setOnClickListener {
                    fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, PilihKartuFragment())?.commit()
                }
                binding.kembaliMengingatBtn.setOnClickListener {
                    binding.scrollView.visibility = View.VISIBLE
                    binding.selanjutnyaButton.visibility = View.VISIBLE
                    binding.finishLayout.visibility = View.GONE
                    count = 1
                    sharedPreferences.edit().putInt("count", count).apply()
                    binding.seekBar.progress = count
                    binding.icDot.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white))
                    viewPagerVideo.setCurrentItem(0, true)
                }
                binding.mulaiLatihanBtn.setOnClickListener {
                    binding.finishLayout.visibility = View.GONE
                    val bundle = Bundle()
                    bundle.putInt("menu", 5)
                    val fragment = PilihKartuFragment()
                    fragment.arguments = bundle
                    fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
                }
            }
        }

        val pilihan = args?.getInt("PILIHAN")
        if (pilihan != null){
            binding.scrollView.visibility = View.GONE
            binding.scrollView2.visibility = View.VISIBLE
            viewPagerImage.isUserInputEnabled = false
            viewPagerImage.adapter = ViewPagerImageAdapter(alfabet, image)
            count = sharedPreferences.getInt("count", 6)
            count++
            binding.seekBar.progress = count
            binding.icDot.setColorFilter(ContextCompat.getColor(requireContext(), R.color.see_listen_secondary))
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
                if (count == 10) {
                    binding.scrollView2.visibility = View.GONE
                    binding.selanjutnyaButton2.visibility = View.GONE
                    binding.finishLayout.visibility = View.VISIBLE
                    binding.titleKeteranganFinishTv.text = "Anda telah belajar huruf U-Z"
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
                        fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, BelajarAlfabetFragment())?.commit()
                    }
                } else {
                    val bundle = Bundle()
                    bundle.putInt("menu", 5)
                    val fragment = PilihKartuFragment()
                    fragment.arguments = bundle
                    fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
                }
            }
        }
        binding.closeButton.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, BelajarAlfabetFragment())?.commit()
        }
    }

    private fun hideBottomBar(){
        val bottomAppBar = requireActivity().findViewById<BottomAppBar>(R.id.bottomAppBar)
        val fab = requireActivity().findViewById<FloatingActionButton>(R.id.floatingActionButtonCamera)

        bottomAppBar.visibility = View.GONE
        fab.visibility = View.GONE
    }

    private fun getItem(id : Int, viewPager : ViewPager2): Int {
        return viewPager.currentItem + id
    }
}