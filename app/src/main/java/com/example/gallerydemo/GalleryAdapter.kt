package com.example.gallerydemo

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.cronet.CronetHttpStack
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.gallerydemo.databinding.GalleryCellBinding
val photoBundleName = "PHOTO"
val photoPos = "PHOTO_POSITION"
class GalleryAdapter : ListAdapter<PhotoItem,GalleryAdapter.MyViewHolder>(DiffCallBack){
    inner class MyViewHolder(var binding: GalleryCellBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val binding = DataBindingUtil.inflate<GalleryCellBinding>(LayoutInflater.from(
            parent.context),R.layout.gallery_cell,parent,false)
        val holder = MyViewHolder(binding)
        holder.itemView.setOnClickListener {
            val bundle = Bundle().apply {
//                putParcelable(photoBundleName, photoItems[holder.adapterPosition])
//                holder.itemView.findNavController().navigate(R.id.action_galleryFragment_to_photoFragment,this)
                putParcelableArrayList(photoBundleName, ArrayList(currentList))
                putInt(photoPos, holder.adapterPosition)
                holder.itemView.findNavController().navigate(R.id.action_galleryFragment_to_photoPagerFragment,this)

            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val bind = DataBindingUtil.bind<GalleryCellBinding>(holder.itemView)

        
        val photoItem = getItem(position)
        holder.binding.shimmerCellLayout.apply {
            setShimmerColor(0x55FFFFFF)
            setShimmerAngle(0)
            startShimmerAnimation()
        }
        Glide.with(holder.itemView)
            .load(photoItem.previewUrl)
            .placeholder(R.drawable.ic_photo_backgrand_24)
            .listener(object : RequestListener<Drawable>{
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false.also {
                        holder.binding.shimmerCellLayout?.stopShimmerAnimation()
                    }
                }

            })
            .into(holder.binding.imageView)
    }

    object DiffCallBack : DiffUtil.ItemCallback<PhotoItem>(){
        override fun areItemsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
            return oldItem.photoId == newItem.photoId
        }

    }
}
