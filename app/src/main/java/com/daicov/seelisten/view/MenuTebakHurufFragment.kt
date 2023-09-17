package com.daicov.seelisten.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.daicov.seelisten.R
import com.daicov.seelisten.databinding.FragmentMenuTebakHurufBinding
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton

@Suppress("DEPRECATION")
class MenuTebakHurufFragment : Fragment() {

    private var fragmentMenuTebakHurufBinding : FragmentMenuTebakHurufBinding? = null
    private lateinit var sharedPreferences : SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, GameFragment())?.commit()
                val bottomAppBar = requireActivity().findViewById<BottomAppBar>(R.id.bottomAppBar)
                val fab = requireActivity().findViewById<FloatingActionButton>(R.id.floatingActionButtonCamera)

                bottomAppBar.visibility = View.VISIBLE
                fab.visibility = View.VISIBLE
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        return inflater.inflate(R.layout.fragment_menu_tebak_huruf, container, false)
    }

    @SuppressLint("CommitPrefEdits")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentMenuTebakHurufBinding.bind(view)
        fragmentMenuTebakHurufBinding = binding
        hideBottomBar()
        binding.hurufFJButton.isEnabled = false
        binding.hurufKOButton.isEnabled = false
        binding.hurufPTButton.isEnabled = false
        binding.hurufUZButton.isEnabled = false

        val args = this.arguments
        val progressAE = args?.getInt("PROGRESS_AE")
        if (progressAE != null){
            saveProgressAE(progressAE)
            binding.progressBarAE.progress = loadProgressAE()
        } else {
            binding.progressBarAE.progress = loadProgressAE()
            binding.icCeklistae.visibility = loadCheckAE()
            binding.icLockfj.visibility = loadLockFJ()
        }

        val progressFJ = args?.getInt("PROGRESS_FJ")
        if (progressFJ != null){
            saveProgressFJ(progressFJ)
            binding.progressBarFJ.progress = loadProgressFJ()
        } else {
            binding.progressBarFJ.progress = loadProgressFJ()
            binding.icCeklistfj.visibility = loadCheckFJ()
        }

        val progressKO = args?.getInt("PROGRESS_KO")
        if (progressKO != null){
            saveProgressKO(progressKO)
            binding.progressBarKO.progress = loadProgressKO()
        } else {
            binding.progressBarKO.progress = loadProgressKO()
            binding.icCeklistko.visibility = loadCheckKO()
        }

        val progressPT = args?.getInt("PROGRESS_PT")
        if (progressPT != null){
            saveProgressPT(progressPT)
            binding.progressBarPT.progress = loadProgressPT()
        } else {
            binding.progressBarPT.progress = loadProgressPT()
            binding.icCeklistpt.visibility = loadCheckPT()
        }

        val progressUZ = args?.getInt("PROGRESS_UZ")
        if (progressUZ != null){
            saveProgressUZ(progressUZ)
            binding.progressBarUZ.progress = loadProgressUZ()
        } else {
            binding.progressBarUZ.progress = loadProgressUZ()
            binding.icCeklistuz.visibility = loadCheckUZ()
        }

        if (binding.progressBarAE.progress == 5){
            finishAE()
        } else if (binding.progressBarFJ.progress == 5){
            finishAE()
            finishFJ()
        } else if (binding.progressBarKO.progress == 5){
            finishAE()
            finishFJ()
            finishKO()
        } else if (binding.progressBarPT.progress == 5){
            finishAE()
            finishFJ()
            finishKO()
            finishPT()
        } else if (binding.progressBarUZ.progress == 5){
            finishAE()
            finishFJ()
            finishKO()
            finishPT()
            finishUZ()
        }

        binding.hurufAEButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("GAME_MENU", 1)
            bundle.putInt("progressGame", 0)
            val fragment = GameTebakHurufFragment()
            fragment.arguments = bundle
            fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
        }

        binding.hurufFJButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("GAME_MENU", 2)
            bundle.putInt("progressGame", 0)
            val fragment = GameTebakHurufFragment()
            fragment.arguments = bundle
            fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
        }

        binding.hurufKOButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("GAME_MENU", 3)
            bundle.putInt("progressGame", 0)
            val fragment = GameTebakHurufFragment()
            fragment.arguments = bundle
            fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
        }

        binding.hurufPTButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("GAME_MENU", 4)
            bundle.putInt("progressGame", 0)
            val fragment = GameTebakHurufFragment()
            fragment.arguments = bundle
            fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
        }

        binding.hurufUZButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("GAME_MENU", 5)
            bundle.putInt("progressGame", 0)
            val fragment = GameTebakHurufFragment()
            fragment.arguments = bundle
            fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
        }
    }

    private fun finishAE(){
        saveProgressAE(5)
        fragmentMenuTebakHurufBinding?.progressBarAE?.progress = loadProgressAE()
        saveCheckAE()
        fragmentMenuTebakHurufBinding?.icCeklistae?.visibility = loadCheckAE()
        fragmentMenuTebakHurufBinding?.hurufFJButton?.isEnabled = true
        fragmentMenuTebakHurufBinding?.hurufFJButton?.setImageResource(R.drawable.ic_alfabetfj_permainan)
        saveLockFJ()
        fragmentMenuTebakHurufBinding?.icLockfj?.visibility = loadLockFJ()
    }

    private fun finishFJ(){
        saveProgressFJ(5)
        fragmentMenuTebakHurufBinding?.progressBarFJ?.progress = loadProgressFJ()
        saveCheckFJ()
        fragmentMenuTebakHurufBinding?.icCeklistfj?.visibility = loadCheckFJ()
        fragmentMenuTebakHurufBinding?.hurufKOButton?.isEnabled = true
        fragmentMenuTebakHurufBinding?.hurufKOButton?.setImageResource(R.drawable.ic_alfabetko_permainan)
        saveLockKO()
        fragmentMenuTebakHurufBinding?.icLockko?.visibility = loadLockKO()
    }

    private fun finishKO(){
        saveProgressKO(5)
        fragmentMenuTebakHurufBinding?.progressBarKO?.progress = loadProgressKO()
        saveCheckKO()
        fragmentMenuTebakHurufBinding?.icCeklistko?.visibility = loadCheckKO()
        fragmentMenuTebakHurufBinding?.hurufPTButton?.isEnabled = true
        fragmentMenuTebakHurufBinding?.hurufPTButton?.setImageResource(R.drawable.ic_alfabetpt_permainan)
        saveLockPT()
        fragmentMenuTebakHurufBinding?.icLockpt?.visibility = loadLockPT()
    }

    private fun finishPT(){
        saveProgressPT(5)
        fragmentMenuTebakHurufBinding?.progressBarPT?.progress = loadProgressPT()
        saveCheckPT()
        fragmentMenuTebakHurufBinding?.icCeklistpt?.visibility = loadCheckPT()
        fragmentMenuTebakHurufBinding?.hurufUZButton?.isEnabled = true
        fragmentMenuTebakHurufBinding?.hurufUZButton?.setImageResource(R.drawable.ic_alfabetuz_permainan)
        saveLockUZ()
        fragmentMenuTebakHurufBinding?.icLockuz?.visibility = loadLockUZ()
    }

    private fun finishUZ(){
        saveProgressUZ(5)
        fragmentMenuTebakHurufBinding?.progressBarUZ?.progress = loadProgressUZ()
        saveCheckUZ()
        fragmentMenuTebakHurufBinding?.icCeklistuz?.visibility = loadCheckUZ()
    }

    private fun hideBottomBar(){
        val bottomAppBar = requireActivity().findViewById<BottomAppBar>(R.id.bottomAppBar)
        val fab = requireActivity().findViewById<FloatingActionButton>(R.id.floatingActionButtonCamera)

        bottomAppBar.visibility = View.GONE
        fab.visibility = View.GONE
    }

    @SuppressLint("CommitPrefEdits")
    fun saveProgressAE(value : Int){
        sharedPreferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("PROGRESS_AE", value)
        editor.apply()
    }

    private fun loadProgressAE(): Int {
        sharedPreferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("PROGRESS_AE", 0)
    }

    @SuppressLint("CommitPrefEdits")
    fun saveProgressFJ(value : Int){
        sharedPreferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("PROGRESS_FJ", value)
        editor.apply()
    }

    private fun loadProgressFJ(): Int {
        sharedPreferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("PROGRESS_FJ", 0)
    }

    @SuppressLint("CommitPrefEdits")
    private fun saveCheckAE(){
        sharedPreferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("CHECK_AE", View.VISIBLE)
        editor.apply()
    }

    private fun loadCheckAE(): Int {
        sharedPreferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("CHECK_AE", View.GONE)
    }

    @SuppressLint("CommitPrefEdits")
    private fun saveLockFJ(){
        sharedPreferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("LOCK_FJ", View.GONE)
        editor.apply()
    }

    private fun loadLockFJ(): Int {
        sharedPreferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("LOCK_FJ", View.VISIBLE)
    }

    @SuppressLint("CommitPrefEdits")
    private fun saveCheckFJ(){
        sharedPreferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("CHECK_AE", View.VISIBLE)
        editor.apply()
    }

    private fun loadCheckFJ(): Int {
        sharedPreferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("CHECK_AE", View.GONE)
    }

    @SuppressLint("CommitPrefEdits")
    private fun saveLockKO(){
        sharedPreferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("LOCK_KO", View.GONE)
        editor.apply()
    }

    private fun loadLockKO(): Int {
        sharedPreferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("LOCK_KO", View.VISIBLE)
    }

    @SuppressLint("CommitPrefEdits")
    fun saveProgressKO(value : Int){
        sharedPreferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("PROGRESS_KO", value)
        editor.apply()
    }

    private fun loadProgressKO(): Int {
        sharedPreferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("PROGRESS_KO", 0)
    }

    @SuppressLint("CommitPrefEdits")
    private fun saveCheckKO(){
        sharedPreferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("CHECK_KO", View.VISIBLE)
        editor.apply()
    }

    private fun loadCheckKO(): Int {
        sharedPreferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("CHECK_KO", View.GONE)
    }

    @SuppressLint("CommitPrefEdits")
    private fun saveLockPT(){
        sharedPreferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("LOCK_PT", View.GONE)
        editor.apply()
    }

    private fun loadLockPT(): Int {
        sharedPreferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("LOCK_PT", View.VISIBLE)
    }

    @SuppressLint("CommitPrefEdits")
    fun saveProgressPT(value : Int){
        sharedPreferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("PROGRESS_PT", value)
        editor.apply()
    }

    private fun loadProgressPT(): Int {
        sharedPreferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("PROGRESS_PT", 0)
    }

    @SuppressLint("CommitPrefEdits")
    private fun saveCheckPT(){
        sharedPreferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("CHECK_PT", View.VISIBLE)
        editor.apply()
    }

    private fun loadCheckPT(): Int {
        sharedPreferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("CHECK_PT", View.GONE)
    }

    @SuppressLint("CommitPrefEdits")
    private fun saveLockUZ(){
        sharedPreferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("LOCK_UZ", View.GONE)
        editor.apply()
    }

    private fun loadLockUZ(): Int {
        sharedPreferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("LOCK_UZ", View.VISIBLE)
    }

    @SuppressLint("CommitPrefEdits")
    fun saveProgressUZ(value : Int){
        sharedPreferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("PROGRESS_UZ", value)
        editor.apply()
    }

    private fun loadProgressUZ(): Int {
        sharedPreferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("PROGRESS_UZ", 0)
    }

    @SuppressLint("CommitPrefEdits")
    private fun saveCheckUZ(){
        sharedPreferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("CHECK_UZ", View.VISIBLE)
        editor.apply()
    }

    private fun loadCheckUZ(): Int {
        sharedPreferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("CHECK_UZ", View.GONE)
    }
}