package com.example.renthouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.renthouse.adaptar.HouseData
import com.example.renthouse.adaptar.PopularAdapter
import com.example.renthouse.adaptar.PopularAdapterHouse
import com.example.renthouse.databinding.ActivityListSearchBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.DecimalFormat

class ListSearchActivity : AppCompatActivity(), PopularAdapterHouse.OnItemClickListener {

    private lateinit var binding: ActivityListSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val name = intent.getStringExtra("ITEM_ADD") ?: ""
        val id_user = intent.getStringExtra("ITEM_ID_USER") ?: ""
        val area = intent.getStringExtra("ITEM_PRICE")?.toIntOrNull() ?: 1000000000
        val inputAddress = findViewById<TextView>(R.id.textView12)
        val inputPrice = findViewById<TextView>(R.id.textView7)

        inputPrice.text = area.toString()
        inputAddress.text = name
        loadApi(area, name,id_user);

        val btnSearch : ImageButton = findViewById(R.id.imageButton)
        btnSearch.setOnClickListener {

            loadApi(inputPrice.text.toString().toIntOrNull() ?: 1000000000, inputAddress.text.toString(),id_user)
        }

    }

    fun  loadApi (price : Int, name : String, u : String){
        val load =  findViewById<ProgressBar>(R.id.progressBar)
        load.visibility = View.VISIBLE
        val db = Firebase.firestore
        db.collection("house")
            .whereLessThanOrEqualTo("price", price)
            .get()
            .addOnSuccessListener { result ->

                val input = findViewById<TextView>(R.id.textView25)
                if(result.isEmpty){
                    input.visibility = View.VISIBLE
                }else {
                    input.visibility = View.GONE
                }
                val filteredByPrice = result.documents.mapNotNull { document ->
                        HouseData(
                            id_house= u,
                            area = document.getString("area") ?: "",
                            address = document.getString("address") ?: "",
                            image_author = document.getString("image_author") ?: "",
                            name = document.getString("name") ?: "",
                            price = document.getDouble("price")?.toInt() ?: 0,
                            imageUrl = document.getString("imageUrl") ?: "",
                            describe = document.getString("describe") ?: "",
                            author = document.getString("author") ?: "",
                            phoneAuthor = document.getString("phoneAuthor") ?: "",
                            images = (document.get("listImage") as? List<*>)?.filterIsInstance<String>()
                                ?.toCollection(ArrayList()) ?: ArrayList(),
                            id_author =  document.getString("id_author") ?: "",
                        ).takeIf { it.address >= name && it.address <= name + "\uf8ff" }
                    }

                    val adapter = PopularAdapterHouse(filteredByPrice, this)
                    binding.Pupuplist.layoutManager = LinearLayoutManager(this)
                    binding.Pupuplist.adapter = adapter

                load.visibility = View.GONE
            }
            .addOnFailureListener {
                it.printStackTrace()
                load.visibility = View.GONE
            }
    }
    override fun onItemClick(houseData: HouseData) {
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra("ITEM_NAME", houseData.name)
            putExtra("ITEM_AREA", houseData.area)
            putExtra("ITEM_PRICE", houseData.price)
            putExtra("ITEM_IMAGE_URL", houseData.imageUrl)
            putExtra("ITEM_DESCRIBE", houseData.describe)
            putExtra("ITEM_AUTHOR", houseData.author)
            putExtra("ITEM_ID_AUTHOR", houseData.id_author)
            putExtra("ITEM_AUTHOR_IMAGE", houseData.image_author)
            putExtra("ITEM_AUTHOR_PHONE", houseData.phoneAuthor)
            putStringArrayListExtra("ITEM_IMAGES",houseData.images)
        }
        startActivity(intent)
    }
}