package com.algostack.smartcircuithouse.services.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "buildings")
data class BuildingData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String
)