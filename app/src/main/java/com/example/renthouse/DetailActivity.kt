package com.example.renthouse

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.renthouse.SharedPreferencesUtil.getValueFromSharedPreferences
import com.example.renthouse.adapter.ImageHorizontalAdapter
import com.example.renthouse.databinding.ActivityDetailBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import java.text.DecimalFormat

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var databases: FirebaseDatabase
    private lateinit var messageReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id_house = intent.getStringExtra("ITEM_ID")
        val name = intent.getStringExtra("ITEM_NAME")
        val area = intent.getStringExtra("ITEM_AREA")
        val author_image = intent.getStringExtra("ITEM_AUTHOR_IMAGE")
        val price = intent.getIntExtra("ITEM_PRICE", 0)
        val imageUrl = intent.getStringExtra("ITEM_IMAGE_URL")
        val describe = intent.getStringExtra("ITEM_DESCRIBE")
        val listImages = intent.getStringArrayListExtra("ITEM_IMAGES")
        val author = intent.getStringExtra("ITEM_AUTHOR")
//            putExtra("ITEM_AUTHOR_IMAGE", houseData.phoneAuthor)
        val phone = intent.getStringExtra("ITEM_AUTHOR_PHONE")
        val id_user = intent.getStringExtra("ITEM_USER")

        val id_author = intent.getStringExtra("ITEM_ID_AUTHOR")



        val imageView: ImageView = findViewById(R.id.imageView8)
        imageView.setOnClickListener {
            finish()
        }


        val editTextPhone = findViewById<ImageView>(R.id.imageView14)
        editTextPhone.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phone")
            }
            startActivity(intent)
        }
        val mess: ImageView = findViewById(R.id.imageView13)
        val db = Firebase.firestore

        mess.setOnClickListener {
            if(id_user == id_author) {
                Toast.makeText(this, "Bạn không thể liên lạc với chính mình!", Toast.LENGTH_SHORT).show()
            } else {
                val query = db.collection("message")
                    .whereEqualTo("create", id_user)
                    .whereEqualTo("house", db.document("/house/${id_house}"))
                    .whereEqualTo("invite", id_author)

                query.get()
                    .addOnSuccessListener { documents ->
                        if (documents.isEmpty) {
                            Log.d(TAG, "Data does not exist")

                            val messageRef = db.collection("message").document()
                            val data = hashMapOf(
                                "create" to id_user,
                                "house" to db.document("/house/${id_house}"),
                                "invite" to id_author
                            )

                            messageRef.set(data)
                                .addOnSuccessListener {
                                    val newDocumentId = messageRef.id
                                    creatRoomMessage(newDocumentId,id_user.toString(),id_author.toString())
                                    chuyentab(author.toString(),author_image.toString(),phone.toString(),id_user.toString(),id_author.toString())
                                    Log.d(TAG, "Document created successfully")
                                }
                                .addOnFailureListener { e ->
                                    Log.w(TAG, "Error creating document", e)
                                }
                        } else {
                            Log.d(TAG, "Data already exists")
                            for (document in documents) {
                                val intent = Intent(this, MessageActivity::class.java).apply {
                                    putExtra("ITEM_NAME", "")
                                    putExtra("ITEM_AREA", "")
                                    putExtra("ITEM_PRICE", "")
                                    putExtra("ITEM_IMAGE_URL", "")
                                    putExtra("ITEM_DESCRIBE", "")
                                    putExtra("ITEM_AUTHOR", author)
                                    putExtra("ITEM_AUTHOR_IMAGE", author_image)
                                    putExtra("ITEM_AUTHOR_PHONE", phone)
                                    putExtra("ITEM_MESS",id_user )
                                    putExtra("ITEM_ID_MESS", document.id)
                                    putExtra("PAY_LOAD", "YES")
//                                    putStringArrayListExtra("ITEM_IMAGES", houseData.images)
                                }
                                startActivity(intent)
                            }

                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.w(TAG, "Error getting documents: ", exception)
                    }
            }
        }

        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(listImages?.get(0), "The animal population decreased by 58 percent in 42 years."))
        imageList.add(SlideModel(listImages?.get(1), "Elephants and tigers may become extinct."))
        imageList.add(SlideModel(listImages?.get(2), "And people do that."))
        imageList.add(SlideModel(listImages?.get(3), "And people do that."))

        val imageSlider: ImageSlider = binding.imageMain
        imageSlider.setImageList(imageList, ScaleTypes.FIT)
        // Nhận dữ liệu từ Intent

        val pricetxt : TextView = binding.textView20
        pricetxt.text = "Giá : "  + DecimalFormat("#,###").format(price) + " VND"

        val destxt : TextView = binding.textView15
        destxt.text =  "Nội thất : " +describe
        val areatxt : TextView = binding.textView14
        areatxt.text =  "Diện tích: " + area
        val authortxt : TextView = binding.textView17
        authortxt.text =  author

//        Glide.with(this).load(imageUrl).into(binding.imageMain)
        Glide.with(this).load(author_image).into(binding.imageView12)
        val recyclerViewNgang: RecyclerView = findViewById(R.id.linearLayout)
        recyclerViewNgang.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewNgang.adapter = ImageHorizontalAdapter(listImages as ArrayList<String>)


        val authorImage: ImageView = findViewById(R.id.imageView12)
        Glide.with(this).load("https://img.tapimg.net/market/images/c3b27c763ad721c9203da1f665baedeb.jpg/appicon").into(authorImage)
    }
    private fun creatRoomMessage(id : String , create : String, invite : String) {
        val database = Firebase.database("https://rent-house-3fe8d-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val newCollectionRef = database.getReference(id)

        val data = hashMapOf(
            "create" to create,
            "invite" to invite
        )

        newCollectionRef.setValue(data)
            .addOnSuccessListener {
                Log.d(TAG, "Node created successfully")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error creating node", e)
            }
    }
    fun chuyentab (author : String,author_image : String,phone : String ,id_user : String, id : String){

        val intent = Intent(this, MessageActivity::class.java).apply {
            putExtra("ITEM_NAME", "")
            putExtra("PAY_LOAD", "YES")
            putExtra("ITEM_AREA", "")
            putExtra("ITEM_PRICE", "")
            putExtra("ITEM_IMAGE_URL", "")
            putExtra("ITEM_DESCRIBE", "")
            putExtra("ITEM_AUTHOR", author)
            putExtra("ITEM_AUTHOR_IMAGE", author_image)
            putExtra("ITEM_AUTHOR_PHONE", phone)
            putExtra("ITEM_MESS",id_user )
            putExtra("ITEM_ID_MESS", id)
//                                    putStringArrayListExtra("ITEM_IMAGES", houseData.images)
        }
        startActivity(intent)
    }

}