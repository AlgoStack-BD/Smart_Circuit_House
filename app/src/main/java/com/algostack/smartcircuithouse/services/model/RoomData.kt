package com.algostack.smartcircuithouse.services.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rooms")
data class RoomData(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val roomNo: String,
    val bedType: String,
    val floorNo: String,
    val buildingId: Int,
    val roomBuildingName: String,
    var isBooked: Boolean = false,
    var customerName: String? = null,
    var customerDetails: String? = null,
    var entryDate: Long? = null,
    var exitDate: Long? = null
)
