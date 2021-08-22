package com.petblowmachine.sspi_admin.activity

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.petblowmachine.sspi_admin.R
import com.petblowmachine.sspi_admin.adapter.DetailsAdapter
import com.petblowmachine.sspi_admin.modal.Applic

class AddNewMachine : AppCompatActivity() {
    private lateinit var edtTextMachineName: EditText
    private lateinit var edtTxtKeyDetail1: EditText
    private lateinit var edtTxtKeyDetail2: EditText
    private lateinit var edtTxtKeyDetail3: EditText
    private lateinit var btnAddEdtTxt: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DetailsAdapter
    private lateinit var arrayList: ArrayList<Int>
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var btnAddMachine: Button
    private lateinit var btnRemoveEdtTxt: ImageView
    private lateinit var db:FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_machine)

        db = FirebaseFirestore.getInstance()
        edtTextMachineName = findViewById(R.id.edtTextMachineName)
        edtTxtKeyDetail1 = findViewById(R.id.edtTextDetail1)
        edtTxtKeyDetail2 = findViewById(R.id.edtTextDetail2)
        edtTxtKeyDetail3 = findViewById(R.id.edtTextDetail3)
        btnAddEdtTxt = findViewById(R.id.btnAddEdtTxt)
        recyclerView = findViewById(R.id.detailsEdtTextRecyclerView)
        btnAddMachine = findViewById(R.id.btnAddMachine)
        btnRemoveEdtTxt = findViewById(R.id.btnRemoveEdtTxt)

        arrayList = ArrayList()
        arrayList.add(0)
        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        adapter = DetailsAdapter(this, arrayList)
        recyclerView.adapter = adapter

        btnAddEdtTxt.setOnClickListener {
            recyclerView.clearFocus()
            arrayList.add(Applic.arrayPos)
            Applic.arrayPos++
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
                Toast.makeText(this,"Enter Atleast one Detail",Toast.LENGTH_SHORT).show()
            }
        }

        btnAddMachine.setOnClickListener {
            if(!TextUtils.isEmpty(edtTextMachineName.text) and !TextUtils.isEmpty(edtTxtKeyDetail1.text)
                and !TextUtils.isEmpty(edtTxtKeyDetail2.text) and !TextUtils.isEmpty(edtTxtKeyDetail3.text)){
                    val machineName = edtTextMachineName.text.toString()
                    Applic.arrayPos = 1
                    println("KeyList ${Applic.detailsArrayKey}")
                    println("ValueList ${Applic.detailsArrayValue}")
                val data = HashMap<String,String>()
                data["detail1"] = edtTxtKeyDetail1.text.toString()
                data["detail2"] = edtTxtKeyDetail2.text.toString()
                data["detail3"] = edtTxtKeyDetail3.text.toString()

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
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(intent)
                                }
                        }
                }
                else{
                    Toast.makeText(this,"Please fill atleast one Detail",Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this,"Please fill all Required Fields",Toast.LENGTH_SHORT).show()
            }
        }

    }
}