package com.algostack.smartcircuithouse.features.home_screen.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.algostack.smartcircuithouse.services.db.RoomRepository
import com.algostack.smartcircuithouse.services.model.RoomData
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class InOutViewModel(private val roomRepository: RoomRepository) : ViewModel() {

    val itemList: LiveData<List<Item>> = roomRepository.getBookedRooms().map { rooms ->
        rooms.map { room ->
            Item(
                room.id,
                room.customerName ?: "",
                room.customerDetails ?: "",
                formatDate(room.entryDate),
                formatDate(room.exitDate),
                room.roomBuildingName ?: "",
                room.roomNo ?: "",
                room.floorNo ?: "",
                room.bedType ?: ""
            )
        }
    }


    fun getRoomsByCustomerName(customerName: String): LiveData<List<RoomData>> {
        return roomRepository.getRoomsByCustomerName(customerName)
    }

    fun getRoomsByCustomerDetails(customerDetails: String): LiveData<List<RoomData>> {
        return roomRepository.getRoomsByCustomerDetails(customerDetails)
    }

    fun getRoomsByEntryDate(entryDate: Long): LiveData<List<RoomData>> {
        return roomRepository.getRoomsByEntryDate(entryDate)
    }

    fun getRoomsByExitDate(exitDate: Long): LiveData<List<RoomData>> {
        return roomRepository.getRoomsByExitDate(exitDate)
    }

    private fun formatDate(dateInMillis: Long?): String {
        return if (dateInMillis != null) {
            val dateFormat = SimpleDateFormat("EEE, MMM dd, yyyy", Locale.getDefault())
            val calendar = Calendar.getInstance().apply { timeInMillis = dateInMillis }
            dateFormat.format(calendar.time)
        } else {
            ""
        }
    }
}
