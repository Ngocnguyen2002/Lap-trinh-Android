package com.example.renthouse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val btn = findViewById<Button>(R.id.button4)

        btn.setOnClickListener{
            create()
        }
    }

    fun create() {
        val db = Firebase.firestore
        val fl_name = findViewById<TextView>(R.id.editTextText)
        val fl_add = findViewById<TextView>(R.id.editTextText2)
        val fl_email = findViewById<TextView>(R.id.editTextTextEmailAddress)
        val fl_phone = findViewById<TextView>(R.id.editTextPhone2)
        val fl_pass = findViewById<TextView>(R.id.editTextTextPassword)
        val fl_passcf = findViewById<TextView>(R.id.editTextTextPassword2)

        if (fl_passcf.text.toString() != fl_pass.text.toString()) {
            // Hiển thị thông báo lỗi nếu mật khẩu mới và mật khẩu xác nhận không khớp
            Toast.makeText(this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show()
        } else {
            val phone = fl_phone.text.toString()

            db.collection("users")
                .whereEqualTo("phone", phone)
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.isEmpty) {
                        val userInfo = hashMapOf(
                            "full_name" to fl_name.text.toString(),
                            "imageAvt" to "https://cellphones.com.vn/sforum/wp-content/uploads/2024/02/anh-avatar-cute-95.jpg",
                            "password" to fl_pass.text.toString(),
                            "address" to fl_add.text.toString(),
                            "phone" to fl_phone.text.toString(),
                            "email" to fl_email.text.toString()
                        )

                        db.collection("users")
                            .add(userInfo)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Đăng ký tài khoản thành công", Toast.LENGTH_SHORT).show()
                                val handler = Handler()
                                handler.postDelayed({
                                    finish()
                                }, 1000) // Chờ 1 giây (1000 milliseconds)
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Đang có lỗi xảy ra vui lòng thử lại sau", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        // Số điện thoại đã tồn tại, hiển thị thông báo lỗi
                        Toast.makeText(this, "Số điện thoại đã được đăng ký", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Đang có lỗi xảy ra vui lòng thử lại sau", Toast.LENGTH_SHORT).show()
                }
        }
    }

}