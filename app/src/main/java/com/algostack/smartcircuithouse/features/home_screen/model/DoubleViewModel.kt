package com.algostack.smartcircuithouse.features.home_screen.model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.algostack.smartcircuithouse.services.db.RoomDB
import com.algostack.smartcircuithouse.services.db.RoomRepository
import com.algostack.smartcircuithouse.services.model.RoomData

class DoubleViewModel(private val context: Context) : ViewModel() {
    private val roomRepository: RoomRepository
    private val doubleRoomsLiveData: LiveData<List<RoomData>>

    init {
        val roomDao = RoomDB.getDatabase(context).roomDao()
        roomRepository = RoomRepository(roomDao)
        doubleRoomsLiveData = roomRepository.getRoomsByBedType("Double")
    }

    fun getDoubleRooms(): LiveData<List<RoomData>> {
        return doubleRoomsLiveData
    }
}