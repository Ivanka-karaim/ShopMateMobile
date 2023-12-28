package com.example.shopmatemobile

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.example.shopmatemobile.databinding.FragmentProfileBinding
import com.example.shopmatemobile.service.ProfileService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Profile.newInstance] factory method to
 * create an instance of this fragment.
 */
class Profile : Fragment() {
    private lateinit var profile: com.example.shopmatemobile.model.Profile
    lateinit var binding : FragmentProfileBinding
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        binding = FragmentProfileBinding.bind(view)
        CoroutineScope(Dispatchers.IO).launch {
            profile = ProfileService.getProfile(requireContext())
            if (isAdded) {
                requireActivity().runOnUiThread {
                    binding.apply {
                        binding.nameSurname.text = profile.firstName+' '+profile.lastName
                        binding.email.text = profile.email
                        binding.phoneNumber.text = profile.phoneNumber
                        val formatterNeeded = DateTimeFormatter.ofPattern("dd.MM.yyyy")
                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                        val dateOfBirth =
                            java.time.LocalDate.parse(profile.dateBirth, formatter)
                        val dateOfBirthFormatted = dateOfBirth.format(formatterNeeded)
                        binding.dateBirth.text = dateOfBirthFormatted
                    }
                }
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Profile.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Profile().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}