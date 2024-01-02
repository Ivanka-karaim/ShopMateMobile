package com.example.shopmatemobile

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.marginEnd
import com.bumptech.glide.Glide
import com.example.shopmatemobile.addResources.RetrofitClient
import com.example.shopmatemobile.addResources.RetrofitClient2
import com.example.shopmatemobile.addResources.SharedPreferencesFactory
import com.example.shopmatemobile.api.FavouriteApi
import com.example.shopmatemobile.api.ProductApi
import com.example.shopmatemobile.api.ReviewApi
import com.example.shopmatemobile.databinding.ActivityProductBinding
import com.example.shopmatemobile.databinding.ItemReviewBinding
import com.example.shopmatemobile.model.Favourite
import com.example.shopmatemobile.model.ProductShopMate
import com.example.shopmatemobile.model.ReviewForAdd
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.create
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale



class ProductActivity : AppCompatActivity() {
    lateinit var binding: ActivityProductBinding
    var productId: String = ""
    var isFavourite: Boolean = false
    lateinit var product: ProductShopMate
     var grade: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(binding.root)




        val receivedId = intent.getStringExtra("ID")
        if (receivedId != null) {
            productId = receivedId
        } else {
            println("Error")
            onBackPressed()
        }


        var favouriteApi = RetrofitClient.getInstance().create(FavouriteApi::class.java)
        var productApi = RetrofitClient2.getInstance().create(ProductApi::class.java)
        var reviewApi = RetrofitClient.getInstance().create(ReviewApi::class.java)
        var token = SharedPreferencesFactory(this).getToken()!!

        CoroutineScope(Dispatchers.IO).launch {
            isFavourite = favouriteApi.checkFavourite( "Bearer " + token, productId)
            var productDB = productApi.getProductById(productId)
            product = ProductShopMate(
                id = productDB.id,
                title = productDB.title,
                description = productDB.description,
                price = productDB.price,
                brand = productDB.brand,
                category = productDB.category,
                thumbnail = productDB.thumbnail,
                images = productDB.images,
                isFavourite = isFavourite,
                grade = reviewApi.getGradeForProduct(productDB.id.toString(), "Bearer " + token)
            )
            val reviewList = reviewApi.getReviews(token="Bearer " + token, idProduct = productDB.id.toString() )
            runOnUiThread {
                Glide.with(this@ProductActivity)
                    .load(product.thumbnail)
                    .into(binding.generalPhoto)

                if(product.isFavourite){
                    binding.likeIcon.setImageResource(R.drawable.baseline_favorite_24)
                }else{
                    binding.likeIcon.setImageResource(R.drawable.baseline_favorite_border_24)
                }

                binding.nameProduct.text = product.title
                binding.descriptionProduct.text = product.description
                binding.brandProduct.text = product.brand
                binding.categoryProduct.text = product.category
                binding.price.text = product.price.toString()

                for(image in product.images){
                    val imageView = ImageView(this@ProductActivity)
                    val params = LinearLayout.LayoutParams(
                        dpToPx(40),
                        dpToPx(40)
                    )
                    params.setMargins(dpToPx(5), 0, dpToPx(5), 0)

                    imageView.layoutParams = params

                    Glide.with(this@ProductActivity)
                        .load(image)
                        .into(imageView)
                    binding.photos.addView(imageView)
                }


                val linearLayout = findViewById<LinearLayout>(R.id.reviews)

                for (review in reviewList) {
                    val itemView = layoutInflater.inflate(R.layout.item_review, null)
                    val userName = itemView.findViewById<TextView>(R.id.reviewUserName)
                    val userSurname = itemView.findViewById<TextView>(R.id.reviewUserSurname)
                    val submitOrder = itemView.findViewById<LinearLayout>(R.id.submitOrder)
                    var description = itemView.findViewById<TextView>(R.id.descriptionReview)
                    var date = itemView.findViewById<TextView>(R.id.dateReview)


                    userName.text = review.userForReview.firstName
                    userSurname.text = review.userForReview.lastName
                    if(review.isVerified)
                    submitOrder.visibility = View.VISIBLE
                    else {
                        submitOrder.visibility = View.GONE
                        submitOrder.layoutParams = LinearLayout.LayoutParams(0, 0)
                    }
                    description.text = review.text
                    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
                    val outputFormat = SimpleDateFormat("d MMMM yyyy", Locale("uk", "UA"))

                    try {
                        val dateTime = inputFormat.parse(review.date)
                        val formattedDate = outputFormat.format(dateTime)
                        date.text = formattedDate
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }
                    if (review.isThisUser){

                        itemView.findViewById<Button>(R.id.deleteReview).visibility = View.VISIBLE
                    } else{
                        itemView.findViewById<Button>(R.id.deleteReview).visibility = View.INVISIBLE
                    }

                    itemView.findViewById<Button>(R.id.deleteReview).setOnClickListener {
                        CoroutineScope(Dispatchers.IO).launch {
                            reviewApi.deleteReview("Bearer " + token, review.id);
                        }
                        finish()
                        startActivity(intent)
                    }
                    var linearLayoutGrade = itemView.findViewById<LinearLayout>(R.id.grades)
                    for(i in 1..review.rating.toInt()) {
                        val imageView = ImageView(this@ProductActivity)
                        val params = LinearLayout.LayoutParams(
                            dpToPx(20),
                            dpToPx(20)
                        )
                        imageView.setImageResource(R.drawable.baseline_grade_dark_24)
                        imageView.layoutParams = params
                        linearLayoutGrade.addView(imageView)
                    }
                    for (i in review.rating.toInt()+1..5){
                        val imageView = ImageView(this@ProductActivity)
                        val params = LinearLayout.LayoutParams(
                            dpToPx(20),
                            dpToPx(20)
                        )
                        imageView.setImageResource(R.drawable.baseline_grade_24)
                        imageView.layoutParams = params
                        linearLayoutGrade.addView(imageView)
                    }
                    linearLayout.addView(itemView)


                }



            }



            }
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.custom_pop_up_add_review)
        dialog.setCancelable(true)
        var linearLayoutGrade = dialog.findViewById<LinearLayout>(R.id.gradesForAdd)
        for(i in 1..5) {
            val imageView = ImageView(this)
            val params = LinearLayout.LayoutParams(
                dpToPx(20),
                dpToPx(20)
            )
            imageView.setImageResource(R.drawable.baseline_grade_dark_24)
            imageView.setOnClickListener {
                editGrade(i, dialog)
            }
            imageView.layoutParams = params
            linearLayoutGrade.addView(imageView)


        }
        dialog.findViewById<Button>(R.id.addReview).setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                var review = ReviewForAdd(productId, dialog.findViewById<EditText>(R.id.textReview).text.toString(), grade.toDouble())
                reviewApi.addReview("Bearer "+token, review)
            }
            finish()
            startActivity(intent)
        }


        binding.addReview.setOnClickListener {
            val closeButton = dialog.findViewById<ImageButton>(R.id.closeButton)
            closeButton.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()


        }
        binding.likeIcon.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                favouriteApi.changeFavourite(Favourite(product.id.toString()), "Bearer "+SharedPreferencesFactory(this@ProductActivity).getToken()!!)
            }
            product.isFavourite = !product.isFavourite
            if(product.isFavourite){
                binding.likeIcon.setImageResource(R.drawable.baseline_favorite_24)
            }else{
                binding.likeIcon.setImageResource(R.drawable.baseline_favorite_border_24)
            }
        }

        binding.back.setOnClickListener {
            onBackPressed()
        }
        }
    fun editGrade(i:Int, dialog: Dialog){
        grade = i
        val linearLayout = dialog.findViewById<LinearLayout>(R.id.gradesForAdd)
        linearLayout.removeAllViews()
        for(i in 1..i) {
            val imageView = ImageView(this@ProductActivity)
            val params = LinearLayout.LayoutParams(
                dpToPx(20),
                dpToPx(20)
            )
            imageView.setImageResource(R.drawable.baseline_grade_dark_24)
            imageView.layoutParams = params
            linearLayout.addView(imageView)
        }
        for (i in i+1..5){
            val imageView = ImageView(this@ProductActivity)
            val params = LinearLayout.LayoutParams(
                dpToPx(20),
                dpToPx(20)
            )
            imageView.setImageResource(R.drawable.baseline_grade_24)
            imageView.layoutParams = params
            linearLayout.addView(imageView)
        }


    }
    fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }




    }
