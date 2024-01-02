package com.example.shopmatemobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.viewpager.widget.ViewPager
import com.example.shopmatemobile.adapter.ImagePagerAdapter

class ImageDialogFragment : DialogFragment() {

    private lateinit var viewPager: ViewPager
    private lateinit var closeButton: Button

    lateinit var imageUrls: List<String>
     var currentPosition: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_image_dialog, container, false)

        viewPager = view.findViewById(R.id.viewPager)
        closeButton = view.findViewById(R.id.closeButton)


        println(imageUrls)
        val adapter = ImagePagerAdapter(childFragmentManager, imageUrls)
        viewPager.adapter = adapter
        viewPager.currentItem = currentPosition

        // Обробник подій для закриття діалогового вікна при натисканні на кнопку "Закрити"
        closeButton.setOnClickListener {
            dismiss()
        }

        return view
    }

    private fun showImageAtPosition(position: Int) {
        if (position in imageUrls.indices) {
            viewPager.currentItem = position
            currentPosition = position
        }
    }
}
