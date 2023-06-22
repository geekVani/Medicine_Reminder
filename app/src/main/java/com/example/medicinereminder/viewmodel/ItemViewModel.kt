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

    // on below line we are creating a variable
    // for our all notes list and repository
    val allItems : LiveData<List<Item>>
    private val repository : ItemRepository

    // on below line we are initializing
    // our dao, repository and all notes
    init {
        val dao = ItemDatabase.getDatabase(application).getItemDao()
        repository = ItemRepository(dao)
        allItems = repository.allItems
    }

    // on below line we are creating a new method for deleting a note. In this we are
    // calling a delete method from our repository to delete our note.
    fun deleteItem (item: Item) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(item)
    }

    // on below line we are creating a new method for adding a new note to our database
    // we are calling a method from our repository to add a new note.
    fun addItem(item: Item) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(item)
    }
}
