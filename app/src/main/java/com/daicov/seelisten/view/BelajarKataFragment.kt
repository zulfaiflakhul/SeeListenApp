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
import com.daicov.seelisten.databinding.FragmentBelajarKataBinding
import com.daicov.seelisten.view.adapter.ViewPagerImageAdapter
import com.daicov.seelisten.view.adapter.ViewPagerVideoAdapter
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton

@Suppress("DEPRECATION")
class BelajarKataFragment : Fragment() {

    private var belajarKaBinding : FragmentBelajarKataBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, BelajarFragment())?.commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val args = this.arguments
        val menu = args?.getInt("menu")
        if (menu == null){
            sharedPreferences.edit().clear().apply()
        }
        return inflater.inflate(R.layout.fragment_belajar_kata, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideBottomBar()
        val binding = FragmentBelajarKataBinding.bind(view)
        belajarKaBinding = binding

        val args = this.arguments

        val alfabet = listOf("Saya","Kamu","Dan","Makan","Baca","Terima Kasih","Nama","Buku")
        val video = listOf(
            "android.resource://${activity?.packageName}/${R.raw.saya}" ,
            "android.resource://${activity?.packageName}/${R.raw.kamu}" ,
            "android.resource://${activity?.packageName}/${R.raw.dan}" ,
            "android.resource://${activity?.packageName}/${R.raw.makan}" ,
            "android.resource://${activity?.packageName}/${R.raw.baca}" ,
            "android.resource://${activity?.packageName}/${R.raw.makasih}" ,
            "android.resource://${activity?.packageName}/${R.raw.nama}" ,
            "android.resource://${activity?.packageName}/${R.raw.buku}"
        )

        val viewPagerVideo : ViewPager2 = binding.viewPager

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
            if (count == 9){
                sharedPreferences.edit().putInt("count", count).apply()
                binding.seekBar.progress = count
                binding.scrollView.visibility = View.GONE
                binding.selanjutnyaButton.visibility = View.GONE
                binding.finishLayout.visibility = View.VISIBLE
                binding.titleKeteranganFinishTv.text = "Anda telah belajar kata sehari-hari"
                binding.deskripsiFinishTv.text = "Lanjutkan proses belajarmu dengan mulai mengingat huruf bersama SeeListen"
                binding.mulaiLatihanBtn.text = "Mulai bermain"
                binding.kembaliMengingatBtn.text = "Kembali belajar"
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
                    viewPagerVideo.setCurrentItem(0, true)
                }
                binding.mulaiLatihanBtn.setOnClickListener {
                    binding.finishLayout.visibility = View.GONE
                    val bundle = Bundle()
                    bundle.putInt("menu", 1)
                    val fragment = PilihKartuFragment()
                    fragment.arguments = bundle
                    fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
                }
            }
        }
        binding.closeButton.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, BelajarFragment())?.commit()
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