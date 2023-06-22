package com.example.medicinereminder.model.roomDB

import android.content.Context
import androidx.room.*

/** ******************************************************
 * Room is basically a database layer on top of the SQLite database.
 * Room takes care of mundane tasks that you used to handle with an SQLite Open Helper.
 * Room uses the DAO to issue queries to its database.
 * Room provides compile-time checks of SQLite statements.
 **********************************************************/

// exportScheme set to true to be able to use automated migration in the future updates.
@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class ItemDatabase : RoomDatabase() {

    abstract fun getItemDao(): ItemDao

    companion object {
        // Singleton prevents multiple instances of the database opening at the same time.
        @Volatile
        private var INSTANCE: ItemDatabase? = null

        fun getDatabase(context: Context): ItemDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ItemDatabase::class.java,
                    "medicine_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
