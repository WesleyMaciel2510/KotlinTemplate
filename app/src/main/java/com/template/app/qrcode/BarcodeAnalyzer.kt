package com.template.app.qrcode

import android.os.SystemClock
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

/**
 * CameraX [ImageAnalysis.Analyzer] that detects barcodes and QR codes using ML Kit.
 *
 * @param debounceMs   Minimum milliseconds between callbacks for the same barcode value (default 2000).
 * @param onBarcodeDetected Invoked on the main thread with the raw barcode string when a new
 *                          barcode is detected and the debounce window has expired.
 */
class BarcodeAnalyzer(
    private val debounceMs: Long = 2_000L,
    private val onBarcodeDetected: (String) -> Unit,
) : ImageAnalysis.Analyzer {

    // ML Kit scanner configured with QR_CODE format only as per spec.
    private val scanner = BarcodeScanning.getClient(
        BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()
    )

    private val mainScope = MainScope()

    // Debounce state: last value → timestamp (elapsed realtime ms)
    private var lastValue: String? = null
    private var lastTimestampMs: Long = 0L

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage == null) {
            imageProxy.close()
            return
        }

        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                val raw = barcodes.firstOrNull()?.rawValue ?: return@addOnSuccessListener
                val now = SystemClock.elapsedRealtime()
                if (raw == lastValue && now - lastTimestampMs < debounceMs) return@addOnSuccessListener
                lastValue = raw
                lastTimestampMs = now
                mainScope.launch { onBarcodeDetected(raw) }
            }
            .addOnCompleteListener {
                // Always close the proxy to unblock the ImageAnalysis pipeline.
                imageProxy.close()
            }
    }
}
