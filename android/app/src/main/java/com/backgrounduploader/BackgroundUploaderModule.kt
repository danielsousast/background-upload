package com.backgrounduploader

import android.content.Context
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import androidx.work.*
import java.util.UUID

class BackgroundUploaderModule : Module() {
    override fun definition() = ModuleDefinition {
        Name("BackgroundUploader")

        Function("startUpload") { filePath: String, config: Map<String, Any> ->
            val context = appContext.reactContext ?: throw Exception("No context")
            val data = workDataOf(
                "filePath" to filePath,
                "config" to config.toString()
            )
            val uploadRequest = OneTimeWorkRequestBuilder<UploadWorker>()
                .setInputData(data)
                .addTag("background-upload")
                .build()
            WorkManager.getInstance(context).enqueue(uploadRequest)
            return@Function uploadRequest.id.toString()
        }

        Function("cancelUpload") { uploadId: String ->
            val context = appContext.reactContext ?: throw Exception("No context")
            WorkManager.getInstance(context).cancelWorkById(UUID.fromString(uploadId))
        }
    }
}
