package com.petblowmachine.sspi_admin.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.petblowmachine.sspi_admin.R
import com.petblowmachine.sspi_admin.activity.AddNewCategory
import com.petblowmachine.sspi_admin.activity.MachinesActivity
import com.petblowmachine.sspi_admin.modal.Applic
import com.petblowmachine.sspi_admin.modal.Category

class CategoryAdapter(private val arrayList:ArrayList<Category>, private val context: Context):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) 1 else 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == 1){
            val addNewCategoryLayout = LayoutInflater.from(parent.context).inflate(R.layout.add_new_row,parent,false)
            ViewHolder1(addNewCategoryLayout)
        } else{
            val itemHolder = LayoutInflater.from(parent.context).inflate(R.layout.category_single_row,parent,false)
            ViewHolder2(itemHolder)
        }
    }

    override fun onBindViewHolder(holder:RecyclerView.ViewHolder, position: Int) {
        val category = arrayList[position]
        if(holder.itemViewType == 2){
            val currentHolder = holder as ViewHolder2
            currentHolder.categoryName.text = category.categoryName
            Glide.with(context).load(category.categoryImg).centerCrop().into(holder.categoryImg)
            currentHolder.itemView.setOnClickListener {
                val intent = Intent(context, MachinesActivity::class.java)
                Applic.categoryName = category.categoryName
                ContextCompat.startActivity(context, intent, null)
            }
        }
        else{
            val currentHolder = holder as ViewHolder1
            currentHolder.itemView.setOnClickListener {
                Applic.categoryName = category.categoryName
                val intent = Intent(context, AddNewCategory::class.java)
                ContextCompat.startActivity(context,intent,null)
            }
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class ViewHolder1(itemView: View): RecyclerView.ViewHolder(itemView){
    }

    class ViewHolder2(itemView: View): RecyclerView.ViewHolder(itemView){
        val categoryName: TextView = itemView.findViewById(R.id.txtCategoryName)
        var categoryImg: ImageView = itemView.findViewById(R.id.imgCategory)
        var button: ImageView = itemView.findViewById(R.id.btnViewAll)
    }
}