package com.example.medicinereminder.model.roomDB

import androidx.lifecycle.LiveData
import androidx.room.*

/** *******************************************************
 * Data Access Objects, refers to the file that houses the
 * code responsible for interacting with the database to
 * perform actions such as inserting, reading, updating,
 * and deleting data
 * *******************************************************/

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertItem(item: Item)
    @Delete
    suspend fun deleteItem(item: Item)
    @Query("SELECT * FROM medicineTable ORDER BY id ASC")
    fun getAllItems(): LiveData<List<Item>>

}
