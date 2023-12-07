package io.soft.imagenee.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "email")

class LocalStorage @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend fun clear() {
        context.dataStore.edit {
            it.clear()
        }
    }

    suspend fun put(email: String) {
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey("email")] = email
        }
    }

    fun get(): Boolean {
        context.dataStore.data.map {
            it[stringPreferencesKey("email")]
        }
        return true
    }

    val getEmail = context.dataStore.data.map {
        it[stringPreferencesKey("email")]
    }
}