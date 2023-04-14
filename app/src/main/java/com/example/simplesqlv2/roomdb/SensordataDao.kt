package com.example.simplesqlv2.roomdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.Query
import java.time.format.DateTimeFormatter


@Dao
interface SensordataDao {
    //Methode zum Instert eines oder mehrere Weather Records
    @Insert(onConflict = IGNORE)
    suspend fun insertSensordata (vararg sensordata: Sensordata?)

    //Funktion zum Löschen eines Sensordata Records (alle Einträge müssen übereinstimmen)
    @Delete
    suspend fun deleteSensordata(sensordata: Sensordata)

    //Alle Einträge in der Tabelle
    @Query("SELECT * FROM TABLE_SENSORDATA")
    fun getAllSensordata(): LiveData<List<Sensordata>>

    //Lösche spezifischen Record (ID muss passen)
    @Query("DELETE FROM TABLE_SENSORDATA WHERE sensordataId = :value")
    suspend fun deleteById(value: Int)

    //Alle Records für einen bestimmten Ort
    @Query("SELECT * FROM TABLE_SENSORDATA WHERE LOCATION = :value")
    suspend fun getSensordataFilterByLocation(value: String): List<Sensordata>

    //Alle Records mit einer Temperatur größer gleich einem bestimmter Wert
    @Query("SELECT * FROM TABLE_SENSORDATA WHERE TEMPERATURE >= :value")
    suspend fun getSensordataFilterByTemperature(value: Int): List<Sensordata>

    //Alle Records für ein bestimmtes Datum
    @Query("SELECT * FROM TABLE_SENSORDATA WHERE DATE BETWEEN :valueStart AND :valueEnd")
    suspend fun getSensordataFilterByDate(valueStart: Long, valueEnd: Long): List<Sensordata>

    //Die Top X mit den höchsten Temperaturen
    @Query("SELECT * FROM TABLE_SENSORDATA ORDER BY TEMPERATURE DESC LIMIT :value")
    suspend fun getSensordataFilterByHottest(value: Int): List<Sensordata>


/*
    //Alle Records mit einer Temperatur >= valu
    @Query(//Ergänzen Sie)
        fun findTemp(value: Int): List<Sensordata>

        //Alle Records mit spezifischem Datum
        @Query(//Ergänzen Sie)
            fun findDate(value: String): List<Sensordata>

            //Die Top 5 Einträge mit den höchsten Temperaturen
            @Query(//Ergänzen Sie)
                fun findHottest(): List<Sensordata>

                // Anzahl von Messungen pro Raum  (optional)
                @Query("SELECT COUNT(Room) AS mCount, Room AS mRoom FROM TABLE_WEATHER GROUP BY Room")
                fun getRoomCount(): List<RoomCount>
*/
    //Sie können beliebige weitere Queries ergänzen.
}
