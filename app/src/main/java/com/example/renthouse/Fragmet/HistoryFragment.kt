package com.example.renthouse.Fragmet

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.renthouse.MessageActivity
import com.example.renthouse.SharedPreferencesUtil.getValueFromSharedPreferences
import com.example.renthouse.adaptar.HouseData
import com.example.renthouse.adaptar.ItemData
import com.example.renthouse.adaptar.ItemMessHouse
import com.example.renthouse.databinding.FragmentHistoryBinding
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.util.Log
import com.google.firebase.firestore.QuerySnapshot


class HistoryFragment : Fragment(), ItemMessHouse.OnItemClickListener {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private var user_id: String? = null
    private val TAG = "HistoryFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val value = context?.getValueFromSharedPreferences("user_info")
        user_id = value?.id
        val db = Firebase.firestore

        val createQuery = db.collection("message")
            .whereEqualTo("create", user_id)
        val inviteQuery = db.collection("message")
            .whereEqualTo("invite", user_id)

        Tasks.whenAllComplete(createQuery.get(), inviteQuery.get())
            .addOnSuccessListener { tasks ->
                val houseDataListApi = mutableListOf<ItemData>()
                val resultSet = mutableSetOf<String>() // Set để tránh trùng lặp

                tasks.forEach { task ->
                    val result = task.result as QuerySnapshot
                    result.documents.forEach { document ->
                        val id = document.id
                        if (!resultSet.contains(id)) { // Kiểm tra nếu ID đã được thêm
                            resultSet.add(id)

                            val houseRef = document.getDocumentReference("house")
                            houseRef?.let {
                                it.get().addOnSuccessListener { houseSnapshot ->
                                    houseSnapshot?.let {
                                        val itemData = ItemData(
                                            area = houseSnapshot.getString("area") ?: "",
                                            address = houseSnapshot.getString("address") ?: "",
                                            image_author = houseSnapshot.getString("image_author") ?: "",
                                            name = houseSnapshot.getString("name") ?: "",
                                            price = houseSnapshot.getDouble("price")?.toInt() ?: 0,
                                            imageUrl = houseSnapshot.getString("imageUrl") ?: "",
                                            describe = houseSnapshot.getString("describe") ?: "",
                                            author = houseSnapshot.getString("author") ?: "",
                                            phoneAuthor = houseSnapshot.getString("phoneAuthor") ?: "",
                                            images = (houseSnapshot.get("listImage") as? List<*>)?.filterIsInstance<String>()?.toCollection(ArrayList()) ?: ArrayList(),
                                            id_mess = id
                                        )
                                        houseDataListApi.add(itemData)
                                        val adapter = ItemMessHouse(houseDataListApi, this)
                                        binding.Pupuplist.layoutManager = LinearLayoutManager(requireContext())
                                        binding.Pupuplist.adapter = adapter
                                    }
                                }
                            }
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }

    }

    override fun onItemClick(houseData: ItemData) {
        val intent = Intent(requireContext(), MessageActivity::class.java).apply {
            putExtra("ITEM_NAME", houseData.name)
            putExtra("ITEM_AREA", houseData.area)
            putExtra("ITEM_PRICE", houseData.price)
            putExtra("ITEM_IMAGE_URL", houseData.imageUrl)
            putExtra("ITEM_DESCRIBE", houseData.describe)
            putExtra("ITEM_AUTHOR", houseData.author)
            putExtra("ITEM_AUTHOR_IMAGE", houseData.image_author)
            putExtra("ITEM_AUTHOR_PHONE", houseData.phoneAuthor)
            putExtra("ITEM_MESS", user_id)
            putExtra("ITEM_ID_MESS", houseData.id_mess)
            putStringArrayListExtra("ITEM_IMAGES", houseData.images)
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
