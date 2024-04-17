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

    suspend fun updateRoomStatus(roomData: RoomData) {
        roomDao.updateRoom(roomData)
    }

    suspend fun updateRoomDetails(roomId: Long, buildingId: Int, buildingName: String, floorNumber: String, roomNumber: String, bedType: String) {
        roomDao.updateRoomDetails(roomId, buildingId, buildingName, floorNumber, roomNumber, bedType)
    }

    suspend fun cancelRoomBooking(roomData: RoomData) {
        roomDao.cancelRoomBooking(roomData.id)
    }

    fun getRoomsByBedType(bedType: String): LiveData<List<RoomData>> {
        return roomDao.getRoomsByBedType(bedType)
    }

    fun getBookedRooms(): LiveData<List<RoomData>> {
        return roomDao.getBookedRooms()
    }

    fun getUnbookedRooms(): LiveData<List<RoomData>> {
        return roomDao.getUnbookedRooms()
    }

    fun getRoomsByCustomerName(customerName: String): LiveData<List<RoomData>> {
        return roomDao.getRoomsByCustomerName(customerName)
    }

    fun getRoomsByCustomerDetails(customerDetails: String): LiveData<List<RoomData>> {
        return roomDao.getRoomsByCustomerDetails(customerDetails)
    }

    fun getRoomsByEntryDate(entryDate: Long): LiveData<List<RoomData>> {
        return roomDao.getRoomsByEntryDate(entryDate)
    }

    fun getRoomsByExitDate(exitDate: Long): LiveData<List<RoomData>> {
        return roomDao.getRoomsByExitDate(exitDate)
    }

    suspend fun deleteRoom(roomData: RoomData) {
        roomDao.delete(roomData)
    }


}
