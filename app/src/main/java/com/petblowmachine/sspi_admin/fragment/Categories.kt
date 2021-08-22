package com.petblowmachine.sspi_admin.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.petblowmachine.sspi_admin.R
import com.petblowmachine.sspi_admin.adapter.CategoryAdapter
import com.petblowmachine.sspi_admin.modal.Category

class Categories : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var arrayList: ArrayList<Category>
    private lateinit var adapter:CategoryAdapter
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_categories, container, false)
        recyclerView = view.findViewById(R.id.categoryRecyclerView)
        db = FirebaseFirestore.getInstance()
        arrayList = ArrayList()

        fetchData()

        adapter = CategoryAdapter(arrayList,activity as Context)

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
                    gridLayoutManager = GridLayoutManager(activity as Context,2)
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
}