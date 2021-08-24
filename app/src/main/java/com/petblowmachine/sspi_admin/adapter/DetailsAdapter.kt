package com.petblowmachine.sspi_admin.adapter

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.petblowmachine.sspi_admin.R
import com.petblowmachine.sspi_admin.modal.Applic
import com.petblowmachine.sspi_admin.modal.MachineDetail
import org.w3c.dom.Text

class DetailsAdapter(private val context:Context,private val arrayList: ArrayList<MachineDetail>)
    :RecyclerView.Adapter<DetailsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.details_single_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.edtTextKey.setText(arrayList[position].key)
        holder.edtTextValue.setText(arrayList[position].value)
        while(Applic.detailsArrayKey.size < arrayList.size){
            Applic.detailsArrayKey.add("")
        }
        while(Applic.detailsArrayValue.size < arrayList.size){
            Applic.detailsArrayValue.add("")
        }

        holder.edtTextKey.setOnFocusChangeListener { view, b ->
            if(!b){
                val data = holder.edtTextKey.text.toString()
                if(!TextUtils.isEmpty(holder.edtTextKey.text)){
                    Applic.detailsArrayKey[position] = data
                }
            }
            if(position == arrayList.size-1){
                val pos = position
                holder.edtTextKey.addTextChangedListener(object:TextWatcher{
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    override fun afterTextChanged(p0: Editable?) {
                        if(holder.edtTextKey.hasFocus()){
                            if(!TextUtils.isEmpty(holder.edtTextKey.text)){
                                Applic.detailsArrayKey[pos] = p0.toString()
                            }
                        }
                    }

                })
            }
        }

        holder.edtTextValue.setOnFocusChangeListener { view, b ->
            if(!b){
                val data = holder.edtTextValue.text.toString()
                if(!TextUtils.isEmpty(holder.edtTextValue.text)){
                    Applic.detailsArrayValue[position] = data
                }
                if(!TextUtils.isEmpty(holder.edtTextKey.text) and !TextUtils.isEmpty(holder.edtTextValue.text)){
                    Applic.areAllDetailsFilled = true
                }
            }
            if(position == arrayList.size-1){
                val pos = position
                holder.edtTextValue.addTextChangedListener(object :TextWatcher{
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                    override fun afterTextChanged(p0: Editable?) {
                        if(holder.edtTextValue.hasFocus()){
                            if(!TextUtils.isEmpty(holder.edtTextValue.text)){
                                Applic.detailsArrayValue[pos] = p0.toString()
                            }
                        }
                    }

                })
            }
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var edtTextKey: EditText = itemView.findViewById(R.id.edtTxtKey1)
        var edtTextValue: EditText = itemView.findViewById(R.id.edtTxtValue1)
    }
}