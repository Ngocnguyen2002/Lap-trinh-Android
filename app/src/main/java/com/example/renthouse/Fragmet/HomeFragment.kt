package com.example.renthouse.Fragmet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.renthouse.DetailActivity
import com.example.renthouse.SharedPreferencesUtil.getValueFromSharedPreferences
import com.example.renthouse.UserInfo
import com.example.renthouse.adaptar.HouseData
import com.example.renthouse.adaptar.PopularAdapter
import com.example.renthouse.databinding.FragmentHomeBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment(), PopularAdapter.OnItemClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        _binding?.let { binding ->  // Ensure binding is not null
            val imageList = ArrayList<SlideModel>()
            imageList.add(SlideModel("https://mads.media/wp-content/uploads/2023/09/370-36-12243-SE-25th-Street-Bellevue-WA-MN-Custom-Homes-Exterior.jpg", "The animal population decreased by 58 percent in 42 years."))
            imageList.add(SlideModel("https://hensleyhomes.com/wp-content/uploads/2022/04/IMG_6215-1-1024x693.jpg", "Elephants and tigers may become extinct."))
            imageList.add(SlideModel("https://hammersmithstructures.com/wp-content/uploads/2023/09/9200WildflowerDrive_JessBlackwellPhotography_017.jpg", "And people do that."))

            val houseDataListApi = mutableListOf<HouseData>()

            val imageSlider: ImageSlider = binding.imageSlider
            imageSlider.setImageList(imageList, ScaleTypes.FIT)

            val db = Firebase.firestore
            db.collection("house")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val id_house = document.id
                        houseDataListApi.add(
                            HouseData(
                                id_house = id_house,
                                area = document.getString("area") ?: "",
                                address = document.getString("address") ?: "",
                                image_author = document.getString("image_author") ?: "",
                                name = document.getString("name") ?: "",
                                price = document.getDouble("price")?.toInt() ?: 0, // Lưu ý rằng price là Int, không phải String
                                imageUrl = document.getString("imageUrl") ?: "",
                                describe = document.getString("describe") ?: "",
                                author = document.getString("author") ?: "",
                                id_author = document.getString("id_author") ?: "",
                                phoneAuthor = document.getString("phoneAuthor") ?: "",
                                images = (document.get("listImage") as? List<*>)?.filterIsInstance<String>()?.toCollection(ArrayList()) ?: ArrayList()
                            )
                        )
                    }
                    val adapter = PopularAdapter(houseDataListApi, this)
                    binding.Pupuplist.layoutManager = LinearLayoutManager(requireContext())
                    binding.Pupuplist.adapter = adapter
                }
                .addOnFailureListener { exception ->
                    // Handle the error
                }
        }
    }

    override fun onItemClick(houseData: HouseData) {
        val value = context?.getValueFromSharedPreferences("user_info")
        val intent = Intent(requireContext(), DetailActivity::class.java).apply {
            putExtra("ITEM_ID", houseData.id_house)
            putExtra("ITEM_NAME", houseData.name)
            putExtra("ITEM_AREA", houseData.area)
            putExtra("ITEM_PRICE", houseData.price)
            putExtra("ITEM_IMAGE_URL", houseData.imageUrl)
            putExtra("ITEM_DESCRIBE", houseData.describe)
            putExtra("ITEM_AUTHOR", houseData.author)
            putExtra("ITEM_ID_AUTHOR", houseData.id_author)
            putExtra("ITEM_AUTHOR_IMAGE", houseData.image_author)
            putExtra("ITEM_AUTHOR_PHONE", houseData.phoneAuthor)
            putExtra("ITEM_USER", value?.id)
            putStringArrayListExtra("ITEM_IMAGES", houseData.images)
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
