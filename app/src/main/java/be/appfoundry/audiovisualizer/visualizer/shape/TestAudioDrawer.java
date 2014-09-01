package be.appfoundry.audiovisualizer.visualizer.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.Log;

import be.appfoundry.audiovisualizer.visualizer.AudioData;
import be.appfoundry.audiovisualizer.visualizer.FFTData;

/**
 * Created by donpironet on 1/09/14.
 */
public class TestAudioDrawer extends ShapeDrawer {
    private static final String TAG = TestAudioDrawer.class.getName();
    private static final int MAXHEIGHT = 100;

    Path p;
    Paint mPaint;

    public TestAudioDrawer(Paint mPaint) {
        this.mPaint = mPaint;
    }

    @Override
    public void onRender(Canvas canvas, AudioData data, Rect rect) {

        int centerX = 400;
        int centerY = 400;
        int R = 200;

        double sum = 0;

        canvas.drawCircle(centerX, centerY, R, mPaint);

        for(int i = 0; i < data.bytes.length; i++)
        {
            sum += data.bytes[i] * data.bytes[i];
        }

        final double amplitude = sum / data.bytes.length;

        Log.d(TAG, "Amplitude is: " + Math.sqrt(amplitude));

        for(int angle = 0; angle < 360; angle+=10)
        {
            p = mySpectrumDrawer(centerX,centerY,R,(int)Math.sqrt(amplitude),angle);
            canvas.drawPath(p, mPaint);
        }

    }

    @Override
    public void onRender(Canvas canvas, FFTData data, Rect rect) {

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
}
