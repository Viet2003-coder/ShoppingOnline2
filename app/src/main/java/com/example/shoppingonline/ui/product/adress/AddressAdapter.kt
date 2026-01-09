package com.example.shoppingonline.ui.product.adress

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.interpolator.R
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingonline.Model.AddressItem

class AddressAdapter(

): RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {
    private val list=mutableListOf<AddressItem>()
    @SuppressLint("NotifyDataSetChanged")
    fun submitData(data: List<AddressItem>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddressViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(com.example.shoppingonline.R.layout.address_item,parent,false)
        return AddressViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: AddressViewHolder,
        position: Int
    ) {
        val address=list[position]
        holder.tvFullname.text=address.fullName
        holder.tvPhone.text=address.phone
        holder.tvFullAdress.text=address.fullAdress
    }

    override fun getItemCount(): Int {
        return list.size
   }

    class AddressViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tvFullname=itemView.findViewById<TextView>(com.example.shoppingonline.R.id.tvFullName)
        val tvFullAdress=itemView.findViewById<TextView>(com.example.shoppingonline.R.id.tvFullAddress)
        val tvPhone=itemView.findViewById<TextView>(com.example.shoppingonline.R.id.tvPhone)
    }
}