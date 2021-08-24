package com.petblowmachine.sspi_admin.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.petblowmachine.sspi_admin.R
import com.petblowmachine.sspi_admin.adapter.RequestAdapter
import com.petblowmachine.sspi_admin.modal.Applic
import com.petblowmachine.sspi_admin.modal.Request
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Requests : Fragment() {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var requestAdapter: RequestAdapter
    private lateinit var requestRecyclerView: RecyclerView
    private lateinit var requestList: ArrayList<Request>
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_requests, container, false)

        requestRecyclerView = view.findViewById(R.id.requestsRecyclerView)
        linearLayoutManager = LinearLayoutManager(activity)

        db = FirebaseFirestore.getInstance()
        requestList = ArrayList()
        db.collection("admin").document("admin").collection("requests").orderBy("time")
            .get()
            .addOnCompleteListener { requestsSnapshot ->
                if(requestsSnapshot.isSuccessful){
                    val result = requestsSnapshot.result!!
                    for(req in result){
                        val name = req["name"].toString()
                        val phoneNumber = req["phone_number"].toString()
                        val machineName = req["machineName"].toString()

                        val firebaseTime = req["time"] as Timestamp
                        val totalMilliseconds = (firebaseTime.seconds+(firebaseTime.nanoseconds)*0.00000001)*1000
                        val date = Date(totalMilliseconds.toLong())
                        val format = SimpleDateFormat("HH:mm dd/MM/yyyy")
                        val time = format.format(date)

                        val request = Request(name,phoneNumber,machineName,time)
                        requestList.add(request)
                    }
                    requestList.reverse()
                    requestAdapter = RequestAdapter(activity as Context,requestRecyclerView,requestList)

                    requestRecyclerView.layoutManager = linearLayoutManager
                    requestRecyclerView.adapter = requestAdapter
                    db.collection("admin").document("admin").collection("requests")
                        .addSnapshotListener { value, error ->
                            requestAdapter.notifyDataSetChanged()
                        }
                }
                else{
                    Toast.makeText(activity,"Failed to fetch requests from server",Toast.LENGTH_LONG).show()
                }
            }

        return view
    }

}