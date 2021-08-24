package com.petblowmachine.sspi_admin.modal

data class Request(
    val personName: String,
    val phoneNumber: String,
    val machineName: String,
    val time: String,
    var visible: Boolean = false
)