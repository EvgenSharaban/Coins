package com.example.customviewwithoutcompose.data.local.datastore

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoinsDataStore @Inject constructor(
    private val sPreferences: SharedPreferences,
) {

    fun getHidedCoinsIds(): Set<String> =
        sPreferences.getStringSet(KEY_HIDDEN_COINS_IDS, emptySet()) ?: emptySet()

    fun addHidedCoinId(id: String) {
        val ids = getHidedCoinsIds() + id
        sPreferences.edit {
            putStringSet(KEY_HIDDEN_COINS_IDS, ids)
        }
    }

    fun getHiddenCoinsFlow(): Flow<Set<String>> {
        return callbackFlow {
            val listener = SharedPreferences.OnSharedPreferenceChangeListener { shared, key ->
                if (key == KEY_HIDDEN_COINS_IDS) {
                    trySend(getHidedCoinsIds())
                }
            }
            sPreferences.registerOnSharedPreferenceChangeListener(listener)
            trySend(getHidedCoinsIds())
            awaitClose {
                sPreferences.unregisterOnSharedPreferenceChangeListener(listener)
            }
        }
    }

    companion object {
        private const val KEY_HIDDEN_COINS_IDS = "hidden_coins_ids"
    }
}