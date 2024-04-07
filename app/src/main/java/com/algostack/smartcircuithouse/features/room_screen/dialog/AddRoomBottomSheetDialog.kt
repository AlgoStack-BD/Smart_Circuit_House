package com.algostack.smartcircuithouse.features.room_screen.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.algostack.smartcircuithouse.R
import com.algostack.smartcircuithouse.services.db.RoomDao
import com.algostack.smartcircuithouse.services.model.RoomData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddRoomBottomSheetDialog(private val roomDao: RoomDao, private val buildingId: Int) :
    BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_room_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etRoomNo = view.findViewById<TextInputEditText>(R.id.editTextRoomNo)
        val etBedType = view.findViewById<TextInputEditText>(R.id.editTextBedType)
        val etFloorNo = view.findViewById<TextInputEditText>(R.id.editTextFloorNo)
        val btnSave = view.findViewById<Button>(R.id.roomBtnSave)

        btnSave.setOnClickListener {
            val roomNo = etRoomNo.text.toString()
            val bedType = etBedType.text.toString()
            val floorNo = etFloorNo.text.toString().toIntOrNull()

            if (roomNo.isNotEmpty() && bedType.isNotEmpty() && floorNo != null) {
                val roomData = RoomData(
                    roomNo = roomNo,
                    bedType = bedType,
                    floorNo = floorNo,
                    buildingId = buildingId
                )
                saveRoomInfo(roomData)
                dismiss()
            } else {
            }
        }
    }

    private fun saveRoomInfo(roomData: RoomData) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                roomDao.insert(roomData)
            } catch (e: Exception) {
            }
        }
    }
}
