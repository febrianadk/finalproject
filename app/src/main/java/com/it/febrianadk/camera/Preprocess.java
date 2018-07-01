package com.it.febrianadk.camera;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Arrays;

/**
 * Created by Aditya Afgan H on 14-Sep-15.
 */
public class Preprocess {
    private Bitmap originalBitmap;
    private int pw = 500, ph = 500;

    public Preprocess(Bitmap originalBitmap) {
        this.originalBitmap = originalBitmap;
    }

    public Bitmap getOriginalBitmap() {
        return this.originalBitmap;
    }

    /* -- Median Filter -- */
    public Bitmap getMedianFilter(Bitmap bitmap) {
        Bitmap medianFilterBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas cMedianFilter = new Canvas(medianFilterBitmap);
        cMedianFilter.drawBitmap(medianFilterBitmap, 0, 0, new Paint());

        for (int x = 0; x < bitmap.getWidth(); x++) {
            for (int y = 0; y < bitmap.getHeight(); y++) {
                int[] color = new int[9];
                int[] wr = new int[9];
                int[] wg = new int[9];
                int[] wb = new int[9];
                int xr = 0, xg = 0, xb = 0;

                if (x - 1 < 0 || y - 1 < 0) {
                    color[0] = Color.rgb(0, 0, 0);
                } else {
                    color[0] = bitmap.getPixel(x - 1, y - 1);
                }

                if (x - 1 < 0) {
                    color[1] = Color.rgb(0, 0, 0);
                } else {
                    color[1] = bitmap.getPixel(x - 1, y);
                }

                if (x - 1 < 0 || y + 1 > bitmap.getHeight() - 1) {
                    color[2] = Color.rgb(0, 0, 0);
                } else {
                    color[2] = bitmap.getPixel(x - 1, y + 1);
                }

                if (y - 1 < 0) {
                    color[3] = Color.rgb(0, 0, 0);
                } else {
                    color[3] = bitmap.getPixel(x, y - 1);
                }

                color[4] = bitmap.getPixel(x, y);

                if (y + 1 > bitmap.getHeight() - 1) {
                    color[5] = Color.rgb(0, 0, 0);
                } else {
                    color[5] = bitmap.getPixel(x, y + 1);
                }

                if (x + 1 > bitmap.getWidth() - 1 || y - 1 < 0) {
                    color[6] = Color.rgb(0, 0, 0);
                } else {
                    color[6] = bitmap.getPixel(x + 1, y - 1);
                }

                if (x + 1 > bitmap.getWidth() - 1) {
                    color[7] = Color.rgb(0, 0, 0);
                } else {
                    color[7] = bitmap.getPixel(x + 1, y);
                }

                if (x + 1 > bitmap.getWidth() - 1 || y + 1 > bitmap.getHeight() - 1) {
                    color[8] = Color.rgb(0, 0, 0);
                } else {
                    color[8] = bitmap.getPixel(x + 1, y + 1);
                }

                for (int i = 0; i < wr.length; i++) {
                    wr[i] = Color.red(color[i]);
                    wg[i] = Color.green(color[i]);
                    wb[i] = Color.blue(color[i]);
                }

                Arrays.sort(wr);
                Arrays.sort(wg);
                Arrays.sort(wb);

                xr = wr[4];
                if (xr < 0) xr = -xr;
                if (xr > 255) xr = 255;

                xg = wg[4];
                if (xg < 0) xg = -xg;
                if (xg > 255) xg = 255;

                xb = wb[4];
                if (xb < 0) xb = -xb;
                if (xb > 255) xb = 255;

                int newFilterColor = Color.rgb(xr, xg, xb);
                medianFilterBitmap.setPixel(x, y, newFilterColor);
            }
        }
        return medianFilterBitmap;
    }

    /* -- Sharpen -- */
    public Bitmap getSharpen(Bitmap bitmap) {
        Bitmap sharpenBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas cSharpen = new Canvas(sharpenBitmap);
        cSharpen.drawBitmap(sharpenBitmap, 0, 0, new Paint());

        int[] l = new int[9];
        l[0] = 0;
        l[1] = -1;
        l[2] = 0;
        l[3] = -1;
        l[4] = 5;
        l[5] = -1;
        l[6] = 0;
        l[7] = -1;
        l[8] = 0;

        for (int x = 0; x < bitmap.getWidth(); x++) {
            for (int y = 0; y < bitmap.getHeight(); y++) {
                int[] color = new int[9];
                int xr = 0, xg = 0, xb = 0;

                if (x - 1 < 0 || y - 1 < 0) {
                    color[0] = Color.rgb(0, 0, 0);
                } else {
                    color[0] = bitmap.getPixel(x - 1, y - 1);
                }

                if (x - 1 < 0) {
                    color[1] = Color.rgb(0, 0, 0);
                } else {
                    color[1] = bitmap.getPixel(x - 1, y);
                }

                if (x - 1 < 0 || y + 1 > bitmap.getHeight() - 1) {
                    color[2] = Color.rgb(0, 0, 0);
                } else {
                    color[2] = bitmap.getPixel(x - 1, y + 1);
                }

                if (y - 1 < 0) {
                    color[3] = Color.rgb(0, 0, 0);
                } else {
                    color[3] = bitmap.getPixel(x, y - 1);
                }

                color[4] = bitmap.getPixel(x, y);

                if (y + 1 > bitmap.getHeight() - 1) {
                    color[5] = Color.rgb(0, 0, 0);
                } else {
                    color[5] = bitmap.getPixel(x, y + 1);
                }

                if (x + 1 > bitmap.getWidth() - 1 || y - 1 < 0) {
                    color[6] = Color.rgb(0, 0, 0);
                } else {
                    color[6] = bitmap.getPixel(x + 1, y - 1);
                }

                if (x + 1 > bitmap.getWidth() - 1) {
                    color[7] = Color.rgb(0, 0, 0);
                } else {
                    color[7] = bitmap.getPixel(x + 1, y);
                }

                if (x + 1 > bitmap.getWidth() - 1 || y + 1 > bitmap.getHeight() - 1) {
                    color[8] = Color.rgb(0, 0, 0);
                } else {
                    color[8] = bitmap.getPixel(x + 1, y + 1);
                }

                for (int i = 0; i < color.length; i++) {
                    xr = xr + (l[i] * Color.red(color[i]));
                    xg = xg + (l[i] * Color.green(color[i]));
                    xb = xb + (l[i] * Color.blue(color[i]));
                }

                if (xr < 0) xr = -xr;
                if (xr > 255) xr = 255;

                if (xg < 0) xg = -xg;
                if (xg > 255) xg = 255;

                if (xb < 0) xb = -xb;
                if (xb > 255) xb = 255;

                int newSharpenColor = Color.rgb(xr, xg, xb);
                sharpenBitmap.setPixel(x, y, newSharpenColor);
            }
        }
        return sharpenBitmap;
    }

    /* -- Binarization -- */
    public Bitmap getBinarization(Bitmap bitmap) {
        Bitmap binarizationBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas cBinarization = new Canvas(binarizationBitmap);
        cBinarization.drawBitmap(binarizationBitmap, 0, 0, new Paint());

        int white = 255;
        double distance_white;

        for (int x = 0; x < bitmap.getWidth(); x++) {
            for (int y = 0; y < bitmap.getHeight(); y++) {

                distance_white = 0;

                int color = bitmap.getPixel(x, y);
                distance_white = Math.pow((Color.red(color) - white), 2)
                        + Math.pow((Color.green(color) - white), 2)
                        + Math.pow((Color.blue(color) - white), 2);

                int x_white = 0;
                if (Math.sqrt(distance_white) < 175) {
                    x_white = 255;
                }

                int newBWColor = Color.rgb(x_white, x_white, x_white);
                binarizationBitmap.setPixel(x, y, newBWColor);
            }
        }
        return binarizationBitmap;
    }

    /* -- Binarization Inverse-- */
    public Bitmap getBinarizationInverse(Bitmap bitmap) {
        Bitmap binarizationInverseBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas cBinarization = new Canvas(binarizationInverseBitmap);
        cBinarization.drawBitmap(binarizationInverseBitmap, 0, 0, new Paint());

        int black = 0;

        for (int x = 0; x < bitmap.getWidth(); x++) {
            double xg;
            for (int y = 0; y < bitmap.getHeight(); y++) {
                int color = bitmap.getPixel(x, y);
                xg = Math.pow((Color.red(color) - black), 2)
                        + Math.pow((Color.green(color) - black), 2)
                        + Math.pow((Color.blue(color) - black), 2);

                int xbw = 0;
                if (Math.sqrt(xg) < 100) {
                    xbw = 255;
                }

                int newBWColor = Color.rgb(xbw, xbw, xbw);
                binarizationInverseBitmap.setPixel(x, y, newBWColor);
            }
        }
        return binarizationInverseBitmap;
    }

    /*-- Auto Crop --*/
    public Bitmap getAutoCrop(Bitmap bitmap, Bitmap bitmap2) {
        // histogram phase //
        int[] white_horizontal = new int[bitmap.getWidth()];
        int[] white_vertical = new int[bitmap.getHeight()];
        int[] horizontal_difference = new int[bitmap.getWidth()];
        int[] vertical_difference = new int[bitmap.getHeight()];
        double white_sum = 0;

        for (int i = 0; i < bitmap.getWidth(); i++) {
            white_horizontal[i] = 0;
        }
        for (int i = 0; i < bitmap.getHeight(); i++) {
            white_vertical[i] = 0;
        }

        int white = 255;

        for (int x = 0; x < bitmap.getWidth(); x++) {
            for (int y = 0; y < bitmap.getHeight(); y++) {
                int color = bitmap.getPixel(x, y);
                if (Color.red(color) == white && Color.green(color) == white && Color.blue(color) == white) {
                    white_horizontal[x]++;
                }
                if (x == 0) {
                    horizontal_difference[x] = 0;
                } else {
                    horizontal_difference[x] = Math.abs(white_horizontal[x - 1] - white_horizontal[x]);
                }
            }
        }

        for (int y = 0; y < bitmap.getHeight(); y++) {
            for (int x = 0; x < bitmap.getWidth(); x++) {
                int color = bitmap.getPixel(x, y);
                if (Color.red(color) == 0 && Color.green(color) == 0 && Color.blue(color) == 0) {
                    white_vertical[y]++;
                }
                if (y == 0) {
                    vertical_difference[y] = 0;
                } else {
                    vertical_difference[y] = Math.abs(white_vertical[y - 1] - white_vertical[y]);
                }
            }
        }
        // end of histogram phase //

        // find point //
        int leftThreshold = 0;
        int rightThreshold = 0;
        int topThreshold = 0;
        int bottomThreshold = 0;

        int leftIndexMinimum = bitmap.getWidth() / 80 * 16;
        int leftIndexMaximum = bitmap.getWidth() / 80 * 26;

        int rightIndexMinimum = bitmap.getWidth() / 80 * 65;
        int rightIndexMaximum = bitmap.getWidth() / 80 * 55;

        int topIndexMinimum = bitmap.getHeight() / 106 * 32;
        int topIndexMaximum = bitmap.getHeight() / 106 * 42;

        int bottomIndexMinimum = bitmap.getHeight() / 106 * 78;
        int bottomIndexMaximum = bitmap.getHeight() / 106 * 68;

        int leftPoint = 0, rightPoint = 0, topPoint = 0, bottomPoint = 0;

        //left
        for (int i = leftIndexMinimum; i <= leftIndexMaximum; i++) {
            if (horizontal_difference[i] > leftThreshold) {
                leftThreshold = horizontal_difference[i];
                leftPoint = i;
                //break;
            }
        }

        //right
        for (int i = rightIndexMinimum; i >= rightIndexMaximum; i--) {
            if (horizontal_difference[i] > rightThreshold) {
                rightThreshold = horizontal_difference[i];
                rightPoint = i;
                //break;
            }
        }

        //top
        for (int i = topIndexMinimum; i <= topIndexMaximum; i++) {
            if (vertical_difference[i] > topThreshold) {
                topThreshold = vertical_difference[i];
                topPoint = i;
                //break;
            }
        }

        //bottom
        for (int i = bottomIndexMinimum; i >= bottomIndexMaximum; i--) {
            if (vertical_difference[i] > bottomThreshold) {
                bottomThreshold = vertical_difference[i];
                bottomPoint = i;
                //break;
            }
        }

        //draw iris image
        int widthBitmapCropped = rightPoint - leftPoint;
        int heightBitmapCropped = bottomPoint - topPoint;

        Bitmap croppedBitmap = Bitmap.createBitmap(widthBitmapCropped + 1, heightBitmapCropped + 1, Bitmap.Config.RGB_565);
        Canvas cIrisCrop = new Canvas(croppedBitmap);
        cIrisCrop.drawBitmap(croppedBitmap, 0, 0, new Paint());

        int xb = 0, yb = 0;

        for (int x = leftPoint - 1; x < rightPoint; x++) {
            for (int y = topPoint - 1; y < bottomPoint; y++) {
                int color = bitmap2.getPixel(x, y);
                croppedBitmap.setPixel(xb, yb, Color.rgb(Color.red(color), Color.green(color), Color.blue(color)));
                yb++;
            }
            xb++;
            yb = 0;
        }
        // end of find point //

        return croppedBitmap;
    }

    /*-- Auto Crop ms syarifa's method --*/
    public Bitmap getIrisCrop(Bitmap bitmap) {
        // help array
        float[] arrayTemp;

        int endPoint_horizontal = 0, firstPoint_horizontal = 0;
        int endPoint_vertical = 0, firstPoint_vertical = 0;

        /*- Horizontal -*/
        float[] black_horizontal = new float[bitmap.getWidth()];
        float[] white_horizontal = new float[bitmap.getWidth()];

        // help index
        int[] idx_horizontal = new int[bitmap.getWidth()];

        for (int i = 0; i < bitmap.getWidth(); i++) {
            black_horizontal[i] = 0;
            white_horizontal[i] = 0;
        }
        for (int x = 0; x < bitmap.getWidth(); x++) {
            for (int y = 0; y < bitmap.getHeight(); y++) {
                int color = bitmap.getPixel(x, y);
                if (Color.red(color) == 0 && Color.green(color) == 0 && Color.blue(color) == 0) {
                    black_horizontal[x]++;
                } else {
                    white_horizontal[x]++;
                }
                idx_horizontal[x] = x;
            }
        }

        // first point //
        int firstLimit1_horizontal = 49, firstLimit2_horizontal = 199;
        float[] firstHisto_horizontal = new float[firstLimit2_horizontal - firstLimit1_horizontal];
        int[] firstIdx_horizontal = new int[firstLimit2_horizontal - firstLimit1_horizontal];

        for (int x = 0; x < firstHisto_horizontal.length; x++) {
            firstHisto_horizontal[x] = white_horizontal[firstLimit1_horizontal];
            firstIdx_horizontal[x] = idx_horizontal[firstLimit1_horizontal];
            firstLimit1_horizontal++;
        }

        // find the highest point
        arrayTemp = Arrays.copyOf(firstHisto_horizontal, firstHisto_horizontal.length);
        Arrays.sort(arrayTemp);
        for (int x = 0; x < firstHisto_horizontal.length; x++) {
            if (firstHisto_horizontal[x] == arrayTemp[arrayTemp.length - 1]) {
                firstPoint_horizontal = firstIdx_horizontal[x];
                break;
            }
        }

        // end point //
        int endLimit1_horizontal = 240, endLimit2_horizontal = 340;
        float[] endHisto_horizontal = new float[endLimit2_horizontal - endLimit1_horizontal];
        int[] endIdx_horizontal = new int[endLimit2_horizontal - endLimit1_horizontal];

        for (int x = 0; x < endHisto_horizontal.length; x++) {
            endHisto_horizontal[x] = white_horizontal[endLimit1_horizontal];
            endIdx_horizontal[x] = idx_horizontal[endLimit1_horizontal];
            endLimit1_horizontal++;
        }

        // find the highest point
        arrayTemp = Arrays.copyOf(endHisto_horizontal, endHisto_horizontal.length);
        Arrays.sort(arrayTemp);
        for (int x = 0; x < endHisto_horizontal.length; x++) {
            if (endHisto_horizontal[x] == arrayTemp[arrayTemp.length - 1]) {
                endPoint_horizontal = endIdx_horizontal[x];
                break;
            }
        }

        /*- Vertical -*/
        float[] black_vertical = new float[bitmap.getHeight()];
        float[] white_vertical = new float[bitmap.getHeight()];

        // help index
        int[] idx_vertical = new int[bitmap.getHeight()];

        for (int i = 0; i < bitmap.getHeight(); i++) {
            black_vertical[i] = 0;
            white_vertical[i] = 0;
        }
        for (int y = 0; y < bitmap.getHeight(); y++) {
            for (int x = 0; x < bitmap.getWidth(); x++) {
                int color = bitmap.getPixel(x, y);
                if (Color.red(color) == 0 && Color.green(color) == 0 && Color.blue(color) == 0) {
                    black_vertical[y]++;
                } else {
                    white_vertical[y]++;
                }
                idx_vertical[y] = y;
            }
        }

        // first point //
        int firstLimit_vertical = 150;
        float[] firstHisto_vertical = new float[firstLimit_vertical];
        int[] firstIdx_vertical = new int[firstLimit_vertical];

        for (int x = 0; x < firstHisto_vertical.length; x++) {
            firstHisto_vertical[x] = white_vertical[x];
            firstIdx_vertical[x] = idx_vertical[x];
        }

        // find the highest point
        arrayTemp = Arrays.copyOf(firstHisto_vertical, firstHisto_vertical.length);
        Arrays.sort(arrayTemp);
        for (int x = 0; x < firstHisto_vertical.length; x++) {
            if (firstHisto_vertical[x] == arrayTemp[arrayTemp.length - 1]) {
                firstPoint_vertical = firstIdx_vertical[x];
                break;
            }
        }

        // end point //
        int endLimit1_vertical = 250, endLimit2_vertical = 300;
        float[] endHisto_vertical = new float[endLimit2_vertical - endLimit1_vertical];
        int[] endIdx_vertical = new int[endLimit2_vertical - endLimit1_vertical];

        for (int x = 0; x < endHisto_vertical.length; x++) {
            endHisto_vertical[x] = white_vertical[endLimit1_vertical];
            endIdx_vertical[x] = idx_vertical[endLimit1_vertical];
            endLimit1_vertical++;
        }

        // find the highest point
        arrayTemp = Arrays.copyOf(endHisto_vertical, endHisto_vertical.length);
        Arrays.sort(arrayTemp);
        for (int x = 0; x < endHisto_vertical.length; x++) {
            if (endHisto_vertical[x] == arrayTemp[arrayTemp.length - 1]) {
                endPoint_vertical = endIdx_vertical[x];
                break;
            }
        }

        /*- Draw Bitmap -*/
        int widthBitmapCropped = endPoint_horizontal - firstPoint_horizontal + 1;
        int hightBitmapCropped = endPoint_vertical - firstPoint_vertical + 1;

        Bitmap croppedBitmap = Bitmap.createBitmap(widthBitmapCropped, hightBitmapCropped, Bitmap.Config.RGB_565);
        Canvas cIrisCrop = new Canvas(croppedBitmap);
        cIrisCrop.drawBitmap(croppedBitmap, 0, 0, new Paint());

        int xb = 0, yb = 0;

        for (int x = firstPoint_horizontal; x <= endPoint_horizontal; x++) {
            for (int y = firstPoint_vertical; y <= endPoint_vertical; y++) {
                int color = bitmap.getPixel(x, y);
                croppedBitmap.setPixel(xb, yb, Color.rgb(Color.red(color), Color.green(color), Color.blue(color)));
                yb++;
            }
            xb++;
            yb = 0;
        }

        //croppedBitmap = Bitmap.createScaledBitmap(croppedBitmap, pw, ph, true);
        return croppedBitmap;
    }

    /* -- GrayscaleBT709 -- */
    public Bitmap getGrayScale(Bitmap bitmap) {
        Bitmap grayScaleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas cGrayScale = new Canvas(grayScaleBitmap);
        cGrayScale.drawBitmap(grayScaleBitmap, 0, 0, new Paint());

        for (int x = 0; x < bitmap.getWidth(); x++) {
            for (int y = 0; y < bitmap.getHeight(); y++) {
                int color = bitmap.getPixel(x, y);
                int xg = (int) ((Color.red(color) * 0.2125)
                        + (Color.green(color) * 0.7154)
                        + (Color.blue(color) * 0.0721));
                int newGrayScaleColor = Color.rgb(xg, xg, xg);
                grayScaleBitmap.setPixel(x, y, newGrayScaleColor);
            }
        }

        return grayScaleBitmap;
    }

    /* -- Sobel Edge Detector -- */
    public Bitmap getSobel(Bitmap bitmap) {
        Bitmap sobelBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas cSobel = new Canvas(sobelBitmap);
        cSobel.drawBitmap(sobelBitmap, 0, 0, new Paint());

        int[] h = new int[9];
        h[0] = -1;
        h[1] = -2;
        h[2] = -1;
        h[3] = 0;
        h[4] = 0;
        h[5] = 0;
        h[6] = 1;
        h[7] = 2;
        h[8] = 1;

        int[] v = new int[9];
        v[0] = 1;
        v[1] = 0;
        v[2] = -1;
        v[3] = 2;
        v[4] = 0;
        v[5] = -2;
        v[6] = 1;
        v[7] = 0;
        v[8] = -1;

        for (int x = 0; x < bitmap.getWidth(); x++) {
            for (int y = 0; y < bitmap.getHeight(); y++) {
                int[] color = new int[9];
                int[] wr = new int[9];
                int[] wg = new int[9];
                int[] wb = new int[9];
                int xr = 0, xg = 0, xb = 0;

                if (x - 1 < 0 || y - 1 < 0) {
                    color[0] = Color.rgb(0, 0, 0);
                } else {
                    color[0] = bitmap.getPixel(x - 1, y - 1);
                }

                if (x - 1 < 0) {
                    color[1] = Color.rgb(0, 0, 0);
                } else {
                    color[1] = bitmap.getPixel(x - 1, y);
                }

                if (x - 1 < 0 || y + 1 > bitmap.getHeight() - 1) {
                    color[2] = Color.rgb(0, 0, 0);
                } else {
                    color[2] = bitmap.getPixel(x - 1, y + 1);
                }

                if (y - 1 < 0) {
                    color[3] = Color.rgb(0, 0, 0);
                } else {
                    color[3] = bitmap.getPixel(x, y - 1);
                }

                color[4] = bitmap.getPixel(x, y);

                if (y + 1 > bitmap.getHeight() - 1) {
                    color[5] = Color.rgb(0, 0, 0);
                } else {
                    color[5] = bitmap.getPixel(x, y + 1);
                }

                if (x + 1 > bitmap.getWidth() - 1 || y - 1 < 0) {
                    color[6] = Color.rgb(0, 0, 0);
                } else {
                    color[6] = bitmap.getPixel(x + 1, y - 1);
                }

                if (x + 1 > bitmap.getWidth() - 1) {
                    color[7] = Color.rgb(0, 0, 0);
                } else {
                    color[7] = bitmap.getPixel(x + 1, y);
                }

                if (x + 1 > bitmap.getWidth() - 1 || y + 1 > bitmap.getHeight() - 1) {
                    color[8] = Color.rgb(0, 0, 0);
                } else {
                    color[8] = bitmap.getPixel(x + 1, y + 1);
                }

                // red
                int xv = 0, xh = 0;
                for (int i = 0; i < wr.length; i++) {
                    xv = xv + (v[i] * Color.red(color[i]));
                    xh = xh + (h[i] * Color.red(color[i]));
                }

                xr = xh + xv;

                if (xr < 0) xr = -xr;
                if (xr > 255) xr = 255;

                //green
                xv = 0;
                xh = 0;
                for (int i = 0; i < wg.length; i++) {
                    xv = xv + (v[i] * Color.green(color[i]));
                    xh = xh + (h[i] * Color.green(color[i]));
                }

                xg = xh + xv;

                if (xg < 0) xg = -xg;
                if (xg > 255) xg = 255;

                //blue
                xv = 0;
                xh = 0;
                for (int i = 0; i < wr.length; i++) {
                    xv = xv + (v[i] * Color.blue(color[i]));
                    xh = xh + (h[i] * Color.blue(color[i]));
                }

                xb = xh + xv;

                if (xb < 0) xb = -xb;
                if (xb > 255) xb = 255;

                int newSobelColor = Color.rgb(xr, xg, xb);
                sobelBitmap.setPixel(x, y, newSobelColor);
            }
        }
        return sobelBitmap;
    }

    /* -- Segementation Heart Area -- */
    public Bitmap getSegmentation(Bitmap bitmap) {
        Bitmap segmentationBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas cSegmentation = new Canvas(segmentationBitmap);
        cSegmentation.drawBitmap(segmentationBitmap, 0, 0, new Paint());

        // heart area
        int leftIndex = (int) (bitmap.getWidth() * 22.5 / 32);
        int rightIndex = bitmap.getWidth() * 25 / 32;
        int topIndex = (int) (bitmap.getHeight() * 12.5 / 32);
        int bottomIndex = bitmap.getHeight() * 17 / 32;

        for (int i = 0; i < bitmap.getWidth(); i++) {
            for (int j = 0; j < bitmap.getHeight(); j++) {
                int color = bitmap.getPixel(i, j);

                if ((i == leftIndex || i == rightIndex) && j >= topIndex && j <= bottomIndex) {
                    segmentationBitmap.setPixel(i, j, Color.RED);
                } else if (i >= leftIndex && i <= rightIndex && (j == topIndex || j == bottomIndex)) {
                    segmentationBitmap.setPixel(i, j, Color.RED);
                } else {
                    segmentationBitmap.setPixel(i, j, Color.rgb(Color.red(color), Color.green(color), Color.blue(color)));
                }
            }
        }

        return segmentationBitmap;
    }

    /* -- ROI -- */
    public Bitmap getROI(Bitmap bitmap) {
        // heart area
        int leftIndex = (int) (bitmap.getWidth() * 22.5 / 32);
        int rightIndex = bitmap.getWidth() * 25 / 32;
        int topIndex = (int) (bitmap.getHeight() * 12.5 / 32);
        int bottomIndex = bitmap.getHeight() * 17 / 32;

        Bitmap roiBitmap = Bitmap.createBitmap(rightIndex - leftIndex, bottomIndex - topIndex, Bitmap.Config.RGB_565);
        Canvas cROI = new Canvas(roiBitmap);
        cROI.drawBitmap(roiBitmap, 0, 0, new Paint());

        int xb = 0, yb = 0;

        for (int x = leftIndex + 1; x <= rightIndex; x++) {
            for (int y = topIndex + 1; y <= bottomIndex; y++) {
                int color = bitmap.getPixel(x, y);
                roiBitmap.setPixel(xb, yb, Color.rgb(Color.red(color), Color.green(color), Color.blue(color)));
                yb++;
            }
            xb++;
            yb = 0;
        }

        return roiBitmap;
    }
}
