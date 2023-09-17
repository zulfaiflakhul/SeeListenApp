package com.daicov.seelisten.view

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.daicov.seelisten.MainActivity
import com.daicov.seelisten.R
import com.daicov.seelisten.databinding.FragmentCameraTebakgambarBinding
import com.daicov.seelisten.databinding.FragmentSusunkataCameraBinding
import com.daicov.seelisten.helper.GestureRecognizerHelper
import com.daicov.seelisten.view.adapter.GestureRecognizerResultsAdapter
import com.daicov.seelisten.view.permissions.PermissionsFragment
import com.daicov.seelisten.viewmodel.MainViewModel
import com.google.mediapipe.tasks.components.containers.Category
import com.google.mediapipe.tasks.vision.core.RunningMode
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.math.min

@Suppress("UNREACHABLE_CODE", "NAME_SHADOWING")
class SusunkataCameraFragment : Fragment(),
    GestureRecognizerHelper.GestureRecognizerListener {

    companion object {
        const val  TAG = "Hand gesture recognizer"
    }

    private var _fragmentSusunkataCamera: FragmentSusunkataCameraBinding? = null

    private val fragmentCameraSusunkataCameraBinding
        get() = _fragmentSusunkataCamera!!

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
    private var cameraFacing = CameraSelector.LENS_FACING_FRONT
    private var torchState: Boolean = false

    /** Blocking ML operations are performed using this executor */
    private lateinit var backgroundExecutor: ExecutorService

    override fun onResume() {
        super.onResume()
        if (!PermissionsFragment.hasPermissions(requireContext())) {
            Navigation.findNavController(
                requireActivity(), R.id.fragment_container
            ).navigate(R.id.action_susunkataCameraFragment_to_susunKataPermissionsFragment)
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
        _fragmentSusunkataCamera = null
        super.onDestroyView()

        backgroundExecutor.shutdown()
        backgroundExecutor.awaitTermination(
            Long.MAX_VALUE, TimeUnit.NANOSECONDS
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                val intent = Intent(Intent(requireContext(), MainActivity::class.java))
                intent.putExtra("fragmentToLoad", "gameFragment")
                startActivity(intent)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        _fragmentSusunkataCamera =
            FragmentSusunkataCameraBinding.inflate(inflater, container, false)
        return fragmentCameraSusunkataCameraBinding.root
    }

    @SuppressLint("MissingPermission", "ResourceType", "InflateParams")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backgroundExecutor = Executors.newSingleThreadExecutor()

        fragmentCameraSusunkataCameraBinding.viewFinder.post {
            setUpCamera()
        }

        fragmentCameraSusunkataCameraBinding.penerjemahBtnFlipCamera.setOnClickListener {
            switchCamera()
        }

        fragmentCameraSusunkataCameraBinding.penerjemahBtnFlashCamera.setOnClickListener {
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
            .setTargetRotation(fragmentCameraSusunkataCameraBinding.viewFinder.display.rotation)
            .build()

        imageAnalyzer =
            ImageAnalysis.Builder().setTargetAspectRatio(AspectRatio.RATIO_16_9)
                .setTargetRotation(fragmentCameraSusunkataCameraBinding.viewFinder.display.rotation)
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

            preview?.setSurfaceProvider(fragmentCameraSusunkataCameraBinding.viewFinder.surfaceProvider)
        } catch (exc: Exception) {
            Log.e(TAG, "Use case binding failed", exc)
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
            fragmentCameraSusunkataCameraBinding.viewFinder.display.rotation
    }

    override fun onResults(
        resultBundle: GestureRecognizerHelper.ResultBundle
    ) {
        activity?.runOnUiThread {
            if (_fragmentSusunkataCamera != null) {
                // Show result of recognized gesture
                val gestureCategories = resultBundle.results.first().gestures()
                if (gestureCategories.isNotEmpty()) {
                    gestureRecognizerResultAdapter.updateResults(
                        gestureCategories.first()
                    )

                    val jawabPertama = fragmentCameraSusunkataCameraBinding.jawabPertamaOverlay
                    val jawabKedua = fragmentCameraSusunkataCameraBinding.jawabKeduaOverlay
                    val jawabKetiga = fragmentCameraSusunkataCameraBinding.jawabKetigaOverlay
                    val jawabKeempat = fragmentCameraSusunkataCameraBinding.jawabKeempatOverlay

                    val category : List<Category> = gestureCategories.first()
                    val adapterCategories: MutableList<Category?> = MutableList(1) {null}
                    val a = category.sortedByDescending { it.score() }
                    val min = min(a.size, adapterCategories.size)
                    for (i in 0 until min) {
                        adapterCategories[i] = category[i]
                        adapterCategories[i].let { category ->
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
                                                intent.putExtra("fragmentToLoad", "gameFragment")
                                                startActivity(intent)
                                            }
                                        }.start()
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
            fragmentCameraSusunkataCameraBinding.penerjemahBtnFlipCamera.isEnabled = hasBackCamera() && hasFrontCamera()
        } catch (exception: CameraInfoUnavailableException) {
            fragmentCameraSusunkataCameraBinding.penerjemahBtnFlipCamera.isEnabled = false
        }
    }

    private fun switchCamera() {
        cameraFacing = if (CameraSelector.LENS_FACING_FRONT == cameraFacing) {
            fragmentCameraSusunkataCameraBinding.penerjemahBtnFlipCamera.setImageResource(R.drawable.ic_switch_camera)
            CameraSelector.LENS_FACING_BACK
        } else {
            fragmentCameraSusunkataCameraBinding.penerjemahBtnFlipCamera.setImageResource(R.drawable.ic_switch_camera)
            CameraSelector.LENS_FACING_FRONT
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
        fragmentCameraSusunkataCameraBinding.penerjemahBtnFlashCamera.setOnClickListener {
            torchState = when (torchState){
                false -> {
                    camera?.cameraControl?.enableTorch(true)
                    fragmentCameraSusunkataCameraBinding.penerjemahBtnFlashCamera.setImageResource(R.drawable.ic_flash_on)
                    true
                }
                true -> {
                    camera?.cameraControl?.enableTorch(false)
                    fragmentCameraSusunkataCameraBinding.penerjemahBtnFlashCamera.setImageResource(R.drawable.ic_flash_off)
                    false
                }
            }
        }
    }
}
