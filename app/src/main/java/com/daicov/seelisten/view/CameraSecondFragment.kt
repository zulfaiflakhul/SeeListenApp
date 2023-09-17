package com.daicov.seelisten.view

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.daicov.seelisten.MainActivity
import com.daicov.seelisten.R
import com.daicov.seelisten.databinding.FragmentCameraSecondBinding
import com.daicov.seelisten.helper.GestureRecognizerHelper
import com.daicov.seelisten.view.adapter.GestureRecognizerResultsAdapter
import com.daicov.seelisten.view.permissions.PermissionsFragment
import com.daicov.seelisten.viewmodel.MainViewModel
import com.google.mediapipe.tasks.components.containers.Category
import com.google.mediapipe.tasks.vision.core.RunningMode
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.math.min

@Suppress("NAME_SHADOWING")
class CameraSecondFragment : Fragment(),
    GestureRecognizerHelper.GestureRecognizerListener {

    private var _fragmentCameraSecondBinding : FragmentCameraSecondBinding? = null
    private val fragmentCameraSecondBinding
        get() = _fragmentCameraSecondBinding!!

    private lateinit var gestureRecognizerHelper: GestureRecognizerHelper
    private val viewModel: MainViewModel by activityViewModels()
    private var defaultNumResults = 1
    private val gestureRecognizerResultAdapter: GestureRecognizerResultsAdapter by lazy {
        GestureRecognizerResultsAdapter().apply {
            updateAdapterSize(defaultNumResults)
        }
    }
    private var preview: Preview? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private var cameraFacing = CameraSelector.LENS_FACING_BACK
    private var torchState: Boolean = false

    private lateinit var backgroundExecutor: ExecutorService

    override fun onResume() {
        super.onResume()
        if (!PermissionsFragment.hasPermissions(requireContext())) {
            Navigation.findNavController(
                requireActivity(), R.id.fragment_container_second
            ).navigate(R.id.action_cameraSecondFragment_to_secondPermissionsFragment)
        }

        backgroundExecutor.execute {
            if (gestureRecognizerHelper.isClosed()) {
                gestureRecognizerHelper.setupGestureRecognizer()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (this::gestureRecognizerHelper.isInitialized) {
            viewModel.setModel(gestureRecognizerHelper.currentModel)
            viewModel.setMinHandDetectionConfidence(gestureRecognizerHelper.minHandDetectionConfidence)
            viewModel.setMinHandTrackingConfidence(gestureRecognizerHelper.minHandTrackingConfidence)
            viewModel.setMinHandPresenceConfidence(gestureRecognizerHelper.minHandPresenceConfidence)
            viewModel.setDelegate(gestureRecognizerHelper.currentDelegate)

            backgroundExecutor.execute { gestureRecognizerHelper.clearGestureRecognizer() }
        }
    }

    override fun onDestroyView() {
        _fragmentCameraSecondBinding = null
        super.onDestroyView()

        backgroundExecutor.shutdown()
        backgroundExecutor.awaitTermination(
            Long.MAX_VALUE, TimeUnit.NANOSECONDS
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragmentCameraSecondBinding = FragmentCameraSecondBinding.inflate(inflater, container, false)
        val args = this.arguments
        when (args?.getInt("game")) {
            1 -> {
                fragmentCameraSecondBinding.titleA.text = "A"
                fragmentCameraSecondBinding.imageA.setImageResource(R.drawable.img_sibi_a)
                fragmentCameraSecondBinding.titleB.text = "B"
                fragmentCameraSecondBinding.imageB.setImageResource(R.drawable.img_sibi_b)
                fragmentCameraSecondBinding.titleC.text = "C"
                fragmentCameraSecondBinding.imageC.setImageResource(R.drawable.img_sibi_c)
                fragmentCameraSecondBinding.titleD.text = "D"
                fragmentCameraSecondBinding.imageD.setImageResource(R.drawable.img_sibi_d)
                fragmentCameraSecondBinding.titleE.text = "E"
                fragmentCameraSecondBinding.imageE.setImageResource(R.drawable.img_sibi_e)
            }
            2 -> {
                fragmentCameraSecondBinding.titleA.text = "F"
                fragmentCameraSecondBinding.imageA.setImageResource(R.drawable.img_sibi_f)
                fragmentCameraSecondBinding.titleB.text = "G"
                fragmentCameraSecondBinding.imageB.setImageResource(R.drawable.img_sibi_g)
                fragmentCameraSecondBinding.titleC.text = "H"
                fragmentCameraSecondBinding.imageC.setImageResource(R.drawable.img_sibi_h)
                fragmentCameraSecondBinding.titleD.text = "I"
                fragmentCameraSecondBinding.imageD.setImageResource(R.drawable.img_sibi_i)
                fragmentCameraSecondBinding.titleE.text = "J"
                fragmentCameraSecondBinding.imageE.setImageResource(R.drawable.img_sibi_j)
            }
            3 -> {
                fragmentCameraSecondBinding.titleA.text = "K"
                fragmentCameraSecondBinding.imageA.setImageResource(R.drawable.img_sibi_k)
                fragmentCameraSecondBinding.titleB.text = "L"
                fragmentCameraSecondBinding.imageB.setImageResource(R.drawable.img_sibi_l)
                fragmentCameraSecondBinding.titleC.text = "M"
                fragmentCameraSecondBinding.imageC.setImageResource(R.drawable.img_sibi_m)
                fragmentCameraSecondBinding.titleD.text = "N"
                fragmentCameraSecondBinding.imageD.setImageResource(R.drawable.img_sibi_n)
                fragmentCameraSecondBinding.titleE.text = "O"
                fragmentCameraSecondBinding.imageE.setImageResource(R.drawable.img_sibi_o)
            }
            4 -> {
                fragmentCameraSecondBinding.titleA.text = "P"
                fragmentCameraSecondBinding.imageA.setImageResource(R.drawable.img_sibi_p)
                fragmentCameraSecondBinding.titleB.text = "Q"
                fragmentCameraSecondBinding.imageB.setImageResource(R.drawable.img_sibi_q)
                fragmentCameraSecondBinding.titleC.text = "R"
                fragmentCameraSecondBinding.imageC.setImageResource(R.drawable.img_sibi_r)
                fragmentCameraSecondBinding.titleD.text = "S"
                fragmentCameraSecondBinding.imageD.setImageResource(R.drawable.img_sibi_s)
                fragmentCameraSecondBinding.titleE.text = "T"
                fragmentCameraSecondBinding.imageE.setImageResource(R.drawable.img_sibi_t)
            }
            5 -> {
                fragmentCameraSecondBinding.titleA.text = "U"
                fragmentCameraSecondBinding.imageA.setImageResource(R.drawable.img_sibi_u)
                fragmentCameraSecondBinding.titleB.text = "V"
                fragmentCameraSecondBinding.imageB.setImageResource(R.drawable.img_sibi_v)
                fragmentCameraSecondBinding.titleC.text = "W"
                fragmentCameraSecondBinding.imageC.setImageResource(R.drawable.img_sibi_w)
                fragmentCameraSecondBinding.titleD.text = "X"
                fragmentCameraSecondBinding.imageD.setImageResource(R.drawable.img_sibi_x)
                fragmentCameraSecondBinding.titleE.text = "Y"
                fragmentCameraSecondBinding.imageE.setImageResource(R.drawable.img_sibi_y)
            }
        }
        return fragmentCameraSecondBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(fragmentCameraSecondBinding.recyclerviewResults) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = gestureRecognizerResultAdapter
        }

        backgroundExecutor = Executors.newSingleThreadExecutor()

        fragmentCameraSecondBinding.viewFinder.post {
            setUpCamera()
        }

        fragmentCameraSecondBinding.penerjemahBtnFlipCamera.setOnClickListener {
            switchCamera()
        }

        fragmentCameraSecondBinding.penerjemahBtnFlashCamera.setOnClickListener {
            turnOnFlash()
        }

        backgroundExecutor.execute {
            gestureRecognizerHelper = GestureRecognizerHelper(
                context = requireContext(),
                runningMode = RunningMode.LIVE_STREAM,
                minHandDetectionConfidence = viewModel.currentMinHandDetectionConfidence,
                minHandTrackingConfidence = viewModel.currentMinHandTrackingConfidence,
                minHandPresenceConfidence = viewModel.currentMinHandPresenceConfidence,
                currentDelegate = viewModel.currentDelegate,
                currentModel = viewModel.currentModel,
                gestureRecognizerListener = this
            )
        }
    }

    private fun setUpCamera() {
        val cameraProviderFuture =
            ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(
            {
                cameraProvider = cameraProviderFuture.get()

                cameraFacing = when {
                    hasFrontCamera() -> CameraSelector.LENS_FACING_FRONT
                    hasBackCamera() -> CameraSelector.LENS_FACING_BACK
                    else -> throw IllegalStateException("Back and front camera are unavailable")
                }

                updateCameraSwitchButton()

                bindCameraUseCases()
            }, ContextCompat.getMainExecutor(requireContext())
        )
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun bindCameraUseCases() {

        // CameraProvider
        val cameraProvider = cameraProvider
            ?: throw IllegalStateException("Camera initialization failed.")

        val cameraSelector =
            CameraSelector.Builder().requireLensFacing(cameraFacing).build()

        preview = Preview.Builder().setTargetAspectRatio(AspectRatio.RATIO_16_9)
            .setTargetRotation(fragmentCameraSecondBinding.viewFinder.display.rotation)
            .build()

        imageAnalyzer =
            ImageAnalysis.Builder().setTargetAspectRatio(AspectRatio.RATIO_16_9)
                .setTargetRotation(fragmentCameraSecondBinding.viewFinder.display.rotation)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                .build()
                .also {
                    it.setAnalyzer(backgroundExecutor) { image ->
                        recognizeHand(image)
                    }
                }

        cameraProvider.unbindAll()

        try {
            camera = cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, imageAnalyzer
            )

            preview?.setSurfaceProvider(fragmentCameraSecondBinding.viewFinder.surfaceProvider)
        } catch (exc: Exception) {
            Log.e(CameraFragment.TAG, "Use case binding failed", exc)
        }
    }

    private fun recognizeHand(imageProxy: ImageProxy) {
        gestureRecognizerHelper.recognizeLiveStream(
            imageProxy = imageProxy,
        )
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        imageAnalyzer?.targetRotation =
            fragmentCameraSecondBinding.viewFinder.display.rotation
    }

    override fun onResults(
        resultBundle: GestureRecognizerHelper.ResultBundle
    ) {
        activity?.runOnUiThread {
            if (_fragmentCameraSecondBinding != null) {
                val gestureCategories = resultBundle.results.first().gestures()
                if (gestureCategories.isNotEmpty()) {
                    gestureRecognizerResultAdapter.updateResults(
                        gestureCategories.first()
                    )
                    val jawabPertama = fragmentCameraSecondBinding.jawabPertamaOverlay
                    val jawabKedua = fragmentCameraSecondBinding.jawabKeduaOverlay
                    val jawabKetiga = fragmentCameraSecondBinding.jawabKetigaOverlay
                    val jawabKeempat = fragmentCameraSecondBinding.jawabKeempatOverlay
                    val jawabKelima = fragmentCameraSecondBinding.jawabKelimaOverlay

                    val category : List<Category> = gestureCategories.first()
                    val adapterCategories: MutableList<Category?> = MutableList(1) {null}
                    val a = category.sortedByDescending { it.score() }
                    val min = min(a.size, adapterCategories.size)
                    for (i in 0 until min){
                        adapterCategories[i] = category[i]
                        adapterCategories[i].let { category ->
                            val args = this.arguments
                            val game = args?.getInt("game")
                            if (game == 1){
                                if (fragmentCameraSecondBinding.jawabKelima.visibility == View.VISIBLE){
                                    if (category?.categoryName() == "A"){
                                        jawabPertama.visibility = View.GONE
                                    } else if (category?.categoryName() == "B"){
                                        if (jawabPertama.visibility == View.GONE){
                                            jawabKedua.visibility = View.GONE
                                        }
                                    } else if (category?.categoryName() == "C"){
                                        if (jawabPertama.visibility == View.GONE && jawabKedua.visibility == View.GONE){
                                            jawabKetiga.visibility = View.GONE
                                        }
                                    } else if (category?.categoryName() == "D"){
                                        if (jawabPertama.visibility == View.GONE && jawabKedua.visibility == View.GONE && jawabKetiga.visibility == View.GONE){
                                            jawabKeempat.visibility = View.GONE
                                        }
                                    } else if (category?.categoryName() == "E"){
                                        if (jawabPertama.visibility == View.GONE && jawabKedua.visibility == View.GONE &&
                                            jawabKetiga.visibility == View.GONE && jawabKeempat.visibility == View.GONE){
                                            jawabKelima.visibility = View.GONE

                                            object : CountDownTimer(2000, 1000) {
                                                override fun onTick(p0: Long) {}
                                                override fun onFinish() {
                                                    fragmentCameraSecondBinding.titleA.text = "B"
                                                    fragmentCameraSecondBinding.imageA.setImageResource(R.drawable.img_sibi_b)
                                                    jawabPertama.visibility = View.VISIBLE
                                                    fragmentCameraSecondBinding.titleB.text = "A"
                                                    fragmentCameraSecondBinding.imageB.setImageResource(R.drawable.img_sibi_a)
                                                    jawabKedua.visibility = View.VISIBLE
                                                    fragmentCameraSecondBinding.titleC.text = "C"
                                                    fragmentCameraSecondBinding.imageC.setImageResource(R.drawable.img_sibi_c)
                                                    jawabKetiga.visibility = View.VISIBLE
                                                    fragmentCameraSecondBinding.titleD.text = "A"
                                                    fragmentCameraSecondBinding.imageD.setImageResource(R.drawable.img_sibi_a)
                                                    jawabKeempat.visibility = View.VISIBLE
                                                    fragmentCameraSecondBinding.jawabKelima.visibility = View.GONE
                                                    fragmentCameraSecondBinding.gameProgressBar.progress = 5
                                                }
                                            }.start()
                                        }
                                    }
                                } else {
                                    if (jawabKetiga.visibility == View.VISIBLE){
                                        if (category?.categoryName() == "B"){
                                            jawabPertama.visibility = View.GONE
                                        } else if (category?.categoryName() == "A"){
                                            if (jawabPertama.visibility == View.GONE){
                                                jawabKedua.visibility = View.GONE
                                            }
                                        } else if (category?.categoryName() == "C") {
                                            if (jawabPertama.visibility == View.GONE && jawabKedua.visibility == View.GONE) {
                                                jawabKetiga.visibility = View.GONE
                                            }
                                        }
                                    } else {
                                        if (category?.categoryName() == "A") {
                                            if (jawabPertama.visibility == View.GONE && jawabKedua.visibility == View.GONE && jawabKetiga.visibility == View.GONE) {
                                                jawabKeempat.visibility = View.GONE

                                                object : CountDownTimer(2000, 1000){
                                                    override fun onTick(p0: Long) {}

                                                    override fun onFinish() {
                                                        val intent = Intent(Intent(requireContext(), MainActivity::class.java))
                                                        intent.putExtra("fragmentToLoad", "gameFragmentAE")
                                                        startActivity(intent)
                                                    }
                                                }.start()
                                            }
                                        }
                                    }
                                }
                            } else if (game == 2){
                                if (fragmentCameraSecondBinding.jawabKelima.visibility == View.VISIBLE){
                                    if (category?.categoryName() == "F"){
                                        jawabPertama.visibility = View.GONE
                                    } else if (category?.categoryName() == "G"){
                                        if (jawabPertama.visibility == View.GONE){
                                            jawabKedua.visibility = View.GONE
                                        }
                                    } else if (category?.categoryName() == "H"){
                                        if (jawabPertama.visibility == View.GONE && jawabKedua.visibility == View.GONE){
                                            jawabKetiga.visibility = View.GONE
                                        }
                                    } else if (category?.categoryName() == "I"){
                                        if (jawabPertama.visibility == View.GONE && jawabKedua.visibility == View.GONE && jawabKetiga.visibility == View.GONE){
                                            jawabKeempat.visibility = View.GONE
                                        }
                                    } else if (category?.categoryName() == "J"){
                                        if (jawabPertama.visibility == View.GONE && jawabKedua.visibility == View.GONE &&
                                            jawabKetiga.visibility == View.GONE && jawabKeempat.visibility == View.GONE){
                                            jawabKelima.visibility = View.GONE

                                            object : CountDownTimer(2000, 1000) {
                                                override fun onTick(p0: Long) {}
                                                override fun onFinish() {
                                                    fragmentCameraSecondBinding.titleA.text = "F"
                                                    fragmentCameraSecondBinding.imageA.setImageResource(R.drawable.img_sibi_f)
                                                    jawabPertama.visibility = View.VISIBLE
                                                    fragmentCameraSecondBinding.titleB.text = "I"
                                                    fragmentCameraSecondBinding.imageB.setImageResource(R.drawable.img_sibi_i)
                                                    jawabKedua.visibility = View.VISIBLE
                                                    fragmentCameraSecondBinding.titleC.text = "J"
                                                    fragmentCameraSecondBinding.imageC.setImageResource(R.drawable.img_sibi_j)
                                                    jawabKetiga.visibility = View.VISIBLE
                                                    fragmentCameraSecondBinding.titleD.text = "I"
                                                    fragmentCameraSecondBinding.imageD.setImageResource(R.drawable.img_sibi_i)
                                                    jawabKeempat.visibility = View.VISIBLE
                                                    fragmentCameraSecondBinding.jawabKelima.visibility = View.GONE
                                                    fragmentCameraSecondBinding.gameProgressBar.progress = 5
                                                }
                                            }.start()
                                        }
                                    }
                                } else {
                                    if (jawabKetiga.visibility == View.VISIBLE){
                                        if (category?.categoryName() == "F"){
                                            jawabPertama.visibility = View.GONE
                                        } else if (category?.categoryName() == "I"){
                                            if (jawabPertama.visibility == View.GONE){
                                                jawabKedua.visibility = View.GONE
                                            }
                                        } else if (category?.categoryName() == "J") {
                                            if (jawabPertama.visibility == View.GONE && jawabKedua.visibility == View.GONE) {
                                                jawabKetiga.visibility = View.GONE
                                            }
                                        }
                                    } else {
                                        if (category?.categoryName() == "I") {
                                            if (jawabPertama.visibility == View.GONE && jawabKedua.visibility == View.GONE && jawabKetiga.visibility == View.GONE) {
                                                jawabKeempat.visibility = View.GONE

                                                object : CountDownTimer(2000, 1000){
                                                    override fun onTick(p0: Long) {}

                                                    override fun onFinish() {
                                                        val intent = Intent(Intent(requireContext(), MainActivity::class.java))
                                                        intent.putExtra("fragmentToLoad", "gameFragmentFJ")
                                                        startActivity(intent)
                                                    }
                                                }.start()
                                            }
                                        }
                                    }
                                }
                            } else if (game == 3){
                                if (fragmentCameraSecondBinding.jawabKelima.visibility == View.VISIBLE){
                                    if (category?.categoryName() == "K"){
                                        jawabPertama.visibility = View.GONE
                                    } else if (category?.categoryName() == "L"){
                                        if (jawabPertama.visibility == View.GONE){
                                            jawabKedua.visibility = View.GONE
                                        }
                                    } else if (category?.categoryName() == "M"){
                                        if (jawabPertama.visibility == View.GONE && jawabKedua.visibility == View.GONE){
                                            jawabKetiga.visibility = View.GONE
                                        }
                                    } else if (category?.categoryName() == "N"){
                                        if (jawabPertama.visibility == View.GONE && jawabKedua.visibility == View.GONE && jawabKetiga.visibility == View.GONE){
                                            jawabKeempat.visibility = View.GONE
                                        }
                                    } else if (category?.categoryName() == "O"){
                                        if (jawabPertama.visibility == View.GONE && jawabKedua.visibility == View.GONE &&
                                            jawabKetiga.visibility == View.GONE && jawabKeempat.visibility == View.GONE){
                                            jawabKelima.visibility = View.GONE

                                            object : CountDownTimer(2000, 1000) {
                                                override fun onTick(p0: Long) {}
                                                override fun onFinish() {
                                                    fragmentCameraSecondBinding.titleA.text = "M"
                                                    fragmentCameraSecondBinding.imageA.setImageResource(R.drawable.img_sibi_m)
                                                    jawabPertama.visibility = View.VISIBLE
                                                    fragmentCameraSecondBinding.titleB.text = "O"
                                                    fragmentCameraSecondBinding.imageB.setImageResource(R.drawable.img_sibi_o)
                                                    jawabKedua.visibility = View.VISIBLE
                                                    fragmentCameraSecondBinding.titleC.text = "N"
                                                    fragmentCameraSecondBinding.imageC.setImageResource(R.drawable.img_sibi_n)
                                                    jawabKetiga.visibility = View.VISIBLE
                                                    fragmentCameraSecondBinding.titleD.text = "O"
                                                    fragmentCameraSecondBinding.imageD.setImageResource(R.drawable.img_sibi_o)
                                                    jawabKeempat.visibility = View.VISIBLE
                                                    fragmentCameraSecondBinding.jawabKelima.visibility = View.GONE
                                                    fragmentCameraSecondBinding.gameProgressBar.progress = 5
                                                }
                                            }.start()
                                        }
                                    }
                                } else {
                                    if (jawabKetiga.visibility == View.VISIBLE){
                                        if (category?.categoryName() == "M"){
                                            jawabPertama.visibility = View.GONE
                                        } else if (category?.categoryName() == "O"){
                                            if (jawabPertama.visibility == View.GONE){
                                                jawabKedua.visibility = View.GONE
                                            }
                                        } else if (category?.categoryName() == "N") {
                                            if (jawabPertama.visibility == View.GONE && jawabKedua.visibility == View.GONE) {
                                                jawabKetiga.visibility = View.GONE
                                            }
                                        }
                                    } else {
                                        if (category?.categoryName() == "O") {
                                            if (jawabPertama.visibility == View.GONE && jawabKedua.visibility == View.GONE && jawabKetiga.visibility == View.GONE) {
                                                jawabKeempat.visibility = View.GONE

                                                object : CountDownTimer(2000, 1000){
                                                    override fun onTick(p0: Long) {}

                                                    override fun onFinish() {
                                                        val intent = Intent(Intent(requireContext(), MainActivity::class.java))
                                                        intent.putExtra("fragmentToLoad", "gameFragmentKO")
                                                        startActivity(intent)
                                                    }
                                                }.start()
                                            }
                                        }
                                    }
                                }
                            } else if (game == 4){
                                if (fragmentCameraSecondBinding.jawabKelima.visibility == View.VISIBLE){
                                    if (category?.categoryName() == "P"){
                                        jawabPertama.visibility = View.GONE
                                    } else if (category?.categoryName() == "Q"){
                                        if (jawabPertama.visibility == View.GONE){
                                            jawabKedua.visibility = View.GONE
                                        }
                                    } else if (category?.categoryName() == "R"){
                                        if (jawabPertama.visibility == View.GONE && jawabKedua.visibility == View.GONE){
                                            jawabKetiga.visibility = View.GONE
                                        }
                                    } else if (category?.categoryName() == "S"){
                                        if (jawabPertama.visibility == View.GONE && jawabKedua.visibility == View.GONE && jawabKetiga.visibility == View.GONE){
                                            jawabKeempat.visibility = View.GONE
                                        }
                                    } else if (category?.categoryName() == "T"){
                                        if (jawabPertama.visibility == View.GONE && jawabKedua.visibility == View.GONE &&
                                            jawabKetiga.visibility == View.GONE && jawabKeempat.visibility == View.GONE){
                                            jawabKelima.visibility = View.GONE

                                            object : CountDownTimer(2000, 1000) {
                                                override fun onTick(p0: Long) {}
                                                override fun onFinish() {
                                                    fragmentCameraSecondBinding.titleA.text = "Q"
                                                    fragmentCameraSecondBinding.imageA.setImageResource(R.drawable.img_sibi_q)
                                                    jawabPertama.visibility = View.VISIBLE
                                                    fragmentCameraSecondBinding.titleB.text = "R"
                                                    fragmentCameraSecondBinding.imageB.setImageResource(R.drawable.img_sibi_r)
                                                    jawabKedua.visibility = View.VISIBLE
                                                    fragmentCameraSecondBinding.titleC.text = "S"
                                                    fragmentCameraSecondBinding.imageC.setImageResource(R.drawable.img_sibi_s)
                                                    jawabKetiga.visibility = View.VISIBLE
                                                    fragmentCameraSecondBinding.titleD.text = "T"
                                                    fragmentCameraSecondBinding.imageD.setImageResource(R.drawable.img_sibi_t)
                                                    jawabKeempat.visibility = View.VISIBLE
                                                    fragmentCameraSecondBinding.jawabKelima.visibility = View.GONE
                                                    fragmentCameraSecondBinding.gameProgressBar.progress = 5
                                                }
                                            }.start()
                                        }
                                    }
                                } else {
                                    if (jawabKetiga.visibility == View.VISIBLE){
                                        if (category?.categoryName() == "Q"){
                                            jawabPertama.visibility = View.GONE
                                        } else if (category?.categoryName() == "R"){
                                            if (jawabPertama.visibility == View.GONE){
                                                jawabKedua.visibility = View.GONE
                                            }
                                        } else if (category?.categoryName() == "S") {
                                            if (jawabPertama.visibility == View.GONE && jawabKedua.visibility == View.GONE) {
                                                jawabKetiga.visibility = View.GONE
                                            }
                                        }
                                    } else {
                                        if (category?.categoryName() == "T") {
                                            if (jawabPertama.visibility == View.GONE && jawabKedua.visibility == View.GONE && jawabKetiga.visibility == View.GONE) {
                                                jawabKeempat.visibility = View.GONE

                                                object : CountDownTimer(2000, 1000){
                                                    override fun onTick(p0: Long) {}

                                                    override fun onFinish() {
                                                        val intent = Intent(Intent(requireContext(), MainActivity::class.java))
                                                        intent.putExtra("fragmentToLoad", "gameFragmentPT")
                                                        startActivity(intent)
                                                    }
                                                }.start()
                                            }
                                        }
                                    }
                                }
                            } else if (game == 5) {
                                if (fragmentCameraSecondBinding.jawabKelima.visibility == View.VISIBLE) {
                                    if (category?.categoryName() == "U") {
                                        jawabPertama.visibility = View.GONE
                                    } else if (category?.categoryName() == "V") {
                                        if (jawabPertama.visibility == View.GONE) {
                                            jawabKedua.visibility = View.GONE
                                        }
                                    } else if (category?.categoryName() == "W") {
                                        if (jawabPertama.visibility == View.GONE && jawabKedua.visibility == View.GONE) {
                                            jawabKetiga.visibility = View.GONE
                                        }
                                    } else if (category?.categoryName() == "X") {
                                        if (jawabPertama.visibility == View.GONE && jawabKedua.visibility == View.GONE && jawabKetiga.visibility == View.GONE) {
                                            jawabKeempat.visibility = View.GONE
                                        }
                                    } else if (category?.categoryName() == "Y") {
                                        if (jawabPertama.visibility == View.GONE && jawabKedua.visibility == View.GONE &&
                                            jawabKetiga.visibility == View.GONE && jawabKeempat.visibility == View.GONE
                                        ) {
                                            jawabKelima.visibility = View.GONE

                                            object : CountDownTimer(2000, 1000) {
                                                override fun onTick(p0: Long) {}
                                                override fun onFinish() {
                                                    fragmentCameraSecondBinding.titleA.text = "V"
                                                    fragmentCameraSecondBinding.imageA.setImageResource(
                                                        R.drawable.img_sibi_v
                                                    )
                                                    jawabPertama.visibility = View.VISIBLE
                                                    fragmentCameraSecondBinding.titleB.text = "Y"
                                                    fragmentCameraSecondBinding.imageB.setImageResource(
                                                        R.drawable.img_sibi_y
                                                    )
                                                    jawabKedua.visibility = View.VISIBLE
                                                    fragmentCameraSecondBinding.titleC.text = "X"
                                                    fragmentCameraSecondBinding.imageC.setImageResource(
                                                        R.drawable.img_sibi_x
                                                    )
                                                    jawabKetiga.visibility = View.VISIBLE
                                                    fragmentCameraSecondBinding.titleD.text = "U"
                                                    fragmentCameraSecondBinding.imageD.setImageResource(
                                                        R.drawable.img_sibi_u
                                                    )
                                                    jawabKeempat.visibility = View.VISIBLE
                                                    fragmentCameraSecondBinding.jawabKelima.visibility =
                                                        View.GONE
                                                    fragmentCameraSecondBinding.gameProgressBar.progress =
                                                        5
                                                }
                                            }.start()
                                        }
                                    }
                                } else {
                                    if (jawabKetiga.visibility == View.VISIBLE){
                                        if (category?.categoryName() == "V"){
                                            jawabPertama.visibility = View.GONE
                                        } else if (category?.categoryName() == "Y"){
                                            if (jawabPertama.visibility == View.GONE){
                                                jawabKedua.visibility = View.GONE
                                            }
                                        } else if (category?.categoryName() == "X") {
                                            if (jawabPertama.visibility == View.GONE && jawabKedua.visibility == View.GONE) {
                                                jawabKetiga.visibility = View.GONE
                                            }
                                        }
                                    } else {
                                        if (category?.categoryName() == "U") {
                                            if (jawabPertama.visibility == View.GONE && jawabKedua.visibility == View.GONE && jawabKetiga.visibility == View.GONE) {
                                                jawabKeempat.visibility = View.GONE

                                                object : CountDownTimer(2000, 1000){
                                                    override fun onTick(p0: Long) {}

                                                    override fun onFinish() {
                                                        val intent = Intent(Intent(requireContext(), MainActivity::class.java))
                                                        intent.putExtra("fragmentToLoad", "gameFragmentUZ")
                                                        startActivity(intent)
                                                    }
                                                }.start()
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                else {
                    gestureRecognizerResultAdapter.updateResults(emptyList())
                }
            }
        }
    }

    override fun onError(error: String, errorCode: Int) {
        activity?.runOnUiThread {
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            gestureRecognizerResultAdapter.updateResults(emptyList())
        }
    }

    private fun updateCameraSwitchButton() {
        try {
            fragmentCameraSecondBinding.penerjemahBtnFlipCamera.isEnabled = hasBackCamera() && hasFrontCamera()
        } catch (exception: CameraInfoUnavailableException) {
            fragmentCameraSecondBinding.penerjemahBtnFlipCamera.isEnabled = false
        }
    }

    private fun switchCamera() {
        cameraFacing = if (CameraSelector.LENS_FACING_BACK == cameraFacing) {
            fragmentCameraSecondBinding.penerjemahBtnFlipCamera.setImageResource(R.drawable.ic_switch_camera)
            CameraSelector.LENS_FACING_FRONT
        } else {
            fragmentCameraSecondBinding.penerjemahBtnFlipCamera.setImageResource(R.drawable.ic_switch_camera)
            CameraSelector.LENS_FACING_BACK
        }
        bindCameraUseCases()
    }

    private fun hasBackCamera(): Boolean {
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA) ?: false
    }

    private fun hasFrontCamera(): Boolean {
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA) ?: false
    }

    private fun turnOnFlash() {
        fragmentCameraSecondBinding.penerjemahBtnFlashCamera.setOnClickListener {
            torchState = when (torchState){
                false -> {
                    camera?.cameraControl?.enableTorch(true)
                    fragmentCameraSecondBinding.penerjemahBtnFlashCamera.setImageResource(R.drawable.ic_flash_on)
                    true
                }
                true -> {
                    camera?.cameraControl?.enableTorch(false)
                    fragmentCameraSecondBinding.penerjemahBtnFlashCamera.setImageResource(R.drawable.ic_flash_off)
                    false
                }
            }
        }
    }
}