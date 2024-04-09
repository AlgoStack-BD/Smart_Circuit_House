package com.algostack.smartcircuithouse.services.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.algostack.smartcircuithouse.services.model.RoomData

@Dao
interface RoomDao {
    @Insert
    suspend fun insert(roomData: RoomData)

    @Update
    suspend fun updateRoom(roomData: RoomData)

    @Query("UPDATE rooms SET isBooked = 0 WHERE id = :roomId")
    suspend fun cancelRoomBooking(roomId: Long)

    @Query("SELECT * FROM rooms")
    fun getAllRooms(): LiveData<List<RoomData>>

    @Query("SELECT * FROM rooms WHERE buildingId = :buildingId")
    fun getRoomsForBuilding(buildingId: Int): LiveData<List<RoomData>>

    @Query("SELECT * FROM rooms WHERE bedType = :bedType")
    fun getRoomsByBedType(bedType: String): LiveData<List<RoomData>>


    @Query("SELECT * FROM rooms WHERE isBooked = 1")
    fun getBookedRooms(): LiveData<List<RoomData>>

    @Query("SELECT * FROM rooms WHERE isBooked = 0")
    fun getUnbookedRooms(): LiveData<List<RoomData>>
}

