package com.petblowmachine.sspi_admin.modal

import androidx.recyclerview.widget.RecyclerView

object Applic {
    var categoryName = ""
    var machineName = ""
    var machineImg = ""
    var categoryImg = ""
    var detailsArrayKey:MutableList<String> = ArrayList()
    var detailsArrayValue = ArrayList<String>()
    var arrayPos = 1
    var areAllDetailsFilled = false
    const val sharedPreferenceName = "SSPISharedPreferences"
}