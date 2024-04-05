package com.algostack.smartcircuithouse.features.room_screen.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.algostack.smartcircuithouse.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BookingBottomSheetDialog : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_book_room, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonBookNow = view.findViewById<Button>(R.id.buttonBookNow)
        buttonBookNow.setOnClickListener {
            buttonBookNow.text = "Booked"
            dismiss()
        }
    }
}
