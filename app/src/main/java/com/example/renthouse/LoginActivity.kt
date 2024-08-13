package com.example.renthouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import android.content.Context
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson

data class UserInfo(
    val name: String,
    val id: String,
    val imageAvt: String,
    val address : String,
    val phone : String,
    val email : String
)

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val btnLogin = findViewById<Button>(R.id.button3)
        val txtPhone = findViewById<TextView>(R.id.editTextPhone)
        val txtPass = findViewById<TextView>(R.id.editTextNumberPassword)

        btnLogin.setOnClickListener {
            // Chuyển sang trang đăng nhập

            val db = Firebase.firestore
            db.collection("users")
                .whereEqualTo("phone", txtPhone.text.toString())  // phoneNumber là giá trị số điện thoại bạn muốn tìm
                .whereEqualTo("password", txtPass.text.toString())  // password là giá trị mật khẩu bạn muốn tìm
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.isEmpty) {
//                        showAlertDialog("Thông tin không chính xác")
                        Toast.makeText(this, "Thông tin tài khoản không chính xác!", Toast.LENGTH_SHORT).show()

                    } else {
                        for (document in documents) {
//                            val userInfo =
//                                document.toObject(UserInfo::class.java)
                            val userInfo = UserInfo(
                                name = document.getString("full_name") ?: "",
                                id = document.id.toString(),
                                imageAvt = document.getString("imageAvt") ?: "",
                                address = document.getString("address") ?: "",
                                phone = document.getString("phone") ?: "",
                                email = document.getString("email") ?: "",
                            )
//                            showAlertDialog(userInfo.toString())
                            val context = applicationContext
                            val sharedPreferences =
                                context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putString("user_info", Gson().toJson(userInfo))
                            editor.apply()
                            val intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    println(exception)
                }
        }


    }

    private fun showAlertDialog(contetn : String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Thông báo")
        builder.setMessage(contetn)
//        builder.setPositiveButton("Yes") { dialog, which ->
//            /*onBackPressed() */// Quay lại màn hình trước đó khi nhấn Yes
//        }
        builder.setNegativeButton("Oki") { dialog, which ->
            dialog.dismiss() // Đóng dialog khi nhấn No
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}