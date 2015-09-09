package com.example.daniel.fraktaleandroid;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.daniel.fraktaleandroid.operating.Fractal;
import com.example.daniel.fraktaleandroid.operating.Julii;
import com.example.daniel.fraktaleandroid.operating.Mandelbrot;
import com.example.daniel.fraktaleandroid.operating.MandelbrotIII;

import de.greenrobot.event.EventBus;


public class MainActivity extends Activity{

    private static final String TAG = "TAG";

    //Flag for only one thread without queue
    public boolean ONE_THREAD = true;

    private Button buttonStart;
    private EditText editTextMax;
    private Spinner spinner;
    private ImageView imageView;
    private ProgressBar progressBar;

    private GestureDetector gestureDetector;

    private Fractal mFractal;
    private Mandelbrot mMandelbrot;
    private Julii mJulii;
    private MandelbrotIII mMandelbrotIII;

    int WIDTH;
    int HEIGHT;
    int MAX_ITER;

    public Bitmap img;

    //interfejs to update ProgressBar from fractals class
    private OnUpdateProgress onUpdateProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        EventBus.getDefault().register(this);

        setUi();
        getDimensionOfScreen();

        onUpdateProgress = new OnUpdateProgress() {
            @Override
            public void OnUpdateProgress(int progress) {
                progressBar.setProgress(progress);
            }
        };

        mFractal = new Fractal();
        mFractal.setSize(WIDTH, HEIGHT);

        progressBar.setVisibility(View.VISIBLE);

        mMandelbrot = new Mandelbrot(img, this, onUpdateProgress);
        mMandelbrot.setSize(WIDTH, HEIGHT);

        mJulii = new Julii(img, this, onUpdateProgress);
        mJulii.setSize(WIDTH, HEIGHT);

        mMandelbrotIII = new MandelbrotIII(img, this, onUpdateProgress);
        mMandelbrotIII.setSize(WIDTH, HEIGHT);

    }


    public void onEventMainThread(GeneratedImg event){
        img = event.getImg();
        repaint();
    }


    private void getDimensionOfScreen() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        WIDTH = size.x;
        HEIGHT = size.y;

        ViewTreeObserver vto = imageView.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            int finalHeight, finalWidth;

            public boolean onPreDraw() {
                imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                finalHeight = imageView.getMeasuredHeight();
                finalWidth = imageView.getMeasuredWidth();
                return true;
            }
        });

        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        img = Bitmap.createBitmap( WIDTH, HEIGHT, conf);
    }


    private void setUi() {
        gestureDetector = new GestureDetector(this, new GestureListener());

        buttonStart = (Button) findViewById(R.id.start);
        editTextMax = (EditText) findViewById(R.id.max_et);
        imageView = (ImageView) findViewById(R.id.imageView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        progressBar.setProgress(0);
        progressBar.setMax(500
        );
        spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sets, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startRenderFractal(null);
            }
        });


        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent e) {

                return gestureDetector.onTouchEvent(e);
            }
        });
    }

    private void startRenderFractal(Point p) {

        if(!ONE_THREAD){
            Toast.makeText(this, "Liczę jeszcze...", Toast.LENGTH_LONG).show();
            return;
        }
        ONE_THREAD = false;

        int i = spinner.getSelectedItemPosition();

        try {
            MAX_ITER = Integer.valueOf(editTextMax.getText().toString());
        }catch(NumberFormatException e ){
            Toast.makeText(this, "Niepoprawna wartość!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        switch (i){

            //Mandelbrot
            case 0:
                mMandelbrot.setMaxIter(i);
                if(p!=null)
                    mMandelbrot.setView(p, i, true);
                else
                    mMandelbrot.resetValues();

                mMandelbrot.setMaxIter(MAX_ITER);
                new Thread(mMandelbrot).start();

            break;

            //Julii
            case 1:
                mJulii.setMaxIter(i);
                if (p != null)
                    mJulii.setView(p, i, true);
                else
                    mJulii.resetValues();
                mJulii.setMaxIter(MAX_ITER);
                new Thread(mJulii).start();

                break;

            //Mandelbrot Cubic
            case 2:
                mMandelbrotIII.setMaxIter(i);
                if (p != null) {
                    mMandelbrotIII.setView(p, i, true);
                } else {
                    mMandelbrotIII.resetValues();
                }
                mMandelbrotIII.setMaxIter(MAX_ITER);
                mMandelbrotIII.setLevel(2);
                new Thread(mMandelbrotIII).start();

                break;

            //Mandelbrot Quadratur
            case 3:
                mMandelbrotIII.setMaxIter(i);
                if (p != null) {
                    mMandelbrotIII.setView(p, i, true);
                } else {
                    mMandelbrotIII.resetValues();
                }
                mMandelbrotIII.setMaxIter(MAX_ITER);
                mMandelbrotIII.setLevel(3);
                new Thread(mMandelbrotIII).start();

                break;

            //Mandelbrot Penta
            case 4:
                mMandelbrotIII.setMaxIter(i);
                if (p != null) {
                    mMandelbrotIII.setView(p, i, true);
                } else {
                    mMandelbrotIII.resetValues();
                }
                mMandelbrotIII.setMaxIter(MAX_ITER);
                mMandelbrotIII.setLevel(4);
                new Thread(mMandelbrotIII).start();

                break;
        }
    }


    public void repaint(){
        imageView.setImageBitmap(img);
        ONE_THREAD = true;
        progressBar.setVisibility(View.INVISIBLE);
    }


    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            Log.d(TAG, "onDown at: (" + e.getX() + "," + e.getY() + ")");
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            int x = (int) e.getX();
            int y = (int) e.getY();

            Log.d(TAG, "Tapped at: (" + x + "," + y + ")");
            startRenderFractal(new Point(x, y));
            return true;
        }
    }



}
