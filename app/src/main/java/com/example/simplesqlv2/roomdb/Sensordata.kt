package com.example.simplesqlv2.roomdb

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "TABLE_SENSORDATA")
data class Sensordata (
    @NonNull @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "sensordataId") var id: Int? = null,

    @ColumnInfo(name = "location") var location: String,

    @ColumnInfo(name = "temperature") var temp: Int,

    @ColumnInfo(name = "humidity") var hum: Int,

    @ColumnInfo(name = "date") var date: Long
) {
    override fun toString(): String {
        val datestring = convertLongToDateString(date)
        val value = "$id: $location T: $temp °C, H: $hum %, $datestring"
        return value
    }
}

