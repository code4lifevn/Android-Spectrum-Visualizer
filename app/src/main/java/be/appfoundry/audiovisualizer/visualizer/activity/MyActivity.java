package be.appfoundry.audiovisualizer.visualizer.activity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import be.appfoundry.audiovisualizer.R;
import be.appfoundry.audiovisualizer.visualizer.VisualizerView;
import be.appfoundry.audiovisualizer.visualizer.shape.BarDrawer;
import be.appfoundry.audiovisualizer.visualizer.shape.CircleDrawer;
import be.appfoundry.audiovisualizer.visualizer.shape.LineDrawer;
import be.appfoundry.audiovisualizer.visualizer.shape.TestDrawer;

/**
 * Created by donpironet on 28/08/14.
 */
public class MyActivity extends Activity {
    private static final String TAG = MyActivity.class.getName();

    private MediaPlayer mPlayer;
    private VisualizerView mVisualizerView;

    private Button mPlay;
    private Button mStop;

    private ProgressBar mProgressBar;
    private Timer mTimer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPlay = (Button) findViewById(R.id.btnPlay);
        mStop = (Button) findViewById(R.id.btnStop);

        mPlay.setOnClickListener(startButtonListener);
        mStop.setOnClickListener(stopButtonListener);

        mProgressBar = (ProgressBar)findViewById (R.id.progressBar);

        mTimer = new Timer();

        mTimer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                mProgressBar.setProgress(mProgressBar.getProgress() + 1);
            }
        }, 0, 1000);
    }

    private View.OnClickListener startButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                startPressed();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private View.OnClickListener stopButtonListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            stopPressed();
        }
    };


    @Override
    protected void onResume()
    {
        super.onResume();
        init();
    }

    @Override
    protected void onPause()
    {
        cleanUp();
        super.onPause();
    }

    @Override
    protected void onDestroy()
    {
        cleanUp();
        super.onDestroy();
    }


    private void init() {
        mPlayer = MediaPlayer.create(this, R.raw.test);
        long time = TimeUnit.MILLISECONDS.toSeconds(mPlayer.getDuration());
        mProgressBar.setMax((int)time);
        mPlayer.setLooping(true);
        mPlayer.start();

        // Link the visualizer view with the mediaplayer
        mVisualizerView = (VisualizerView) findViewById(R.id.visualizerView);
        mVisualizerView.link(mPlayer);

        //addTestDrawer();

        //Add a Circle to the view
        addCircleRenderer();

        //Add the LineDrawer to the view
        //addLineRenderer();

        //Add a Bar to the view
        //addBarGraphRenderers();

    }

    private void cleanUp()
    {
        if (mPlayer != null)
        {
            mVisualizerView.release();
            mPlayer.release();
            mPlayer = null;
        }
    }

    private void addTestDrawer()
    {
        Paint paint = new Paint();
        paint.setStrokeWidth(10f);
        paint.setAntiAlias(true);
        paint.setColor(Color.rgb(255,0,0));
        TestDrawer barGraphRendererBottom = new TestDrawer(16, paint, false);
        mVisualizerView.addRenderer(barGraphRendererBottom);
    }

    private void addCircleRenderer()
    {
        Paint paint = new Paint();
        paint.setStrokeWidth(3f);
        paint.setAntiAlias(true);
        paint.setColor(Color.rgb(255,0,0)); // Red
        CircleDrawer circleRenderer = new CircleDrawer(paint, true);
        mVisualizerView.addRenderer(circleRenderer);
    }

    // Creates a bar
    private void addBarGraphRenderers()
    {
        Paint paint = new Paint();
        paint.setStrokeWidth(50f);
        paint.setAntiAlias(true);
        paint.setColor(Color.rgb(255,0,0));
        BarDrawer barGraphRendererBottom = new BarDrawer(16, paint, false);
        mVisualizerView.addRenderer(barGraphRendererBottom);
    }

    // Creates a Line
    private void addLineRenderer()
    {
        Paint linePaint = new Paint();
        linePaint.setStrokeWidth(1f);
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.rgb(232,85,5));

        Paint lineFlashPaint = new Paint();
        lineFlashPaint.setStrokeWidth(5f);
        lineFlashPaint.setAntiAlias(true);
        lineFlashPaint.setColor(Color.rgb(255,0,0));
        LineDrawer lineDrawer = new LineDrawer(linePaint, lineFlashPaint, false);
        mVisualizerView.addRenderer(lineDrawer);
    }

    // Starts the mediaPlayer
    private void startPressed() throws IllegalStateException, IOException
    {
        if(mPlayer.isPlaying())
        {
            return;
        }
        Log.d(TAG, "Duration Song " + TimeUnit.MILLISECONDS.toSeconds(mPlayer.getDuration()));
        mPlayer.prepare();
        mPlayer.start();
    }

    // Stops the mediaPlayer
    private void stopPressed()
    {
        mVisualizerView.flash();
        mPlayer.stop();
    }
}
