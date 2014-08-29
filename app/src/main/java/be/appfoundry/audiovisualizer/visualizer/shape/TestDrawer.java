package be.appfoundry.audiovisualizer.visualizer.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import be.appfoundry.audiovisualizer.visualizer.AudioData;
import be.appfoundry.audiovisualizer.visualizer.FFTData;

/**
 * Created by donpironet on 29/08/14.
 */
public class TestDrawer extends ShapeDrawer {
    private static final String TAG = BarDrawer.class.getName();

    private static final int MAXHEIGHT = 200;
    private int mDivisions;
    private Paint mPaint;
    private boolean mTop;

    /**
     * Renders the FFT data as a series of lines, in histogram form
     *
     * @param divisions - must be a power of 2. Controls how many lines to draw
     * @param paint     - Paint to draw lines with
     * @param top       - whether to draw the lines at the top of the canvas, or the bottom
     */
    public TestDrawer(int divisions,
                      Paint paint,
                      boolean top) {
        super();
        mDivisions = divisions;
        mPaint = paint;
        mTop = top;
    }

    @Override
    public void onRender(Canvas canvas, AudioData data, Rect rect) {
        // Do nothing, we only display FFT data
    }

    @Override
    public void onRender(Canvas canvas, FFTData data, Rect rect) {

        for (int i = 0; i < data.bytes.length / mDivisions; i++) {
            mFFTPoints[i * 4] = i * 4 * mDivisions;
            mFFTPoints[i * 4 + 2] = i * 4 * mDivisions;
            byte rfk = data.bytes[mDivisions * i];
            byte ifk = data.bytes[mDivisions * i + 1];
            float magnitude = (rfk * rfk + ifk * ifk);
            int dbValue = (int) (10 * Math.log10(magnitude));
        }

        int r = 200;

        canvas.save();
        mPaint.setStyle(Paint.Style.STROKE);

        canvas.translate(rect.width() / 2, rect.height() / 2);
        canvas.drawCircle(0, 0, r, mPaint);
        mPaint.setStyle(Paint.Style.FILL);

        for (int angle = 0; angle <= 360; angle += 10) {
            canvas.rotate(10.f);
            canvas.save();
            canvas.translate(0, -r); // R is the imaginary circle radius and in this way the center of each rectangle is lying on the circle. Change that if you want a bigger or smaller circle.
            canvas.drawRect(-10, -35, 10, 35, mPaint);
            canvas.restore();
        }
        canvas.restore();
    }
}
