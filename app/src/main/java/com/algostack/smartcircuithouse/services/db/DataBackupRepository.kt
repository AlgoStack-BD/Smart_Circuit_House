package com.algostack.smartcircuithouse.services.db

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.algostack.smartcircuithouse.services.model.BuildingData
import com.algostack.smartcircuithouse.services.model.RoomData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class DataBackupRepository (
    private val buildingDao: BuildingDao,
    private val roomDao: RoomDao
)  {

    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val firebaseBuildingReference: DatabaseReference = firebaseDatabase.getReference("Building")
    private val firebaseRoomReference: DatabaseReference = firebaseDatabase.getReference("Room")




    suspend fun getAllBuildingsForBackup()  {

        val buildingDataList = buildingDao.getAllBuildingsForBackup()
        println("Building Data List: $buildingDataList")

            firebaseBuildingReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        // Data exists in Firebase
                        // Check if ID number exists
                        for (buildingData in buildingDataList) {
                            if (!snapshot.hasChild(buildingData.id.toString())) {
                                // ID number doesn't exist, save data
                                firebaseBuildingReference.child(buildingData.id.toString()).setValue(buildingData)
                            }
                        }
                    } else {
                        // Firebase database is empty, save all data
                        buildingDataList.forEach { roomData ->
                            firebaseBuildingReference.child(roomData.id.toString()).setValue(roomData)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })


        }


    suspend fun getAllRoomDataForBackup()  {

            val roomDataList = roomDao.getAllRoomDataForBackup()
            println("Room Data List: $roomDataList")

                firebaseRoomReference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            // Data exists in Firebase
                            // Check if ID number exists
                            for (roomData in roomDataList) {
                                if (!snapshot.hasChild(roomData.id.toString())) {
                                    // ID number doesn't exist, save data
                                    firebaseRoomReference.child(roomData.id.toString()).setValue(roomData)
                                }
                            }
                        } else {
                            // Firebase database is empty, save all data
                            roomDataList.forEach { roomData ->
                                firebaseRoomReference.child(roomData.id.toString()).setValue(roomData)
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle error
                    }
                })
    }

    suspend fun syncDataFromServer() {
        println("Syncing data from server")

        try {
            // Execute the Firebase operation within a coroutine scope
            val snapshot = withContext(Dispatchers.IO) {
                firebaseBuildingReference.get().await()
            }
            val roomSnapshot = withContext(Dispatchers.IO) {
                firebaseRoomReference.get().await()
            }

            if (snapshot.exists()) {
                println("Building data found!")
                val buildingDataList = mutableListOf<BuildingData>()
                for (data in snapshot.children) {
                    val id = data.child("id").value.toString().toInt()
                    val name = data.child("name").value.toString()
                    buildingDataList.add(BuildingData(id, name))
                }

                println("Building data list: $buildingDataList")
                // Save data to local database
                buildingDataList.forEach { buildingData ->
                    buildingDao.insert(buildingData)
                }
            } else {
                println("No building data found")
            }

            if (roomSnapshot.exists()) {
                println("Room data found!")
                val roomDataList = mutableListOf<RoomData>()
                for (data in roomSnapshot.children) {
                    val id = data.child("id").value.toString().toLong() ?: 0
                    val buildingId = data.child("buildingId").value.toString() ?: ""
                    val roomBuildingName = data.child("roomBuildingName").value.toString() ?: ""
                    val floorNo = data.child("floorNo").value.toString() ?: ""
                    val roomNo = data.child("roomNo").value.toString().toInt()  ?: 0
                    val bedType = data.child("bedType").value.toString() ?: ""
                    val isBooked = data.child("booked").value.toString().toBoolean()
                    val customerName = data.child("customerName").value.toString() ?: ""
                    val customerDetails = data.child("customerDetails").value.toString() ?: ""
                    val entryDate = data.child("entryDate").value.toString().toLong() ?: 0
                    val exitDate = data.child("exitDate").value.toString().toLong() ?: 0
                    roomDataList.add(RoomData(id, buildingId, roomBuildingName, floorNo, roomNo, bedType, isBooked, customerName, customerDetails, entryDate, exitDate))
                }

                println("Room data list: $roomDataList")
                // Save data to local database
                roomDataList.forEach { roomData ->
                    roomDao.insert(roomData)
                }
            } else {
                println("No room data found")
            }
        } catch (e: Exception) {
            println("Error getting data from firebase: ${e.message}")
        }
    }








}






