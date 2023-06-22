package com.example.medicinereminder.model.roomDB

import androidx.room.*


/** ***************************************
 * To  represent the structure of our item data
 * ****************************************/

@Entity(tableName = "medicineTable")
class Item (@ColumnInfo(name = "name")
            val medicineName: String,
            @ColumnInfo(name = "description")
            val pillDescription: String,
            @ColumnInfo(name = "start")
            val fromDate: String,
            @ColumnInfo(name = "end")
            val toDate: String,
            @ColumnInfo(name = "reminder")
            val time: String
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

