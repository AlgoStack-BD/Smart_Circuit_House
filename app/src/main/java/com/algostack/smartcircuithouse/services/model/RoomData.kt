package com.algostack.smartcircuithouse.services.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rooms")
data class RoomData(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val roomNo: String,
    val bedType: String,
    val floorNo: Int,
    val buildingId: Int,
    var isBooked: Boolean = false
)
