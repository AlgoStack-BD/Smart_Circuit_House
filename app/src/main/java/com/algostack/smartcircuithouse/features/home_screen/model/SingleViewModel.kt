package com.algostack.smartcircuithouse.features.home_screen.model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.algostack.smartcircuithouse.services.db.RoomDB
import com.algostack.smartcircuithouse.services.db.RoomRepository
import com.algostack.smartcircuithouse.services.model.RoomData

class SingleViewModel(private val context: Context) : ViewModel() {
    private val roomRepository: RoomRepository
    private val singleRoomsLiveData: LiveData<List<RoomData>>

    init {
        val roomDao = RoomDB.getDatabase(context).roomDao()
        roomRepository = RoomRepository(roomDao)
        singleRoomsLiveData = roomRepository.getRoomsByBedType("Single")
    }

    fun getSingleRooms(): LiveData<List<RoomData>> {
        return singleRoomsLiveData
    }
}
