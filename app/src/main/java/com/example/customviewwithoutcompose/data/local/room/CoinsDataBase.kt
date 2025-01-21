package com.example.customviewwithoutcompose.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.customviewwithoutcompose.data.local.room.entities.CoinRoomEntity

@Database(
    entities = [CoinRoomEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CoinsDataBase : RoomDatabase() {

    abstract fun coinsDao(): CoinsDao

    companion object {
        private const val DATABASE_NAME = "coins_db"

        @Volatile
        private var INSTANCE: CoinsDataBase? = null

        fun getDataBase(context: Context): CoinsDataBase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDataBase(context).also { dataBase ->
                    INSTANCE = dataBase
                }
            }
        }

        private fun buildDataBase(context: Context): CoinsDataBase {
            return Room.databaseBuilder(
                context.applicationContext,
                CoinsDataBase::class.java,
                DATABASE_NAME
            )
                .build()
        }

    }
}