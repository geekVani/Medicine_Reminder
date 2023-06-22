package com.example.medicinereminder.ui

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.medicinereminder.broadcast.ReminderBroadcastReceiver
import com.example.medicinereminder.databinding.ActivityAddItemBinding
import com.example.medicinereminder.model.roomDB.Item
import com.example.medicinereminder.viewmodel.ItemViewModel
import java.text.SimpleDateFormat
import java.util.*

class AddItemActivity : AppCompatActivity() {
    private lateinit var itemBinding: ActivityAddItemBinding
    private lateinit var viewModal: ItemViewModel
    // for notifications
    private lateinit var alarmManager: AlarmManager
    private lateinit var reminderBroadcastReceiver: ReminderBroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        itemBinding = ActivityAddItemBinding.inflate(layoutInflater)
        setContentView(itemBinding.root)

        viewModal = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[ItemViewModel::class.java]
        // for notifications
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        reminderBroadcastReceiver = ReminderBroadcastReceiver()

        setUpDateTimepickers()

        itemBinding.btnSave.setOnClickListener {
            saveItem()
        }
    }

    /**
     * To save item in recycler view on button click
     * */
    private fun saveItem() {
        val medicineName = itemBinding.etMedicineName.text.toString()
        val description =  itemBinding.etDescription.text.toString()
        val fromDate =  itemBinding.etStartDate.text.toString()
        val toDate =  itemBinding.etEndDate.text.toString()
        val doseTime =  itemBinding.etTime.text.toString()

        if (medicineName.isNotEmpty() && description.isNotEmpty() && fromDate.isNotEmpty() && toDate.isNotEmpty() && doseTime.isNotEmpty()) {
            viewModal.addItem(Item(medicineName, description, fromDate, toDate, doseTime))
            val startDate = parseDate(fromDate)
            val endDate = parseDate(toDate)
            scheduleReminders(startDate, endDate, medicineName, description) // passing data to schedule reminder function
            startActivity(Intent(this@AddItemActivity, MainActivity::class.java))
            Toast.makeText(this, "$medicineName Added", Toast.LENGTH_LONG).show()
            finish()
        } else {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_LONG).show()
        }
    }

    private fun parseDate(dateString: String): Date {
        val format = SimpleDateFormat("dd/mm/yyyy", Locale.getDefault())
        return format.parse(dateString) ?: Date()
    }

    /**
     * Setting Calendar view to get date and time picker dialog boxes
     * */
    private fun setUpDateTimepickers() {
        val calendar = Calendar.getInstance()

        itemBinding.etStartDate.setOnClickListener {
            showDatePicker { year, month, day ->
                val formattedDate = formatDate(year, month, day)
                itemBinding.etStartDate.setText(formattedDate)
            }
        }

        itemBinding.etEndDate.setOnClickListener {
            showDatePicker { year, month, day ->
                val formattedDate = formatDate(year, month, day)
                itemBinding.etEndDate.setText(formattedDate)
            }
        }

        itemBinding.etTime.setOnClickListener {
            showTimePicker { hourOfDay, minute ->
                val formattedTime = formatTime(hourOfDay, minute)
                itemBinding.etTime.setText(formattedTime)
            }
        }
    }

    private fun showDatePicker(onDateSet: (year: Int, month: Int, day: Int) -> Unit) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                onDateSet(year, month, dayOfMonth)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun showTimePicker(onTimeSet: (hourOfDay: Int, minute: Int) -> Unit) {
        val calendar = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(
            this,
            { _: TimePicker, hourOfDay: Int, minute: Int ->
                onTimeSet(hourOfDay, minute)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
    }

    private fun formatDate(year: Int, month: Int, day: Int): String {
        val calendar = Calendar.getInstance().apply {
            set(year, month, day)
        }
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return simpleDateFormat.format(calendar.time)
    }

    private fun formatTime(hourOfDay: Int, minute: Int): String {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
        }
        val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return simpleDateFormat.format(calendar.time)
    }

    /**
     * Setting notifications
     * */
    private fun scheduleReminder(calendar: Calendar, hour: Int, minute: Int, medicineName: String, description: String) {
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)

        val reminderIntent = Intent(this, ReminderBroadcastReceiver::class.java).apply {
            putExtra("medicineName", medicineName)
            putExtra("description", description)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            reminderIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }
    private fun scheduleReminders(startDate: Date, endDate: Date, medicineName: String, description: String) {
        val calendar = Calendar.getInstance()
        calendar.time = startDate
        val endCalendar = Calendar.getInstance()
        endCalendar.time = endDate

        while (calendar.before(endCalendar) || calendar == endCalendar) {
            scheduleReminder(calendar.clone() as Calendar, 10, 0, medicineName, description) // Morning reminder at 10:00am
            scheduleReminder(calendar.clone() as Calendar, 15, 0, medicineName, description) // Afternoon reminder at 3:00pm
            scheduleReminder(calendar.clone() as Calendar, 19, 0, medicineName, description) // Evening reminder at 7:00pm

            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
    }
}

