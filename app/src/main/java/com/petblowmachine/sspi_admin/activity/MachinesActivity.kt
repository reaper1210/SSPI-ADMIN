package com.petblowmachine.sspi_admin.activity

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import com.petblowmachine.sspi_admin.R
import com.petblowmachine.sspi_admin.adapter.MachineAdapter
import com.petblowmachine.sspi_admin.modal.Applic
import com.petblowmachine.sspi_admin.modal.MachineInfo

class MachinesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnDeleteCategory: CardView
    private lateinit var linearLayout: LinearLayoutManager
    private lateinit var machinesAdapter: MachineAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var arrList:ArrayList<MachineInfo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_machines)

        db = FirebaseFirestore.getInstance()

        recyclerView = findViewById(R.id.machinesRecyclerView)
        btnDeleteCategory = findViewById(R.id.deleteCategoryCardView)

        linearLayout = LinearLayoutManager(this)
        arrList = ArrayList()
        arrList.add(MachineInfo("Add","","","",""))

        db.collection("categories").document(Applic.categoryName).collection("Machines")
            .get()
            .addOnSuccessListener {
                for(document in it){
                    arrList.add(MachineInfo(document.id,document["machineImg"].toString(),
                        document["detail1"].toString(),document["detail2"].toString(),
                        document["detail3"].toString()))
                }
                recyclerView.layoutManager = linearLayout
                recyclerView.adapter = machinesAdapter
            }

        machinesAdapter = MachineAdapter(this, arrList)

        btnDeleteCategory.setOnClickListener {

            val builder = AlertDialog.Builder(this)
            builder.setMessage("Do you want to delete ${Applic.categoryName} Category?")
                .setTitle("Delete Category")
                .setPositiveButton("Yes") { _, _ ->
                    finish()
                    db.collection("categories").document(Applic.categoryName).delete()
                        .addOnCompleteListener {
                            if(it.isSuccessful){
                                Toast.makeText(this,"${Applic.categoryName} deleted successfully",Toast.LENGTH_SHORT).show()
                                val intent = Intent(this,MainActivity::class.java)
                                startActivity(intent)
                            }
                            else{
                                Toast.makeText(this,"Failed to delete ${Applic.categoryName}",Toast.LENGTH_SHORT).show()
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

    override fun onBackPressed() {
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }

}