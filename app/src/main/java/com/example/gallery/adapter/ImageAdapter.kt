package com.example.gallery.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gallery.databinding.ImageItemBinding
import com.example.gallery.model.ImageModel

class ImageAdapter(
    private val images: MutableList<ImageModel>,
    var onClickImage: ((String, String) -> Unit),
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(val binding: ImageItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ImageItemBinding.inflate(inflater, parent, false)
        return ImageViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = images[position]
        with(holder.binding) {
            title.text = image.description ?: image.user.username
            author.text = image.user.name

            Glide.with(imageView.context)
                .load(image.urls.regular)
                .placeholder(ColorDrawable(Color.parseColor(image.color)))
                .into(imageView)
            imageView.setOnClickListener {
                onClickImage(image.urls.full, image.description ?: image.user.username)
            }

        }
    }

}