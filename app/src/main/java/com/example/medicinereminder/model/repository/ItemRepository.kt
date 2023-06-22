package com.example.medicinereminder.model.repository

import androidx.lifecycle.LiveData
import com.example.medicinereminder.model.roomDB.Item
import com.example.medicinereminder.model.roomDB.ItemDao

/**
 * Repo classes serve as a bridge between the data source (DAO) and the ViewModel,
 * providing methods for inserting, deleting, and updating data.
 * */

class ItemRepository(private val itemsDao: ItemDao) {

    // creating a variable and getting all the items from DAO class.
    val allItems: LiveData<List<Item>> = itemsDao.getAllItems()

    // creating an insert method for adding the items to database.
    suspend fun insert(item: Item) {
        itemsDao.insertItem(item)
    }

    // delete method for deleting item from database.
    suspend fun delete(item: Item){
        itemsDao.deleteItem(item)
    }

}
