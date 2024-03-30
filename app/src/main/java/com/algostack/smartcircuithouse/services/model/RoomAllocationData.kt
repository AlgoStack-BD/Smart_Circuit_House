package com.algostack.smartcircuithouse.services.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "room_details")
data class RoomAllocationData(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val roomNo: Int,
    val roomType: String, // single or double
    val customerName: String,
    val customerDetails: String,
    val arrivalDate: String,
    val departureDate: String,
)