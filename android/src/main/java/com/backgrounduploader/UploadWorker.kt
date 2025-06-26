package com.backgrounduploader

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.Data
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.IOException
import org.json.JSONObject

class UploadWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        val url = inputData.getString("url") ?: return Result.failure()
        val filePath = inputData.getString("filePath") ?: return Result.failure()
        val method = inputData.getString("method") ?: "POST"
        val headersJson = inputData.getString("headers")
        val fieldsJson = inputData.getString("fields")

        return try {
            val file = File(filePath)
            if (!file.exists()) {
                return Result.failure(Data.Builder().putString("error", "File not found").build())
            }

            val client = OkHttpClient.Builder()
                .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                .build()

            val requestBuilder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)

            // Add form fields if provided
            fieldsJson?.let { json ->
                val fields = JSONObject(json)
                fields.keys().forEach { key ->
                    requestBuilder.addFormDataPart(key, fields.getString(key))
                }
            }

            // Add file
            val mediaType = when (file.extension.lowercase()) {
                "jpg", "jpeg" -> "image/jpeg".toMediaType()
                "png" -> "image/png".toMediaType()
                "mp4" -> "video/mp4".toMediaType()
                "mov" -> "video/quicktime".toMediaType()
                else -> "application/octet-stream".toMediaType()
            }

            requestBuilder.addFormDataPart(
                "file",
                file.name,
                file.asRequestBody(mediaType)
            )

            val requestBody = requestBuilder.build()
            val httpRequestBuilder = Request.Builder()
                .url(url)
                .method(method, requestBody)

            // Add headers if provided
            headersJson?.let { json ->
                val headers = JSONObject(json)
                headers.keys().forEach { key ->
                    httpRequestBuilder.addHeader(key, headers.getString(key))
                }
            }

            val request = httpRequestBuilder.build()
            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                NotificationHelper.showUploadSuccess(applicationContext, file.name)
                Result.success(Data.Builder()
                    .putString("response", response.body?.string())
                    .putInt("statusCode", response.code)
                    .build())
            } else {
                NotificationHelper.showUploadError(applicationContext, file.name, "HTTP ${response.code}")
                Result.failure(Data.Builder()
                    .putString("error", "HTTP ${response.code}: ${response.message}")
                    .putInt("statusCode", response.code)
                    .build())
            }
        } catch (e: IOException) {
            NotificationHelper.showUploadError(applicationContext, File(filePath).name, e.message ?: "Network error")
            Result.failure(Data.Builder().putString("error", e.message).build())
        } catch (e: Exception) {
            NotificationHelper.showUploadError(applicationContext, File(filePath).name, e.message ?: "Unknown error")
            Result.failure(Data.Builder().putString("error", e.message).build())
        }
    }
}