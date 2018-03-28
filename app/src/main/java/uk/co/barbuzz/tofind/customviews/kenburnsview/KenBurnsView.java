package uk.co.barbuzz.tofind.customviews.kenburnsview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.Random;

import uk.co.barbuzz.tofind.R;

/**
 *
 * Created by f.laurent on 21/11/13.
 * from this blog - http://flavienlaurent.com/blog/2013/11/20/making-your-action-bar-not-boring/
 */
public class KenBurnsView extends FrameLayout {

    private static final String TAG = "KenBurnsView";

    private final Handler mHandler;
    private final Context mContext;
    private int[] mResourceIds;
    private ImageView[] mImageViews;
    private int mActiveImageIndex = -1;
    private int mActiveResourceIndex;
    private ImageView activeImageView;
    private ImageView inactiveImageView;

    private final Random random = new Random();
    private int mSwapMs = 10000;
    private int mFadeInOutMs = 400;

    private float maxScaleFactor = 1.5F;
    private float minScaleFactor = 1.2F;

    public KenBurnsView(Context context) {
        this(context, null);
    }

    public KenBurnsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KenBurnsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        mHandler = new Handler();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //startKenBurnsAnimation();
        swapImage(0, 0);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View view = inflate(getContext(), R.layout.element_kenburns_view, this);

        mImageViews = new ImageView[2];
        mImageViews[0] = (ImageView) view.findViewById(R.id.image0);
        mImageViews[1] = (ImageView) view.findViewById(R.id.image1);
    }

    public void setResourceIds(int... resourceIds) {
        mResourceIds = resourceIds;
    }

    public void swapImage(int imagePos, int prevPos) {
        //make sure the correct image is loaded when swiping back
        if (imagePos < prevPos) {
            if (imagePos==0) {
                fillImageView(activeImageView, mResourceIds[0]);
                fillImageView(inactiveImageView, mResourceIds[1]);
                inactiveImageView.setAlpha(1f);
            } else {
                fillImageView(activeImageView, mResourceIds[imagePos-1]);
                fillImageView(inactiveImageView, mResourceIds[imagePos]);
            }
        }

        if(imagePos == 0) {
            fillFirstTwoImageViews();
            mActiveImageIndex = imagePos;
            animate(mImageViews[0]);
            return;
        }

        int inactiveIndex = mActiveImageIndex;
        mActiveImageIndex = (1 + mActiveImageIndex) % mImageViews.length;
        mActiveResourceIndex = imagePos;

        activeImageView = mImageViews[mActiveImageIndex];
        activeImageView.setAlpha(0.0f);
        inactiveImageView = mImageViews[inactiveIndex];

        animate(activeImageView);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(mFadeInOutMs);
        ObjectAnimator alpha1 = ObjectAnimator.ofFloat(inactiveImageView, "alpha", 1.0f, 0.0f);
        ObjectAnimator alpha2 = ObjectAnimator.ofFloat(activeImageView, "alpha", 0.0f, 1.0f);
        animatorSet.playTogether(
                alpha1,
                alpha2
        );
        animatorSet.start();
        alpha2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //fill the inactive view with the next image
                if (mActiveResourceIndex != mResourceIds.length-1) {
                    fillImageView(inactiveImageView);
                }
            }
        });

    }

    private void start(View view, long duration, float fromScale, float toScale, float fromTranslationX, float fromTranslationY, float toTranslationX, float toTranslationY) {
        view.setScaleX(fromScale);
        view.setScaleY(fromScale);
        view.setTranslationX(fromTranslationX);
        view.setTranslationY(fromTranslationY);
        ViewPropertyAnimator propertyAnimator = view.animate().translationX(toTranslationX).translationY(toTranslationY).scaleX(toScale).scaleY(toScale).setDuration(duration);
        propertyAnimator.start();
    }

    private float pickScale() {
        return this.minScaleFactor + this.random.nextFloat() * (this.maxScaleFactor - this.minScaleFactor);
    }

    private float pickTranslation(int value, float ratio) {
        return value * (ratio - 1.0f) * (this.random.nextFloat() - 0.5f);
    }

    public void animate(View view) {
        float fromScale = pickScale();
        float toScale = pickScale();
        float fromTranslationX = pickTranslation(view.getWidth(), fromScale);
        float fromTranslationY = pickTranslation(view.getHeight(), fromScale);
        float toTranslationX = pickTranslation(view.getWidth(), toScale);
        float toTranslationY = pickTranslation(view.getHeight(), toScale);
        start(view, this.mSwapMs, fromScale, toScale, fromTranslationX, fromTranslationY, toTranslationX, toTranslationY);
    }

    private void fillFirstTwoImageViews() {
        for (int i = 0; i < mImageViews.length; i++) {
            fillImageView(mImageViews[i], mResourceIds[i]);
            //color overlay
                    /*.transform(new ScaledContrast(0xff3F51B5))
                    .into(mImageViews[i]);*/
        }
    }

    public void fillImageView(ImageView inactiveImageView) {
        int index;
        if (mActiveResourceIndex == mResourceIds.length-1) {
            index = 0;
            mActiveResourceIndex = 0;
        } else {
            index = mActiveResourceIndex + 1;
        }
        fillImageView(inactiveImageView, mResourceIds[index]);
    }

    private void fillImageView(ImageView imageView, int resourceId) {
        imageView.setImageResource(resourceId);
    }
}
