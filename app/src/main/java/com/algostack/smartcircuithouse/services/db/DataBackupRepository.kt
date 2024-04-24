package com.algostack.smartcircuithouse.services.db

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.algostack.smartcircuithouse.R
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



/*
    suspend fun getAllDataForBackup(context: Context)  {
        progressData(context)


        //Building Data Backup
        val buildingDataList = buildingDao.getAllBuildingsForBackup()
            firebaseBuildingReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        // Data exists in Firebase
                        // Check if ID number exists
                        for (buildingData in buildingDataList) {
                            if (!snapshot.hasChild(buildingData.primaryKey.toString())) {
                                // ID number doesn't exist, save data
                                firebaseBuildingReference.child(buildingData.id.toString()).setValue(buildingData)
                            }
                        }
                        processingBuilding = true
                        progressData(context)
                    } else {
                        // Firebase database is empty, save all data
                        buildingDataList.forEach { roomData ->
                            firebaseBuildingReference.child(roomData.primaryKey.toString()).setValue(roomData)
                        }
                        processingBuilding = true
                        progressData(context)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }



            })


        // here is room data backup
        val roomDataList = roomDao.getAllRoomDataForBackup()
        firebaseRoomReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Data exists in Firebase
                    // Check if ID number exists and isBooked are not changed
                    for (roomData in roomDataList) {
                        if (!snapshot.hasChild(roomData.primaryKey)) {
                            // ID number doesn't exist, save data
                            firebaseRoomReference.child(roomData.id).setValue(roomData)
                        } else {
                            // ID number exists, check if isBooked is changed
                            val isBooked = snapshot.child(roomData.primaryKey).child("booked").value.toString().toBoolean()
                            if (isBooked != roomData.isBooked) {
                                // isBooked is changed, update data
                                firebaseRoomReference.child(roomData.primaryKey).setValue(roomData)
                            }
                        }

                        processingRoom = true
                        progressData(context)

                    }

                } else {
                    // Firebase database is empty, save all data
                    roomDataList.forEach { roomData ->
                        firebaseRoomReference.child(roomData.primaryKey.toString()).setValue(roomData)
                    }

                    processingRoom = true
                      progressData(context)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error

                processingBuilding = false
            }
        })


    }
*/


    suspend fun getAllDataForBackup(context: Context) {
        val progressDialog = Dialog(context).apply {
            setContentView(R.layout.custom_progressba)
            show()
        }

        try {
            // Backup building data
            val buildingDataList = buildingDao.getAllBuildingsForBackup()
            withContext(Dispatchers.IO) {
                try {
                    val snapshot = firebaseBuildingReference.get().await()
                    if (snapshot.exists()) {
                        for (buildingData in buildingDataList) {
                            if (!snapshot.hasChild(buildingData.primaryKey.toString())) {
                                firebaseBuildingReference.child(buildingData.id.toString()).setValue(buildingData).await()
                            }
                        }
                    } else {
                        buildingDataList.forEach { roomData ->
                            firebaseBuildingReference.child(roomData.primaryKey.toString()).setValue(roomData).await()
                        }
                    }

                } catch (e: Exception) {
                    throw Exception("Failed to backup building data: ${e.message}")
                }
            }

            // Backup room data
            val roomDataList = roomDao.getAllRoomDataForBackup()
            withContext(Dispatchers.IO) {
                try {
                    val snapshot = firebaseRoomReference.get().await()
                    if (snapshot.exists()) {
                        for (roomData in roomDataList) {
                            if (!snapshot.hasChild(roomData.primaryKey)) {
                                firebaseRoomReference.child(roomData.id).setValue(roomData).await()
                            } else {
                                val isBooked = snapshot.child(roomData.primaryKey).child("booked").value.toString().toBoolean()
                                if (isBooked != roomData.isBooked) {
                                    firebaseRoomReference.child(roomData.primaryKey).setValue(roomData).await()
                                }
                            }
                        }
                    } else {
                        roomDataList.forEach { roomData ->
                            firebaseRoomReference.child(roomData.primaryKey.toString()).setValue(roomData).await()
                        }
                    }

                } catch (e: Exception) {
                    throw Exception("Failed to backup room data: ${e.message}")
                }
            }

            progressDialog.dismiss()
        } catch (e: Exception) {
            progressDialog.dismiss()
            Toast.makeText(context, "Backup failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }



    suspend fun syncDataFromServer(context: Context) {

        val dialog = Dialog(context).apply {
            setContentView(R.layout.custom_progressba)
            show()
        }


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
                    val id = data.child("id").value.toString(). toString()
                    val name = data.child("name").value.toString()
                    val primaryKey = data.child("primaryKey").value.toString()
                    buildingDataList.add(BuildingData(id, name, primaryKey))
                }

                println("Building data list: $buildingDataList")
                // Save data to local database
                buildingDataList.forEach { buildingData ->
                    buildingDao.insert(buildingData)
                }

            } else {
                println("No building data found")
            }

            // Room data
            if (roomSnapshot.exists()) {
                println("Room data found!")
                val roomDataList = mutableListOf<RoomData>()
                for (data in roomSnapshot.children) {
                    val id = data.child("id").value.toString().toString() ?: ""
                    val buildingId = data.child("buildingId").value.toString() ?: ""
                    val roomBuildingName = data.child("roomBuildingName").value.toString() ?: ""
                    val floorNo = data.child("floorNo").value.toString() ?: ""
                    val roomNo = data.child("roomNo").value.toString() ?: ""
                    val bedType = data.child("bedType").value.toString() ?: ""
                    val isBooked = data.child("booked").value.toString().toBoolean()
                    val customerName = data.child("customerName").value.toString() ?: ""
                    val customerDetails = data.child("customerDetails").value.toString() ?: ""
                    val entryDate = data.child("entryDate").value.toString().toLong() ?: 0
                    val exitDate = data.child("exitDate").value.toString().toLong() ?: 0
                    val primaryKey = data.child("primaryKey").value.toString() ?: ""
                    roomDataList.add(RoomData(id, buildingId, roomBuildingName, floorNo, roomNo, bedType, isBooked, customerName, customerDetails, entryDate, exitDate, primaryKey))
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


        dialog.dismiss()
    }




}










