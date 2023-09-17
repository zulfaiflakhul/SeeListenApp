package com.daicov.seelisten.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.SystemClock
import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.camera.core.ImageProxy
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.framework.image.MPImage
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.core.Delegate
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.gesturerecognizer.GestureRecognizer
import com.google.mediapipe.tasks.vision.gesturerecognizer.GestureRecognizerResult

class GestureRecognizerHelper(
    var minHandDetectionConfidence: Float = DEFAULT_HAND_DETECTION_CONFIDENCE,
    var minHandTrackingConfidence: Float = DEFAULT_HAND_TRACKING_CONFIDENCE,
    var minHandPresenceConfidence: Float = DEFAULT_HAND_PRESENCE_CONFIDENCE,
    var currentDelegate: Int = DELEGATE_GPU,
    var currentModel: Int = SIBI_ALFABET,
    var runningMode: RunningMode = RunningMode.IMAGE,
    val context: Context,
    val gestureRecognizerListener: GestureRecognizerListener? = null
) {

    private var gestureRecognizer: GestureRecognizer? = null

    init {
        setupGestureRecognizer()
    }

    fun clearGestureRecognizer() {
        gestureRecognizer?.close()
        gestureRecognizer = null
    }

    fun setupGestureRecognizer() {
        val baseOptionBuilder = BaseOptions.builder()

        baseOptionBuilder.setDelegate(Delegate.GPU)

        val modelName =
            when (currentModel){
                SIBI_ALFABET -> "sibi2_epoch500_batch_size32.task"
                SIBI_ANGKA -> "number_SIBI.task"
                SIBI_KATA -> "gesture_recognizer.task"
                SIBI_KALIMAT -> "gesture_recognizer_SIBI_KataAlfabet.task"
                ASL_ALFABET -> "ASL_Alfabet_epoch500_batch_size32.task"
                ASL_ANGKA -> "number_ASL.task"
                else -> "sibi2_epoch500_batch_size32.task"
            }
        baseOptionBuilder.setModelAssetPath(modelName)

        try {
            val baseOptions = baseOptionBuilder.build()
            val optionsBuilder =
                GestureRecognizer.GestureRecognizerOptions.builder()
                    .setBaseOptions(baseOptions)
                    .setMinHandDetectionConfidence(minHandDetectionConfidence)
                    .setMinTrackingConfidence(minHandTrackingConfidence)
                    .setMinHandPresenceConfidence(minHandPresenceConfidence)
                    .setRunningMode(runningMode)

            if (runningMode == RunningMode.LIVE_STREAM) {
                optionsBuilder
                    .setResultListener(this::returnLivestreamResult)
                    .setErrorListener(this::returnLivestreamError)
            }
            val options = optionsBuilder.build()
            gestureRecognizer =
                GestureRecognizer.createFromOptions(context, options)
        } catch (e: IllegalStateException) {
            gestureRecognizerListener?.onError(
                "Gesture recognizer failed to initialize. See error logs for " + "details"
            )
            Log.e(
                TAG,
                "MP Task Vision failed to load the task with error: " + e.message
            )
        } catch (e: RuntimeException) {
            gestureRecognizerListener?.onError(
                "Gesture recognizer failed to initialize. See error logs for " + "details",
                GPU_ERROR
            )
            Log.e(
                TAG,
                "MP Task Vision failed to load the task with error: " + e.message
            )
        }
    }

    fun recognizeLiveStream(
        imageProxy: ImageProxy,
    ) {
        val frameTime = SystemClock.uptimeMillis()

        val bitmapBuffer = Bitmap.createBitmap(
            imageProxy.width, imageProxy.height, Bitmap.Config.ARGB_8888
        )
        imageProxy.use { bitmapBuffer.copyPixelsFromBuffer(imageProxy.planes[0].buffer) }
        imageProxy.close()

        val matrix = Matrix().apply {
            postRotate(imageProxy.imageInfo.rotationDegrees.toFloat())

            postScale(
                -1f, 1f, imageProxy.width.toFloat(), imageProxy.height.toFloat()
            )
        }

        // Rotate bitmap to match what our model expects
        val rotatedBitmap = Bitmap.createBitmap(
            bitmapBuffer,
            0,
            0,
            bitmapBuffer.width,
            bitmapBuffer.height,
            matrix,
            true
        )

        val mpImage = BitmapImageBuilder(rotatedBitmap).build()

        recognizeAsync(mpImage, frameTime)
    }

    @VisibleForTesting
    fun recognizeAsync(mpImage: MPImage, frameTime: Long) {
        gestureRecognizer?.recognizeAsync(mpImage, frameTime)
    }

    fun isClosed(): Boolean {
        return gestureRecognizer == null
    }

    private fun returnLivestreamResult(
        result: GestureRecognizerResult, input: MPImage
    ) {
        val finishTimeMs = SystemClock.uptimeMillis()
        val inferenceTime = finishTimeMs - result.timestampMs()

        gestureRecognizerListener?.onResults(
            ResultBundle(
                listOf(result), inferenceTime, input.height, input.width
            )
        )
    }

    private fun returnLivestreamError(error: RuntimeException) {
        gestureRecognizerListener?.onError(
            error.message ?: "An unknown error has occurred"
        )
    }

    companion object {
        val TAG = "GestureRecognizerHelper ${this.hashCode()}"
        private const val MP_RECOGNIZER_TASK = "sibi2_epoch500_batch_size32.task"

        const val DELEGATE_GPU = 1
        const val SIBI_ALFABET = 0
        const val SIBI_ANGKA = 1
        const val SIBI_KATA = 2
        const val SIBI_KALIMAT = 3
        const val ASL_ALFABET = 8
        const val ASL_ANGKA = 9
        const val ASL_KATA = 10
        const val ASL_KALIMAT = 11
        const val DEFAULT_HAND_DETECTION_CONFIDENCE = 0.5F
        const val DEFAULT_HAND_TRACKING_CONFIDENCE = 0.5F
        const val DEFAULT_HAND_PRESENCE_CONFIDENCE = 0.5F
        const val OTHER_ERROR = 0
        const val GPU_ERROR = 1
    }

    data class ResultBundle(
        val results: List<GestureRecognizerResult>,
        val inferenceTime: Long,
        val inputImageHeight: Int,
        val inputImageWidth: Int,
    )

    interface GestureRecognizerListener {
        fun onError(error: String, errorCode: Int = OTHER_ERROR)
        fun onResults(resultBundle: ResultBundle)
    }
}
