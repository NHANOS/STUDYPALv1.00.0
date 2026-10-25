package com.example.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.security.MessageDigest
import java.util.UUID

sealed class AuthState {
    data object Unauthenticated : AuthState()
    data class Authenticated(val username: String) : AuthState()
    data object Guest : AuthState()
}

sealed class AuthResult {
    data class Success(val username: String, val isNewAccount: Boolean) : AuthResult()
    data class Error(val message: String) : AuthResult()
}

class AuthManager private constructor(private val context: Context) {

    private val accountPrefs: SharedPreferences =
        context.getSharedPreferences(PREFS_ACCOUNTS, Context.MODE_PRIVATE)

    private val sessionPrefs: SharedPreferences =
        context.getSharedPreferences(PREFS_SESSION, Context.MODE_PRIVATE)

    private val _authState = MutableStateFlow(restoreSession())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private fun restoreSession(): AuthState {
        val sessionType = sessionPrefs.getString(KEY_SESSION_TYPE, null)
        val username = sessionPrefs.getString(KEY_SESSION_USERNAME, null)

        return when {
            sessionType == SESSION_AUTHENTICATED && !username.isNullOrBlank() -> {
                // Ensure account still exists
                if (accountExists(username)) {
                    AuthState.Authenticated(username)
                } else {
                    AuthState.Unauthenticated
                }
            }
            sessionType == SESSION_GUEST -> AuthState.Guest
            else -> AuthState.Unauthenticated
        }
    }

    fun isLoggedIn(): Boolean {
        return _authState.value !is AuthState.Unauthenticated
    }

    fun isGuest(): Boolean {
        return _authState.value is AuthState.Guest
    }

    fun getCurrentUsername(): String? {
        return when (val state = _authState.value) {
            is AuthState.Authenticated -> state.username
            AuthState.Guest -> "Guest"
            AuthState.Unauthenticated -> null
        }
    }

    /**
     * Checks if a user already exists (case-insensitive key).
     */
    fun accountExists(name: String): Boolean {
        val key = getAccountKey(name)
        return accountPrefs.contains("${key}_hash")
    }

    /**
     * Login or create account with Name and Password.
     * If user does not exist, automatically creates a new account.
     * If user exists, validates password.
     */
    fun loginOrCreateAccount(name: String, password: String): AuthResult {
        val trimmedName = name.trim()
        val trimmedPassword = password.trim()

        if (trimmedName.isEmpty()) {
            return AuthResult.Error("Please enter your name.")
        }
        if (trimmedPassword.isEmpty()) {
            return AuthResult.Error("Please enter your password.")
        }

        val key = getAccountKey(trimmedName)

        return if (accountExists(trimmedName)) {
            // Returning user: verify password
            val storedHash = accountPrefs.getString("${key}_hash", null)
            val storedSalt = accountPrefs.getString("${key}_salt", null)

            if (storedHash != null && storedSalt != null) {
                val computedHash = hashPassword(trimmedPassword, storedSalt)
                if (computedHash == storedHash) {
                    val originalDisplayName = accountPrefs.getString("${key}_name", trimmedName) ?: trimmedName
                    saveSession(SESSION_AUTHENTICATED, originalDisplayName)
                    _authState.value = AuthState.Authenticated(originalDisplayName)

                    // Synchronize profile manager display name
                    ProfileManager.getInstance(context).saveName(originalDisplayName)

                    AuthResult.Success(username = originalDisplayName, isNewAccount = false)
                } else {
                    AuthResult.Error("Incorrect password for '$trimmedName'. Please check your password.")
                }
            } else {
                AuthResult.Error("Account verification error. Please try again.")
            }
        } else {
            // First time login: automatically create the account!
            val salt = UUID.randomUUID().toString()
            val hash = hashPassword(trimmedPassword, salt)

            accountPrefs.edit()
                .putString("${key}_name", trimmedName)
                .putString("${key}_hash", hash)
                .putString("${key}_salt", salt)
                .putLong("${key}_created", System.currentTimeMillis())
                .apply()

            saveSession(SESSION_AUTHENTICATED, trimmedName)
            _authState.value = AuthState.Authenticated(trimmedName)

            // Synchronize profile manager display name
            ProfileManager.getInstance(context).saveName(trimmedName)

            AuthResult.Success(username = trimmedName, isNewAccount = true)
        }
    }

    /**
     * Guest mode login: allows immediate access without credentials.
     * Does NOT overwrite existing registered accounts.
     */
    fun loginAsGuest(): AuthResult {
        saveSession(SESSION_GUEST, "Guest")
        _authState.value = AuthState.Guest

        // Set default profile name to "Guest" if none set
        val profileManager = ProfileManager.getInstance(context)
        if (profileManager.getProfile().name == ProfileManager.DEFAULT_NAME) {
            profileManager.saveName("Guest")
        }

        return AuthResult.Success(username = "Guest", isNewAccount = false)
    }

    /**
     * Logs out the user, clearing the active session.
     * Keeps registered credentials intact.
     */
    fun logout() {
        sessionPrefs.edit().clear().apply()
        _authState.value = AuthState.Unauthenticated
    }

    private fun saveSession(sessionType: String, username: String) {
        sessionPrefs.edit()
            .putString(KEY_SESSION_TYPE, sessionType)
            .putString(KEY_SESSION_USERNAME, username)
            .apply()
    }

    private fun getAccountKey(name: String): String {
        return "acc_" + name.trim().lowercase().replace("\\s+".toRegex(), "_")
    }

    /**
     * Secure SHA-256 password hashing with salt.
     */
    private fun hashPassword(password: String, salt: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val input = "$salt:$password"
        val hashBytes = digest.digest(input.toByteArray(Charsets.UTF_8))
        return hashBytes.joinToString("") { "%02x".format(it) }
    }

    companion object {
        private const val PREFS_ACCOUNTS = "study_x_accounts"
        private const val PREFS_SESSION = "study_x_session"

        private const val KEY_SESSION_TYPE = "session_type"
        private const val KEY_SESSION_USERNAME = "session_username"

        private const val SESSION_AUTHENTICATED = "AUTHENTICATED"
        private const val SESSION_GUEST = "GUEST"

        @Volatile
        private var INSTANCE: AuthManager? = null

        fun getInstance(context: Context): AuthManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: AuthManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}
