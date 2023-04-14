package com.example.simplesqlv2.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.simplesqlv2.roomdb.AppDatabase
import com.example.simplesqlv2.roomdb.Sensordata
import com.example.simplesqlv2.roomdb.SensordataDao
import com.example.simplesqlv2.roomdb.convertDateStringToLong
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val dao: SensordataDao

    // Alle Daten in der Datenbank
    val allSensordata: LiveData<List<Sensordata>>

    // Daten als Ergebnis einer Abfrage mit Filter
    private var _sensordata = MutableLiveData<List<Sensordata>>()
    val sensordata: LiveData<List<Sensordata>>
        get() = _sensordata

    init {
        // Datenbank initialisieren
        val sensordataDb = AppDatabase.getInstance(application)
        // DAO zum Zugriff auf die DB
        dao = sensordataDb!!.getDao()
        // allSensordata als LiveData über DAO Funktion abrufen
        allSensordata = dao.getAllSensordata()
    }

    // Hier folgen nun die Coroutine Funktionen, um
    // Daten in die DB zu schreiben, zu Löschen oder gefiltert abzurufen
    // Diese Funktionen können vom UI aufgerufen werden.
    // Sie greifen selbst wiederum auf DAO Funktionen zurück.
    // In den Funktionen ist ein einfaches Fehlerhandling integriert.
    // Fehlen Inputdaten, werden default Werte verwendet.

    // Einen Datensatz einfügen
    fun insertSensordata(sensordata: Sensordata) {
        viewModelScope.launch {
            dao.insertSensordata(sensordata)
        }
    }

    // Einen Datensatz anhand seiner ID löschen
    fun deleteById(idString: String ) {
        val id = idString.toIntOrNull() ?: -1
        viewModelScope.launch {
            dao.deleteById(id)
        }
    }

    // Abfrage mit Filterung nach Ort
    fun getSensordataFilterByLocation(location: String) {
        viewModelScope.launch {
            _sensordata.value = dao.getSensordataFilterByLocation(location)
        }
    }

    // Abfrage mit Filterung nach Temperatur (alle Einträge mit einer Temp größer gleich)
    fun getSensordataFilterByTemperature(tempstring: String) {
        val temp = tempstring.toIntOrNull() ?: -1
        viewModelScope.launch {
            _sensordata.value = dao.getSensordataFilterByTemperature(temp)
        }
    }


    // Abfrage nach Datum. Alle Eintrage von einem bestimmten Tag
    // In der DB sind die Daten mit Zeiten hinterlegt
    // Wir müssen also auf alle Zeitstempel zwischen 00:00 und 23:59
    // an dem jeweiligen Datum filtern
    fun getSensordataFilterByDate(datestring: String) {
        val date = if (datestring.isNotEmpty()) datestring else "01.01.2000"
        val beginOfDay = convertDateStringToLong(date + " 00:00")
        val endOfDay = convertDateStringToLong(date + " 23:59")
        viewModelScope.launch {
            _sensordata.value = dao.getSensordataFilterByDate(beginOfDay, endOfDay)
            Log.i(">>>>", sensordata.toString())
        }
    }

    // Abfrage der Einträge mit den X größten Temperaturwerten
    // Parameter ist die Anzahl X der Einträge, die wir zur Anzeige
    // bringen wollen.
    // Das Sortieren der Daten und das Limitieren auf X Werte erfolgt
    // über die SQL Abfrage (siehe SensordataDao)
    fun getSensordataFilterByHottest(topXstring: String) {
        val topX = topXstring.toIntOrNull() ?: 1
        viewModelScope.launch {
            _sensordata.value = dao.getSensordataFilterByHottest(topX)
        }
    }
}
