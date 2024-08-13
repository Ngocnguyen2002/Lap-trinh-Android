package com.example.renthouse.adaptar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.renthouse.R
import com.example.renthouse.databinding.ItemMessHouseBinding
import java.text.DecimalFormat

data class ItemData(
    val area : String,
    val address : String,
    val image_author : String,
    val name: String,
    val price: Int,
    val imageUrl: String,
    val describe : String,
    val author : String,
    val phoneAuthor : String,
    val images : ArrayList<String>,
    val id_mess : String
)

class ItemMessHouse (
    private val houseDataList: List<ItemData>,
    private val itemClickListener: OnItemClickListener,// Thêm interface
    ) :
    RecyclerView.Adapter<ItemMessHouse.PopularViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        val binding = ItemMessHouseBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PopularViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        val houseData = houseDataList[position]

        holder.bind(houseData)
    }

    override fun getItemCount(): Int = houseDataList.size

    inner class PopularViewHolder(private val binding: ItemMessHouseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(houseData: ItemData) {
            binding.nameHouse.text = houseData.name
            binding.textView30.text = houseData.area
            binding.textView31.text =
                "Giá : " + DecimalFormat("#,###").format(houseData.price) + " VND"
            Glide.with(binding.imageHouse.context).load(houseData.imageUrl).into(binding.imageHouse)

            binding.root.setOnClickListener {
                itemClickListener.onItemClick(houseData)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(houseData: ItemData)
    }
}