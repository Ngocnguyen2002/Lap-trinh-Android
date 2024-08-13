package com.example.renthouse.adaptar

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.renthouse.DetailActivity
import com.example.renthouse.HomeActivity
import com.example.renthouse.R
import com.example.renthouse.databinding.ItemNewHomeBinding
import java.text.DecimalFormat


class PopularAdapterHouse (
    private val houseDataList: List<HouseData>,
    private val itemClickListener: OnItemClickListener,// Thêm interface
) :
    RecyclerView.Adapter<PopularAdapterHouse.PopularViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        val binding = ItemNewHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PopularViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        val HouseData = houseDataList[position]
        holder.bind(HouseData)
    }

    override fun getItemCount(): Int = houseDataList.size

    inner class PopularViewHolder(private val binding: ItemNewHomeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(houseData: HouseData) {
            binding.textView26.text = "Nhà trọ " + houseData.name
            binding.textView28.text = houseData.describe.take(40) +"..."
            binding.textView27.text = houseData.address
            binding.textView29.text = "Giá :" +  DecimalFormat("#,###").format(houseData.price) + " VND"
            Glide.with(binding.imageHouse.context).load(houseData.imageUrl).into(binding.imageHouse)

            binding.root.setOnClickListener {
                itemClickListener.onItemClick(houseData)
            }

            binding.button15.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, DetailActivity::class.java).apply {
                    putExtra("ITEM_NAME", houseData.name)
                    putExtra("ITEM_AREA", houseData.area)
                    putExtra("ITEM_PRICE", houseData.price)
                    putExtra("ITEM_IMAGE_URL", houseData.imageUrl)
                    putExtra("ITEM_DESCRIBE", houseData.describe)
                    putExtra("ITEM_AUTHOR", houseData.author)
                    putExtra("ITEM_AUTHOR_IMAGE", houseData.image_author)
                    putExtra("ITEM_AUTHOR_PHONE", houseData.phoneAuthor)
                    putStringArrayListExtra("ITEM_IMAGES",houseData.images)
                }
                context.startActivity(intent)
            }
        }

    }

    interface OnItemClickListener {
        fun onItemClick(houseData: HouseData)
    }
}

