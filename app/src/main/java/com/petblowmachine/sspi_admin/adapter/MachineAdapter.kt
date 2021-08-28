package com.petblowmachine.sspi_admin.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.petblowmachine.sspi_admin.R
import com.petblowmachine.sspi_admin.activity.AddNewMachine
import com.petblowmachine.sspi_admin.modal.Applic
import com.petblowmachine.sspi_admin.modal.MachineInfo

class MachineAdapter(private val context: Context, private var itemList: ArrayList<MachineInfo>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) 1 else 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == 1){
            val view = LayoutInflater.from(context).inflate(R.layout.add_new_mahcine_single_row,parent,false)
            ViewHolder1(view)
        } else{
            val view = LayoutInflater.from(context).inflate(R.layout.machines_single_row,parent,false)
            ViewHolder2(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder.itemViewType == 1){
            val currentHolder = holder as ViewHolder1
            currentHolder.itemView.setOnClickListener {
                val intent = Intent(context,AddNewMachine::class.java)
                ContextCompat.startActivity(context,intent,null)
            }
        }
        else{
            val currentHolder= holder as ViewHolder2
            val currentMachine = itemList[position]
            Glide.with(context).load(currentMachine.machineImg).centerCrop().into(holder.machineImg)
            currentHolder.machineName.text = currentMachine.machineName
            currentHolder.detailOne.text = currentMachine.detail1
            currentHolder.detailTwo.text = currentMachine.detail2
            currentHolder.detailThree.text = currentMachine.detail3
            currentHolder.itemView.setOnClickListener {
                Applic.machineName = currentMachine.machineName
                Applic.machineImg = currentMachine.machineImg
                val intent = Intent(context,AddNewMachine::class.java)
                intent.putExtra("edit",true)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun updateList(list: ArrayList<MachineInfo>) {
        itemList = list
        notifyDataSetChanged()
    }

    class ViewHolder1(view:View): RecyclerView.ViewHolder(view){

    }

    class ViewHolder2(view: View): RecyclerView.ViewHolder(view){
        val machineName: TextView = view.findViewById(R.id.txtSingleRowMachineName)
        val machineImg: ImageView = view.findViewById(R.id.imgSingleRowMachineImage)
        val detailOne: TextView = view.findViewById(R.id.txtSingleRowMachineDetailOne)
        val detailTwo: TextView = view.findViewById(R.id.txtSingleRowMachineDetailTwo)
        val detailThree: TextView = view.findViewById(R.id.txtSingleRowMachineDetailThree)
    }

}