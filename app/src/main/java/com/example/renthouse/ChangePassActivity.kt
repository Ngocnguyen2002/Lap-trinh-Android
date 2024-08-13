package com.example.renthouse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.renthouse.SharedPreferencesUtil.getValueFromSharedPreferences
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.example.renthouse.SharedPreferencesUtil.getValueFromSharedPreferences
import android.content.Context
import android.os.Handler
import com.example.renthouse.SharedPreferencesUtil.getValueFromSharedPreferences
import kotlinx.coroutines.delay

class ChangePassActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_pass)
        val imageView: ImageView = findViewById(R.id.imageButton4)
        val id = intent.getStringExtra("ID").toString()

        imageView.setOnClickListener {
            finish()
        }
        val pass_old = findViewById<TextView>(R.id.editTextTextPassword3)
        val pass_new = findViewById<TextView>(R.id.editTextTextPassword4)
        val pass_cf = findViewById<TextView>(R.id.editTextTextPassword5)
        val btn = findViewById<Button>(R.id.button10)
//        name.text = value?.name
        btn.setOnClickListener {
            val oldPassword = pass_old.text.toString()
            val newPassword = pass_new.text.toString()
            val confirmPassword = pass_cf.text.toString()

            if (newPassword != confirmPassword) {
                // Hiển thị thông báo lỗi nếu mật khẩu mới và mật khẩu xác nhận không khớp
                Toast.makeText(this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val db = Firebase.firestore
            val userRef = db.collection("users").document(id) // Giả sử `id` là ID của tài khoản người dùng

            userRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document != null && document.exists()) {
                        val currentPassword = document.getString("password")
                        if (currentPassword == oldPassword) {
                            userRef.update("password", newPassword)
                                .addOnCompleteListener { updateTask ->
                                    if (updateTask.isSuccessful) {
                                        Toast.makeText(this, "Cập nhật mật khẩu thành công", Toast.LENGTH_SHORT).show()

                                        val handler = Handler()
                                        handler.postDelayed({
                                            finish()
                                        }, 1000) // Chờ 1 giây (1000 milliseconds)

                                    } else {
                                        Toast.makeText(this, "Lỗi khi cập nhật mật khẩu", Toast.LENGTH_SHORT).show()
                                        Log.e("TAG", "Lỗi khi cập nhật mật khẩu", updateTask.exception)
                                    }
                                }
                        } else {
                            Toast.makeText(this, "Mật khẩu hiện tại không đúng", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Không tìm thấy tài khoản" , Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Lỗi khi đọc dữ liệu từ Firestore", Toast.LENGTH_SHORT).show()
                    Log.e("TAG", "Lỗi khi đọc dữ liệu từ Firestore", task.exception)
                }
            }
        }


    }
}