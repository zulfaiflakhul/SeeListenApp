package com.daicov.seelisten.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.camera.core.*
import com.daicov.seelisten.CameraSecondActivity
import com.daicov.seelisten.R
import com.daicov.seelisten.databinding.FragmentGameTebakHurufBinding
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton

@Suppress("ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "DEPRECATION")
class GameTebakHurufFragment : Fragment() {

    private var fragmentGameTebakHurufBinding : FragmentGameTebakHurufBinding? = null
    private var isTrue : Boolean? = null
    private var jawabanA = false
    private var jawabanB = false
    private var jawabanC = false
    private var scoreA : Int? = null
    private var scoreB : Int? = null
    private var scoreC : Int? = null
    private var scoreD : Int? = null
    private var scoreE : Int? = null
    private var score = 0
    private var progress = 1
    private var visible: Int = 0
    private lateinit var sharedPreferences : SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, MenuTebakHurufFragment())?.commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        return inflater.inflate(R.layout.fragment_game_tebak_huruf, container, false)
    }

    @SuppressLint("InflateParams", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentGameTebakHurufBinding.bind(view)
        fragmentGameTebakHurufBinding = binding

        scoreA = loadScoreA()
        scoreB = loadScoreB()
        scoreC = loadScoreC()
        scoreD = loadScoreD()
        scoreE = loadScoreE()
        val sp = PreferenceManager.getDefaultSharedPreferences(requireContext())
        visible = sp.getInt("a0", View.GONE)
        binding.iconAFalse.visibility = visible
        visible = sp.getInt("b0", View.GONE)
        binding.iconBFalse.visibility = visible
        visible = sp.getInt("c0", View.GONE)
        binding.iconCFalse.visibility = visible
        visible = sp.getInt("d0", View.GONE)
        binding.iconDFalse.visibility = visible
        visible = sp.getInt("e0", View.GONE)
        binding.iconEFalse.visibility = visible

        visible = sp.getInt("a1", View.GONE)
        binding.iconATrue.visibility = visible
        visible = sp.getInt("b1", View.GONE)
        binding.iconBTrue.visibility = visible
        visible = sp.getInt("c1", View.GONE)
        binding.iconCTrue.visibility = visible
        visible = sp.getInt("d1", View.GONE)
        binding.iconDTrue.visibility = visible
        visible = sp.getInt("e1", View.GONE)
        binding.iconETrue.visibility = visible

        hideBottomBar()
        val args = this.arguments
        val progressMenu = args?.getInt("progressGame")

        val gameMenu = args?.getInt("GAME_MENU")
        val bundle = Bundle()
        when (gameMenu) {
            1 -> {

                binding.benarButton.setOnClickListener {
                    val dialogView = layoutInflater.inflate(R.layout.bottom_sheet_benar, null)
                    val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme2)
                    dialog.setContentView(dialogView)
                    dialog.setCanceledOnTouchOutside(false)
                    binding.benarButton.setBackgroundResource(R.drawable.radio_pilihan_selected)

                    val selanjutnyaBtn = dialog.findViewById<Button>(R.id.bottomSheetSelanjutnyaButton)
                    val gamePertama = binding.gamePertama
                    val gameKedua = binding.gameKedua

                    selanjutnyaBtn?.setOnClickListener {
                        gamePertama.visibility = View.GONE
                        gameKedua.visibility = View.VISIBLE
                        progress++
                        saveScoreA()
                        jawabEBenar()
                        binding.gameProgressBar.setProgress(progress, true)
                        dialog.dismiss()
                    }
                    score++
                    dialog.show()
                }
                binding.salahButton.setOnClickListener {
                    binding.gamePertama.visibility = View.GONE
                    binding.gameKedua.visibility = View.VISIBLE
                    progress++
                    jawabESalah()
                    binding.gameProgressBar.setProgress(progress, true)
                }

                binding.pilihanA.setOnClickListener {
                    binding.overlayClickA.visibility = View.VISIBLE
                    binding.overlayClickB.visibility = View.GONE
                    binding.overlayClickC.visibility = View.GONE
                    binding.overlayClickD.visibility = View.GONE
                    isTrue = false
                }

                binding.pilihanB.setOnClickListener {
                    binding.overlayClickA.visibility = View.GONE
                    binding.overlayClickB.visibility = View.VISIBLE
                    binding.overlayClickC.visibility = View.GONE
                    binding.overlayClickD.visibility = View.GONE
                    isTrue = false
                }

                binding.pilihanC.setOnClickListener {
                    binding.overlayClickA.visibility = View.GONE
                    binding.overlayClickB.visibility = View.GONE
                    binding.overlayClickC.visibility = View.VISIBLE
                    binding.overlayClickD.visibility = View.GONE
                    isTrue = true
                }

                binding.pilihanD.setOnClickListener {
                    binding.overlayClickA.visibility = View.GONE
                    binding.overlayClickB.visibility = View.GONE
                    binding.overlayClickC.visibility = View.GONE
                    binding.overlayClickD.visibility = View.VISIBLE
                    isTrue = false
                }

                binding.periksaJawabanGame2.setOnClickListener {
                    if (isTrue == true){
                        binding.overlayClickC.setBackgroundResource(R.color.see_listen_true)
                        binding.periksaJawabanGame2.visibility = View.GONE
                        val dialogView = layoutInflater.inflate(R.layout.bottom_sheet_benar, null)
                        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme2)
                        dialog.setContentView(dialogView)
                        dialog.setCanceledOnTouchOutside(false)

                        val selanjutnyaBtn = dialog.findViewById<Button>(R.id.bottomSheetSelanjutnyaButton)
                        val gameKedua = binding.gameKedua
                        val gameKetiga = binding.gameKetiga

                        selanjutnyaBtn?.setOnClickListener {
                            gameKedua.visibility = View.GONE
                            gameKetiga.visibility = View.VISIBLE
                            progress++
                            saveScoreB()
                            jawabDBenar()
                            binding.gameProgressBar.setProgress(progress, true)
                            dialog.dismiss()
                        }
                        score++
                        dialog.show()
                    } else {
                        binding.overlayClickA.setBackgroundResource(R.color.see_listen_false)
                        binding.overlayClickB.setBackgroundResource(R.color.see_listen_false)
                        binding.overlayClickD.setBackgroundResource(R.color.see_listen_false)
                        binding.periksaJawabanGame2.visibility = View.GONE
                        val dialogView = layoutInflater.inflate(R.layout.bottom_sheet_salah, null)
                        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme2)
                        dialog.setContentView(dialogView)
                        dialog.setCanceledOnTouchOutside(false)

                        val selanjutnyaBtn = dialog.findViewById<Button>(R.id.bottomSheetSelanjutnyaButton)
                        val gameKedua = binding.gameKedua
                        val gameKetiga = binding.gameKetiga

                        selanjutnyaBtn?.setOnClickListener {
                            gameKedua.visibility = View.GONE
                            gameKetiga.visibility = View.VISIBLE
                            progress++
                            jawabDSalah()
                            binding.gameProgressBar.setProgress(progress, true)
                            dialog.dismiss()
                        }
                        dialog.show()
                    }
                }
                val bahasaA = binding.pilihanbahasaA
                val bahasaB = binding.pilihanBahasaB
                val bahasaC = binding.pilihanBahasaC
                val isyaratA = binding.pilihanIsyaratA
                val isyaratB = binding.pilihanIsyaratB
                val isyaratC = binding.pilihanIsyaratC
                bahasaA.setOnClickListener {
                    if (isyaratA.isEnabled){
                        isyaratB.isEnabled = true
                        isyaratC.isEnabled = true
                    } else if (isyaratB.isEnabled){
                        isyaratA.isEnabled = true
                        isyaratC.isEnabled = true
                    } else if (isyaratC.isEnabled){
                        isyaratA.isEnabled = true
                        isyaratB.isEnabled = true
                    }
                    binding.overlayClickBahasaA.visibility = View.VISIBLE
                    binding.overlayClickBahasaB.visibility = View.GONE
                    binding.overlayClickBahasaC.visibility = View.GONE
                    isyaratA.setOnClickListener {
                        binding.overlayClickIsyaratA.visibility = View.VISIBLE
                        binding.overlayClickIsyaratA.setBackgroundResource(R.color.see_listen_false)
                        binding.overlayClickBahasaA.visibility = View.GONE
                        binding.containerA.setBackgroundResource(R.color.see_listen_false)
                        isyaratB.isEnabled = false
                        isyaratC.isEnabled = false
                        bahasaA.isEnabled = false
                        jawabASalah()
                    }
                    isyaratB.setOnClickListener {
                        binding.overlayClickIsyaratB.visibility = View.VISIBLE
                        binding.overlayClickIsyaratB.setBackgroundResource(R.color.see_listen_false)
                        binding.overlayClickBahasaA.visibility = View.GONE
                        binding.containerA.setBackgroundResource(R.color.see_listen_false)
                        isyaratA.isEnabled = false
                        isyaratC.isEnabled = false
                        bahasaA.isEnabled = false
                        jawabASalah()
                    }
                    isyaratC.setOnClickListener {
                        binding.overlayClickIsyaratC.visibility = View.VISIBLE
                        binding.overlayClickIsyaratC.setBackgroundResource(R.color.see_listen_true)
                        binding.overlayClickBahasaA.visibility = View.GONE
                        binding.containerA.setBackgroundResource(R.color.see_listen_true)
                        isyaratA.isEnabled = false
                        isyaratB.isEnabled = false
                        bahasaA.isEnabled = false
                        jawabanA = true
                        score++
                        saveScoreC()
                        jawabABenar()
                    }
                }
                bahasaB.setOnClickListener {
                    if (isyaratA.isEnabled){
                        isyaratB.isEnabled = true
                        isyaratC.isEnabled = true
                    } else if (isyaratB.isEnabled){
                        isyaratA.isEnabled = true
                        isyaratC.isEnabled = true
                    } else if (isyaratC.isEnabled){
                        isyaratA.isEnabled = true
                        isyaratB.isEnabled = true
                    }
                    isyaratA.setOnClickListener {
                        binding.overlayClickIsyaratA.visibility = View.VISIBLE
                        binding.overlayClickIsyaratA.setBackgroundResource(R.color.see_listen_true)
                        binding.overlayClickBahasaB.visibility = View.GONE
                        binding.containerB.setBackgroundResource(R.color.see_listen_true)
                        isyaratB.isEnabled = false
                        isyaratC.isEnabled = false
                        bahasaB.isEnabled = false
                        jawabanB = true
                        score++
                        saveScoreD()
                        jawabBBenar()
                    }
                    isyaratB.setOnClickListener {
                        binding.overlayClickIsyaratB.visibility = View.VISIBLE
                        binding.overlayClickIsyaratB.setBackgroundResource(R.color.see_listen_false)
                        binding.overlayClickBahasaB.visibility = View.GONE
                        binding.containerB.setBackgroundResource(R.color.see_listen_false)
                        isyaratA.isEnabled = false
                        isyaratC.isEnabled = false
                        bahasaB.isEnabled = false
                        jawabBSalah()
                    }
                    isyaratC.setOnClickListener {
                        binding.overlayClickIsyaratC.visibility = View.VISIBLE
                        binding.overlayClickIsyaratC.setBackgroundResource(R.color.see_listen_false)
                        binding.overlayClickBahasaB.visibility = View.GONE
                        binding.containerB.setBackgroundResource(R.color.see_listen_false)
                        isyaratA.isEnabled = false
                        isyaratB.isEnabled = false
                        bahasaB.isEnabled = false
                        jawabBSalah()
                    }
                    binding.overlayClickBahasaA.visibility = View.GONE
                    binding.overlayClickBahasaB.visibility = View.VISIBLE
                    binding.overlayClickBahasaC.visibility = View.GONE
                }
                bahasaC.setOnClickListener {
                    if (isyaratA.isEnabled){
                        isyaratB.isEnabled = true
                        isyaratC.isEnabled = true
                    } else if (isyaratB.isEnabled){
                        isyaratA.isEnabled = true
                        isyaratC.isEnabled = true
                    } else if (isyaratC.isEnabled){
                        isyaratA.isEnabled = true
                        isyaratB.isEnabled = true
                    }
                    isyaratA.setOnClickListener {
                        binding.overlayClickIsyaratA.visibility = View.VISIBLE
                        binding.overlayClickIsyaratA.setBackgroundResource(R.color.see_listen_false)
                        binding.overlayClickBahasaC.visibility = View.GONE
                        binding.containerC.setBackgroundResource(R.color.see_listen_false)
                        isyaratB.isEnabled = false
                        isyaratC.isEnabled = false
                        bahasaC.isEnabled = false
                        jawabCSalah()
                    }
                    isyaratB.setOnClickListener {
                        binding.overlayClickIsyaratB.visibility = View.VISIBLE
                        binding.overlayClickIsyaratB.setBackgroundResource(R.color.see_listen_true)
                        binding.overlayClickBahasaC.visibility = View.GONE
                        binding.containerC.setBackgroundResource(R.color.see_listen_true)
                        isyaratA.isEnabled = false
                        isyaratC.isEnabled = false
                        bahasaC.isEnabled = false
                        jawabanC = true
                        score++
                        saveScoreE()
                        jawabCBenar()
                    }
                    isyaratC.setOnClickListener {
                        binding.overlayClickIsyaratC.visibility = View.VISIBLE
                        binding.overlayClickIsyaratC.setBackgroundResource(R.color.see_listen_false)
                        binding.overlayClickBahasaC.visibility = View.GONE
                        binding.containerC.setBackgroundResource(R.color.see_listen_false)
                        isyaratA.isEnabled = false
                        isyaratB.isEnabled = false
                        bahasaC.isEnabled = false
                        jawabCSalah()
                    }
                    binding.overlayClickBahasaA.visibility = View.GONE
                    binding.overlayClickBahasaB.visibility = View.GONE
                    binding.overlayClickBahasaC.visibility = View.VISIBLE
                }
                binding.selanjutnyaGame3.setOnClickListener {
                    if (jawabanA && jawabanB && jawabanC){
                        binding.selanjutnyaGame3.visibility = View.GONE
                        val dialogView = layoutInflater.inflate(R.layout.bottom_sheet_benar, null)
                        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme2)
                        dialog.setContentView(dialogView)
                        dialog.setCanceledOnTouchOutside(false)

                        val selanjutnyaBtn = dialog.findViewById<Button>(R.id.bottomSheetSelanjutnyaButton)

                        selanjutnyaBtn?.setOnClickListener {
                            val intent = Intent(Intent(requireContext(), CameraSecondActivity::class.java))
                            intent.putExtra("GAME", "gameAE")
                            startActivity(intent)
                            dialog.dismiss()
                        }
                        dialog.show()
                    } else {
                        val intent = Intent(Intent(requireContext(), CameraSecondActivity::class.java))
                        intent.putExtra("GAME", "gameAE")
                        startActivity(intent)
                    }
                }

                binding.closeGameButton.setOnClickListener {
                    val fragment = MenuTebakHurufFragment()
                    bundle.putInt("PROGRESS_AE", progress)
                    fragment.arguments = bundle
                    fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
                }
            }
            2 -> {
                // Game Pertama
                binding.titleMenuTv.text = "Huruf F - J"
                binding.titleGamePertama.text = "Apakah ini huruf I?"
                binding.imageHurufE.setImageResource(R.drawable.img_sibi_i)

                binding.benarButton.setOnClickListener {
                    val dialogView = layoutInflater.inflate(R.layout.bottom_sheet_benar, null)
                    val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme2)
                    dialog.setContentView(dialogView)
                    dialog.setCanceledOnTouchOutside(false)
                    binding.benarButton.setBackgroundResource(R.drawable.radio_pilihan_selected)

                    val selanjutnyaBtn = dialog.findViewById<Button>(R.id.bottomSheetSelanjutnyaButton)
                    val gamePertama = binding.gamePertama
                    val gameKedua = binding.gameKedua

                    selanjutnyaBtn?.setOnClickListener {
                        gamePertama.visibility = View.GONE
                        gameKedua.visibility = View.VISIBLE
                        progress++
                        saveScoreA()
                        jawabDBenar()
                        binding.gameProgressBar.setProgress(progress, true)
                        dialog.dismiss()
                    }
                    score++
                    dialog.show()
                }
                binding.salahButton.setOnClickListener {
                    binding.gamePertama.visibility = View.GONE
                    binding.gameKedua.visibility = View.VISIBLE
                    progress++
                    jawabDSalah()
                    binding.gameProgressBar.setProgress(progress, true)
                }

                // Game Kedua
                binding.titleGameKedua.text = "Pilihlah tangan yang sesuai untuk huruf “F”"
                binding.imagePilihanA.setImageResource(R.drawable.img_sibi_f)
                binding.imagePilihanB.setImageResource(R.drawable.img_sibi_i)
                binding.imagePilihanC.setImageResource(R.drawable.img_sibi_h)
                binding.imagePilihanD.setImageResource(R.drawable.img_sibi_j)

                binding.pilihanA.setOnClickListener {
                    binding.overlayClickA.visibility = View.VISIBLE
                    binding.overlayClickB.visibility = View.GONE
                    binding.overlayClickC.visibility = View.GONE
                    binding.overlayClickD.visibility = View.GONE
                    isTrue = true
                }

                binding.pilihanB.setOnClickListener {
                    binding.overlayClickA.visibility = View.GONE
                    binding.overlayClickB.visibility = View.VISIBLE
                    binding.overlayClickC.visibility = View.GONE
                    binding.overlayClickD.visibility = View.GONE
                    isTrue = false
                }

                binding.pilihanC.setOnClickListener {
                    binding.overlayClickA.visibility = View.GONE
                    binding.overlayClickB.visibility = View.GONE
                    binding.overlayClickC.visibility = View.VISIBLE
                    binding.overlayClickD.visibility = View.GONE
                    isTrue = false
                }

                binding.pilihanD.setOnClickListener {
                    binding.overlayClickA.visibility = View.GONE
                    binding.overlayClickB.visibility = View.GONE
                    binding.overlayClickC.visibility = View.GONE
                    binding.overlayClickD.visibility = View.VISIBLE
                    isTrue = false
                }

                binding.periksaJawabanGame2.setOnClickListener {
                    if (isTrue == true){
                        binding.overlayClickA.setBackgroundResource(R.color.see_listen_true)
                        binding.periksaJawabanGame2.visibility = View.GONE
                        val dialogView = layoutInflater.inflate(R.layout.bottom_sheet_benar, null)
                        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme2)
                        dialog.setContentView(dialogView)
                        dialog.setCanceledOnTouchOutside(false)

                        val selanjutnyaBtn = dialog.findViewById<Button>(R.id.bottomSheetSelanjutnyaButton)
                        val gameKedua = binding.gameKedua
                        val gameKetiga = binding.gameKetiga

                        selanjutnyaBtn?.setOnClickListener {
                            gameKedua.visibility = View.GONE
                            gameKetiga.visibility = View.VISIBLE
                            progress++
                            saveScoreB()
                            jawabABenar()
                            binding.gameProgressBar.setProgress(progress, true)
                            dialog.dismiss()
                        }
                        score++
                        dialog.show()
                    } else {
                        binding.overlayClickB.setBackgroundResource(R.color.see_listen_false)
                        binding.overlayClickC.setBackgroundResource(R.color.see_listen_false)
                        binding.overlayClickD.setBackgroundResource(R.color.see_listen_false)
                        binding.periksaJawabanGame2.visibility = View.GONE
                        val dialogView = layoutInflater.inflate(R.layout.bottom_sheet_salah, null)
                        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme2)
                        dialog.setContentView(dialogView)
                        dialog.setCanceledOnTouchOutside(false)

                        val selanjutnyaBtn = dialog.findViewById<Button>(R.id.bottomSheetSelanjutnyaButton)
                        val imageTrue = dialog.findViewById<ImageView>(R.id.imageTrue)
                        val gameKedua = binding.gameKedua
                        val gameKetiga = binding.gameKetiga

                        imageTrue?.setImageResource(R.drawable.img_sibi_f)
                        selanjutnyaBtn?.setOnClickListener {
                            gameKedua.visibility = View.GONE
                            gameKetiga.visibility = View.VISIBLE
                            progress++
                            jawabASalah()
                            binding.gameProgressBar.setProgress(progress, true)
                            dialog.dismiss()
                        }
                        dialog.show()
                    }
                }

                // Game Ketiga
                binding.pilihanATv.text = "G"
                binding.pilihanBTv.text = "H"
                binding.pilihanCTv.text = "J"
                binding.pilihanAImg.setImageResource(R.drawable.img_sibi_g)
                binding.pilihanBImg.setImageResource(R.drawable.img_sibi_j)
                binding.pilihanCImg.setImageResource(R.drawable.img_sibi_h)
                val bahasaA = binding.pilihanbahasaA
                val bahasaB = binding.pilihanBahasaB
                val bahasaC = binding.pilihanBahasaC
                val isyaratA = binding.pilihanIsyaratA
                val isyaratB = binding.pilihanIsyaratB
                val isyaratC = binding.pilihanIsyaratC
                bahasaA.setOnClickListener {
                    if (isyaratA.isEnabled){
                        isyaratB.isEnabled = true
                        isyaratC.isEnabled = true
                    } else if (isyaratB.isEnabled){
                        isyaratA.isEnabled = true
                        isyaratC.isEnabled = true
                    } else if (isyaratC.isEnabled){
                        isyaratA.isEnabled = true
                        isyaratB.isEnabled = true
                    }
                    binding.overlayClickBahasaA.visibility = View.VISIBLE
                    binding.overlayClickBahasaB.visibility = View.GONE
                    binding.overlayClickBahasaC.visibility = View.GONE
                    isyaratA.setOnClickListener {
                        binding.overlayClickIsyaratA.visibility = View.VISIBLE
                        binding.overlayClickIsyaratA.setBackgroundResource(R.color.see_listen_true)
                        binding.overlayClickBahasaA.visibility = View.GONE
                        binding.containerA.setBackgroundResource(R.color.see_listen_true)
                        isyaratB.isEnabled = false
                        isyaratC.isEnabled = false
                        bahasaA.isEnabled = false
                        jawabanA = true
                        score++
                        saveScoreC()
                        jawabBBenar()
                    }
                    isyaratB.setOnClickListener {
                        binding.overlayClickIsyaratB.visibility = View.VISIBLE
                        binding.overlayClickIsyaratB.setBackgroundResource(R.color.see_listen_false)
                        binding.overlayClickBahasaA.visibility = View.GONE
                        binding.containerA.setBackgroundResource(R.color.see_listen_false)
                        isyaratA.isEnabled = false
                        isyaratC.isEnabled = false
                        bahasaA.isEnabled = false
                        jawabBSalah()
                    }
                    isyaratC.setOnClickListener {
                        binding.overlayClickIsyaratC.visibility = View.VISIBLE
                        binding.overlayClickIsyaratC.setBackgroundResource(R.color.see_listen_false)
                        binding.overlayClickBahasaA.visibility = View.GONE
                        binding.containerA.setBackgroundResource(R.color.see_listen_false)
                        isyaratA.isEnabled = false
                        isyaratB.isEnabled = false
                        bahasaA.isEnabled = false
                        jawabBSalah()
                    }
                }
                bahasaB.setOnClickListener {
                    if (isyaratA.isEnabled){
                        isyaratB.isEnabled = true
                        isyaratC.isEnabled = true
                    } else if (isyaratB.isEnabled){
                        isyaratA.isEnabled = true
                        isyaratC.isEnabled = true
                    } else if (isyaratC.isEnabled){
                        isyaratA.isEnabled = true
                        isyaratB.isEnabled = true
                    }
                    isyaratA.setOnClickListener {
                        binding.overlayClickIsyaratA.visibility = View.VISIBLE
                        binding.overlayClickIsyaratA.setBackgroundResource(R.color.see_listen_false)
                        binding.overlayClickBahasaB.visibility = View.GONE
                        binding.containerB.setBackgroundResource(R.color.see_listen_false)
                        isyaratB.isEnabled = false
                        isyaratC.isEnabled = false
                        bahasaB.isEnabled = false
                        jawabCSalah()
                    }
                    isyaratB.setOnClickListener {
                        binding.overlayClickIsyaratB.visibility = View.VISIBLE
                        binding.overlayClickIsyaratB.setBackgroundResource(R.color.see_listen_false)
                        binding.overlayClickBahasaB.visibility = View.GONE
                        binding.containerB.setBackgroundResource(R.color.see_listen_false)
                        isyaratA.isEnabled = false
                        isyaratC.isEnabled = false
                        bahasaB.isEnabled = false
                        jawabCSalah()
                    }
                    isyaratC.setOnClickListener {
                        binding.overlayClickIsyaratC.visibility = View.VISIBLE
                        binding.overlayClickIsyaratC.setBackgroundResource(R.color.see_listen_true)
                        binding.overlayClickBahasaB.visibility = View.GONE
                        binding.containerB.setBackgroundResource(R.color.see_listen_true)
                        isyaratA.isEnabled = false
                        isyaratB.isEnabled = false
                        bahasaB.isEnabled = false
                        jawabanB = true
                        score++
                        saveScoreD()
                        jawabCBenar()
                    }
                    binding.overlayClickBahasaA.visibility = View.GONE
                    binding.overlayClickBahasaB.visibility = View.VISIBLE
                    binding.overlayClickBahasaC.visibility = View.GONE
                }
                bahasaC.setOnClickListener {
                    if (isyaratA.isEnabled){
                        isyaratB.isEnabled = true
                        isyaratC.isEnabled = true
                    } else if (isyaratB.isEnabled){
                        isyaratA.isEnabled = true
                        isyaratC.isEnabled = true
                    } else if (isyaratC.isEnabled){
                        isyaratA.isEnabled = true
                        isyaratB.isEnabled = true
                    }
                    isyaratA.setOnClickListener {
                        binding.overlayClickIsyaratA.visibility = View.VISIBLE
                        binding.overlayClickIsyaratA.setBackgroundResource(R.color.see_listen_false)
                        binding.overlayClickBahasaC.visibility = View.GONE
                        binding.containerC.setBackgroundResource(R.color.see_listen_false)
                        isyaratB.isEnabled = false
                        isyaratC.isEnabled = false
                        bahasaC.isEnabled = false
                        jawabESalah()
                    }
                    isyaratB.setOnClickListener {
                        binding.overlayClickIsyaratB.visibility = View.VISIBLE
                        binding.overlayClickIsyaratB.setBackgroundResource(R.color.see_listen_true)
                        binding.overlayClickBahasaC.visibility = View.GONE
                        binding.containerC.setBackgroundResource(R.color.see_listen_true)
                        isyaratA.isEnabled = false
                        isyaratC.isEnabled = false
                        bahasaC.isEnabled = false
                        jawabanC = true
                        score++
                        saveScoreE()
                        jawabEBenar()
                    }
                    isyaratC.setOnClickListener {
                        binding.overlayClickIsyaratC.visibility = View.VISIBLE
                        binding.overlayClickIsyaratC.setBackgroundResource(R.color.see_listen_false)
                        binding.overlayClickBahasaC.visibility = View.GONE
                        binding.containerC.setBackgroundResource(R.color.see_listen_false)
                        isyaratA.isEnabled = false
                        isyaratB.isEnabled = false
                        bahasaC.isEnabled = false
                        jawabESalah()
                    }
                    binding.overlayClickBahasaA.visibility = View.GONE
                    binding.overlayClickBahasaB.visibility = View.GONE
                    binding.overlayClickBahasaC.visibility = View.VISIBLE
                }
                binding.selanjutnyaGame3.setOnClickListener {
                    if (jawabanA && jawabanB && jawabanC){
                        binding.selanjutnyaGame3.visibility = View.GONE
                        val dialogView = layoutInflater.inflate(R.layout.bottom_sheet_benar, null)
                        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme2)
                        dialog.setContentView(dialogView)
                        dialog.setCanceledOnTouchOutside(false)

                        val selanjutnyaBtn = dialog.findViewById<Button>(R.id.bottomSheetSelanjutnyaButton)

                        selanjutnyaBtn?.setOnClickListener {
                            val intent = Intent(Intent(requireContext(), CameraSecondActivity::class.java))
                            intent.putExtra("GAME", "gameFJ")
                            startActivity(intent)
                            dialog.dismiss()
                        }
                        dialog.show()
                    } else {
                        val intent = Intent(Intent(requireContext(), CameraSecondActivity::class.java))
                        intent.putExtra("GAME", "gameFJ")
                        startActivity(intent)
                    }
                }

                binding.closeGameButton.setOnClickListener {
                    val fragment = MenuTebakHurufFragment()
                    bundle.putInt("PROGRESS_AE", 5)
                    bundle.putInt("PROGRESS_FJ", progress)
                    fragment.arguments = bundle
                    fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
                }
            }
            3 -> {
                binding.titleMenuTv.text = "Huruf K - O"
                binding.titleGamePertama.text = "Apakah ini huruf O?"
                binding.imageHurufE.setImageResource(R.drawable.img_sibi_o)

                binding.benarButton.setOnClickListener {
                    val dialogView = layoutInflater.inflate(R.layout.bottom_sheet_benar, null)
                    val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme2)
                    dialog.setContentView(dialogView)
                    dialog.setCanceledOnTouchOutside(false)
                    binding.benarButton.setBackgroundResource(R.drawable.radio_pilihan_selected)

                    val selanjutnyaBtn = dialog.findViewById<Button>(R.id.bottomSheetSelanjutnyaButton)
                    val gamePertama = binding.gamePertama
                    val gameKedua = binding.gameKedua

                    selanjutnyaBtn?.setOnClickListener {
                        gamePertama.visibility = View.GONE
                        gameKedua.visibility = View.VISIBLE
                        progress++
                        saveScoreA()
                        jawabEBenar()
                        binding.gameProgressBar.setProgress(progress, true)
                        dialog.dismiss()
                    }
                    score++
                    dialog.show()
                }
                binding.salahButton.setOnClickListener {
                    binding.gamePertama.visibility = View.GONE
                    binding.gameKedua.visibility = View.VISIBLE
                    progress++
                    jawabESalah()
                    binding.gameProgressBar.setProgress(progress, true)
                }

                // Game Kedua
                binding.titleGameKedua.text = "Pilihlah tangan yang sesuai untuk huruf “M”"
                binding.imagePilihanA.setImageResource(R.drawable.img_sibi_k)
                binding.imagePilihanB.setImageResource(R.drawable.img_sibi_m)
                binding.imagePilihanC.setImageResource(R.drawable.img_sibi_o)
                binding.imagePilihanD.setImageResource(R.drawable.img_sibi_n)

                binding.pilihanA.setOnClickListener {
                    binding.overlayClickA.visibility = View.VISIBLE
                    binding.overlayClickB.visibility = View.GONE
                    binding.overlayClickC.visibility = View.GONE
                    binding.overlayClickD.visibility = View.GONE
                    isTrue = false
                }

                binding.pilihanB.setOnClickListener {
                    binding.overlayClickA.visibility = View.GONE
                    binding.overlayClickB.visibility = View.VISIBLE
                    binding.overlayClickC.visibility = View.GONE
                    binding.overlayClickD.visibility = View.GONE
                    isTrue = true
                }

                binding.pilihanC.setOnClickListener {
                    binding.overlayClickA.visibility = View.GONE
                    binding.overlayClickB.visibility = View.GONE
                    binding.overlayClickC.visibility = View.VISIBLE
                    binding.overlayClickD.visibility = View.GONE
                    isTrue = false
                }

                binding.pilihanD.setOnClickListener {
                    binding.overlayClickA.visibility = View.GONE
                    binding.overlayClickB.visibility = View.GONE
                    binding.overlayClickC.visibility = View.GONE
                    binding.overlayClickD.visibility = View.VISIBLE
                    isTrue = false
                }

                binding.periksaJawabanGame2.setOnClickListener {
                    if (isTrue == true){
                        binding.overlayClickA.setBackgroundResource(R.color.see_listen_true)
                        binding.periksaJawabanGame2.visibility = View.GONE
                        val dialogView = layoutInflater.inflate(R.layout.bottom_sheet_benar, null)
                        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme2)
                        dialog.setContentView(dialogView)
                        dialog.setCanceledOnTouchOutside(false)

                        val selanjutnyaBtn = dialog.findViewById<Button>(R.id.bottomSheetSelanjutnyaButton)
                        val gameKedua = binding.gameKedua
                        val gameKetiga = binding.gameKetiga

                        selanjutnyaBtn?.setOnClickListener {
                            gameKedua.visibility = View.GONE
                            gameKetiga.visibility = View.VISIBLE
                            progress++
                            saveScoreB()
                            jawabCBenar()
                            binding.gameProgressBar.setProgress(progress, true)
                            dialog.dismiss()
                        }
                        score++
                        dialog.show()
                    } else {
                        binding.overlayClickB.setBackgroundResource(R.color.see_listen_false)
                        binding.overlayClickC.setBackgroundResource(R.color.see_listen_false)
                        binding.overlayClickD.setBackgroundResource(R.color.see_listen_false)
                        binding.periksaJawabanGame2.visibility = View.GONE
                        val dialogView = layoutInflater.inflate(R.layout.bottom_sheet_salah, null)
                        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme2)
                        dialog.setContentView(dialogView)
                        dialog.setCanceledOnTouchOutside(false)

                        val selanjutnyaBtn = dialog.findViewById<Button>(R.id.bottomSheetSelanjutnyaButton)
                        val imageTrue = dialog.findViewById<ImageView>(R.id.imageTrue)
                        val gameKedua = binding.gameKedua
                        val gameKetiga = binding.gameKetiga

                        imageTrue?.setImageResource(R.drawable.img_sibi_f)
                        selanjutnyaBtn?.setOnClickListener {
                            gameKedua.visibility = View.GONE
                            gameKetiga.visibility = View.VISIBLE
                            progress++
                            jawabCSalah()
                            binding.gameProgressBar.setProgress(progress, true)
                            dialog.dismiss()
                        }
                        dialog.show()
                    }
                }

                // Game Ketiga
                binding.pilihanATv.text = "K"
                binding.pilihanBTv.text = "L"
                binding.pilihanCTv.text = "N"
                binding.pilihanAImg.setImageResource(R.drawable.img_sibi_l)
                binding.pilihanBImg.setImageResource(R.drawable.img_sibi_k)
                binding.pilihanCImg.setImageResource(R.drawable.img_sibi_n)
                val bahasaA = binding.pilihanbahasaA
                val bahasaB = binding.pilihanBahasaB
                val bahasaC = binding.pilihanBahasaC
                val isyaratA = binding.pilihanIsyaratA
                val isyaratB = binding.pilihanIsyaratB
                val isyaratC = binding.pilihanIsyaratC
                bahasaA.setOnClickListener {
                    if (isyaratA.isEnabled){
                        isyaratB.isEnabled = true
                        isyaratC.isEnabled = true
                    } else if (isyaratB.isEnabled){
                        isyaratA.isEnabled = true
                        isyaratC.isEnabled = true
                    } else if (isyaratC.isEnabled){
                        isyaratA.isEnabled = true
                        isyaratB.isEnabled = true
                    }
                    binding.overlayClickBahasaA.visibility = View.VISIBLE
                    binding.overlayClickBahasaB.visibility = View.GONE
                    binding.overlayClickBahasaC.visibility = View.GONE
                    isyaratA.setOnClickListener {
                        binding.overlayClickIsyaratA.visibility = View.VISIBLE
                        binding.overlayClickIsyaratA.setBackgroundResource(R.color.see_listen_false)
                        binding.overlayClickBahasaA.visibility = View.GONE
                        binding.containerA.setBackgroundResource(R.color.see_listen_false)
                        isyaratB.isEnabled = false
                        isyaratC.isEnabled = false
                        bahasaA.isEnabled = false
                        jawabASalah()
                    }
                    isyaratB.setOnClickListener {
                        binding.overlayClickIsyaratB.visibility = View.VISIBLE
                        binding.overlayClickIsyaratB.setBackgroundResource(R.color.see_listen_true)
                        binding.overlayClickBahasaA.visibility = View.GONE
                        binding.containerA.setBackgroundResource(R.color.see_listen_true)
                        isyaratA.isEnabled = false
                        isyaratC.isEnabled = false
                        bahasaA.isEnabled = false
                        jawabanA = true
                        score++
                        saveScoreC()
                        jawabABenar()
                    }
                    isyaratC.setOnClickListener {
                        binding.overlayClickIsyaratC.visibility = View.VISIBLE
                        binding.overlayClickIsyaratC.setBackgroundResource(R.color.see_listen_false)
                        binding.overlayClickBahasaA.visibility = View.GONE
                        binding.containerA.setBackgroundResource(R.color.see_listen_false)
                        isyaratA.isEnabled = false
                        isyaratB.isEnabled = false
                        bahasaA.isEnabled = false
                        jawabASalah()
                    }
                }
                bahasaB.setOnClickListener {
                    if (isyaratA.isEnabled){
                        isyaratB.isEnabled = true
                        isyaratC.isEnabled = true
                    } else if (isyaratB.isEnabled){
                        isyaratA.isEnabled = true
                        isyaratC.isEnabled = true
                    } else if (isyaratC.isEnabled){
                        isyaratA.isEnabled = true
                        isyaratB.isEnabled = true
                    }
                    isyaratA.setOnClickListener {
                        binding.overlayClickIsyaratA.visibility = View.VISIBLE
                        binding.overlayClickIsyaratA.setBackgroundResource(R.color.see_listen_true)
                        binding.overlayClickBahasaB.visibility = View.GONE
                        binding.containerB.setBackgroundResource(R.color.see_listen_true)
                        isyaratB.isEnabled = false
                        isyaratC.isEnabled = false
                        bahasaB.isEnabled = false
                        jawabanB = true
                        score++
                        saveScoreD()
                        jawabBBenar()
                    }
                    isyaratB.setOnClickListener {
                        binding.overlayClickIsyaratB.visibility = View.VISIBLE
                        binding.overlayClickIsyaratB.setBackgroundResource(R.color.see_listen_false)
                        binding.overlayClickBahasaB.visibility = View.GONE
                        binding.containerB.setBackgroundResource(R.color.see_listen_false)
                        isyaratA.isEnabled = false
                        isyaratC.isEnabled = false
                        bahasaB.isEnabled = false
                        jawabBSalah()
                    }
                    isyaratC.setOnClickListener {
                        binding.overlayClickIsyaratC.visibility = View.VISIBLE
                        binding.overlayClickIsyaratC.setBackgroundResource(R.color.see_listen_false)
                        binding.overlayClickBahasaB.visibility = View.GONE
                        binding.containerB.setBackgroundResource(R.color.see_listen_false)
                        isyaratA.isEnabled = false
                        isyaratB.isEnabled = false
                        bahasaB.isEnabled = false
                        jawabBSalah()
                    }
                    binding.overlayClickBahasaA.visibility = View.GONE
                    binding.overlayClickBahasaB.visibility = View.VISIBLE
                    binding.overlayClickBahasaC.visibility = View.GONE
                }
                bahasaC.setOnClickListener {
                    if (isyaratA.isEnabled){
                        isyaratB.isEnabled = true
                        isyaratC.isEnabled = true
                    } else if (isyaratB.isEnabled){
                        isyaratA.isEnabled = true
                        isyaratC.isEnabled = true
                    } else if (isyaratC.isEnabled){
                        isyaratA.isEnabled = true
                        isyaratB.isEnabled = true
                    }
                    isyaratA.setOnClickListener {
                        binding.overlayClickIsyaratA.visibility = View.VISIBLE
                        binding.overlayClickIsyaratA.setBackgroundResource(R.color.see_listen_false)
                        binding.overlayClickBahasaC.visibility = View.GONE
                        binding.containerC.setBackgroundResource(R.color.see_listen_false)
                        isyaratB.isEnabled = false
                        isyaratC.isEnabled = false
                        bahasaC.isEnabled = false
                        jawabDSalah()
                    }
                    isyaratB.setOnClickListener {
                        binding.overlayClickIsyaratB.visibility = View.VISIBLE
                        binding.overlayClickIsyaratB.setBackgroundResource(R.color.see_listen_false)
                        binding.overlayClickBahasaC.visibility = View.GONE
                        binding.containerC.setBackgroundResource(R.color.see_listen_false)
                        isyaratA.isEnabled = false
                        isyaratC.isEnabled = false
                        bahasaC.isEnabled = false
                        jawabDSalah()
                    }
                    isyaratC.setOnClickListener {
                        binding.overlayClickIsyaratC.visibility = View.VISIBLE
                        binding.overlayClickIsyaratC.setBackgroundResource(R.color.see_listen_true)
                        binding.overlayClickBahasaC.visibility = View.GONE
                        binding.containerC.setBackgroundResource(R.color.see_listen_true)
                        isyaratA.isEnabled = false
                        isyaratB.isEnabled = false
                        bahasaC.isEnabled = false
                        jawabanC = true
                        score++
                        saveScoreE()
                        jawabDBenar()
                    }
                    binding.overlayClickBahasaA.visibility = View.GONE
                    binding.overlayClickBahasaB.visibility = View.GONE
                    binding.overlayClickBahasaC.visibility = View.VISIBLE
                }
                binding.selanjutnyaGame3.setOnClickListener {
                    if (jawabanA && jawabanB && jawabanC){
                        binding.selanjutnyaGame3.visibility = View.GONE
                        val dialogView = layoutInflater.inflate(R.layout.bottom_sheet_benar, null)
                        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme2)
                        dialog.setContentView(dialogView)
                        dialog.setCanceledOnTouchOutside(false)

                        val selanjutnyaBtn = dialog.findViewById<Button>(R.id.bottomSheetSelanjutnyaButton)

                        selanjutnyaBtn?.setOnClickListener {
                            val intent = Intent(Intent(requireContext(), CameraSecondActivity::class.java))
                            intent.putExtra("GAME", "gameKO")
                            startActivity(intent)
                            dialog.dismiss()
                        }
                        dialog.show()
                    } else {
                        val intent = Intent(Intent(requireContext(), CameraSecondActivity::class.java))
                        intent.putExtra("GAME", "gameKO")
                        startActivity(intent)
                    }
                }

                binding.closeGameButton.setOnClickListener {
                    val fragment = MenuTebakHurufFragment()
                    bundle.putInt("PROGRESS_AE", 5)
                    bundle.putInt("PROGRESS_FJ", 5)
                    bundle.putInt("PROGRESS_KO", progress)
                    fragment.arguments = bundle
                    fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
                }
            }
            4 -> {
                binding.titleMenuTv.text = "Huruf P - T"
                binding.titleGamePertama.text = "Apakah ini huruf T?"
                binding.imageHurufE.setImageResource(R.drawable.img_sibi_t)

                binding.benarButton.setOnClickListener {
                    val dialogView = layoutInflater.inflate(R.layout.bottom_sheet_benar, null)
                    val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme2)
                    dialog.setContentView(dialogView)
                    dialog.setCanceledOnTouchOutside(false)
                    binding.benarButton.setBackgroundResource(R.drawable.radio_pilihan_selected)

                    val selanjutnyaBtn = dialog.findViewById<Button>(R.id.bottomSheetSelanjutnyaButton)
                    val gamePertama = binding.gamePertama
                    val gameKedua = binding.gameKedua

                    selanjutnyaBtn?.setOnClickListener {
                        gamePertama.visibility = View.GONE
                        gameKedua.visibility = View.VISIBLE
                        progress++
                        saveScoreA()
                        jawabEBenar()
                        binding.gameProgressBar.setProgress(progress, true)
                        dialog.dismiss()
                    }
                    score++
                    dialog.show()
                }
                binding.salahButton.setOnClickListener {
                    binding.gamePertama.visibility = View.GONE
                    binding.gameKedua.visibility = View.VISIBLE
                    progress++
                    jawabESalah()
                    binding.gameProgressBar.setProgress(progress, true)
                }

                // Game Kedua
                binding.titleGameKedua.text = "Pilihlah tangan yang sesuai untuk huruf “S”"
                binding.imagePilihanA.setImageResource(R.drawable.img_sibi_r)
                binding.imagePilihanB.setImageResource(R.drawable.img_sibi_t)
                binding.imagePilihanC.setImageResource(R.drawable.img_sibi_p)
                binding.imagePilihanD.setImageResource(R.drawable.img_sibi_s)

                binding.pilihanA.setOnClickListener {
                    binding.overlayClickA.visibility = View.VISIBLE
                    binding.overlayClickB.visibility = View.GONE
                    binding.overlayClickC.visibility = View.GONE
                    binding.overlayClickD.visibility = View.GONE
                    isTrue = false
                }

                binding.pilihanB.setOnClickListener {
                    binding.overlayClickA.visibility = View.GONE
                    binding.overlayClickB.visibility = View.VISIBLE
                    binding.overlayClickC.visibility = View.GONE
                    binding.overlayClickD.visibility = View.GONE
                    isTrue = false
                }

                binding.pilihanC.setOnClickListener {
                    binding.overlayClickA.visibility = View.GONE
                    binding.overlayClickB.visibility = View.GONE
                    binding.overlayClickC.visibility = View.VISIBLE
                    binding.overlayClickD.visibility = View.GONE
                    isTrue = false
                }

                binding.pilihanD.setOnClickListener {
                    binding.overlayClickA.visibility = View.GONE
                    binding.overlayClickB.visibility = View.GONE
                    binding.overlayClickC.visibility = View.GONE
                    binding.overlayClickD.visibility = View.VISIBLE
                    isTrue = true
                }

                binding.periksaJawabanGame2.setOnClickListener {
                    if (isTrue == true) {
                        binding.overlayClickA.setBackgroundResource(R.color.see_listen_true)
                        binding.periksaJawabanGame2.visibility = View.GONE
                        val dialogView = layoutInflater.inflate(R.layout.bottom_sheet_benar, null)
                        val dialog =
                            BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme2)
                        dialog.setContentView(dialogView)
                        dialog.setCanceledOnTouchOutside(false)

                        val selanjutnyaBtn =
                            dialog.findViewById<Button>(R.id.bottomSheetSelanjutnyaButton)
                        val gameKedua = binding.gameKedua
                        val gameKetiga = binding.gameKetiga

                        selanjutnyaBtn?.setOnClickListener {
                            gameKedua.visibility = View.GONE
                            gameKetiga.visibility = View.VISIBLE
                            progress++
                            saveScoreB()
                            jawabDBenar()
                            binding.gameProgressBar.setProgress(progress, true)
                            dialog.dismiss()
                        }
                        score++
                        dialog.show()
                    } else {
                        binding.overlayClickB.setBackgroundResource(R.color.see_listen_false)
                        binding.overlayClickC.setBackgroundResource(R.color.see_listen_false)
                        binding.overlayClickD.setBackgroundResource(R.color.see_listen_false)
                        binding.periksaJawabanGame2.visibility = View.GONE
                        val dialogView = layoutInflater.inflate(R.layout.bottom_sheet_salah, null)
                        val dialog =
                            BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme2)
                        dialog.setContentView(dialogView)
                        dialog.setCanceledOnTouchOutside(false)

                        val selanjutnyaBtn =
                            dialog.findViewById<Button>(R.id.bottomSheetSelanjutnyaButton)
                        val imageTrue = dialog.findViewById<ImageView>(R.id.imageTrue)
                        val gameKedua = binding.gameKedua
                        val gameKetiga = binding.gameKetiga

                        imageTrue?.setImageResource(R.drawable.img_sibi_f)
                        selanjutnyaBtn?.setOnClickListener {
                            gameKedua.visibility = View.GONE
                            gameKetiga.visibility = View.VISIBLE
                            progress++
                            jawabDSalah()
                            binding.gameProgressBar.setProgress(progress, true)
                            dialog.dismiss()
                        }
                        dialog.show()
                    }
                }

                // Game Ketiga
                binding.pilihanATv.text = "P"
                binding.pilihanBTv.text = "Q"
                binding.pilihanCTv.text = "R"
                binding.pilihanAImg.setImageResource(R.drawable.img_sibi_q)
                binding.pilihanBImg.setImageResource(R.drawable.img_sibi_r)
                binding.pilihanCImg.setImageResource(R.drawable.img_sibi_p)
                val bahasaA = binding.pilihanbahasaA
                val bahasaB = binding.pilihanBahasaB
                val bahasaC = binding.pilihanBahasaC
                val isyaratA = binding.pilihanIsyaratA
                val isyaratB = binding.pilihanIsyaratB
                val isyaratC = binding.pilihanIsyaratC
                bahasaA.setOnClickListener {
                    if (isyaratA.isEnabled){
                        isyaratB.isEnabled = true
                        isyaratC.isEnabled = true
                    } else if (isyaratB.isEnabled){
                        isyaratA.isEnabled = true
                        isyaratC.isEnabled = true
                    } else if (isyaratC.isEnabled){
                        isyaratA.isEnabled = true
                        isyaratB.isEnabled = true
                    }
                    binding.overlayClickBahasaA.visibility = View.VISIBLE
                    binding.overlayClickBahasaB.visibility = View.GONE
                    binding.overlayClickBahasaC.visibility = View.GONE
                    isyaratA.setOnClickListener {
                        binding.overlayClickIsyaratA.visibility = View.VISIBLE
                        binding.overlayClickIsyaratA.setBackgroundResource(R.color.see_listen_false)
                        binding.overlayClickBahasaA.visibility = View.GONE
                        binding.containerA.setBackgroundResource(R.color.see_listen_false)
                        isyaratB.isEnabled = false
                        isyaratC.isEnabled = false
                        bahasaA.isEnabled = false
                        jawabASalah()
                    }
                    isyaratB.setOnClickListener {
                        binding.overlayClickIsyaratB.visibility = View.VISIBLE
                        binding.overlayClickIsyaratB.setBackgroundResource(R.color.see_listen_false)
                        binding.overlayClickBahasaA.visibility = View.GONE
                        binding.containerA.setBackgroundResource(R.color.see_listen_false)
                        isyaratA.isEnabled = false
                        isyaratC.isEnabled = false
                        bahasaA.isEnabled = false
                        jawabASalah()
                    }
                    isyaratC.setOnClickListener {
                        binding.overlayClickIsyaratC.visibility = View.VISIBLE
                        binding.overlayClickIsyaratC.setBackgroundResource(R.color.see_listen_true)
                        binding.overlayClickBahasaA.visibility = View.GONE
                        binding.containerA.setBackgroundResource(R.color.see_listen_true)
                        isyaratA.isEnabled = false
                        isyaratB.isEnabled = false
                        bahasaA.isEnabled = false
                        jawabanA = true
                        score++
                        saveScoreC()
                        jawabABenar()
                    }
                }
                bahasaB.setOnClickListener {
                    if (isyaratA.isEnabled){
                        isyaratB.isEnabled = true
                        isyaratC.isEnabled = true
                    } else if (isyaratB.isEnabled){
                        isyaratA.isEnabled = true
                        isyaratC.isEnabled = true
                    } else if (isyaratC.isEnabled){
                        isyaratA.isEnabled = true
                        isyaratB.isEnabled = true
                    }
                    isyaratA.setOnClickListener {
                        binding.overlayClickIsyaratA.visibility = View.VISIBLE
                        binding.overlayClickIsyaratA.setBackgroundResource(R.color.see_listen_true)
                        binding.overlayClickBahasaB.visibility = View.GONE
                        binding.containerB.setBackgroundResource(R.color.see_listen_true)
                        isyaratB.isEnabled = false
                        isyaratC.isEnabled = false
                        bahasaB.isEnabled = false
                        jawabanB = true
                        score++
                        saveScoreD()
                        jawabBBenar()
                    }
                    isyaratB.setOnClickListener {
                        binding.overlayClickIsyaratB.visibility = View.VISIBLE
                        binding.overlayClickIsyaratB.setBackgroundResource(R.color.see_listen_false)
                        binding.overlayClickBahasaB.visibility = View.GONE
                        binding.containerB.setBackgroundResource(R.color.see_listen_false)
                        isyaratA.isEnabled = false
                        isyaratC.isEnabled = false
                        bahasaB.isEnabled = false
                        jawabBSalah()
                    }
                    isyaratC.setOnClickListener {
                        binding.overlayClickIsyaratC.visibility = View.VISIBLE
                        binding.overlayClickIsyaratC.setBackgroundResource(R.color.see_listen_false)
                        binding.overlayClickBahasaB.visibility = View.GONE
                        binding.containerB.setBackgroundResource(R.color.see_listen_false)
                        isyaratA.isEnabled = false
                        isyaratB.isEnabled = false
                        bahasaB.isEnabled = false
                        jawabBSalah()
                    }
                    binding.overlayClickBahasaA.visibility = View.GONE
                    binding.overlayClickBahasaB.visibility = View.VISIBLE
                    binding.overlayClickBahasaC.visibility = View.GONE
                }
                bahasaC.setOnClickListener {
                    if (isyaratA.isEnabled){
                        isyaratB.isEnabled = true
                        isyaratC.isEnabled = true
                    } else if (isyaratB.isEnabled){
                        isyaratA.isEnabled = true
                        isyaratC.isEnabled = true
                    } else if (isyaratC.isEnabled){
                        isyaratA.isEnabled = true
                        isyaratB.isEnabled = true
                    }
                    isyaratA.setOnClickListener {
                        binding.overlayClickIsyaratA.visibility = View.VISIBLE
                        binding.overlayClickIsyaratA.setBackgroundResource(R.color.see_listen_false)
                        binding.overlayClickBahasaC.visibility = View.GONE
                        binding.containerC.setBackgroundResource(R.color.see_listen_false)
                        isyaratB.isEnabled = false
                        isyaratC.isEnabled = false
                        bahasaC.isEnabled = false
                        jawabCSalah()
                    }
                    isyaratB.setOnClickListener {
                        binding.overlayClickIsyaratB.visibility = View.VISIBLE
                        binding.overlayClickIsyaratB.setBackgroundResource(R.color.see_listen_true)
                        binding.overlayClickBahasaC.visibility = View.GONE
                        binding.containerC.setBackgroundResource(R.color.see_listen_true)
                        isyaratA.isEnabled = false
                        isyaratC.isEnabled = false
                        bahasaC.isEnabled = false
                        jawabanC = true
                        score++
                        saveScoreE()
                        jawabCBenar()
                    }
                    isyaratC.setOnClickListener {
                        binding.overlayClickIsyaratC.visibility = View.VISIBLE
                        binding.overlayClickIsyaratC.setBackgroundResource(R.color.see_listen_false)
                        binding.overlayClickBahasaC.visibility = View.GONE
                        binding.containerC.setBackgroundResource(R.color.see_listen_false)
                        isyaratA.isEnabled = false
                        isyaratB.isEnabled = false
                        bahasaC.isEnabled = false
                        jawabCSalah()
                    }
                    binding.overlayClickBahasaA.visibility = View.GONE
                    binding.overlayClickBahasaB.visibility = View.GONE
                    binding.overlayClickBahasaC.visibility = View.VISIBLE
                }
                binding.selanjutnyaGame3.setOnClickListener {
                    if (jawabanA && jawabanB && jawabanC) {
                        binding.selanjutnyaGame3.visibility = View.GONE
                        val dialogView = layoutInflater.inflate(R.layout.bottom_sheet_benar, null)
                        val dialog =
                            BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme2)
                        dialog.setContentView(dialogView)
                        dialog.setCanceledOnTouchOutside(false)

                        val selanjutnyaBtn =
                            dialog.findViewById<Button>(R.id.bottomSheetSelanjutnyaButton)

                        selanjutnyaBtn?.setOnClickListener {
                            val intent =
                                Intent(Intent(requireContext(), CameraSecondActivity::class.java))
                            intent.putExtra("GAME", "gamePT")
                            startActivity(intent)
                            dialog.dismiss()
                        }
                        dialog.show()
                    } else {
                        val intent = Intent(Intent(requireContext(), CameraSecondActivity::class.java))
                        intent.putExtra("GAME", "gamePT")
                        startActivity(intent)
                    }
                }

                binding.closeGameButton.setOnClickListener {
                    val fragment = MenuTebakHurufFragment()
                    bundle.putInt("PROGRESS_AE", 5)
                    bundle.putInt("PROGRESS_FJ", 5)
                    bundle.putInt("PROGRESS_KO", 5)
                    bundle.putInt("PROGRESS_PT", progress)
                    fragment.arguments = bundle
                    fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)
                        ?.commit()
                }
            }
            5 -> {
                binding.titleMenuTv.text = "Huruf U - Z"
                binding.titleGamePertama.text = "Apakah ini huruf Y?"
                binding.imageHurufE.setImageResource(R.drawable.img_sibi_y)

                binding.benarButton.setOnClickListener {
                    val dialogView = layoutInflater.inflate(R.layout.bottom_sheet_benar, null)
                    val dialog =
                        BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme2)
                    dialog.setContentView(dialogView)
                    dialog.setCanceledOnTouchOutside(false)
                    binding.benarButton.setBackgroundResource(R.drawable.radio_pilihan_selected)

                    val selanjutnyaBtn =
                        dialog.findViewById<Button>(R.id.bottomSheetSelanjutnyaButton)
                    val gamePertama = binding.gamePertama
                    val gameKedua = binding.gameKedua

                    selanjutnyaBtn?.setOnClickListener {
                        gamePertama.visibility = View.GONE
                        gameKedua.visibility = View.VISIBLE
                        progress++
                        saveScoreA()
                        jawabEBenar()
                        binding.gameProgressBar.setProgress(progress, true)
                        dialog.dismiss()
                    }
                    score++
                    dialog.show()
                }
                binding.salahButton.setOnClickListener {
                    binding.gamePertama.visibility = View.GONE
                    binding.gameKedua.visibility = View.VISIBLE
                    progress++
                    jawabESalah()
                    binding.gameProgressBar.setProgress(progress, true)
                }

                // Game Kedua
                binding.titleGameKedua.text = "Pilihlah tangan yang sesuai untuk huruf “X”"
                binding.imagePilihanA.setImageResource(R.drawable.img_sibi_r)
                binding.imagePilihanB.setImageResource(R.drawable.img_sibi_t)
                binding.imagePilihanC.setImageResource(R.drawable.img_sibi_p)
                binding.imagePilihanD.setImageResource(R.drawable.img_sibi_x)

                binding.pilihanA.setOnClickListener {
                    binding.overlayClickA.visibility = View.VISIBLE
                    binding.overlayClickB.visibility = View.GONE
                    binding.overlayClickC.visibility = View.GONE
                    binding.overlayClickD.visibility = View.GONE
                    isTrue = false
                }

                binding.pilihanB.setOnClickListener {
                    binding.overlayClickA.visibility = View.GONE
                    binding.overlayClickB.visibility = View.VISIBLE
                    binding.overlayClickC.visibility = View.GONE
                    binding.overlayClickD.visibility = View.GONE
                    isTrue = false
                }

                binding.pilihanC.setOnClickListener {
                    binding.overlayClickA.visibility = View.GONE
                    binding.overlayClickB.visibility = View.GONE
                    binding.overlayClickC.visibility = View.VISIBLE
                    binding.overlayClickD.visibility = View.GONE
                    isTrue = false
                }

                binding.pilihanD.setOnClickListener {
                    binding.overlayClickA.visibility = View.GONE
                    binding.overlayClickB.visibility = View.GONE
                    binding.overlayClickC.visibility = View.GONE
                    binding.overlayClickD.visibility = View.VISIBLE
                    isTrue = true
                }

                binding.periksaJawabanGame2.setOnClickListener {
                    if (isTrue == true) {
                        binding.overlayClickA.setBackgroundResource(R.color.see_listen_true)
                        binding.periksaJawabanGame2.visibility = View.GONE
                        val dialogView = layoutInflater.inflate(R.layout.bottom_sheet_benar, null)
                        val dialog =
                            BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme2)
                        dialog.setContentView(dialogView)
                        dialog.setCanceledOnTouchOutside(false)

                        val selanjutnyaBtn =
                            dialog.findViewById<Button>(R.id.bottomSheetSelanjutnyaButton)
                        val gameKedua = binding.gameKedua
                        val gameKetiga = binding.gameKetiga

                        selanjutnyaBtn?.setOnClickListener {
                            gameKedua.visibility = View.GONE
                            gameKetiga.visibility = View.VISIBLE
                            progress++
                            saveScoreB()
                            jawabDBenar()
                            binding.gameProgressBar.setProgress(progress, true)
                            dialog.dismiss()
                        }
                        score++
                        dialog.show()
                    } else {
                        binding.overlayClickB.setBackgroundResource(R.color.see_listen_false)
                        binding.overlayClickC.setBackgroundResource(R.color.see_listen_false)
                        binding.overlayClickD.setBackgroundResource(R.color.see_listen_false)
                        binding.periksaJawabanGame2.visibility = View.GONE
                        val dialogView = layoutInflater.inflate(R.layout.bottom_sheet_salah, null)
                        val dialog =
                            BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme2)
                        dialog.setContentView(dialogView)
                        dialog.setCanceledOnTouchOutside(false)

                        val selanjutnyaBtn =
                            dialog.findViewById<Button>(R.id.bottomSheetSelanjutnyaButton)
                        val imageTrue = dialog.findViewById<ImageView>(R.id.imageTrue)
                        val gameKedua = binding.gameKedua
                        val gameKetiga = binding.gameKetiga

                        imageTrue?.setImageResource(R.drawable.img_sibi_f)
                        selanjutnyaBtn?.setOnClickListener {
                            gameKedua.visibility = View.GONE
                            gameKetiga.visibility = View.VISIBLE
                            progress++
                            jawabDSalah()
                            binding.gameProgressBar.setProgress(progress, true)
                            dialog.dismiss()
                        }
                        dialog.show()
                    }
                }

                // Game Ketiga
                binding.pilihanATv.text = "U"
                binding.pilihanBTv.text = "V"
                binding.pilihanCTv.text = "W"
                binding.pilihanAImg.setImageResource(R.drawable.img_sibi_u)
                binding.pilihanBImg.setImageResource(R.drawable.img_sibi_v)
                binding.pilihanCImg.setImageResource(R.drawable.img_sibi_w)
                val bahasaA = binding.pilihanbahasaA
                val bahasaB = binding.pilihanBahasaB
                val bahasaC = binding.pilihanBahasaC
                val isyaratA = binding.pilihanIsyaratA
                val isyaratB = binding.pilihanIsyaratB
                val isyaratC = binding.pilihanIsyaratC
                bahasaA.setOnClickListener {
                    if (isyaratA.isEnabled) {
                        isyaratB.isEnabled = true
                        isyaratC.isEnabled = true
                    } else if (isyaratB.isEnabled) {
                        isyaratA.isEnabled = true
                        isyaratC.isEnabled = true
                    } else if (isyaratC.isEnabled) {
                        isyaratA.isEnabled = true
                        isyaratB.isEnabled = true
                    }
                    binding.overlayClickBahasaA.visibility = View.VISIBLE
                    binding.overlayClickBahasaB.visibility = View.GONE
                    binding.overlayClickBahasaC.visibility = View.GONE
                    isyaratA.setOnClickListener {
                        binding.overlayClickIsyaratA.visibility = View.VISIBLE
                        binding.overlayClickIsyaratA.setBackgroundResource(R.color.see_listen_false)
                        binding.overlayClickBahasaA.visibility = View.GONE
                        binding.containerA.setBackgroundResource(R.color.see_listen_false)
                        isyaratB.isEnabled = false
                        isyaratC.isEnabled = false
                        bahasaA.isEnabled = false
                        jawabASalah()
                    }
                    isyaratB.setOnClickListener {
                        binding.overlayClickIsyaratB.visibility = View.VISIBLE
                        binding.overlayClickIsyaratB.setBackgroundResource(R.color.see_listen_false)
                        binding.overlayClickBahasaA.visibility = View.GONE
                        binding.containerA.setBackgroundResource(R.color.see_listen_false)
                        isyaratA.isEnabled = false
                        isyaratC.isEnabled = false
                        bahasaA.isEnabled = false
                        jawabASalah()
                    }
                    isyaratC.setOnClickListener {
                        binding.overlayClickIsyaratC.visibility = View.VISIBLE
                        binding.overlayClickIsyaratC.setBackgroundResource(R.color.see_listen_true)
                        binding.overlayClickBahasaA.visibility = View.GONE
                        binding.containerA.setBackgroundResource(R.color.see_listen_true)
                        isyaratA.isEnabled = false
                        isyaratB.isEnabled = false
                        bahasaA.isEnabled = false
                        jawabanA = true
                        score++
                        saveScoreC()
                        jawabABenar()
                    }
                }
                bahasaB.setOnClickListener {
                    if (isyaratA.isEnabled) {
                        isyaratB.isEnabled = true
                        isyaratC.isEnabled = true
                    } else if (isyaratB.isEnabled) {
                        isyaratA.isEnabled = true
                        isyaratC.isEnabled = true
                    } else if (isyaratC.isEnabled) {
                        isyaratA.isEnabled = true
                        isyaratB.isEnabled = true
                    }
                    isyaratA.setOnClickListener {
                        binding.overlayClickIsyaratA.visibility = View.VISIBLE
                        binding.overlayClickIsyaratA.setBackgroundResource(R.color.see_listen_true)
                        binding.overlayClickBahasaB.visibility = View.GONE
                        binding.containerB.setBackgroundResource(R.color.see_listen_true)
                        isyaratB.isEnabled = false
                        isyaratC.isEnabled = false
                        bahasaB.isEnabled = false
                        jawabanB = true
                        score++
                        saveScoreD()
                        jawabBBenar()
                    }
                    isyaratB.setOnClickListener {
                        binding.overlayClickIsyaratB.visibility = View.VISIBLE
                        binding.overlayClickIsyaratB.setBackgroundResource(R.color.see_listen_false)
                        binding.overlayClickBahasaB.visibility = View.GONE
                        binding.containerB.setBackgroundResource(R.color.see_listen_false)
                        isyaratA.isEnabled = false
                        isyaratC.isEnabled = false
                        bahasaB.isEnabled = false
                        jawabBSalah()
                    }
                    isyaratC.setOnClickListener {
                        binding.overlayClickIsyaratC.visibility = View.VISIBLE
                        binding.overlayClickIsyaratC.setBackgroundResource(R.color.see_listen_false)
                        binding.overlayClickBahasaB.visibility = View.GONE
                        binding.containerB.setBackgroundResource(R.color.see_listen_false)
                        isyaratA.isEnabled = false
                        isyaratB.isEnabled = false
                        bahasaB.isEnabled = false
                        jawabBSalah()
                    }
                    binding.overlayClickBahasaA.visibility = View.GONE
                    binding.overlayClickBahasaB.visibility = View.VISIBLE
                    binding.overlayClickBahasaC.visibility = View.GONE
                }
                bahasaC.setOnClickListener {
                    if (isyaratA.isEnabled) {
                        isyaratB.isEnabled = true
                        isyaratC.isEnabled = true
                    } else if (isyaratB.isEnabled) {
                        isyaratA.isEnabled = true
                        isyaratC.isEnabled = true
                    } else if (isyaratC.isEnabled) {
                        isyaratA.isEnabled = true
                        isyaratB.isEnabled = true
                    }
                    isyaratA.setOnClickListener {
                        binding.overlayClickIsyaratA.visibility = View.VISIBLE
                        binding.overlayClickIsyaratA.setBackgroundResource(R.color.see_listen_false)
                        binding.overlayClickBahasaC.visibility = View.GONE
                        binding.containerC.setBackgroundResource(R.color.see_listen_false)
                        isyaratB.isEnabled = false
                        isyaratC.isEnabled = false
                        bahasaC.isEnabled = false
                        jawabCSalah()
                    }
                    isyaratB.setOnClickListener {
                        binding.overlayClickIsyaratB.visibility = View.VISIBLE
                        binding.overlayClickIsyaratB.setBackgroundResource(R.color.see_listen_true)
                        binding.overlayClickBahasaC.visibility = View.GONE
                        binding.containerC.setBackgroundResource(R.color.see_listen_true)
                        isyaratA.isEnabled = false
                        isyaratC.isEnabled = false
                        bahasaC.isEnabled = false
                        jawabanC = true
                        score++
                        saveScoreE()
                        jawabCBenar()
                    }
                    isyaratC.setOnClickListener {
                        binding.overlayClickIsyaratC.visibility = View.VISIBLE
                        binding.overlayClickIsyaratC.setBackgroundResource(R.color.see_listen_false)
                        binding.overlayClickBahasaC.visibility = View.GONE
                        binding.containerC.setBackgroundResource(R.color.see_listen_false)
                        isyaratA.isEnabled = false
                        isyaratB.isEnabled = false
                        bahasaC.isEnabled = false
                        jawabCSalah()
                    }
                    binding.overlayClickBahasaA.visibility = View.GONE
                    binding.overlayClickBahasaB.visibility = View.GONE
                    binding.overlayClickBahasaC.visibility = View.VISIBLE
                }
                binding.selanjutnyaGame3.setOnClickListener {
                    if (jawabanA && jawabanB && jawabanC) {
                        binding.selanjutnyaGame3.visibility = View.GONE
                        val dialogView = layoutInflater.inflate(R.layout.bottom_sheet_benar, null)
                        val dialog =
                            BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme2)
                        dialog.setContentView(dialogView)
                        dialog.setCanceledOnTouchOutside(false)

                        val selanjutnyaBtn =
                            dialog.findViewById<Button>(R.id.bottomSheetSelanjutnyaButton)

                        selanjutnyaBtn?.setOnClickListener {
                            val intent =
                                Intent(Intent(requireContext(), CameraSecondActivity::class.java))
                            intent.putExtra("GAME", "gameUZ")
                            startActivity(intent)
                            dialog.dismiss()
                        }
                        dialog.show()
                    } else {
                        val intent =
                            Intent(Intent(requireContext(), CameraSecondActivity::class.java))
                        intent.putExtra("GAME", "gameUZ")
                        startActivity(intent)
                    }
                }

                binding.closeGameButton.setOnClickListener {
                    val fragment = MenuTebakHurufFragment()
                    bundle.putInt("PROGRESS_AE", 5)
                    bundle.putInt("PROGRESS_FJ", 5)
                    bundle.putInt("PROGRESS_KO", 5)
                    bundle.putInt("PROGRESS_PT", 5)
                    bundle.putInt("PROGRESS_UZ", progress)
                    fragment.arguments = bundle
                    fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)
                        ?.commit()
                }
            }
        }

        if (progressMenu == 5 && gameMenu == 1){
            binding.gamePertama.visibility = View.GONE
            binding.finishGameLayout.visibility = View.VISIBLE
            progress = 5
            binding.gameProgressBar.progress = progress
            if (scoreA == 1 && scoreB == 1 && scoreC == 1 &&
                scoreD == 1 && scoreE == 1){
                binding.imageFinish.setImageResource(R.drawable.img_game_benarsemua)
                binding.titleFinishTv.text = "Selamat!"
                binding.titleKeteranganFinishTv.text = "Anda telah menguasai huruf A-E"
                binding.deskripsiFinishTv.text = "Uji ingatanmu dengan permainan seru lainnya bersama SeeListen"
                binding.mulaiLatihanBtn.text = "Bermain selanjutnya"
                binding.kembaliMengingatBtn.text = "Mungkin lain kali"
                binding.mulaiLatihanBtn.setOnClickListener {
                    bundle.putInt("PROGRESS_AE", progress)
                    val fragment = MenuTebakHurufFragment()
                    fragment.arguments = bundle
                    fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
                }
                binding.kembaliMengingatBtn.setOnClickListener {
                    bundle.putInt("PROGRESS_AE", progress)
                    val fragment = MenuTebakHurufFragment()
                    fragment.arguments = bundle
                    fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
                }
            } else if (scoreA == 0 && scoreB == 0 && scoreC == 0 &&
                scoreD == 0 && scoreE == 0){
                binding.titleFinishTv.text = "Bagus!"
                binding.titleKeteranganFinishTv.text = "Anda telah menyelesaikan huruf A-E"
                binding.deskripsiFinishTv.text = "Namun, Anda terlihat membutuhkan lebih banyak latihan lagi"
                binding.keteranganSalahTv.visibility = View.VISIBLE
                binding.keteranganSalahTv.text = "Huruf yang perlu dipelajari lagi"
                binding.iconFalseContainer.visibility = View.VISIBLE
                binding.mulaiLatihanBtn.text = "Kembali bermain"
                binding.kembaliMengingatBtn.text = "Mungkin lain kali"
                jawabASalah()
                jawabBSalah()
                jawabCSalah()
                jawabDSalah()
                jawabESalah()
                binding.mulaiLatihanBtn.setOnClickListener {
                    bundle.putInt("PROGRESS_AE", 1)
                    val fragment = MenuTebakHurufFragment()
                    fragment.arguments = bundle
                    fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
                }
                binding.kembaliMengingatBtn.setOnClickListener{
                    bundle.putInt("PROGRESS_AE", 1)
                    val fragment = MenuTebakHurufFragment()
                    fragment.arguments = bundle
                    fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
                }
            }
            score = scoreA!! + scoreB!! + scoreC!! + scoreD!! + scoreE!!
            if (score < 5){
                setupjawabanSalah()
                binding.mulaiLatihanBtn.setOnClickListener {
                    bundle.putInt("PROGRESS_AE", 1)
                    val fragment = MenuTebakHurufFragment()
                    fragment.arguments = bundle
                    fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
                }
                binding.kembaliMengingatBtn.setOnClickListener{
                    bundle.putInt("PROGRESS_AE", 1)
                    val fragment = MenuTebakHurufFragment()
                    fragment.arguments = bundle
                    fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
                }
            }
        } else if (progressMenu == 5 && gameMenu == 2){
            binding.gamePertama.visibility = View.GONE
            binding.finishGameLayout.visibility = View.VISIBLE
            progress = 5
            binding.iconATrue.setImageResource(R.drawable.ic_true_f)
            binding.iconBTrue.setImageResource(R.drawable.ic_true_g)
            binding.iconCTrue.setImageResource(R.drawable.ic_true_h)
            binding.iconDTrue.setImageResource(R.drawable.ic_true_i)
            binding.iconETrue.setImageResource(R.drawable.ic_true_j)
            binding.iconAFalse.setImageResource(R.drawable.ic_false_f)
            binding.iconBFalse.setImageResource(R.drawable.ic_false_g)
            binding.iconCFalse.setImageResource(R.drawable.ic_false_h)
            binding.iconDFalse.setImageResource(R.drawable.ic_false_i)
            binding.iconEFalse.setImageResource(R.drawable.ic_false_j)

            binding.gameProgressBar.progress = progress
            if (scoreA == 1 && scoreB == 1 && scoreC == 1 &&
                scoreD == 1 && scoreE == 1){
                binding.imageFinish.setImageResource(R.drawable.img_game_benarsemua)
                binding.titleFinishTv.text = "Selamat!"
                binding.titleKeteranganFinishTv.text = "Anda telah menguasai huruf F-J"
                binding.deskripsiFinishTv.text = "Uji ingatanmu dengan permainan seru lainnya bersama SeeListen"
                binding.mulaiLatihanBtn.text = "Bermain selanjutnya"
                binding.kembaliMengingatBtn.text = "Mungkin lain kali"
                binding.mulaiLatihanBtn.setOnClickListener {
                    bundle.putInt("PROGRESS_FJ", progress)
                    val fragment = MenuTebakHurufFragment()
                    fragment.arguments = bundle
                    fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
                }
                binding.kembaliMengingatBtn.setOnClickListener {
                    bundle.putInt("PROGRESS_FJ", progress)
                    val fragment = MenuTebakHurufFragment()
                    fragment.arguments = bundle
                    fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
                }
            } else if (scoreA == 0 && scoreB == 0 && scoreC == 0 &&
                scoreD == 0 && scoreE == 0){
                binding.titleFinishTv.text = "Bagus!"
                binding.titleKeteranganFinishTv.text = "Anda telah menyelesaikan huruf F-J"
                binding.deskripsiFinishTv.text = "Namun, Anda terlihat membutuhkan lebih banyak latihan lagi"
                binding.keteranganSalahTv.visibility = View.VISIBLE
                binding.keteranganSalahTv.text = "Huruf yang perlu dipelajari lagi"
                binding.iconFalseContainer.visibility = View.VISIBLE
                binding.mulaiLatihanBtn.text = "Kembali bermain"
                binding.kembaliMengingatBtn.text = "Mungkin lain kali"
                jawabASalah()
                jawabBSalah()
                jawabCSalah()
                jawabDSalah()
                jawabESalah()
                binding.mulaiLatihanBtn.setOnClickListener {
                    bundle.putInt("PROGRESS_FJ", 1)
                    val fragment = MenuTebakHurufFragment()
                    fragment.arguments = bundle
                    fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
                }
                binding.kembaliMengingatBtn.setOnClickListener{
                    bundle.putInt("PROGRESS_FJ", 1)
                    val fragment = MenuTebakHurufFragment()
                    fragment.arguments = bundle
                    fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
                }
            }
            score = scoreA!! + scoreB!! + scoreC!! + scoreD!! + scoreE!!
            if (score < 5){
                setupjawabanSalah()
                binding.mulaiLatihanBtn.setOnClickListener {
                    bundle.putInt("PROGRESS_FJ", 1)
                    val fragment = MenuTebakHurufFragment()
                    fragment.arguments = bundle
                    fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
                }
                binding.kembaliMengingatBtn.setOnClickListener{
                    bundle.putInt("PROGRESS_FJ", 1)
                    val fragment = MenuTebakHurufFragment()
                    fragment.arguments = bundle
                    fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
                }
            }
        } else if (progressMenu == 5 && gameMenu == 3){
            binding.gamePertama.visibility = View.GONE
            binding.finishGameLayout.visibility = View.VISIBLE
            progress = 5
            binding.iconATrue.setImageResource(R.drawable.ic_true_k)
            binding.iconBTrue.setImageResource(R.drawable.ic_true_l)
            binding.iconCTrue.setImageResource(R.drawable.ic_true_m)
            binding.iconDTrue.setImageResource(R.drawable.ic_true_n)
            binding.iconETrue.setImageResource(R.drawable.ic_true_o)
            binding.iconAFalse.setImageResource(R.drawable.ic_false_k)
            binding.iconBFalse.setImageResource(R.drawable.ic_false_l)
            binding.iconCFalse.setImageResource(R.drawable.ic_false_m)
            binding.iconDFalse.setImageResource(R.drawable.ic_false_n)
            binding.iconEFalse.setImageResource(R.drawable.ic_false_o)
            binding.gameProgressBar.progress = progress
            if (scoreA == 1 && scoreB == 1 && scoreC == 1 &&
                scoreD == 1 && scoreE == 1){
                binding.imageFinish.setImageResource(R.drawable.img_game_benarsemua)
                binding.titleFinishTv.text = "Selamat!"
                binding.titleKeteranganFinishTv.text = "Anda telah menguasai huruf K-O"
                binding.deskripsiFinishTv.text = "Uji ingatanmu dengan permainan seru lainnya bersama SeeListen"
                binding.mulaiLatihanBtn.text = "Bermain selanjutnya"
                binding.kembaliMengingatBtn.text = "Mungkin lain kali"
                binding.kembaliMengingatBtn.setOnClickListener {
                    bundle.putInt("PROGRESS_KO", progress)
                    val fragment = MenuTebakHurufFragment()
                    fragment.arguments = bundle
                    fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
                }
                binding.mulaiLatihanBtn.setOnClickListener {
                    bundle.putInt("PROGRESS_KO", progress)
                    val fragment = MenuTebakHurufFragment()
                    fragment.arguments = bundle
                    fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
                }
            } else if (scoreA == 0 && scoreB == 0 && scoreC == 0 &&
                scoreD == 0 && scoreE == 0){
                binding.titleFinishTv.text = "Bagus!"
                binding.titleKeteranganFinishTv.text = "Anda telah menyelesaikan huruf K_O"
                binding.deskripsiFinishTv.text = "Namun, Anda terlihat membutuhkan lebih banyak latihan lagi"
                binding.keteranganSalahTv.visibility = View.VISIBLE
                binding.keteranganSalahTv.text = "Huruf yang perlu dipelajari lagi"
                binding.iconFalseContainer.visibility = View.VISIBLE
                binding.mulaiLatihanBtn.text = "Kembali bermain"
                binding.kembaliMengingatBtn.text = "Mungkin lain kali"
                jawabASalah()
                jawabBSalah()
                jawabCSalah()
                jawabDSalah()
                jawabESalah()
                binding.mulaiLatihanBtn.setOnClickListener {
                    bundle.putInt("PROGRESS_KO", 1)
                    val fragment = MenuTebakHurufFragment()
                    fragment.arguments = bundle
                    fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
                }
                binding.kembaliMengingatBtn.setOnClickListener{
                    bundle.putInt("PROGRESS_KO", 1)
                    val fragment = MenuTebakHurufFragment()
                    fragment.arguments = bundle
                    fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
                }
            }
            score = scoreA!! + scoreB!! + scoreC!! + scoreD!! + scoreE!!
            if (score < 5){
                setupjawabanSalah()
                binding.mulaiLatihanBtn.setOnClickListener {
                    bundle.putInt("PROGRESS_KO", 1)
                    val fragment = MenuTebakHurufFragment()
                    fragment.arguments = bundle
                    fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
                }
                binding.kembaliMengingatBtn.setOnClickListener{
                    bundle.putInt("PROGRESS_KO", 1)
                    val fragment = MenuTebakHurufFragment()
                    fragment.arguments = bundle
                    fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
                }
            }
        } else if (progressMenu == 5 && gameMenu == 4){
            binding.gamePertama.visibility = View.GONE
            binding.finishGameLayout.visibility = View.VISIBLE
            progress = 5
            binding.iconATrue.setImageResource(R.drawable.ic_true_p)
            binding.iconBTrue.setImageResource(R.drawable.ic_true_q)
            binding.iconCTrue.setImageResource(R.drawable.ic_true_r)
            binding.iconDTrue.setImageResource(R.drawable.ic_true_s)
            binding.iconETrue.setImageResource(R.drawable.ic_true_t)
            binding.iconAFalse.setImageResource(R.drawable.ic_false_p)
            binding.iconBFalse.setImageResource(R.drawable.ic_false_q)
            binding.iconCFalse.setImageResource(R.drawable.ic_false_r)
            binding.iconDFalse.setImageResource(R.drawable.ic_false_s)
            binding.iconEFalse.setImageResource(R.drawable.ic_false_t)
            binding.gameProgressBar.progress = progress
            if (scoreA == 1 && scoreB == 1 && scoreC == 1 &&
                scoreD == 1 && scoreE == 1){
                binding.imageFinish.setImageResource(R.drawable.img_game_benarsemua)
                binding.titleFinishTv.text = "Selamat!"
                binding.titleKeteranganFinishTv.text = "Anda telah menguasai huruf P-T"
                binding.deskripsiFinishTv.text = "Uji ingatanmu dengan permainan seru lainnya bersama SeeListen"
                binding.mulaiLatihanBtn.text = "Bermain selanjutnya"
                binding.kembaliMengingatBtn.text = "Mungkin lain kali"
                binding.kembaliMengingatBtn.setOnClickListener {
                    bundle.putInt("PROGRESS_PT", progress)
                    val fragment = MenuTebakHurufFragment()
                    fragment.arguments = bundle
                    fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
                }
                binding.mulaiLatihanBtn.setOnClickListener {
                    bundle.putInt("PROGRESS_PT", progress)
                    val fragment = MenuTebakHurufFragment()
                    fragment.arguments = bundle
                    fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
                }
            } else if (scoreA == 0 && scoreB == 0 && scoreC == 0 &&
                scoreD == 0 && scoreE == 0){
                binding.titleFinishTv.text = "Bagus!"
                binding.titleKeteranganFinishTv.text = "Anda telah menyelesaikan huruf P-T"
                binding.deskripsiFinishTv.text = "Namun, Anda terlihat membutuhkan lebih banyak latihan lagi"
                binding.keteranganSalahTv.visibility = View.VISIBLE
                binding.keteranganSalahTv.text = "Huruf yang perlu dipelajari lagi"
                binding.iconFalseContainer.visibility = View.VISIBLE
                binding.mulaiLatihanBtn.text = "Kembali bermain"
                binding.kembaliMengingatBtn.text = "Mungkin lain kali"
                jawabASalah()
                jawabBSalah()
                jawabCSalah()
                jawabDSalah()
                jawabESalah()
                binding.mulaiLatihanBtn.setOnClickListener {
                    bundle.putInt("PROGRESS_PT", 1)
                    val fragment = MenuTebakHurufFragment()
                    fragment.arguments = bundle
                    fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
                }
                binding.kembaliMengingatBtn.setOnClickListener{
                    bundle.putInt("PROGRESS_PT", 1)
                    val fragment = MenuTebakHurufFragment()
                    fragment.arguments = bundle
                    fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
                }
            }
            score = scoreA!! + scoreB!! + scoreC!! + scoreD!! + scoreE!!
            if (score < 5){
                setupjawabanSalah()
                binding.mulaiLatihanBtn.setOnClickListener {
                    bundle.putInt("PROGRESS_PT", 1)
                    val fragment = MenuTebakHurufFragment()
                    fragment.arguments = bundle
                    fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
                }
                binding.kembaliMengingatBtn.setOnClickListener{
                    bundle.putInt("PROGRESS_PT", 1)
                    val fragment = MenuTebakHurufFragment()
                    fragment.arguments = bundle
                    fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
                }
            }
        } else if (progressMenu == 5 && gameMenu == 5){
            binding.gamePertama.visibility = View.GONE
            binding.finishGameLayout.visibility = View.VISIBLE
            progress = 5
            binding.iconATrue.setImageResource(R.drawable.ic_true_u)
            binding.iconBTrue.setImageResource(R.drawable.ic_true_v)
            binding.iconCTrue.setImageResource(R.drawable.ic_true_w)
            binding.iconDTrue.setImageResource(R.drawable.ic_true_x)
            binding.iconETrue.setImageResource(R.drawable.ic_true_y)
            binding.iconAFalse.setImageResource(R.drawable.ic_false_u)
            binding.iconBFalse.setImageResource(R.drawable.ic_false_v)
            binding.iconCFalse.setImageResource(R.drawable.ic_false_w)
            binding.iconDFalse.setImageResource(R.drawable.ic_false_x)
            binding.iconEFalse.setImageResource(R.drawable.ic_false_y)
            binding.gameProgressBar.progress = progress
            if (scoreA == 1 && scoreB == 1 && scoreC == 1 &&
                scoreD == 1 && scoreE == 1){
                binding.imageFinish.setImageResource(R.drawable.img_game_benarsemua)
                binding.titleFinishTv.text = "Selamat!"
                binding.titleKeteranganFinishTv.text = "Anda telah menguasai huruf U-Z"
                binding.deskripsiFinishTv.text = "Uji ingatanmu dengan permainan seru lainnya bersama SeeListen"
                binding.mulaiLatihanBtn.text = "Bermain selanjutnya"
                binding.kembaliMengingatBtn.text = "Mungkin lain kali"
                binding.kembaliMengingatBtn.setOnClickListener {
                    bundle.putInt("PROGRESS_UZ", progress)
                    val fragment = MenuTebakHurufFragment()
                    fragment.arguments = bundle
                    fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
                }
                binding.mulaiLatihanBtn.setOnClickListener {
                    bundle.putInt("PROGRESS_UZ", progress)
                    val fragment = MenuTebakHurufFragment()
                    fragment.arguments = bundle
                    fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
                }
            } else if (scoreA == 0 && scoreB == 0 && scoreC == 0 &&
                scoreD == 0 && scoreE == 0){
                binding.titleFinishTv.text = "Bagus!"
                binding.titleKeteranganFinishTv.text = "Anda telah menyelesaikan huruf U-Z"
                binding.deskripsiFinishTv.text = "Namun, Anda terlihat membutuhkan lebih banyak latihan lagi"
                binding.keteranganSalahTv.visibility = View.VISIBLE
                binding.keteranganSalahTv.text = "Huruf yang perlu dipelajari lagi"
                binding.iconFalseContainer.visibility = View.VISIBLE
                binding.mulaiLatihanBtn.text = "Kembali bermain"
                binding.kembaliMengingatBtn.text = "Mungkin lain kali"
                jawabASalah()
                jawabBSalah()
                jawabCSalah()
                jawabDSalah()
                jawabESalah()
                binding.mulaiLatihanBtn.setOnClickListener {
                    bundle.putInt("PROGRESS_UZ", 1)
                    val fragment = MenuTebakHurufFragment()
                    fragment.arguments = bundle
                    fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
                }
                binding.kembaliMengingatBtn.setOnClickListener{
                    bundle.putInt("PROGRESS_UZ", 1)
                    val fragment = MenuTebakHurufFragment()
                    fragment.arguments = bundle
                    fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
                }
            }
            score = scoreA!! + scoreB!! + scoreC!! + scoreD!! + scoreE!!
            if (score < 5){
                setupjawabanSalah()
                binding.mulaiLatihanBtn.setOnClickListener {
                    bundle.putInt("PROGRESS_UZ", 1)
                    val fragment = MenuTebakHurufFragment()
                    fragment.arguments = bundle
                    fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
                }
                binding.kembaliMengingatBtn.setOnClickListener{
                    bundle.putInt("PROGRESS_UZ", 1)
                    val fragment = MenuTebakHurufFragment()
                    fragment.arguments = bundle
                    fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, fragment)?.commit()
                }
            }
        } else {
            sharedPreferences = requireActivity().getSharedPreferences("pref", 0)
            sharedPreferences.edit().clear().apply()
            sp.edit().clear().apply()
        }
    }

    private fun hideBottomBar(){
        val bottomAppBar = requireActivity().findViewById<BottomAppBar>(R.id.bottomAppBar)
        val fab = requireActivity().findViewById<FloatingActionButton>(R.id.floatingActionButtonCamera)

        bottomAppBar.visibility = View.GONE
        fab.visibility = View.GONE
    }

    @SuppressLint("CommitPrefEdits")
    private fun saveScoreA(){
        sharedPreferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("SCORE_A", 1)
        editor.apply()
    }

    private fun loadScoreA(): Int {
        sharedPreferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("SCORE_A", 0)
    }

    @SuppressLint("CommitPrefEdits")
    private fun saveScoreB(){
        sharedPreferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("SCORE_B", 1)
        editor.apply()
    }

    private fun loadScoreB(): Int {
        sharedPreferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("SCORE_B", 0)
    }

    @SuppressLint("CommitPrefEdits")
    private fun saveScoreC(){
        sharedPreferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("SCORE_C", 1)
        editor.apply()
    }

    private fun loadScoreC(): Int {
        sharedPreferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("SCORE_C", 0)
    }

    @SuppressLint("CommitPrefEdits")
    private fun saveScoreD(){
        sharedPreferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("SCORE_D", 1)
        editor.apply()
    }

    private fun loadScoreD(): Int {
        sharedPreferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("SCORE_D", 0)
    }

    @SuppressLint("CommitPrefEdits")
    private fun saveScoreE(){
        sharedPreferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("SCORE_E", 1)
        editor.apply()
    }

    private fun loadScoreE(): Int {
        sharedPreferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("SCORE_E", 0)
    }

    @SuppressLint("SetTextI18n")
    private fun setupjawabanSalah(){
        fragmentGameTebakHurufBinding?.titleFinishTv?.text = "Bagus!"
        fragmentGameTebakHurufBinding?.titleKeteranganFinishTv?.text = "Anda telah menyelesaikan game tebak huruf"
        fragmentGameTebakHurufBinding?.deskripsiFinishTv?.text = "Namun, Anda terlihat membutuhkan lebih banyak latihan lagi"
        fragmentGameTebakHurufBinding?.keteranganBenarTv?.visibility = View.VISIBLE
        fragmentGameTebakHurufBinding?.keteranganBenarTv?.text = "Huruf yang perlu dipertahankan"
        fragmentGameTebakHurufBinding?.keteranganSalahTv?.visibility = View.VISIBLE
        fragmentGameTebakHurufBinding?.keteranganSalahTv?.text = "Huruf yang perlu dipelajari lagi"
        fragmentGameTebakHurufBinding?.iconFalseContainer?.visibility = View.VISIBLE
        fragmentGameTebakHurufBinding?.iconTrueContainer?.visibility = View.VISIBLE
        fragmentGameTebakHurufBinding?.mulaiLatihanBtn?.text = "Kembali bermain"
        fragmentGameTebakHurufBinding?.kembaliMengingatBtn?.text = "Mungkin lain kali"
    }

    private fun jawabABenar(){
        fragmentGameTebakHurufBinding?.iconATrue?.visibility = View.VISIBLE
        val sp = PreferenceManager.getDefaultSharedPreferences(requireContext())
        sp.edit().putInt("a1", View.VISIBLE).apply()
    }

    private fun jawabBBenar(){
        fragmentGameTebakHurufBinding?.iconBTrue?.visibility = View.VISIBLE
        val sp = PreferenceManager.getDefaultSharedPreferences(requireContext())
        sp.edit().putInt("b1", View.VISIBLE).apply()
    }

    private fun jawabCBenar(){
        fragmentGameTebakHurufBinding?.iconCTrue?.visibility = View.VISIBLE
        val sp = PreferenceManager.getDefaultSharedPreferences(requireContext())
        sp.edit().putInt("c1", View.VISIBLE).apply()
    }

    private fun jawabDBenar(){
        fragmentGameTebakHurufBinding?.iconDTrue?.visibility = View.VISIBLE
        val sp = PreferenceManager.getDefaultSharedPreferences(requireContext())
        sp.edit().putInt("d1", View.VISIBLE).apply()
    }

    private fun jawabEBenar(){
        fragmentGameTebakHurufBinding?.iconETrue?.visibility = View.VISIBLE
        val sp = PreferenceManager.getDefaultSharedPreferences(requireContext())
        sp.edit().putInt("e1", View.VISIBLE).apply()
    }

    private fun jawabASalah(){
        fragmentGameTebakHurufBinding?.iconAFalse?.visibility = View.VISIBLE
        val sp = PreferenceManager.getDefaultSharedPreferences(requireContext())
        sp.edit().putInt("a0", View.VISIBLE).apply()
    }

    private fun jawabBSalah(){
        fragmentGameTebakHurufBinding?.iconBFalse?.visibility = View.VISIBLE
        val sp = PreferenceManager.getDefaultSharedPreferences(requireContext())
        sp.edit().putInt("b0", View.VISIBLE).apply()
    }

    private fun jawabCSalah(){
        fragmentGameTebakHurufBinding?.iconCFalse?.visibility = View.VISIBLE
        val sp = PreferenceManager.getDefaultSharedPreferences(requireContext())
        sp.edit().putInt("c0", View.VISIBLE).apply()
    }

    private fun jawabDSalah(){
        fragmentGameTebakHurufBinding?.iconDFalse?.visibility = View.VISIBLE
        val sp = PreferenceManager.getDefaultSharedPreferences(requireContext())
        sp.edit().putInt("d0", View.VISIBLE).apply()
    }

    private fun jawabESalah(){
        fragmentGameTebakHurufBinding?.iconEFalse?.visibility = View.VISIBLE
        val sp = PreferenceManager.getDefaultSharedPreferences(requireContext())
        sp.edit().putInt("e0", View.VISIBLE).apply()
    }
}