package ar.yendoc.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.yendoc.R
import ar.yendoc.databinding.FragmentPhotosBinding
import com.squareup.picasso.Picasso
import kotlinx.android.parcel.Parcelize
import java.io.File




class PhotosFragment : Fragment() {

    companion object {
        const val EXTRA_PHOTO = "PhotoDetailFragment.EXTRA_PHOTO"
    }

    private var _binding: FragmentPhotosBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var imageGalleryAdapter: ImageGalleryAdapter
    private var idVisita : Int = 0
    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotosBinding.inflate(inflater, container, false)

        sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        idVisita = sharedPref.getInt(getString(R.string.id_visita),0)

        val layoutManager = GridLayoutManager(this.context, 2)
        recyclerView = this.binding.rvImages
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager
        imageGalleryAdapter = ImageGalleryAdapter(this.requireActivity().baseContext, GalleryPhotos.getPhotos(idVisita))

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        recyclerView.adapter = imageGalleryAdapter
    }

    private inner class ImageGalleryAdapter(val context: Context, val photos: Array<GalleryPhotos>)
        : RecyclerView.Adapter<ImageGalleryAdapter.MyViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageGalleryAdapter.MyViewHolder {
            val context = parent.context
            val inflater = LayoutInflater.from(context)
            val photoView = inflater.inflate(R.layout.photo_item, parent, false)
            return MyViewHolder(photoView)
        }

        override fun onBindViewHolder(holder: ImageGalleryAdapter.MyViewHolder, position: Int) {
            val galleryPhoto = photos[position]
            val imageView = holder.photoImageView

            Picasso.get()
                .load(galleryPhoto.url)
                .placeholder(ColorDrawable(Color.WHITE))
                .error(ColorDrawable(Color.BLACK))
                .fit()
                .into(imageView)
        }

        override fun getItemCount(): Int {
            return photos.size
        }

        inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

            var photoImageView: ImageView = itemView.findViewById(R.id.iv_photo)

            init {
                itemView.setOnClickListener(this)
            }

            override fun onClick(view: View) {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val galleryPhoto = photos[position]
                    val intent = Intent(context, PhotoDetailFragment::class.java).apply {
                        putExtra(PhotosFragment.EXTRA_PHOTO, galleryPhoto)
                    }
                    startActivity(intent)

                }
            }
        }


    }
}

@Parcelize
data class GalleryPhotos(val url: String) : Parcelable{

    companion object {
        fun getPhotos(idVisita: Int): Array<GalleryPhotos> {
            var rutas = arrayListOf<GalleryPhotos>()
            val sdCard = Environment.getExternalStorageDirectory()
            val dir = sdCard.absolutePath + "/yendoc"
            File(dir).walk().forEach {
                if(it.toString().contains(".jpg") && it.toString().contains("Img_$idVisita")){
                    rutas.add(GalleryPhotos("file://$it"))
                }
            }
            return rutas.toTypedArray()
        }
    }
}