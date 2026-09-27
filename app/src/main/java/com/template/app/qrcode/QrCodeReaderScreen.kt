package com.template.app.qrcode

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import java.util.concurrent.Executors

/**
 * Top-level composable for QR code scanning with spotlight overlay and confirmation dialog.
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun QrCodeReaderScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    debounceMs: Long = 2_000L,
) {
    val context = LocalContext.current
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    var detectedUrl by remember { mutableStateOf<String?>(null) }
    var isScanningPaused by remember { mutableStateOf(false) }

    // Request permission on first composition.
    LaunchedEffect(Unit) {
        if (!cameraPermissionState.status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        when {
            cameraPermissionState.status.isGranted -> {
                CameraContent(
                    onBarcodeDetected = { value ->
                        if (!isScanningPaused) {
                            detectedUrl = value
                            isScanningPaused = true
                        }
                    },
                    isScanningPaused = isScanningPaused,
                    debounceMs = debounceMs,
                    modifier = Modifier.fillMaxSize(),
                )

                // Spotlight Overlay
                SpotlightOverlay()
            }

            cameraPermissionState.status.shouldShowRationale -> {
                PermissionRationaleContent(
                    onRequestPermission = { cameraPermissionState.launchPermissionRequest() },
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(24.dp),
                )
            }

            else -> {
                PermissionPermanentlyDeniedContent(
                    onOpenSettings = {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        }
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(24.dp),
                )
            }
        }

        // Confirmation Dialog
        detectedUrl?.let { url ->
            AlertDialog(
                onDismissRequest = {
                    detectedUrl = null
                    isScanningPaused = false
                },
                title = { Text(text = "Confirmação") },
                text = { Text(text = "Você deseja ir para '$url'?") },
                confirmButton = {
                    TextButton(onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                        detectedUrl = null
                        onNavigateBack()
                    }) {
                        Text("SIM")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        detectedUrl = null
                        onNavigateBack()
                    }) {
                        Text("NÃO")
                    }
                }
            )
        }
    }
}

@Composable
private fun SpotlightOverlay() {
    val density = LocalDensity.current
    val frameSize = 280.dp
    val frameSizePx = with(density) { frameSize.toPx() }
    val cornerRadius = 16.dp
    val cornerRadiusPx = with(density) { cornerRadius.toPx() }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val left = (canvasWidth - frameSizePx) / 2
        val top = (canvasHeight - frameSizePx) / 2
        val rect = Rect(left, top, left + frameSizePx, top + frameSizePx)

        val path = Path().apply {
            addRoundRect(
                RoundRect(
                    rect = rect,
                    cornerRadius = CornerRadius(cornerRadiusPx)
                )
            )
        }

        // Draw the semi-transparent background everywhere EXCEPT the spotlight rect
        clipPath(path, clipOp = ClipOp.Difference) {
            drawRect(Color.Black.copy(alpha = 0.5f))
        }

        // Draw the white border for the spotlight
        drawRoundRect(
            color = Color.White,
            topLeft = Offset(left, top),
            size = Size(frameSizePx, frameSizePx),
            cornerRadius = CornerRadius(cornerRadiusPx),
            style = Stroke(width = 2.dp.toPx())
        )
    }
}

@Composable
private fun CameraContent(
    onBarcodeDetected: (String) -> Unit,
    isScanningPaused: Boolean,
    debounceMs: Long,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }
    val preview = remember { Preview.Builder().build() }
    val analysisExecutor = remember { Executors.newSingleThreadExecutor() }

    LaunchedEffect(Unit) {
        val providerFuture = ProcessCameraProvider.getInstance(context)
        providerFuture.addListener(
            {
                cameraProvider = providerFuture.get()
            },
            ContextCompat.getMainExecutor(context),
        )
    }

    DisposableEffect(cameraProvider, isScanningPaused) {
        val provider = cameraProvider ?: return@DisposableEffect onDispose {}

        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also { analysis ->
                if (!isScanningPaused) {
                    analysis.setAnalyzer(
                        analysisExecutor,
                        BarcodeAnalyzer(
                            debounceMs = debounceMs,
                            onBarcodeDetected = onBarcodeDetected,
                        ),
                    )
                }
            }

        try {
            provider.unbindAll()
            provider.bindToLifecycle(
                lifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                imageAnalysis,
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }

        onDispose {
            provider.unbindAll()
            analysisExecutor.shutdown()
        }
    }

    cameraProvider?.let {
        CameraPreview(
            cameraProvider = it,
            preview = preview,
            modifier = modifier,
        )
    }
}

@Composable
private fun PermissionRationaleContent(
    onRequestPermission: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Camera access is required to scan QR codes.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRequestPermission) {
            Text("Grant Permission")
        }
    }
}

@Composable
private fun PermissionPermanentlyDeniedContent(
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Camera permission was denied. Please enable it in app settings to use the scanner.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onOpenSettings) {
            Text("Open Settings")
        }
    }
}
