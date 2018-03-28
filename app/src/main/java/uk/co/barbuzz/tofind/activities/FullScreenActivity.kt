package uk.co.barbuzz.tofind.activities

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView

import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

import uk.co.barbuzz.tofind.R
import uk.co.barbuzz.tofind.fragments.SitesFragment

class FullScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen)

        val imgFull = findViewById<ImageView>(R.id.imgFull)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imgFull.transitionName = getString(R.string.site_image_transition_name) + 0//SitesFragment.EXTRA_TRANSITION_IMAGE);
        }

        Picasso.with(this).load(intent.getIntExtra(SitesFragment.EXTRA_IMAGE, 0)).into(object : Target {
            override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                imgFull.setImageBitmap(bitmap)
                supportStartPostponedEnterTransition()
            }

            override fun onBitmapFailed(errorDrawable: Drawable) {
                supportStartPostponedEnterTransition()
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable) {

            }
        })
    }
}
