package com.petblowmachine.sspi_admin.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.petblowmachine.sspi_admin.R
import com.petblowmachine.sspi_admin.adapter.CategoryAdapter
import com.petblowmachine.sspi_admin.modal.Category
import com.petblowmachine.sspi_admin.modal.MachineInfo

class Categories : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var arrayList: ArrayList<Category>
    private lateinit var adapter:CategoryAdapter
    private lateinit var edtTxtSearchbar: EditText
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_categories, container, false)
        recyclerView = view.findViewById(R.id.categoryRecyclerView)
        edtTxtSearchbar = view.findViewById(R.id.searchEdtTxtCatFrag)
        db = FirebaseFirestore.getInstance()
        arrayList = ArrayList()
        arrayList.add(Category("Add",""))

        fetchData()

        adapter = CategoryAdapter(arrayList,activity as Context)

        edtTxtSearchbar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                filter(p0.toString())
            }

        })

        return view
    }

    private fun fetchData(){
        if(activity!=null){
            db.collection("categories")
                .get()
                .addOnSuccessListener {
                    for(document in it){
                        arrayList.add(Category(document.id,document["categoryImg"].toString()))
                    }
                    val context = activity as Context
                    gridLayoutManager = GridLayoutManager(activity,2)
                    recyclerView.layoutManager = gridLayoutManager
                    recyclerView.adapter = adapter
                }
                .addOnFailureListener {
                }
        }

        else{
            println("null")
        }
    }

    private fun filter(text: String?) {
        val temp = ArrayList<Category>()
        for (i in arrayList) {
            if ((i.categoryName.lowercase()).contains(text.toString().lowercase())) {
                temp.add(i)
            }
        }
        if(temp.size <=1 ){
            temp.add(0, Category("",""))
        }
        adapter.updateList(temp)
    }

}