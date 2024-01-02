package com.example.shopmatemobile.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.shopmatemobile.ImageFragment

class ImagePagerAdapter(
    fragmentManager: FragmentManager,
    private val imageUrls: List<String>
) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int = imageUrls.size

    override fun getItem(position: Int): Fragment {
        return ImageFragment.newInstance(imageUrls[position])
    }
}


