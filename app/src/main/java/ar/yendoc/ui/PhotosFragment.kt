package ar.yendoc.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.yendoc.R
import ar.yendoc.databinding.FragmentPhotosBinding
import com.squareup.picasso.Picasso
import kotlinx.android.parcel.Parcelize

class PhotosFragment : Fragment() {

    companion object {
        const val EXTRA_SUNSET_PHOTO = "SunsetPhotoActivity.EXTRA_SUNSET_PHOTO"
    }

    private var _binding: FragmentPhotosBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var imageGalleryAdapter: ImageGalleryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPhotosBinding.inflate(inflater, container, false)


        val layoutManager = GridLayoutManager(this.context, 2)
        recyclerView = this.binding.rvImages
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager
        imageGalleryAdapter = ImageGalleryAdapter(this.requireActivity().baseContext, SunsetPhoto.getSunsetPhotos())

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        recyclerView.adapter = imageGalleryAdapter
    }

    private inner class ImageGalleryAdapter(val context: Context, val sunsetPhotos: Array<SunsetPhoto>)
        : RecyclerView.Adapter<ImageGalleryAdapter.MyViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageGalleryAdapter.MyViewHolder {
            val context = parent.context
            val inflater = LayoutInflater.from(context)
            val photoView = inflater.inflate(R.layout.photo_item, parent, false)
            return MyViewHolder(photoView)
        }

        override fun onBindViewHolder(holder: ImageGalleryAdapter.MyViewHolder, position: Int) {
            val sunsetPhoto = sunsetPhotos[position]
            val imageView = holder.photoImageView

            Picasso.get()
                .load(sunsetPhoto.url)
                .placeholder(ColorDrawable(Color.GREEN))
                .error(ColorDrawable(Color.RED))
                .fit()
                .into(imageView)
        }

        override fun getItemCount(): Int {
            return sunsetPhotos.size
        }

        inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

            var photoImageView: ImageView = itemView.findViewById(R.id.iv_photo)

            init {
                itemView.setOnClickListener(this)
            }

            override fun onClick(view: View) {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val sunsetPhoto = sunsetPhotos[position]
                    val intent = Intent(context, PhotosFragment::class.java).apply {
                        putExtra(PhotosFragment.EXTRA_SUNSET_PHOTO, sunsetPhoto)
                    }
                    startActivity(intent)
                }
            }
        }


    }
}

@Parcelize
data class SunsetPhoto(val url: String) : Parcelable{

    companion object {
        fun getSunsetPhotos(): Array<SunsetPhoto> {
            return arrayOf<SunsetPhoto>(SunsetPhoto("https://goo.gl/32YN2B"),
                SunsetPhoto("https://goo.gl/Wqz4Ev"),
                SunsetPhoto("https://goo.gl/U7XXdF"),
                SunsetPhoto("https://goo.gl/ghVPFq"),
                SunsetPhoto("https://goo.gl/qEaCWe"),
                SunsetPhoto("https://goo.gl/vutGmM"))
        }
    }
}