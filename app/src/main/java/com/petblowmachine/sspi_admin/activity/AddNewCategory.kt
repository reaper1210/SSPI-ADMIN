package com.petblowmachine.sspi_admin.activity

import android.app.Activity
import android.content.Intent
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.petblowmachine.sspi_admin.R
import com.petblowmachine.sspi_admin.modal.Applic

class AddNewCategory : AppCompatActivity() {
    private lateinit var edtTxtCategoryName: EditText
    private lateinit var btnAddCategory: Button
    private lateinit var imgCategory: ImageView
    private lateinit var db:FirebaseFirestore
    private lateinit var imageUri:Uri
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_category)

        Applic.categoryImg = ""
        db = FirebaseFirestore.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        edtTxtCategoryName = findViewById(R.id.edtTxtCategoryName)
        btnAddCategory = findViewById(R.id.btnAddCategory)
        imgCategory = findViewById(R.id.addCatImg)

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

        imgCategory.setOnClickListener {

            Applic.categoryName = edtTxtCategoryName.text.toString()
            if(Applic.categoryName!=""){
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
            else{
                Toast.makeText(this, "Add the Category Name First", Toast.LENGTH_SHORT).show()
            }
        }


        btnAddCategory.setOnClickListener {
            if (Applic.categoryImg.isNotBlank()) {
                Applic.categoryName = edtTxtCategoryName.text.toString()
                val data = hashMapOf(
                    "categoryImg" to Applic.categoryImg
                )
                db.collection("categories").document(Applic.categoryName)
                    .set(data)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Category Added Successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@AddNewCategory, MainActivity::class.java)
                        startActivity(intent)
                        Applic.categoryImg = ""
                        Applic.categoryName = ""
                    }
            }
            else {
                edtTxtCategoryName.error = "Category Image Required"
            }
        }
    }

       private fun uploadImage() {
            val filePath = storageReference.child("machineImages").child(Applic.categoryName)
            filePath.putFile(imageUri).addOnSuccessListener {
                filePath.downloadUrl.addOnSuccessListener {
                    Applic.categoryImg = it.toString()
                    Glide.with(this)
                        .load(imageUri)
                        .centerCrop()
                        .into(imgCategory)
                }
            }
        }
    }