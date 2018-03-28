package uk.co.barbuzz.tofind.activities;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import uk.co.barbuzz.tofind.R;
import uk.co.barbuzz.tofind.fragments.SitesFragment;

public class FullScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        final ImageView imgFull=findViewById(R.id.imgFull);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imgFull.setTransitionName(getString(R.string.site_image_transition_name)+0);//SitesFragment.EXTRA_TRANSITION_IMAGE);
        }

        Picasso.with(this).load(getIntent().getIntExtra(SitesFragment.EXTRA_IMAGE,0)).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                imgFull.setImageBitmap(bitmap);
                supportStartPostponedEnterTransition();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                supportStartPostponedEnterTransition();
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }
}
