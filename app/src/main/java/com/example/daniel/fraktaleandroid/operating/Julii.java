package com.example.daniel.fraktaleandroid.operating;

import android.graphics.Bitmap;

import com.example.daniel.fraktaleandroid.GeneratedImg;
import com.example.daniel.fraktaleandroid.MainActivity;
import com.example.daniel.fraktaleandroid.OnUpdateProgress;

import de.greenrobot.event.EventBus;

public class Julii extends Fractal implements Runnable {

    OnUpdateProgress updateProgress;
    private final MainActivity context;
    public Bitmap img;


    public Julii(Bitmap img, MainActivity context,
                      OnUpdateProgress updateProgress) {
        this.img = img;
        this.context = context;
        this.updateProgress = updateProgress;

    }

    @Override
    public synchronized void run() {

        renderFractal();
    }

    /**
     * Reset default values
     */
    public void resetValues() {
        MINRE = -2.5;
        MAXRE = 3.0;
        MINIM = -1.75;
        zoom = 2;
        refactor();
    }
        

    public void renderFractal() {
        color();

        double C_re = -0.123;
        double C_im = 0.745;

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

                    if (Z_re2 + Z_im2 > 4) {
                        partSet = false;
                        break;
                    }
                    
                    Z_im = 2 * Z_re * Z_im + C_im;
                    Z_re = Z_re2 - Z_im2 + C_re;

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

    public Bitmap getImg(){
        return this.img;
    }
}
