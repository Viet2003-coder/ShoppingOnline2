package com.example.shoppingonline.ui.product.cart

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shoppingonline.Model.CartItem
import com.example.shoppingonline.R

class CartAdapter(
    private val onButtonBuy:(CartItem) ->Unit
): RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    private val list = mutableListOf<CartItem>()
    @SuppressLint("NotifyDataSetChanged")
    fun submitData(data: List<CartItem>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.cart_item,parent,false)
        return CartViewHolder(view)
    }
    override fun onBindViewHolder(
        holder: CartViewHolder,
        position: Int
    ) {
        val cartItem=list[position]
        holder.tvTitle.text=list[position].title
        holder.tvPrice.text=list[position].price.toString()
        Glide.with(holder.itemView.context)
            .load(list[position].thumbnail)
            .placeholder(R.drawable.outline_download_for_offline_24) // ảnh khi loading
            .error(R.drawable.outline_hide_image_24)               // ảnh khi lỗi
            .into(holder.imgBook)
        holder.btnbuy.setOnClickListener {
            onButtonBuy(cartItem)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle = itemView.findViewById<TextView>(R.id.tvTitlecart)
        val tvPrice = itemView.findViewById<TextView>(R.id.tvPricecart)
        val imgBook=itemView.findViewById<ImageView>(R.id.imgProductcart)
        val btnbuy=itemView.findViewById<Button>(R.id.btnbuynowcart)
    }
}