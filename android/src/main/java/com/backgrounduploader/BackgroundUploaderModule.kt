package com.backgrounduploader

import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import expo.modules.kotlin.Promise
import expo.modules.kotlin.exception.Exceptions
import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

class BackgroundUploaderModule : Module() {
  override fun definition() = ModuleDefinition {
    Name("BackgroundUploader")

    AsyncFunction("uploadFile") { config: Map<String, Any>, promise: Promise ->
      try {
        val context = appContext.reactContext ?: throw Exception("Context not available")
        
        val uploadWorkRequest = OneTimeWorkRequestBuilder<UploadWorker>()
          .setInputData(workDataOf(
            "url" to config["url"],
            "filePath" to config["filePath"],
            "method" to (config["method"] ?: "POST"),
            "headers" to config["headers"],
            "fields" to config["fields"]
          ))
          .setConstraints(
            Constraints.Builder()
              .setRequiredNetworkType(NetworkType.CONNECTED)
              .build()
          )
          .build()

        WorkManager.getInstance(context).enqueue(uploadWorkRequest)
        
        promise.resolve(mapOf(
          "id" to uploadWorkRequest.id.toString(),
          "status" to "queued"
        ))
      } catch (e: Exception) {
        promise.reject("UPLOAD_ERROR", e.message, e)
      }
    }

    AsyncFunction("cancelUpload") { uploadId: String, promise: Promise ->
      try {
        val context = appContext.reactContext ?: throw Exception("Context not available")
        WorkManager.getInstance(context).cancelWorkById(java.util.UUID.fromString(uploadId))
        promise.resolve(true)
      } catch (e: Exception) {
        promise.reject("CANCEL_ERROR", e.message, e)
      }
    }
  }
}