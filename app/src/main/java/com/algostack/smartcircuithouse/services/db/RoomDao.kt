package com.algostack.smartcircuithouse.services.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.algostack.smartcircuithouse.services.model.RoomData

@Dao
interface RoomDao {
    @Insert
    suspend fun insert(roomData: RoomData)

    @Query("SELECT * FROM rooms")
    fun getAllRooms(): LiveData<List<RoomData>>

    @Query("SELECT * FROM rooms WHERE buildingId = :buildingId")
    fun getRoomsForBuilding(buildingId: Int): LiveData<List<RoomData>>
}

