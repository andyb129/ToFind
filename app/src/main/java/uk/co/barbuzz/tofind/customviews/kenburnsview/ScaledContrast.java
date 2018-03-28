package uk.co.barbuzz.tofind.customviews.kenburnsview;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

import com.squareup.picasso.Transformation;

import static android.graphics.Bitmap.Config.ARGB_8888;
import static android.graphics.Bitmap.createBitmap;

/**
 * class from this article by square about adding colorizing a picture
 *
 * https://medium.com/square-corner-blog/welcome-to-the-color-matrix-64d112e3f43d#.fkjb6qpi5
 *
 */
public class ScaledContrast implements Transformation {

    private final int destinationColor;

    public ScaledContrast(int destinationColor) {
        this.destinationColor = destinationColor;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        float lr = 0.2126f;
        float lg = 0.7152f;
        float lb = 0.0722f;
        ColorMatrix grayscaleMatrix = new ColorMatrix(new float[] {
                lr, lg, lb, 0, 0, //
                lr, lg, lb, 0, 0, //
                lr, lg, lb, 0, 0, //
                0, 0, 0, 0, 255, //
        });

        int dr = Color.red(destinationColor);
        int dg = Color.green(destinationColor);
        int db = Color.blue(destinationColor);
        float drf = dr / 255f;
        float dgf = dg / 255f;
        float dbf = db / 255f;
        ColorMatrix tintMatrix = new ColorMatrix(new float[] {
                drf, 0, 0, 0, 0, //
                0, dgf, 0, 0, 0, //
                0, 0, dbf, 0, 0, //
                0, 0, 0, 1, 0, //
        });
        tintMatrix.preConcat(grayscaleMatrix);
        float lDestination = drf * lr + dgf * lg + dbf * lb;
        float scale = 1f - lDestination;
        float translate = 1 - scale * 0.5f;
        ColorMatrix scaleMatrix = new ColorMatrix(new float[] {
                scale, 0, 0, 0, dr * translate, //
                0, scale, 0, 0, dg * translate, //
                0, 0, scale, 0, db * translate, //
                0, 0, 0, 1, 0, //
        });
        scaleMatrix.preConcat(tintMatrix);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(scaleMatrix);
        Paint paint = new Paint();
        paint.setColorFilter(filter);
        int width = source.getWidth();
        int height = source.getHeight();
        Bitmap destination = createBitmap(width, height, ARGB_8888);
        Canvas canvas = new Canvas(destination);
        canvas.drawBitmap(source, 0, 0, paint);
        source.recycle();
        return destination;
    }

    @Override
    public String key() {
        String hexColor = String.format("#%08x", destinationColor);
        return "scaled-contrast(destinationColor=" + hexColor + ")";
    }
}
