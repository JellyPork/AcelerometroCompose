package com.argent.acelerometrocompose.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.argent.acelerometrocompose.ktor.dto.UserResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoreData(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("storeData")
        val USERDATA_ID = stringPreferencesKey("user_data_id")
        val USERDATA_NAME = stringPreferencesKey("user_data_name")
        val USERDATA_EMAIL = stringPreferencesKey("user_data_email")
//        val CLIENTDATA_ID = intPreferencesKey("client_data_id")
//        val PROVIDER_ID = intPreferencesKey("provider_data_id")
//        val SKIPPED_ONBOARDING = booleanPreferencesKey("skipped_onboarding")
//        val AUTH_TOKEN = stringPreferencesKey("auth_token")

    }

    suspend fun saveUserId(userResponse: String){
        context.dataStore.edit { preferences ->
            if (userResponse != null) {
                preferences[USERDATA_ID] = userResponse
            }
        }
    }
    val getUserId: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[USERDATA_ID] ?: ""
    }

    suspend fun saveUserName(userResponse: String){
        context.dataStore.edit { preferences ->
            if (userResponse != null) {
                preferences[USERDATA_NAME] = userResponse
            }
        }
    }
    val getUserName: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[USERDATA_NAME] ?: ""
    }

    suspend fun saveUserEmail(userResponse: String){
        context.dataStore.edit { preferences ->
            if (userResponse != null) {
                preferences[USERDATA_EMAIL] = userResponse
            }
        }
    }
    val getUserEmail: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[USERDATA_EMAIL] ?: ""
    }




}

//Saving value example
//    context.dataStore.edit { prefs -> prefs[Keys.SKIPPED_ONBOARDING] = skipped_onboarding }
//Reading saved value example
//    val hideVisited = preferences[Keys.HIDE_VISITED] ?: false
//https://stackoverflow.com/questions/64913836/android-datastore-calling-context-createdatastore-from-java
//https://issuetracker.google.com/issues/173726702?pli=1
//https://dev.to/thecoder93/if-use-jetpack-compose-dont-use-shared-preference-p8p
