package com.example.gallerydemo

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.gallerydemo.databinding.FragmentPhotoPagerBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PhotoPagerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PhotoPagerFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentPhotoPagerBinding

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
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_photo_pager, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val photoList = arguments?.getParcelableArrayList<PhotoItem>(photoBundleName)
        val position = arguments?.getInt(photoPos)?:0
        PhotoAdapter().apply {
            submitList(photoList)
            binding.viewPager.adapter = this
            binding.viewPager.currentItem = position
        }
        binding.pageTextView.text = getString(R.string.photo_page, position+1, photoList?.size)
        binding.viewPager.apply {
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.pageTextView.text = getString(R.string.photo_page, position+1, photoList?.size)
                }
            })
        }
        binding.saveButton.setOnClickListener {
            Log.d(TAG, "onActivityCreated: 点击了")
            Glide.with(requireContext())
                    .load(photoList?.get(binding.viewPager.currentItem)?.fullUrl)
                    .into(object : CustomTarget<Drawable>(){
                        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                            val bitmap = (resource as BitmapDrawable).bitmap
                            savePhoto(bitmap)
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                        }
                    })
        }
    }

    fun savePhoto(bitmap: Bitmap) {
        val saveUri = requireContext().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                ContentValues())?: kotlin.run {
            Toast.makeText(requireContext(), "存储失败", Toast.LENGTH_SHORT).show()
            return
        }
        requireContext().contentResolver.openOutputStream(saveUri).use {
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, it)){
                Toast.makeText(requireContext(), "存储成功", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireContext(), "存储失败", Toast.LENGTH_SHORT).show()
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
         * @return A new instance of fragment PhotoPagerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PhotoPagerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}