package com.example.medicinereminder.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.medicinereminder.model.repository.ItemRepository
import com.example.medicinereminder.model.roomDB.Item
import com.example.medicinereminder.model.roomDB.ItemDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel classes extend the AndroidViewModel class and provide methods for interacting
 * with the repository to perform database operations.
 * */

class ItemViewModel (application: Application) :AndroidViewModel(application) {

    // creating a variable  all items list and repository
    val allItems : LiveData<List<Item>>
    private val repository : ItemRepository

    // initializing dao, repository and all items
    init {
        val dao = ItemDatabase.getDatabase(application).getItemDao()
        repository = ItemRepository(dao)
        allItems = repository.allItems
    }

    // creating a new method for deleting a item
    fun deleteItem (item: Item) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(item)
    }

    // creating a new method for adding a new item to database
    fun addItem(item: Item) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(item)
    }
}
