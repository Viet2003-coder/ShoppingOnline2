package com.example.shoppingonline.ui.product

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shoppingonline.Model.Product
import com.example.shoppingonline.R

class ProductAdapter(
    private val onItemClick:(Product)-> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    private val list = mutableListOf<Product>()
    @SuppressLint("NotifyDataSetChanged")
    fun submitData(data: List<Product>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_item, parent, false)
        return ProductViewHolder(view)    }

    override fun onBindViewHolder(
        holder: ProductViewHolder,
        position: Int
    ) {
            val product=list[position]
            holder.tvTitle.text=product.title ?: "Không có tiêu đề"
            holder.tvPrice.text=product.price.toString()
            holder.tvdes.text=product.description
        Glide.with(holder.itemView.context)
            .load(product.thumbnail)
            .placeholder(R.drawable.outline_download_for_offline_24) // ảnh khi loading
            .error(R.drawable.outline_hide_image_24)               // ảnh khi lỗi
            .into(holder.imgBook)
        holder.itemView.setOnClickListener {
            onItemClick(product)
        }
        }

    override fun getItemCount(): Int {
        return list.size
    }

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        val tvPrice = view.findViewById<TextView>(R.id.tvPrice)
        val imgBook=view.findViewById<ImageView>(R.id.imgProduct)
        val tvdes=view.findViewById<TextView>(R.id.tvsoluong)
    }
}