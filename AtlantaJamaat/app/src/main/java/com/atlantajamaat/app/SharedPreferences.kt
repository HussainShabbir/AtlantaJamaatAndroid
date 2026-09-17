package com.atlantajamaat.app

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SharedPrefManager private constructor(context: Context) {

    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "user_prefs"

        // Keys
        const val KEY_ITS_ID = "ITS_ID"
        const val KEY_IS_LOGGED_IN = "IS_LOGGED_IN"
        const val KEY_USER_NAME = "USER_NAME"
        const val TOKEN = "TOKEN"

        @Volatile
        private var INSTANCE: SharedPrefManager? = null

        fun getInstance(context: Context): SharedPrefManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SharedPrefManager(context).also { INSTANCE = it }
            }
        }
    }

    // 🔹 Generic Save Method
    @Suppress("UNCHECKED_CAST")
    fun <T> save(key: String, value: T) {
        prefs.edit {
            when (value) {
                is String -> putString(key, value)
                is Int -> putInt(key, value)
                is Boolean -> putBoolean(key, value)
                is Float -> putFloat(key, value)
                is Long -> putLong(key, value)
                else -> throw IllegalArgumentException("This type cannot be saved to SharedPreferences")
            }
        }
    }

    // 🔹 Generic Get Method
    @Suppress("UNCHECKED_CAST")
    fun <T> get(key: String, defaultValue: T): T {
        return when (defaultValue) {
            is String -> (prefs.getString(key, defaultValue) ?: defaultValue) as T
            is Int -> prefs.getInt(key, defaultValue) as T
            is Boolean -> prefs.getBoolean(key, defaultValue) as T
            is Float -> prefs.getFloat(key, defaultValue) as T
            is Long -> prefs.getLong(key, defaultValue) as T
            else -> defaultValue
        }
    }

    // 🔹 Specific Helper Methods for ITS ID
    fun saveItsId(itsId: String) {
        save(KEY_ITS_ID, itsId)
    }

    fun getItsId(): String? {
        val id = get(KEY_ITS_ID, "")
        return if (id.isEmpty()) null else id
    }

    // 🔹 Clear Particular Key or All Data
    fun remove(key: String) {
        prefs.edit { remove(key) }
    }

    fun clearAll() {
        prefs.edit { clear() }
    }
}