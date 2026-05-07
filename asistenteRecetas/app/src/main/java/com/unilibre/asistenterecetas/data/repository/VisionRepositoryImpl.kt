package com.unilibre.asistenterecetas.data.repository

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.unilibre.asistenterecetas.domain.repository.VisionRepository
import javax.inject.Inject

class VisionRepositoryImpl @Inject constructor() : VisionRepository {
    
    private val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

    @OptIn(ExperimentalGetImage::class)
    override fun analizarIngredientes(imageProxy: ImageProxy, onResult: (List<String>) -> Unit) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            
            labeler.process(image)
                .addOnSuccessListener { labels ->
                    val result = labels.map { it.text }
                    onResult(result)
                }
                .addOnFailureListener {
                    onResult(emptyList())
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }
}
