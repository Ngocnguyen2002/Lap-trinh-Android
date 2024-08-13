package com.example.renthouse.adaptar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.renthouse.databinding.ItemHouseBinding
import java.text.DecimalFormat

data class HouseData(
    val id_house : String,
    val area : String,
    val address : String,
    val image_author : String,
    val name: String,
    val price: Int,
    val imageUrl: String,
    val describe : String,
    val author : String,
    val id_author : String,
    val phoneAuthor : String,
    val images : ArrayList<String>
)

class PopularAdapter(
    private val houseDataList: List<HouseData>,
    private val itemClickListener: OnItemClickListener,// Thêm interface
) :
    RecyclerView.Adapter<PopularAdapter.PopularViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        val binding = ItemHouseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PopularViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        val houseData = houseDataList[position]
        holder.bind(houseData)
    }

    override fun getItemCount(): Int = houseDataList.size

    inner class PopularViewHolder(private val binding: ItemHouseBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(houseData: HouseData) {
            binding.nameHouse.text = houseData.name
            binding.textView10.text = houseData.area
            binding.priceRent.text = "Giá : "  + DecimalFormat("#,###").format(houseData.price) + " VND"
            Glide.with(binding.imageHouse.context).load(houseData.imageUrl).into(binding.imageHouse)

            binding.root.setOnClickListener {
                itemClickListener.onItemClick(houseData)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(houseData: HouseData)
    }
}
