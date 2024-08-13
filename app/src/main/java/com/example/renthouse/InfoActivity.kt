package com.example.renthouse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class InfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        val id = intent.getStringExtra("ID").toString()
        val name = intent.getStringExtra("NAME_U").toString()
        val address = intent.getStringExtra("ADDRESS_U").toString()
        val phone = intent.getStringExtra("PHONE_U").toString()
        val email = intent.getStringExtra("EMAIL_U").toString()
        val name_input = findViewById<TextView>(R.id.editTextText4)
        val phone_input = findViewById<TextView>(R.id.editTextText6)
        val address_input = findViewById<TextView>(R.id.editTextText7)
        val gender = findViewById<TextView>(R.id.editTextText8)
        val btn = findViewById<Button>(R.id.button11)
        val email_input = findViewById<TextView>(R.id.editTextText3)

        name_input.text = name
        phone_input.text = phone
        address_input.text = address
        gender.text = "nam"
        email_input.text = email

//        name.text = value?.name

        val back : ImageView = findViewById(R.id.imageView26)
        back.setOnClickListener{
            finish()
        }
        btn.setOnClickListener {
            val oldPassword = name_input.text.toString()
            val newPassword = phone_input.text.toString()
            val confirmPassword = address_input.text.toString()

            val db = Firebase.firestore
            val userRef = db.collection("users").document(id) // Giả sử `id` là ID của tài khoản người dùng

            userRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document != null && document.exists()) {
                        val updates = mutableMapOf<String, Any>()
                        updates["address"] =  confirmPassword
                        updates["full_name"] = oldPassword
                        updates["phone"] = newPassword
                        userRef.update(updates)
                            .addOnSuccessListener {
                                // Xử lý khi cập nhật thành công
                                Toast.makeText(this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show()
                                val handler = Handler()
                                handler.postDelayed({
                                    finish()
                                }, 1000) // Chờ 1 giây (1000 milliseconds)
                            }
                            .addOnFailureListener { exception ->
                                // Xử lý khi cập nhật thất bại
                                Toast.makeText(this, "Xử lý khi cập nhật thất bại", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(this, "Không tìm thấy tài khoản" + id, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Lỗi khi đọc dữ liệu từ Firestore", Toast.LENGTH_SHORT).show()
                    Log.e("TAG", "Lỗi khi đọc dữ liệu từ Firestore", task.exception)
                }
            }
        }

    }
}