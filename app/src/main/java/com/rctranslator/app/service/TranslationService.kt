package com.rctranslator.app.service

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.net.URLEncoder

/**
 * Translation service using LibreTranslate API (free and open-source)
 * Alternative: Can be replaced with Google Translate API, DeepL, or other services
 */
class TranslationService {
    private val client = OkHttpClient()
    
    // Using LibreTranslate public API endpoint
    // Note: For production, consider using your own instance or a paid API service
    private val baseUrl = "https://libretranslate.de/translate"
    
    // Alternative: Use MyMemory Translation API (free tier available)
    // private val baseUrl = "https://api.mymemory.translated.net/get"
    
    suspend fun translate(text: String, sourceLang: String, targetLang: String): String {
        return withContext(Dispatchers.IO) {
            try {
                translateWithLibreTranslate(text, sourceLang, targetLang)
            } catch (e: Exception) {
                Log.e("TranslationService", "LibreTranslate failed, trying MyMemory", e)
                try {
                    translateWithMyMemory(text, sourceLang, targetLang)
                } catch (e2: Exception) {
                    Log.e("TranslationService", "Translation failed", e2)
                    throw e2
                }
            }
        }
    }
    
    private fun translateWithLibreTranslate(text: String, sourceLang: String, targetLang: String): String {
        val requestBody = """
            {
                "q": ${JSONObject.quote(text)},
                "source": "$sourceLang",
                "target": "$targetLang",
                "format": "text"
            }
        """.trimIndent()
        
        val request = Request.Builder()
            .url(baseUrl)
            .post(okhttp3.RequestBody.create(
                okhttp3.MediaType.get("application/json; charset=utf-8"),
                requestBody
            ))
            .addHeader("Content-Type", "application/json")
            .build()
        
        val response = client.newCall(request).execute()
        val responseBody = response.body?.string()
        
        if (response.isSuccessful && responseBody != null) {
            val json = JSONObject(responseBody)
            return json.getString("translatedText")
        } else {
            throw Exception("Translation API error: ${response.code}")
        }
    }
    
    private fun translateWithMyMemory(text: String, sourceLang: String, targetLang: String): String {
        val encodedText = URLEncoder.encode(text, "UTF-8")
        val url = "https://api.mymemory.translated.net/get?q=$encodedText&langpair=$sourceLang|$targetLang"
        
        val request = Request.Builder()
            .url(url)
            .get()
            .build()
        
        val response = client.newCall(request).execute()
        val responseBody = response.body?.string()
        
        if (response.isSuccessful && responseBody != null) {
            val json = JSONObject(responseBody)
            val responseStatus = json.getInt("responseStatus")
            
            if (responseStatus == 200) {
                val responseData = json.getJSONObject("responseData")
                return responseData.getString("translatedText")
            } else {
                throw Exception("MyMemory API error: $responseStatus")
            }
        } else {
            throw Exception("Translation API error: ${response.code}")
        }
    }
}
