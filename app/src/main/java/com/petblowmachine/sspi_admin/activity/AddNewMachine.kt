package com.petblowmachine.sspi_admin.activity

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.Drawable
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.petblowmachine.sspi_admin.R
import com.petblowmachine.sspi_admin.adapter.DetailsAdapter
import com.petblowmachine.sspi_admin.modal.Applic
import com.petblowmachine.sspi_admin.modal.MachineDetail

class AddNewMachine : AppCompatActivity() {

    private lateinit var txtAddMachine: TextView
    private lateinit var txtAddImage: TextView
    private lateinit var edtTextMachineName: EditText
    private lateinit var edtTxtKeyDetail1: EditText
    private lateinit var edtTxtKeyDetail2: EditText
    private lateinit var edtTxtKeyDetail3: EditText
    private lateinit var btnAddEdtTxt: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DetailsAdapter
    private lateinit var arrayList: ArrayList<MachineDetail>
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var btnAddMachine: Button
    private lateinit var btnDeleteMachine: Button
    private lateinit var btnRemoveEdtTxt: ImageView
    private lateinit var addMachineImg:ImageView
    private lateinit var imageUri: Uri
    private lateinit var db:FirebaseFirestore
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_machine)

        arrayList = ArrayList()
        db = FirebaseFirestore.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        edtTextMachineName = findViewById(R.id.edtTextMachineName)
        edtTxtKeyDetail1 = findViewById(R.id.edtTextDetail1)
        edtTxtKeyDetail2 = findViewById(R.id.edtTextDetail2)
        edtTxtKeyDetail3 = findViewById(R.id.edtTextDetail3)
        btnAddEdtTxt = findViewById(R.id.btnAddEdtTxt)
        recyclerView = findViewById(R.id.detailsEdtTextRecyclerView)
        btnAddMachine = findViewById(R.id.btnAddMachine)
        btnDeleteMachine = findViewById(R.id.btnDeleteMachine)
        btnRemoveEdtTxt = findViewById(R.id.btnRemoveEdtTxt)
        addMachineImg = findViewById(R.id.addMachineImg)
        txtAddMachine = findViewById(R.id.headerTextAddMachAct)
        txtAddImage = findViewById(R.id.headClkToAddImg)

        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager

        if(intent.getBooleanExtra("edit",false)){

            db.collection("categories").document(Applic.categoryName).collection("Machines").document(Applic.machineName)
                .get()
                .addOnCompleteListener { getMachine->
                    if(getMachine.isSuccessful){
                        val result = getMachine.result
                        val data = result?.data
                        val detailOne = data?.get("detail1").toString()
                        val detailTwo = data?.get("detail2").toString()
                        val detailThree = data?.get("detail3").toString()
                        edtTxtKeyDetail1.setText(detailOne)
                        edtTxtKeyDetail2.setText(detailTwo)
                        edtTxtKeyDetail3.setText(detailThree)
                    }
                    else{
                        Toast.makeText(this,"Some Error Occurred while retrieving machine details",Toast.LENGTH_LONG).show()
                    }
                }

            db.collection("categories").document(Applic.categoryName)
                .collection("Machines").document(Applic.machineName)
                .collection("details").document("details").get()
                .addOnCompleteListener { getDetails ->

                    if(getDetails.isSuccessful){
                        val document = getDetails.result!!
                        for(docField in document.data!!){
                            val key = docField.key.toString()
                            val value = docField.value.toString()
                            arrayList.add(MachineDetail(key,value))
                        }
                        adapter = DetailsAdapter(this, arrayList)
                        recyclerView.adapter = adapter
                    }
                    else{
                        Toast.makeText(this,"Some Error Occurred while retrieving machine details",Toast.LENGTH_LONG).show()
                    }

                }

            edtTextMachineName.setText(Applic.machineName)
            Glide.with(this).load(Applic.machineImg).centerCrop().error(R.drawable.add_img).into(addMachineImg)
            txtAddMachine.text = "Edit Machine"
            txtAddImage.text = "Click to edit Image"
            btnAddMachine.text = "Update Details"

            btnAddMachine.setOnClickListener {
                if(!TextUtils.isEmpty(edtTextMachineName.text) and !TextUtils.isEmpty(edtTxtKeyDetail1.text)
                    and !TextUtils.isEmpty(edtTxtKeyDetail2.text) and !TextUtils.isEmpty(edtTxtKeyDetail3.text)){
                    if(Applic.machineImg!=""){
                        val machineName = edtTextMachineName.text.toString()
                        val data = HashMap<String,String>()
                        data["detail1"] = edtTxtKeyDetail1.text.toString()
                        data["detail2"] = edtTxtKeyDetail2.text.toString()
                        data["detail3"] = edtTxtKeyDetail3.text.toString()
                        data["machineImg"] = Applic.machineImg

                        val data2 = HashMap<String,String>()
                        for(i in 0 until Applic.detailsArrayKey.size){
                            data2[Applic.detailsArrayKey[i]] = Applic.detailsArrayValue[i]
                        }
                        if(Applic.areAllDetailsFilled){

                            db.collection("categories").document(Applic.categoryName).collection("Machines")
                                .document(Applic.machineName).delete()
                                .addOnCompleteListener { delete ->
                                    if(delete.isSuccessful){
                                        db.collection("categories").document(Applic.categoryName).collection("Machines")
                                            .document(machineName)
                                            .set(data)
                                            .addOnSuccessListener {
                                                db.collection("categories").document(Applic.categoryName).collection("Machines")
                                                    .document(machineName).collection("details").document("details")
                                                    .set(data2)
                                                    .addOnSuccessListener {
                                                        Toast.makeText(this,"Updated SuccessFully",Toast.LENGTH_SHORT).show()
                                                        val intent = Intent(this@AddNewMachine,MachinesActivity::class.java)
                                                        startActivity(intent)
                                                    }
                                            }
                                    }
                                    else{
                                        Toast.makeText(this,"Error while updating machine",Toast.LENGTH_SHORT).show()
                                    }
                                }


                        }
                        else{
                            Toast.makeText(this,"Please fill atleast one Detail",Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        Toast.makeText(this,"Machine Image Required",Toast.LENGTH_SHORT).show()
                    }

                }
                else{
                    Toast.makeText(this,"Please fill all Required Fields",Toast.LENGTH_SHORT).show()
                }
            }

            btnDeleteMachine.setOnClickListener {

                val builder = AlertDialog.Builder(this)
                builder.setMessage("Do you want to delete ${Applic.machineName} Machine?")
                    .setTitle("Delete Machine")
                    .setPositiveButton("Yes") { _, _ ->
                        finish()
                        db.collection("categories").document(Applic.categoryName)
                            .collection("Machines").document(Applic.machineName)
                            .delete()
                            .addOnCompleteListener {
                                if(it.isSuccessful){
                                    Toast.makeText(this,"${Applic.machineName} deleted successfully",Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this,MachinesActivity::class.java)
                                    startActivity(intent)
                                }
                                else{
                                    Toast.makeText(this,"Failed to delete ${Applic.machineName}",Toast.LENGTH_SHORT).show()
                                }
                            }


                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.cancel()
                    }
                    .create()
                    .show()

            }

        }
        else{
            Applic.machineImg = ""
            btnDeleteMachine.visibility = View.GONE
            arrayList.add(MachineDetail("",""))
            adapter = DetailsAdapter(this, arrayList)
            recyclerView.adapter = adapter

            btnAddMachine.setOnClickListener {
                if(!TextUtils.isEmpty(edtTextMachineName.text) and !TextUtils.isEmpty(edtTxtKeyDetail1.text)
                    and !TextUtils.isEmpty(edtTxtKeyDetail2.text) and !TextUtils.isEmpty(edtTxtKeyDetail3.text)){
                    if(Applic.machineImg!=""){
                        val machineName = edtTextMachineName.text.toString()
                        val data = HashMap<String,String>()
                        data["detail1"] = edtTxtKeyDetail1.text.toString()
                        data["detail2"] = edtTxtKeyDetail2.text.toString()
                        data["detail3"] = edtTxtKeyDetail3.text.toString()
                        data["machineImg"] = Applic.machineImg

                        val data2 = HashMap<String,String>()
                        for(i in 0 until Applic.detailsArrayKey.size){
                            data2[Applic.detailsArrayKey[i]] = Applic.detailsArrayValue[i]
                        }
                        if(Applic.areAllDetailsFilled){
                            db.collection("categories").document(Applic.categoryName).collection("Machines")
                                .document(machineName)
                                .set(data)
                                .addOnSuccessListener {
                                    db.collection("categories").document(Applic.categoryName).collection("Machines")
                                        .document(machineName).collection("details").document("details")
                                        .set(data2)
                                        .addOnSuccessListener {
                                            Toast.makeText(this,"Added SuccessFully",Toast.LENGTH_SHORT).show()
                                            val intent = Intent(this@AddNewMachine,MachinesActivity::class.java)
                                            startActivity(intent)
                                        }
                                }
                        }
                        else{
                            Toast.makeText(this,"Please fill atleast one Detail",Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        Toast.makeText(this,"Machine Image Required",Toast.LENGTH_SHORT).show()
                    }

                }
                else{
                    Toast.makeText(this,"Please fill all Required Fields",Toast.LENGTH_SHORT).show()
                }
            }

        }

        val startForProfileImageResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                val resultCode = result.resultCode
                val data = result.data

                if (resultCode == Activity.RESULT_OK) {
                    val fileUri = data?.data!!
                    imageUri = fileUri
                    uploadImage()
                } else if (resultCode == ImagePicker.RESULT_ERROR) {
                    Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }

        addMachineImg.setOnClickListener {
            ImagePicker.with(this)
                .compress(1024)
                .crop(160f,128f)
                .maxResultSize(
                    1080,
                    1080
                )
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
        }

        btnAddEdtTxt.setOnClickListener {
            recyclerView.clearFocus()
            arrayList.add(MachineDetail("",""))
            adapter.notifyItemInserted(arrayList.size - 1)
        }

        btnRemoveEdtTxt.setOnClickListener {
            recyclerView.clearFocus()
            if(arrayList.size>1){
                arrayList.removeAt(arrayList.size-1)
                Applic.detailsArrayKey.removeAt(Applic.detailsArrayKey.lastIndex)
                Applic.detailsArrayValue.removeAt(Applic.detailsArrayValue.lastIndex)
                adapter.notifyItemRemoved(arrayList.size)
            }
            else{
                Toast.makeText(this,"Enter at least one Detail",Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun uploadImage() {
        val filePath = storageReference.child("machineImages").child(Applic.categoryName)
        filePath.putFile(imageUri).addOnSuccessListener {
            filePath.downloadUrl.addOnSuccessListener {
                Applic.machineImg = it.toString()
                Glide.with(this)
                    .load(imageUri)
                    .centerCrop()
                    .into(addMachineImg)
            }
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this,MachinesActivity::class.java)
        startActivity(intent)
    }

}