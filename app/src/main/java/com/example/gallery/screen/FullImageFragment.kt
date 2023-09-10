package com.example.gallery.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.gallery.databinding.FragmentFullImageBinding

class FullImageFragment : Fragment() {
    lateinit var binding: FragmentFullImageBinding
    private val args: FullImageFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFullImageBinding.inflate(layoutInflater, container, false)

        val imageUrl = args.imageRegular

        Glide.with(requireContext())
            .load(imageUrl)
            .into(binding.fullImage)


        return binding.root
    }
}