package com.daicov.seelisten.view

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.daicov.seelisten.R
import com.daicov.seelisten.databinding.FragmentPilihKartuBinding
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton


@Suppress("DEPRECATION")
class PilihKartuFragment : Fragment() {

    private var fragmentPilihKartuBinding : FragmentPilihKartuBinding? = null
    private var selectedCard : Int = 0
    private var visible: Int = 0

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
        return inflater.inflate(R.layout.fragment_pilih_kartu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideBottomBar()
        val binding = FragmentPilihKartuBinding.bind(view)
        fragmentPilihKartuBinding = binding

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        visible = sharedPreferences.getInt("c1", View.VISIBLE)
        binding.kartuPertama.visibility = visible
        visible = sharedPreferences.getInt("c2", View.VISIBLE)
        binding.kartuKedua.visibility = visible
        visible = sharedPreferences.getInt("c3", View.VISIBLE)
        binding.kartuKetiga.visibility = visible
        visible = sharedPreferences.getInt("c4", View.VISIBLE)
        binding.kartuEmpat.visibility = visible
        visible = sharedPreferences.getInt("c5", View.VISIBLE)
        binding.kartuLima.visibility = visible

        val args = this.arguments
        val menu = args?.getInt("menu")
        if (menu == 1){
            binding.kartuPertama.setOnClickListener {
                binding.kartuPertama.visibility = View.GONE
                sharedPreferences.edit().putInt("c1", View.GONE).apply()
                selectedCard = 6
                val bundle = Bundle()
                bundle.putInt("PILIHAN", selectedCard)
                val fragment = BelajarAeFragment()
                fragment.arguments = bundle

                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
            }

            binding.kartuKedua.setOnClickListener {
                binding.kartuKedua.visibility = View.GONE
                sharedPreferences.edit().putInt("c2", View.GONE).apply()
                selectedCard = 7
                val bundle = Bundle()
                bundle.putInt("PILIHAN", selectedCard)
                val fragment = BelajarAeFragment()
                fragment.arguments = bundle

                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
            }

            binding.kartuKetiga.setOnClickListener {
                binding.kartuKetiga.visibility = View.GONE
                sharedPreferences.edit().putInt("c3", View.GONE).apply()
                selectedCard = 8
                val bundle = Bundle()
                bundle.putInt("PILIHAN", selectedCard)
                val fragment = BelajarAeFragment()
                fragment.arguments = bundle

                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
            }

            binding.kartuEmpat.setOnClickListener {
                binding.kartuEmpat.visibility = View.GONE
                sharedPreferences.edit().putInt("c4", View.GONE).apply()
                selectedCard = 9
                val bundle = Bundle()
                bundle.putInt("PILIHAN", selectedCard)
                val fragment = BelajarAeFragment()
                fragment.arguments = bundle

                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
            }

            binding.kartuLima.setOnClickListener {
                binding.kartuLima.visibility = View.GONE
                sharedPreferences.edit().putInt("c5", View.GONE).apply()
                selectedCard = 10
                val bundle = Bundle()
                bundle.putInt("PILIHAN", selectedCard)
                val fragment = BelajarAeFragment()
                fragment.arguments = bundle

                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
            }
        } else if (menu == 2){
            binding.kartuPertama.setOnClickListener {
                binding.kartuPertama.visibility = View.GONE
                sharedPreferences.edit().putInt("c1", View.GONE).apply()
                selectedCard = 6
                val bundle = Bundle()
                bundle.putInt("PILIHAN", selectedCard)
                val fragment = BelajarFjFragment()
                fragment.arguments = bundle

                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
            }

            binding.kartuKedua.setOnClickListener {
                binding.kartuKedua.visibility = View.GONE
                sharedPreferences.edit().putInt("c2", View.GONE).apply()
                selectedCard = 7
                val bundle = Bundle()
                bundle.putInt("PILIHAN", selectedCard)
                val fragment = BelajarFjFragment()
                fragment.arguments = bundle

                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
            }

            binding.kartuKetiga.setOnClickListener {
                binding.kartuKetiga.visibility = View.GONE
                sharedPreferences.edit().putInt("c3", View.GONE).apply()
                selectedCard = 8
                val bundle = Bundle()
                bundle.putInt("PILIHAN", selectedCard)
                val fragment = BelajarFjFragment()
                fragment.arguments = bundle

                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
            }

            binding.kartuEmpat.setOnClickListener {
                binding.kartuEmpat.visibility = View.GONE
                sharedPreferences.edit().putInt("c4", View.GONE).apply()
                selectedCard = 9
                val bundle = Bundle()
                bundle.putInt("PILIHAN", selectedCard)
                val fragment = BelajarFjFragment()
                fragment.arguments = bundle

                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
            }

            binding.kartuLima.setOnClickListener {
                binding.kartuLima.visibility = View.GONE
                sharedPreferences.edit().putInt("c5", View.GONE).apply()
                selectedCard = 10
                val bundle = Bundle()
                bundle.putInt("PILIHAN", selectedCard)
                val fragment = BelajarFjFragment()
                fragment.arguments = bundle

                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
            }
        } else if (menu == 3){
            binding.kartuPertama.setOnClickListener {
                binding.kartuPertama.visibility = View.GONE
                sharedPreferences.edit().putInt("c1", View.GONE).apply()
                selectedCard = 6
                val bundle = Bundle()
                bundle.putInt("PILIHAN", selectedCard)
                val fragment = BelajarKoFragment()
                fragment.arguments = bundle

                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
            }

            binding.kartuKedua.setOnClickListener {
                binding.kartuKedua.visibility = View.GONE
                sharedPreferences.edit().putInt("c2", View.GONE).apply()
                selectedCard = 7
                val bundle = Bundle()
                bundle.putInt("PILIHAN", selectedCard)
                val fragment = BelajarKoFragment()
                fragment.arguments = bundle

                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
            }

            binding.kartuKetiga.setOnClickListener {
                binding.kartuKetiga.visibility = View.GONE
                sharedPreferences.edit().putInt("c3", View.GONE).apply()
                selectedCard = 8
                val bundle = Bundle()
                bundle.putInt("PILIHAN", selectedCard)
                val fragment = BelajarKoFragment()
                fragment.arguments = bundle

                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
            }

            binding.kartuEmpat.setOnClickListener {
                binding.kartuEmpat.visibility = View.GONE
                sharedPreferences.edit().putInt("c4", View.GONE).apply()
                selectedCard = 9
                val bundle = Bundle()
                bundle.putInt("PILIHAN", selectedCard)
                val fragment = BelajarKoFragment()
                fragment.arguments = bundle

                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
            }

            binding.kartuLima.setOnClickListener {
                binding.kartuLima.visibility = View.GONE
                sharedPreferences.edit().putInt("c5", View.GONE).apply()
                selectedCard = 10
                val bundle = Bundle()
                bundle.putInt("PILIHAN", selectedCard)
                val fragment = BelajarKoFragment()
                fragment.arguments = bundle

                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
            }
        } else if (menu == 4){
            binding.kartuPertama.setOnClickListener {
                binding.kartuPertama.visibility = View.GONE
                sharedPreferences.edit().putInt("c1", View.GONE).apply()
                selectedCard = 6
                val bundle = Bundle()
                bundle.putInt("PILIHAN", selectedCard)
                val fragment = BelajarPtFragment()
                fragment.arguments = bundle

                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
            }

            binding.kartuKedua.setOnClickListener {
                binding.kartuKedua.visibility = View.GONE
                sharedPreferences.edit().putInt("c2", View.GONE).apply()
                selectedCard = 7
                val bundle = Bundle()
                bundle.putInt("PILIHAN", selectedCard)
                val fragment = BelajarPtFragment()
                fragment.arguments = bundle

                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
            }

            binding.kartuKetiga.setOnClickListener {
                binding.kartuKetiga.visibility = View.GONE
                sharedPreferences.edit().putInt("c3", View.GONE).apply()
                selectedCard = 8
                val bundle = Bundle()
                bundle.putInt("PILIHAN", selectedCard)
                val fragment = BelajarPtFragment()
                fragment.arguments = bundle

                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
            }

            binding.kartuEmpat.setOnClickListener {
                binding.kartuEmpat.visibility = View.GONE
                sharedPreferences.edit().putInt("c4", View.GONE).apply()
                selectedCard = 9
                val bundle = Bundle()
                bundle.putInt("PILIHAN", selectedCard)
                val fragment = BelajarPtFragment()
                fragment.arguments = bundle

                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
            }

            binding.kartuLima.setOnClickListener {
                binding.kartuLima.visibility = View.GONE
                sharedPreferences.edit().putInt("c5", View.GONE).apply()
                selectedCard = 10
                val bundle = Bundle()
                bundle.putInt("PILIHAN", selectedCard)
                val fragment = BelajarPtFragment()
                fragment.arguments = bundle

                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
            }
        } else if (menu == 5){
            binding.kartuPertama.setOnClickListener {
                binding.kartuPertama.visibility = View.GONE
                sharedPreferences.edit().putInt("c1", View.GONE).apply()
                selectedCard = 6
                val bundle = Bundle()
                bundle.putInt("PILIHAN", selectedCard)
                val fragment = BelajarUzFragment()
                fragment.arguments = bundle

                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
            }

            binding.kartuKedua.setOnClickListener {
                binding.kartuKedua.visibility = View.GONE
                sharedPreferences.edit().putInt("c2", View.GONE).apply()
                selectedCard = 7
                val bundle = Bundle()
                bundle.putInt("PILIHAN", selectedCard)
                val fragment = BelajarUzFragment()
                fragment.arguments = bundle

                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
            }

            binding.kartuKetiga.setOnClickListener {
                binding.kartuKetiga.visibility = View.GONE
                sharedPreferences.edit().putInt("c3", View.GONE).apply()
                selectedCard = 8
                val bundle = Bundle()
                bundle.putInt("PILIHAN", selectedCard)
                val fragment = BelajarUzFragment()
                fragment.arguments = bundle

                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
            }

            binding.kartuEmpat.setOnClickListener {
                binding.kartuEmpat.visibility = View.GONE
                sharedPreferences.edit().putInt("c4", View.GONE).apply()
                selectedCard = 9
                val bundle = Bundle()
                bundle.putInt("PILIHAN", selectedCard)
                val fragment = BelajarUzFragment()
                fragment.arguments = bundle

                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
            }

            binding.kartuLima.setOnClickListener {
                binding.kartuLima.visibility = View.GONE
                sharedPreferences.edit().putInt("c5", View.GONE).apply()
                selectedCard = 10
                val bundle = Bundle()
                bundle.putInt("PILIHAN", selectedCard)
                val fragment = BelajarUzFragment()
                fragment.arguments = bundle

                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
            }
        } else if (menu == 6){
            binding.kartuPertama.setOnClickListener {
                binding.kartuPertama.visibility = View.GONE
                sharedPreferences.edit().putInt("c1", View.GONE).apply()
                selectedCard = 6
                val bundle = Bundle()
                bundle.putInt("PILIHAN", selectedCard)
                val fragment = BelajarSatuFragment()
                fragment.arguments = bundle

                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
            }

            binding.kartuKedua.setOnClickListener {
                binding.kartuKedua.visibility = View.GONE
                sharedPreferences.edit().putInt("c2", View.GONE).apply()
                selectedCard = 7
                val bundle = Bundle()
                bundle.putInt("PILIHAN", selectedCard)
                val fragment = BelajarSatuFragment()
                fragment.arguments = bundle

                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
            }

            binding.kartuKetiga.setOnClickListener {
                binding.kartuKetiga.visibility = View.GONE
                sharedPreferences.edit().putInt("c3", View.GONE).apply()
                selectedCard = 8
                val bundle = Bundle()
                bundle.putInt("PILIHAN", selectedCard)
                val fragment = BelajarSatuFragment()
                fragment.arguments = bundle

                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
            }

            binding.kartuEmpat.setOnClickListener {
                binding.kartuEmpat.visibility = View.GONE
                sharedPreferences.edit().putInt("c4", View.GONE).apply()
                selectedCard = 9
                val bundle = Bundle()
                bundle.putInt("PILIHAN", selectedCard)
                val fragment = BelajarSatuFragment()
                fragment.arguments = bundle

                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
            }

            binding.kartuLima.setOnClickListener {
                binding.kartuLima.visibility = View.GONE
                sharedPreferences.edit().putInt("c5", View.GONE).apply()
                selectedCard = 10
                val bundle = Bundle()
                bundle.putInt("PILIHAN", selectedCard)
                val fragment = BelajarSatuFragment()
                fragment.arguments = bundle

                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
            }
        }

        if (binding.kartuPertama.visibility == View.GONE && binding.kartuKedua.visibility == View.GONE &&
            binding.kartuKetiga.visibility == View.GONE && binding.kartuEmpat.visibility == View.GONE &&
            binding.kartuLima.visibility == View.GONE) {
            sharedPreferences.edit().clear().apply()
        }
    }

    private fun hideBottomBar(){
        val bottomAppBar = requireActivity().findViewById<BottomAppBar>(R.id.bottomAppBar)
        val fab = requireActivity().findViewById<FloatingActionButton>(R.id.floatingActionButtonCamera)

        bottomAppBar.visibility = View.GONE
        fab.visibility = View.GONE
    }

}