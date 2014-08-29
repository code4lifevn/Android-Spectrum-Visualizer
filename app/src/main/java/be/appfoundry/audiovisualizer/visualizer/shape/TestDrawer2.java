package be.appfoundry.audiovisualizer.visualizer.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;

import be.appfoundry.audiovisualizer.visualizer.AudioData;
import be.appfoundry.audiovisualizer.visualizer.FFTData;

/**
 * Created by donpironet on 29/08/14.
 */
public class TestDrawer2 extends ShapeDrawer{

    private static final String TAG = TestDrawer2.class.getName();

    private static final int MAXHEIGHT = 300;
    private Paint mPaint;
    private Path p;

    /**
     * Renders the FFT data as a series of lines, in histogram form
     *
     * @param paint     - Paint to draw lines with
     */
    public TestDrawer2(Paint paint) {
        super();
        mPaint = paint;
    }

    @Override
    public void onRender(Canvas canvas, AudioData data, Rect rect) {

    }

    @Override
    public void onRender(Canvas canvas, FFTData data, Rect rect) {

        int centerX = 400;
        int centerY = 400;
        int R = 200;

        canvas.drawCircle(centerX, centerY, R, mPaint);

        for(int angle = 0; angle < 360; angle+=10)
        {
            byte rfk = data.bytes[angle];
            byte ifk = data.bytes[angle+ 1];
            float magnitude = (rfk * rfk + ifk * ifk);
            int dbValue = (int) (10 * Math.log10(magnitude));

            p = mySpectrumDrawer(centerX,centerY,R,positiveModulo(dbValue) *2 ,angle);
            canvas.drawPath(p, mPaint);
        }
    }

    private Path mySpectrumDrawer(int centerX, int centerY,int R,int height, int angel){

        p = new Path();

        int dX = (int) (R*(Math.cos(Math.toRadians(angel))));
        int dY = (int) (R*(Math.sin(Math.toRadians(angel))));

        int dhx = (int) (height/2*(Math.cos(Math.toRadians(angel))));
        int dhy = (int) (height/2*(Math.sin(Math.toRadians(angel))));

        p.moveTo(centerX + dX - dhx , centerY - dY + dhy);
        p.lineTo(centerX + dX + dhx , centerY - dY - dhy);

        return p;
    }

    private int positiveModulo(int number) {
        int result = number % MAXHEIGHT;
        if (result < 0) {
            result += MAXHEIGHT;
        }
        return result;
    }
}
