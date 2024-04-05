package com.algostack.smartcircuithouse.services.db

import androidx.lifecycle.LiveData
import com.algostack.smartcircuithouse.services.model.RoomData

class RoomRepository(private val roomDao: RoomDao) {

    val allRooms: LiveData<List<RoomData>> = roomDao.getAllRooms()

    fun getRoomsForBuilding(buildingId: Int): LiveData<List<RoomData>> {
        return roomDao.getRoomsForBuilding(buildingId)
    }

    suspend fun insert(roomData: RoomData) {
        roomDao.insert(roomData)
    }
}
