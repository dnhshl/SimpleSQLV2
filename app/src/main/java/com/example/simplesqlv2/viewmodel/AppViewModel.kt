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
    val allSensordata: LiveData<List<Sensordata>>
    private var _filteredSensordata = MutableLiveData<List<Sensordata>>()
    private val filteredSensordata: LiveData<List<Sensordata>>
        get() = _filteredSensordata
    private var _sensordata = MutableLiveData<List<Sensordata>>()
    val sensordata: LiveData<List<Sensordata>>
        get() = _sensordata


    //val searchResults: MutableLiveData<List<Sensordata>>

    init {
        val sensordataDb = AppDatabase.getInstance(application)
        dao = sensordataDb!!.getDao()
        allSensordata = dao.getAllSensordata()
        //_sensordata.value = allSensordata.value
    }

    fun insertSensordata(sensordata: Sensordata) {
        viewModelScope.launch {
            dao.insertSensordata(sensordata)
        }
    }

    fun deleteById(idString: String ) {
        val id = idString.toIntOrNull() ?: -1
        viewModelScope.launch {
            dao.deleteById(id)
        }
    }

    fun getSensordataFilterByLocation(location: String) {
        viewModelScope.launch {
            _sensordata.value = dao.getSensordataFilterByLocation(location)
        }
    }

    fun getSensordataFilterByTemperature(tempstring: String) {
        val temp = tempstring.toIntOrNull() ?: -1
        viewModelScope.launch {
            _sensordata.value = dao.getSensordataFilterByTemperature(temp)
        }
    }


    fun getSensordataFilterByDate(datestring: String) {
        val date = if (datestring.isNotEmpty()) datestring else "01.01.2000"
        val beginOfDay = convertDateStringToLong(date + " 00:00")
        val endOfDay = convertDateStringToLong(date + " 23:59")
        viewModelScope.launch {
            _sensordata.value = dao.getSensordataFilterByDate(beginOfDay, endOfDay)
            Log.i(">>>>", sensordata.toString())
        }
    }

    fun getSensordataFilterByHottest(topXstring: String) {
        val topX = topXstring.toIntOrNull() ?: 1
        viewModelScope.launch {
            _sensordata.value = dao.getSensordataFilterByHottest(topX)
        }
    }

    fun resetFilter() {
        _sensordata.value = allSensordata.value
    }
}
