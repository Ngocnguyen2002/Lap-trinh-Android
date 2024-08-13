package com.example.renthouse

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.renthouse.Fragmet.HistoryFragment
import com.example.renthouse.adaptar.MessageAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

data class Message(
    val from: String? = null,
    val mess: String? = null
)

class MessageActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messages: MutableList<Message>
    private lateinit var databases: FirebaseDatabase
    private lateinit var messageReference: DatabaseReference
    private  lateinit var txtSend : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        val name = intent.getStringExtra("ITEM_NAME")
        val type = intent.getStringExtra("PAY_LOAD") ?: ""
        val area = intent.getStringExtra("ITEM_AREA")
        val author_image = intent.getStringExtra("ITEM_AUTHOR_IMAGE")
        val price = intent.getIntExtra("ITEM_PRICE", 0)
        val imageUrl = intent.getStringExtra("ITEM_IMAGE_URL")
        val describe = intent.getStringExtra("ITEM_DESCRIBE")
        val listImages = intent.getStringArrayListExtra("ITEM_IMAGES")
        val author = intent.getStringExtra("ITEM_AUTHOR")
//            putExtra("ITEM_AUTHOR_IMAGE", houseData.phoneAuthor)

        val phone = intent.getStringExtra("ITEM_AUTHOR_PHONE")

        val user_id = intent.getStringExtra("ITEM_MESS")
        val mess_id = intent.getStringExtra("ITEM_ID_MESS")
        val txtname : TextView = findViewById(R.id.textView32)
        txtname.text = author.toString()

        val txtphone : TextView = findViewById(R.id.textView33)
        txtphone.text = phone.toString()

        val authorImage: ImageView = findViewById(R.id.imageView21)
        Glide.with(this).load(author_image.toString()).into(authorImage)

        val editTextPhone = findViewById<ImageView>(R.id.imageView22)
        editTextPhone.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phone")
            }
            startActivity(intent)
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        messages = mutableListOf()
        messageAdapter = MessageAdapter(messages,user_id.toString())
        recyclerView.adapter = messageAdapter

        val back = findViewById<ImageView>(R.id.imageView20)
        back.setOnClickListener {
            if(type != ""){
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("SHOW_HISTORY_FRAGMENT", true)
                startActivity(intent)
            }else{
                finish()
            }
        }

        val database = FirebaseDatabase.getInstance("https://rent-house-3fe8d-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val postReference = database.getReference("${mess_id}/content")

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                messages.clear()
                for (snapshot in dataSnapshot.children) {
                    val message = snapshot.getValue(Message::class.java)

                    message?.let {
                        // Kiểm tra nếu 'from' và 'mess' không null trước khi thêm vào danh sách
                        if (it.from != null && it.mess != null) {
                            messages.add(it)
                            Log.d(TAG, "From: ${it.from}, Message: ${it.mess}")
                        }
                    }
                }
                messageAdapter.notifyDataSetChanged()
                recyclerView.scrollToPosition(messages.size - 1)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting data failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }

        postReference.addValueEventListener(postListener)
        val btnSend = findViewById<ImageView>(R.id.imageView24)
        txtSend = findViewById<TextView>(R.id.editTextText9)

        btnSend.setOnClickListener {
            databases = FirebaseDatabase.getInstance("https://rent-house-3fe8d-default-rtdb.asia-southeast1.firebasedatabase.app/")
            messageReference = database.getReference("${mess_id}/content")

            // Gửi tin nhắn khi cần
            sendMessage(user_id.toString(), txtSend.text.toString())
        }

    }
    private fun sendMessage(from: String, message: String) {
        val newMessageRef = messageReference.push()
        if(message.isNotEmpty()){
            val newMessage = Message(from = from, mess = message)

            newMessageRef.setValue(newMessage)
                .addOnSuccessListener {
                    // Ghi tin nhắn thành công
                    txtSend.text =""
                    recyclerView.scrollToPosition(messages.size - 1)
                    Log.d(TAG, "Message sent successfully!")
                }
                .addOnFailureListener { e ->
                    // Xảy ra lỗi khi ghi tin nhắn
                    Log.e(TAG, "Error sending message: $e")
                }
        }

    }
}

