package com.algostack.smartcircuithouse.features.home_screen.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.algostack.smartcircuithouse.services.db.RoomRepository
import com.algostack.smartcircuithouse.services.model.RoomData
import kotlinx.coroutines.launch
import java.text.ParseException
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

    fun filterItemsByExitDate(selectedDateInMillis: Long): List<Item> {
        val itemList = itemList.value ?: emptyList()
        return itemList.filter { item ->
            val exitDateInMillis = parseDateToMillis(item.exitDate)
            exitDateInMillis >= selectedDateInMillis
        }
    }

    private fun parseDateToMillis(dateString: String): Long {
        return try {
            val dateFormat = SimpleDateFormat("EEE, MMM dd, yyyy", Locale.getDefault())
            val date = dateFormat.parse(dateString)
            date?.time ?: 0L
        } catch (e: ParseException) {
            Log.e("InOutViewModel", "Error parsing date: $dateString", e)
            0L
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

    fun cancelBooking(roomId: Long) {
        viewModelScope.launch {
            roomRepository.cancelBooking(roomId)
        }
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
