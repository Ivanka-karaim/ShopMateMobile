package com.example.shopmatemobile

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.DialogFragment
import androidx.viewpager.widget.ViewPager
import com.example.shopmatemobile.adapter.ImagePagerAdapter

class ImageDialogFragment : DialogFragment() {

    private lateinit var viewPager: ViewPager
    private lateinit var closeButton: ImageButton

    lateinit var imageUrls: List<String>
     var currentPosition: Int = 0

    override fun onResume() {
        super.onResume()
        val params = dialog?.window?.attributes
        params?.width = ViewGroup.LayoutParams.MATCH_PARENT
        params?.height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }

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

        closeButton.setOnClickListener {
            dismiss()
        }

        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        view?.setBackgroundColor(Color.parseColor("#СС000000"))
        return view
    }

    private fun showImageAtPosition(position: Int) {
        if (position in imageUrls.indices) {
            viewPager.currentItem = position
            currentPosition = position
        }
    }
}
