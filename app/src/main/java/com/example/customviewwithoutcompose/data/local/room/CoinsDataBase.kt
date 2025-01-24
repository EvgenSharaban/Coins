package com.example.customviewwithoutcompose.data.local.room

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import com.example.customviewwithoutcompose.data.local.room.entities.CoinRoomEntity
import com.example.customviewwithoutcompose.data.local.room.entities.NoteRoomEntity

@Database(
    entities = [CoinRoomEntity::class, NoteRoomEntity::class],
    autoMigrations = [
        AutoMigration(
            from = 1,
            to = 2,
            spec = CoinsDataBase.MyAutoMigration::class
        )
    ],
    version = 2,
    exportSchema = true
)
abstract class CoinsDataBase : RoomDatabase() {

    class MyAutoMigration : AutoMigrationSpec

    abstract fun coinsDao(): CoinsDao

    companion object {
        private const val DATABASE_NAME = "coins.db"

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