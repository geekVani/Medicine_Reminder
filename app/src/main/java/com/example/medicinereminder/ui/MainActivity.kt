package com.example.medicinereminder.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medicinereminder.adapter.ItemDeleteInterface
import com.example.medicinereminder.adapter.ItemRVAdapter
import com.example.medicinereminder.broadcast.ReminderBroadcastReceiver
import com.example.medicinereminder.databinding.ActivityMainBinding
import com.example.medicinereminder.model.roomDB.Item
import com.example.medicinereminder.viewmodel.ItemViewModel

class MainActivity : AppCompatActivity(), ItemDeleteInterface {
    lateinit var mainBinding: ActivityMainBinding
    lateinit var viewModal: ItemViewModel
    private lateinit var alarmManager: AlarmManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        // Initializing the view model.
        viewModal = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[ItemViewModel::class.java]

        // initialize alarmManager variable
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Setting layout manager to the recycler view.
        mainBinding.itemsRV.layoutManager = LinearLayoutManager(this)

        // Initializing the adapter for the recycler view.
        val itemRVAdapter = ItemRVAdapter(this)

        // Setting the adapter to the recycler view.
        mainBinding.itemsRV.adapter = itemRVAdapter

        // Observe the allItems LiveData in the ItemViewModel.
        viewModal.allItems.observe(
            this,
        ) { items ->
            itemRVAdapter.updateData(items)
        }

        mainBinding.fabAddPill.setOnClickListener {
            // Opening a new intent to add a new medicine reminder.
            val intent = Intent(this@MainActivity, AddItemActivity::class.java)
            startActivity(intent)
            this.finish()
        }
    }

    override fun onDeleteIconClick(item: Item) {
        // Deleting the medicine reminder using the view model.
        viewModal.deleteItem(item)

        // Cancel the reminders for the deleted item
        cancelReminder()

        // Displaying a toast message.
        Toast.makeText(this, "${item.medicineName} Deleted", Toast.LENGTH_LONG).show()
    }

    private fun cancelReminder() {
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            Intent(this, ReminderBroadcastReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Cancel the reminder using AlarmManager
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }
}
