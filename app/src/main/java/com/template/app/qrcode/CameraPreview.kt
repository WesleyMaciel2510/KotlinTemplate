package com.template.app.qrcode

import android.view.ViewGroup
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner

/**
 * Internal composable that renders a live CameraX [PreviewView] inside an [AndroidView].
 *
 * Binding of the [Preview] use case to the [ProcessCameraProvider] is handled by the caller
 * ([QrCodeReaderScreen]) which passes in the already-obtained [cameraProvider] and [preview].
 * This composable is responsible only for surface attachment.
 *
 * @param cameraProvider The resolved [ProcessCameraProvider].
 * @param preview        The [Preview] use case to attach to this surface.
 * @param modifier       Applied to the [AndroidView].
 */
@Composable
internal fun CameraPreview(
    cameraProvider: ProcessCameraProvider,
    preview: Preview,
    modifier: Modifier = Modifier,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val previewView = remember {
        PreviewView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
            scaleType = PreviewView.ScaleType.FILL_CENTER
            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
        }
    }

    AndroidView(
        factory = { previewView },
        modifier = modifier,
        update = {
            preview.surfaceProvider = previewView.surfaceProvider
        },
    )
}
