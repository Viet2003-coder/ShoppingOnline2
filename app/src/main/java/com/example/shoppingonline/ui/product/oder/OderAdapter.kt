package com.example.shoppingonline.ui.product.oder

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shoppingonline.Model.Oder
import com.example.shoppingonline.R
import kotlin.math.ceil


class OderAdapter(
    private val onclicItem:(Oder)->Unit
): RecyclerView.Adapter<OderAdapter.oderViewHolder>() {
    private val list= mutableListOf<Oder>()
    @SuppressLint("NotifyDataSetChanged")
    fun submitData(data: List<Oder>){
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): oderViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_oder,parent,false)
        return oderViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: oderViewHolder,
        position: Int
    ) {
        val oder=list[position]
        holder.tvIdOder.text="Mã đơn: #${oder.orderId.takeLast(6)}"
        holder.tvProductName.text=oder.item.title
        holder.tvStatus.text=oder.status.name
        holder.tvQuantity.text="Số lượng: x${oder.item.stock}"
        val totalPrice= ceil(oder.totalPrice)
        holder.tvTotalPrice.text="Tổng tiền: ${totalPrice}đ"
        Glide.with(holder.itemView.context).load(oder.item.thumbnail).placeholder(R.drawable.outline_download_for_offline_24) // ảnh khi loading
            .error(R.drawable.outline_hide_image_24)               // ảnh khi lỗi
            .into(holder.imgProduct)
        holder.itemView.setOnClickListener {
            onclicItem(oder)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class oderViewHolder (itemView: android.view.View): RecyclerView.ViewHolder(itemView){
        val tvIdOder=itemView.findViewById<TextView>(R.id.tvOrderId)
        val tvStatus=itemView.findViewById<TextView>(R.id.tvOrderStatus)
        val tvProductName=itemView.findViewById<TextView>(R.id.tvProductName)
        val tvTotalPrice=itemView.findViewById<TextView>(R.id.tvTotalPrice)
        val tvQuantity=itemView.findViewById<TextView>(R.id.tvQuantity)
        val imgProduct=itemView.findViewById<ImageView>(R.id.imgProduct)
    }
}