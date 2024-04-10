import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import com.algostack.smartcircuithouse.R
import com.algostack.smartcircuithouse.features.home_screen.model.BuildingViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText

class AddBuildingBottomSheetDialog : BottomSheetDialogFragment() {

    private val viewModel: BuildingViewModel by viewModels()

    private lateinit var etBuildingName: TextInputEditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_add_building, container, false)
        etBuildingName = view.findViewById(R.id.etBuildingName)
        val btnSave = view.findViewById<Button>(R.id.btnSave)
        btnSave.setOnClickListener {
            val buildingName = etBuildingName.text.toString()
            if (buildingName.isNotEmpty()) {
                viewModel.saveBuilding(requireContext(), buildingName)
                dismiss()
            } else {
                etBuildingName.error = "Building name is required"
            }
        }
        return view
    }
}
