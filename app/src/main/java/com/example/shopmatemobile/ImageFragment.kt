package com.example.shopmatemobile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ImageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ImageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    companion object {
        private const val IMAGE_URL = "imageUrl"

        fun newInstance(imageUrl: String): ImageFragment {
            val args = Bundle()
            args.putString(IMAGE_URL, imageUrl)
            val fragment = ImageFragment()
            fragment.arguments = args
            return fragment
        }
    }
    private lateinit var imageUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imageUrl = it.getString(IMAGE_URL, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_image, container, false)
        val imageView: ImageView = rootView.findViewById<ImageView>(R.id.imageView)
        // Встановлюємо зображення у ваш ImageView
        // Glide або Picasso можна використовувати для завантаження зображення з URL до ImageView
        Glide.with(this).load(imageUrl).into(imageView)
        return rootView
    }



}