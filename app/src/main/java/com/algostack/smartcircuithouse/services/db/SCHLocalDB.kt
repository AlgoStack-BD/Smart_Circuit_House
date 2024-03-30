package com.algostack.smartcircuithouse.services.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.algostack.smartcircuithouse.services.model.RoomAllocationData

@Database(
    entities = [RoomAllocationData::class],
    version = 1)
abstract class SCHLocalDB : RoomDatabase(){
    // initialize DAO
}