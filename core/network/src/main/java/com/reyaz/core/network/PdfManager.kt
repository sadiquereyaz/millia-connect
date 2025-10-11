package com.reyaz.core.network

import android.content.Context
import android.util.Log
import com.reyaz.core.analytics.AnalyticsTracker
import com.reyaz.core.network.model.DownloadResult
import com.reyaz.core.network.utils.SSLTrustUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

private const val TAG = "PDF_MANAGER"

class PdfManager(
    private val context: Context,
    private val analyticsTracker: AnalyticsTracker
) {

    fun downloadPdf(url: String, fileName: String): Flow<DownloadResult> = flow {
        analyticsTracker.logEvent(
            "pdf_download_started",
            mapOf(
                "url" to url,
                "file_name" to fileName
            )
        )
//        Log.d(TAG, "connection opening...")
        SSLTrustUtils.trustAllHosts()
        var connection: HttpURLConnection? = null
        try {
            /*val urlConnection = URL(url)
            connection = urlConnection.openConnection() as HttpURLConnection
            connection.instanceFollowRedirects = false
            connection.connect()

            if (connection.responseCode != HttpURLConnection.HTTP_OK) {
//                throw IOException("Server returned HTTP ${connection.responseCode}")
            }*/
            connection = followRedirects(url)

            val fileSize = connection.contentLength
//            Log.d(TAG, "File size: $fileSize")
            if (fileSize < 0) emit(DownloadResult.Progress(null))

            val input = BufferedInputStream(connection.inputStream)
            val file = File(context.filesDir, sanitizeFileName(fileName))

            if (file.exists()) {
                analyticsTracker.logEvent(
                    "pdf_download_skipped",
                    mapOf(
                        "url" to url,
                        "file_name" to fileName,
                        "message" to "File already exists. Skipping download."
                    )
                )
                Log.d(TAG, "File '$fileName' already exists. Attempting to replace.")
                if (!file.delete()) {
                    // If deletion fails, it's a critical error as we can't replace the file.
                    val errorMessage = "Failed to delete existing file: $fileName"
                    Log.e(TAG, errorMessage)
                    emit(DownloadResult.Error(IOException(errorMessage)))
                } else {
                    Log.d(TAG, "Existing file '$fileName' successfully deleted.")
                }
            }
            val output = FileOutputStream(file)
            val data = ByteArray(8192)
            var total: Long = 0
            var count: Int
            var lastProgress = 0

            while (input.read(data).also { count = it } != -1) {
                total += count
                output.write(data, 0, count)

                // Calculate progress based on total bytes read if fileSize is valid
                if (fileSize > 0) { // Avoid division by zero if fileSize is unknown/invalid
                    val progress = ((total * 100) / fileSize).toInt()
                    if (progress != lastProgress) {
                        emit(DownloadResult.Progress(progress))
                        lastProgress = progress
                    }
                }
            }
            output.flush()
            output.close()
            input.close()
//            Log.d(TAG, "File '$fileName' downloaded successfully.")
//            Log.d(TAG, "Downloaded file size: $total bytes")
            emit(DownloadResult.Success(file.absolutePath))
            analyticsTracker.logEvent(
                "pdf_download_success",
                mapOf(
                    "url" to url,
                    "file_name" to fileName,
                    "file_path" to file.absolutePath,
                    "file_size" to total
                )
            )
        } catch (e: Exception) {
            Log.d(TAG, "Error: $e")
            emit(DownloadResult.Error(e))
            analyticsTracker.logEvent(
                "pdf_download_failed",
                mapOf(
                    "url" to url,
                    "file_name" to fileName,
                    "error_message" to (e.message ?: "unknown")
                )
            )
        } finally {
            connection?.disconnect()
        }
//        }
    }.flowOn(Dispatchers.IO)

    private fun followRedirects(originalUrl: String): HttpURLConnection {
        var url = URL(originalUrl)
        var connection = url.openConnection() as HttpURLConnection
        connection.instanceFollowRedirects = false

        while (true) {
            connection.connect()
            val responseCode = connection.responseCode
            if (responseCode in 300..399) {
//                Log.d(TAG, "Response code: $responseCode")
//                Log.d(TAG, "Redirecting to: ${connection.getHeaderField("Location")}")
                val newLocation = connection.getHeaderField("Location") ?: break
                connection.disconnect()
                url = URL(newLocation)
                connection = url.openConnection() as HttpURLConnection
                connection.instanceFollowRedirects = false
            } else {
                Log.d(TAG, "Redirects completed")
                break
            }
        }

        return connection
    }

    suspend fun deleteFile(path: String) = withContext(Dispatchers.IO) {
        val file = File(path)
        if (file.exists()) {
            file.delete()
            Log.d(TAG, "File deleted")

            analyticsTracker.logEvent(
                "pdf_file_deleted",
                mapOf("file_path" to path)
            )
        } else {
            Log.d(TAG, "File not found")
            analyticsTracker.logEvent(
                "pdf_file_delete_failed",
                mapOf("file_path" to path, "error_message" to "file not found")
            )
        }
    }


    private fun sanitizeFileName(name: String): String {
        // Remove illegal characters: anything other than a-zA-Z0-9, dot, hyphen, underscore
        val cleanedName = name.replace(Regex("[^a-zA-Z0-9._-]"), "_")

        // Ensure it ends with .pdf
        return if (cleanedName.endsWith(".pdf", ignoreCase = true)) {
            cleanedName
        } else {
            "$cleanedName.pdf"
        }
    }

}
