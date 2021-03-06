package com.example.daniel.fraktaleandroid.operating;

import android.graphics.Bitmap;

import com.example.daniel.fraktaleandroid.GeneratedImg;
import com.example.daniel.fraktaleandroid.MainActivity;
import com.example.daniel.fraktaleandroid.OnUpdateProgress;

import de.greenrobot.event.EventBus;

public class MandelbrotIII extends Fractal implements Runnable {
    
    private int LEVEL = 2;

    OnUpdateProgress updateProgress;

    private final MainActivity context;
    public Bitmap img;


    public MandelbrotIII(Bitmap img, MainActivity context,
                      OnUpdateProgress updateProgress) {
        this.img = img;
        this.context = context;
        this.updateProgress = updateProgress;

    }


    /**
     * Reset default values
     */
    public void resetValues() {

        MINRE = -2.5;
        MAXRE = 2.8;
        MINIM = -1.75;

        zoom = 3;
        this.LEVEL = 2;
        refactor();
    }


    /**
     * new Thread for render
     */
    @Override
    public synchronized void run() {
        renderFractal();
    }

    public void renderFractal() {
        color();

        for (int y = 0; y < HEIGHT; y++) {

            double c_im = MAXIM - y * IM_FACTOR;

            for (int x = 0; x < WIDTH; x++) {
                updateProgress.OnUpdateProgress(y);

                double c_re = MINRE + x * RE_FACTOR;
                double Z_re = c_re, Z_im = c_im;

                int iteration = 0;
                boolean partSet = true;

                while (iteration < MAX_ITER) {

                    double Z_re2 = Z_re * Z_re, Z_im2 = Z_im * Z_im;
                    
                    double ztmp_re = Z_re, ztmp_im = Z_im;

                    double tmp_re, tmp_im;
                    
                    for(int i=0; i<LEVEL; i++){
                        
                        tmp_re = Z_re * ztmp_re - Z_im * ztmp_im;
                        tmp_im = Z_re * ztmp_im + ztmp_re * Z_im;
                        
                        Z_re = tmp_re;
                        Z_im = tmp_im;
                        
                    }
                    
                    if (Z_re2 + Z_im2 > 4) {
                        partSet = false;
                        break;
                    }
                    
                    Z_im += c_im;
                    Z_re += c_re;

                    iteration++;
                }
                if (partSet) {
                    img.setPixel(x, y, 0);
                }else
                    img.setPixel(x, y, colors[iteration]);
            }
        }
        EventBus.getDefault().post(new GeneratedImg(img));
    }

    
    public void setLevel(int level){
        this.LEVEL = level;
    }
}
