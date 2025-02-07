package com.example.customviewwithoutcompose.data.local.datastore

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoinsDataStore @Inject constructor(
    private val sPreferences: SharedPreferences,
) {

    fun getHidedCoinsIds(): Set<String> =
        sPreferences.getStringSet(HIDED_COINS_IDS, emptySet()) ?: emptySet()

    fun setHidedCoinsIds(ids: Set<String>) =
        sPreferences.edit().putStringSet(HIDED_COINS_IDS, ids).apply()

    companion object {
        const val HIDED_COINS_IDS = "hided_coins_ids"
    }
}