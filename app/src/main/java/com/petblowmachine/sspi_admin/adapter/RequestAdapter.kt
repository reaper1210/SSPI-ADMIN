package com.petblowmachine.sspi_admin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.api.Distribution
import com.petblowmachine.sspi_admin.R
import com.petblowmachine.sspi_admin.modal.Applic
import com.petblowmachine.sspi_admin.modal.Request

class RequestAdapter(private val context: Context,private val recyclerView: RecyclerView, private val requestList: ArrayList<Request>): RecyclerView.Adapter<RequestAdapter.RequestViewHolder>() {

    class RequestViewHolder(view: View): RecyclerView.ViewHolder(view){
        val txtPersonName: TextView = view.findViewById(R.id.singleRowRequestPersonName)
        val txtPhoneNumber: TextView = view.findViewById(R.id.singleRowRequestPhoneNumber)
        val txtMachineName: TextView = view.findViewById(R.id.singleRowRequestMachineName)
        val txtTime: TextView = view.findViewById(R.id.singleRowRequestTime)
        val imgArrow: ImageView = view.findViewById(R.id.imgSingleRowRequestArrow)
        val requestLayout: MaterialCardView = view.findViewById(R.id.singleRowRequestLayout)
        val expandLayout: ConstraintLayout = view.findViewById(R.id.singleRowRequestExpandedLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_row_request,parent,false)
        return RequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {

        holder.txtPersonName.text = requestList[position].personName
        holder.txtPhoneNumber.text = requestList[position].phoneNumber
        holder.txtMachineName.text = requestList[position].machineName
        holder.txtTime.text = requestList[position].time

        if(requestList[position].visible){
            holder.expandLayout.visibility = View.VISIBLE
            val btn = holder.imgArrow
            holder.requestLayout.setOnClickListener {
                btn.setImageResource(R.drawable.ic_arrow_up)
                requestList[position].visible = false
                notifyItemChanged(position)
            }
        }
        else{
            holder.expandLayout.visibility = View.GONE
            val btn = holder.imgArrow
            holder.requestLayout.setOnClickListener {
                btn.setImageResource(R.drawable.ic_arrow_down)
                requestList[position].visible = true
                notifyItemChanged(position)

                //scroll when last card is expanded
//                if(position==requestList.size-1){
//                    LinearLayoutManager(context).smoothScrollToPosition(recyclerView,null,itemCount)
//                }
            }
        }

    }

    override fun getItemCount(): Int {
        return requestList.size
    }

}