package ar.yendoc.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import ar.yendoc.R
import ar.yendoc.databinding.FragmentPhotoDetailBinding
import com.squareup.picasso.Picasso

class PhotoDetailFragment() : AppCompatActivity() {
    companion object {
        const val EXTRA_PHOTO = "PhotoDetailFragment.EXTRA_PHOTO"
    }

    private lateinit var imageView: ImageView
    private lateinit var galleryPhotoSelected: GalleryPhotos

    private var _binding: FragmentPhotoDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_photo_detail)

        galleryPhotoSelected = intent.getParcelableExtra(EXTRA_PHOTO)!!
        imageView = findViewById(R.id.image)
    }

    override fun onStart() {
        super.onStart()

        Picasso.get()
            .load(galleryPhotoSelected.url)
            .placeholder(ColorDrawable(Color.WHITE))
            .error(ColorDrawable(Color.BLACK))
            .fit()
            .into(imageView)
    }
}