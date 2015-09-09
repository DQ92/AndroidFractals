package com.example.daniel.fraktaleandroid.operating;

import android.graphics.Color;
import android.graphics.Point;

import com.example.daniel.fraktaleandroid.ColorConventer;

public class Fractal {
    
    /**
     * Size of window
     */
    protected int WIDTH = 1;
    protected int HEIGHT = 1;

    //The Minimum and Maximum real value
    protected double MINRE = -2.0;
    protected double MAXRE = 1.0;
    
    //The minimum and maximum imaginary values
    protected double MINIM = -1.1;
    protected double MAXIM = MINIM + (MAXRE - MINRE) * HEIGHT / WIDTH;
    

    protected double RE_FACTOR = (MAXRE - MINRE) / (WIDTH - 1);
    protected double IM_FACTOR = (MAXIM - MINIM) / (HEIGHT - 1);

    protected int MAX_ITER = 20;
    protected double zoom = 3;

    protected int[] colors;


    public void setView(Point clickPoint, int max, boolean zoomIn) {
        MAX_ITER = max;
        System.out.println(clickPoint.x + ": " + clickPoint.y);
        double c_im = MAXIM - clickPoint.y * IM_FACTOR;
        double c_re = MINRE + clickPoint.x * RE_FACTOR;
        double rediff, imdiff;
        if (zoomIn) {
            rediff = (MAXRE - MINRE) / zoom;
            imdiff = (MAXIM - MINIM) / zoom;
        } else {
            rediff = (MAXRE - MINRE) * zoom;
            imdiff = (MAXIM - MINIM) * zoom;
        }
        MINRE = c_re - (rediff / 2);
        MAXRE = c_re + (rediff / 2);
        MINIM = c_im - imdiff / 2;
        refactor();
    }

    
    public void setMaxIter(int max) {
        MAX_ITER = max;
        refactor();
    }

    
    /**
     * Sets the factors and MAXIM values
     */
    protected void refactor() {
        RE_FACTOR = (MAXRE - MINRE) / (WIDTH - 1);
        MAXIM = MINIM + (MAXRE - MINRE) * HEIGHT / WIDTH;
        IM_FACTOR = (MAXIM - MINIM) / (HEIGHT - 1);
    }
    
    
    protected void color() {

        this.colors = new int[MAX_ITER+1];
        for (int i = 0; i < MAX_ITER; i++) {

            int r = Color.red(i);
            int g = Color.green(i);
            int b = Color.blue(i);

            float[] hsv = new float[3];
            Color.RGBToHSV(r, g, b, hsv);

            this.colors[i] = ColorConventer.HSBtoRGB(i / 256f, 1, i / (i + 8f));
        }
    }

    
    public void setSize(int w, int h) {
        this.WIDTH = w;
        this.HEIGHT = h;
        refactor();
    }


}
