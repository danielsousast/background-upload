package com.backgrounduploader

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.Data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okio.source
import java.io.File

class UploadWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val filePath = inputData.getString("filePath") ?: return@withContext Result.failure()
        val configStr = inputData.getString("config") ?: return@withContext Result.failure()
        val file = File(filePath)
        if (!file.exists()) return@withContext Result.failure()

        // Parse config (simplificado)
        val url = Regex("url=([^,}]+)").find(configStr)?.groupValues?.getOrNull(1) ?: return@withContext Result.failure()
        val fieldName = Regex("fieldName=([^,}]+)").find(configStr)?.groupValues?.getOrNull(1) ?: "file"
        val headers = mutableMapOf<String, String>()
        Regex("headers=\{([^}]*)}").find(configStr)?.groupValues?.getOrNull(1)?.split(",")?.forEach {
            val (k, v) = it.split("=").map { s -> s.trim() }
            if (k.isNotEmpty()) headers[k] = v
        }

        NotificationHelper.createNotificationChannel(applicationContext)
        val notification = NotificationHelper.buildNotification(
            applicationContext,
            "Upload em background",
            "Enviando ${file.name}..."
        )
        setForeground(createForegroundInfo(0, notification))

        val client = OkHttpClient()
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart(fieldName, file.name, file.asRequestBody("application/octet-stream".toMediaTypeOrNull()))
            .build()
        val reqBuilder = Request.Builder().url(url).post(requestBody)
        headers.forEach { (k, v) -> reqBuilder.addHeader(k, v) }
        val request = reqBuilder.build()
        try {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                setProgress(Data.Builder().putInt("progress", 100).build())
                return@withContext Result.success()
            }
            return@withContext Result.failure()
        } catch (e: Exception) {
            return@withContext Result.retry()
        }
    }
}
