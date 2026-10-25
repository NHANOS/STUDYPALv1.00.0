package com.example.chat.data

import com.example.BuildConfig
import com.example.chat.model.ChatMessage
import com.example.chat.model.MessageRole
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

sealed class OpenAiResult<out T> {
    data class Success<out T>(val data: T) : OpenAiResult<T>()
    data class Error(val message: String, val code: Int? = null) : OpenAiResult<Nothing>()
}

class OpenAiService(
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(45, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(45, TimeUnit.SECONDS)
        .build()
) {

    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    // Runtime custom key for testing or overriding if BuildConfig is empty
    var runtimeApiKeyOverride: String? = null

    val effectiveApiKey: String
        get() {
            val runtime = runtimeApiKeyOverride?.trim()
            if (!runtime.isNullOrEmpty()) return runtime

            // Priority 1: Check OPENROUTER_API_KEY from BuildConfig
            try {
                val openRouterKey = BuildConfig.OPENROUTER_API_KEY.trim()
                if (openRouterKey.isNotEmpty() && openRouterKey != "YOUR_OPENROUTER_API_KEY") {
                    return openRouterKey
                }
            } catch (_: Throwable) {
                // Ignore if BuildConfig field not yet refreshed
            }

            // Priority 2: Fallback to OPENAI_API_KEY
            return try {
                val openAiKey = BuildConfig.OPENAI_API_KEY.trim()
                if (openAiKey.isNotEmpty() && openAiKey != "YOUR_OPENAI_API_KEY") openAiKey else ""
            } catch (_: Throwable) {
                ""
            }
        }

    fun hasValidApiKey(): Boolean = effectiveApiKey.isNotEmpty()

    suspend fun sendChatCompletion(
        history: List<ChatMessage>,
        systemPrompt: String = DEFAULT_SYSTEM_PROMPT
    ): OpenAiResult<String> = withContext(Dispatchers.IO) {
        val apiKey = effectiveApiKey
        if (apiKey.isEmpty()) {
            return@withContext OpenAiResult.Error(
                "OpenRouter API Key is missing. Please configure OPENROUTER_API_KEY in the Secrets panel in AI Studio or enter your key in Chat AI settings."
            )
        }

        try {
            val jsonBody = JSONObject().apply {
                put("model", CHAT_MODEL)
                put("temperature", 0.7)

                val messagesArray = JSONArray()
                // System instructions
                messagesArray.put(JSONObject().apply {
                    put("role", "system")
                    put("content", systemPrompt)
                })

                // Recent conversation history (RAM only)
                for (msg in history) {
                    if (msg.isError || msg.isImage) continue
                    val roleStr = when (msg.role) {
                        MessageRole.USER -> "user"
                        MessageRole.ASSISTANT -> "assistant"
                        MessageRole.SYSTEM -> "system"
                    }
                    messagesArray.put(JSONObject().apply {
                        put("role", roleStr)
                        put("content", msg.text)
                    })
                }
                put("messages", messagesArray)
            }

            val request = Request.Builder()
                .url(CHAT_COMPLETIONS_URL)
                .addHeader("Authorization", "Bearer $apiKey")
                .addHeader("HTTP-Referer", HTTP_REFERER)
                .addHeader("X-Title", X_TITLE)
                .addHeader("Content-Type", "application/json")
                .post(jsonBody.toString().toRequestBody(jsonMediaType))
                .build()

            client.newCall(request).execute().use { response ->
                val bodyStr = response.body?.string() ?: ""
                if (!response.isSuccessful) {
                    val errMsg = parseErrorMessage(bodyStr, response.code)
                    return@withContext OpenAiResult.Error(errMsg, response.code)
                }

                val jsonResponse = JSONObject(bodyStr)
                val choices = jsonResponse.getJSONArray("choices")
                if (choices.length() > 0) {
                    val firstChoice = choices.getJSONObject(0)
                    val messageObj = firstChoice.getJSONObject("message")
                    val reply = messageObj.getString("content")
                    return@withContext OpenAiResult.Success(reply.trim())
                } else {
                    return@withContext OpenAiResult.Error("Empty response received from OpenRouter.")
                }
            }
        } catch (e: IOException) {
            return@withContext OpenAiResult.Error("Network error: ${e.localizedMessage ?: "Unable to connect to OpenRouter"}")
        } catch (e: Exception) {
            return@withContext OpenAiResult.Error("Error: ${e.localizedMessage ?: e.javaClass.simpleName}")
        }
    }

    suspend fun generateImage(prompt: String): OpenAiResult<String> = withContext(Dispatchers.IO) {
        val apiKey = effectiveApiKey
        if (apiKey.isEmpty()) {
            return@withContext OpenAiResult.Error(
                "OpenRouter API Key is missing. Please configure OPENROUTER_API_KEY in the Secrets panel in AI Studio or enter your key in Chat AI settings."
            )
        }

        try {
            val jsonBody = JSONObject().apply {
                put("model", IMAGE_MODEL)
                put("prompt", prompt.trim())
            }

            val request = Request.Builder()
                .url(IMAGE_GENERATIONS_URL)
                .addHeader("Authorization", "Bearer $apiKey")
                .addHeader("HTTP-Referer", HTTP_REFERER)
                .addHeader("X-Title", X_TITLE)
                .addHeader("Content-Type", "application/json")
                .post(jsonBody.toString().toRequestBody(jsonMediaType))
                .build()

            client.newCall(request).execute().use { response ->
                val bodyStr = response.body?.string() ?: ""
                if (!response.isSuccessful) {
                    val errMsg = parseErrorMessage(bodyStr, response.code)
                    return@withContext OpenAiResult.Error(errMsg, response.code)
                }

                val jsonResponse = JSONObject(bodyStr)
                if (jsonResponse.has("data")) {
                    val dataArray = jsonResponse.getJSONArray("data")
                    if (dataArray.length() > 0) {
                        val item = dataArray.getJSONObject(0)
                        if (item.has("url")) {
                            return@withContext OpenAiResult.Success(item.getString("url"))
                        } else if (item.has("b64_json")) {
                            val mime = if (item.has("media_type")) item.getString("media_type") else "image/png"
                            val dataUri = "data:$mime;base64,${item.getString("b64_json")}"
                            return@withContext OpenAiResult.Success(dataUri)
                        }
                    }
                }
                return@withContext OpenAiResult.Error("No image data returned from image generation.")
            }
        } catch (e: IOException) {
            return@withContext OpenAiResult.Error("Network error during image generation: ${e.localizedMessage ?: "Connection failed"}")
        } catch (e: Exception) {
            return@withContext OpenAiResult.Error("Image Generation Error: ${e.localizedMessage ?: e.javaClass.simpleName}")
        }
    }

    private fun parseErrorMessage(responseBody: String, statusCode: Int): String {
        return try {
            val json = JSONObject(responseBody)
            if (json.has("error")) {
                val errorVal = json.get("error")
                if (errorVal is JSONObject) {
                    val message = errorVal.optString("message", "")
                    if (message.isNotEmpty()) return message
                } else if (errorVal is String && errorVal.isNotEmpty()) {
                    return errorVal
                }
            }
            when (statusCode) {
                401 -> "Invalid OpenRouter API Key. Please verify your credentials."
                402 -> "Insufficient credits or model access limit reached on OpenRouter."
                429 -> "Rate limit or free quota exceeded on OpenRouter account. Please retry shortly."
                500, 502, 503 -> "OpenRouter servers or upstream providers are temporarily unavailable. Please retry shortly."
                else -> "HTTP $statusCode error from OpenRouter."
            }
        } catch (e: Exception) {
            when (statusCode) {
                401 -> "Invalid OpenRouter API Key (HTTP 401)."
                402 -> "Payment required / credits exhausted (HTTP 402)."
                429 -> "OpenRouter Rate Limit Exceeded (HTTP 429)."
                else -> "Server error ($statusCode)"
            }
        }
    }

    companion object {
        const val BASE_URL = "https://openrouter.ai/api/v1"
        const val CHAT_COMPLETIONS_URL = "$BASE_URL/chat/completions"
        const val IMAGE_GENERATIONS_URL = "$BASE_URL/images"
        const val CHAT_MODEL = "openrouter/free"
        const val IMAGE_MODEL = "bytedance-seed/seedream-4.5"
        const val HTTP_REFERER = "https://studypal.app"
        const val X_TITLE = "STUDYPAL AI Tutor"

        const val DEFAULT_SYSTEM_PROMPT =
            "You are STUDYPAL Chat AI, an expert, enthusiastic, and supportive academic tutor for high school and competitive exam students (Classes 9-12, CBSE, JEE, NEET). " +
            "Provide structured explanations, clear step-by-step problem-solving, intuitive real-world analogies, formatted equations, and clean code blocks where applicable. " +
            "Use Markdown formatting with bolding, lists, and headings for readability. Keep responses focused, encouraging, and accurate."

        fun isImageRequest(input: String): Boolean {
            val text = input.trim().lowercase()
            if (text.startsWith("/image") || text.startsWith("/draw") || text.startsWith("/img")) {
                return true
            }
            val triggers = listOf(
                "generate an image",
                "generate image",
                "draw an image",
                "create an image",
                "create a picture",
                "draw a picture",
                "make an image",
                "paint an image",
                "render an image"
            )
            return triggers.any { text.contains(it) }
        }

        fun extractImagePrompt(input: String): String {
            val trimmed = input.trim()
            for (prefix in listOf("/image", "/draw", "/img")) {
                if (trimmed.lowercase().startsWith(prefix)) {
                    val promptPart = trimmed.substring(prefix.length).trim()
                    if (promptPart.isNotEmpty()) return promptPart
                }
            }
            val triggers = listOf(
                "generate an image of",
                "generate image of",
                "generate an image for",
                "generate image for",
                "draw an image of",
                "draw a picture of",
                "create an image of",
                "create a picture of",
                "make an image of"
            )
            val lower = trimmed.lowercase()
            for (trigger in triggers) {
                val index = lower.indexOf(trigger)
                if (index != -1) {
                    val extracted = trimmed.substring(index + trigger.length).trim()
                    if (extracted.isNotEmpty()) return extracted
                }
            }
            return trimmed
        }
    }
}

typealias OpenRouterService = OpenAiService
typealias OpenRouterResult<T> = OpenAiResult<T>

