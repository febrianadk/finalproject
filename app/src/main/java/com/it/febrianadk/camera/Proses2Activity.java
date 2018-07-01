package com.it.febrianadk.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Proses2Activity extends AppCompatActivity {

    public Button button_Back_proses2, button_Next_proses3;
    private Bitmap newBitmap=null, tempBitmap, tempBitmap2;
    private ImageView gambar_crop_lingkaran,  gambar_grayscale, gambar_sobel,
            gambar_segmentasi, gambar_ROI;
    private Preprocess preprocess;
    private TextView tes, textView_Result2;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proses2);

        newBitmap = BitmapData.bitmap;

        gambar_crop_lingkaran = (ImageView) findViewById(R.id.gambar_crop_lingkaran);
        gambar_grayscale = (ImageView) findViewById(R.id.gambar_grayscale);
        gambar_sobel = (ImageView) findViewById(R.id.gambar_sobel);
        gambar_segmentasi = (ImageView) findViewById(R.id.gambar_segmentasi);
        gambar_ROI= (ImageView) findViewById(R.id.gambar_ROI);
        textView_Result2 = (TextView) findViewById(R.id.textView_Result2);

        button_Next_proses3=(Button)findViewById(R.id.button_Next_proses3);
        button_Back_proses2=(Button)findViewById(R.id.button_Back_proses2);

        button_Next_proses3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent m = new Intent(Proses2Activity.this, MulaiProses3Activity.class);
                finish();
                startActivity(m);
            }
        });

        button_Back_proses2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent m = new Intent(Proses2Activity.this, MulaiActivity.class);
                startActivity(m);
            }
        });

        process();

        BitmapData.processed = true;
    }

    public String getFeatureExtraction(Bitmap bitmap) {
        double roiBitmapArea = bitmap.getWidth() * bitmap.getHeight();

        // Grayscale
        int blackRasioGrayscale = 0, whiteRasioGrayscale = 0;
        for (int i = 0; i < bitmap.getWidth(); i++) {
            for (int j = 0; j < bitmap.getHeight(); j++) {
                int color = bitmap.getPixel(i, j);
                int cGray = (Color.red(color) + Color.green(color) + Color.blue(color)) / 3;

                if (cGray <= 50) {
                    blackRasioGrayscale++;
                } else {
                    whiteRasioGrayscale++;
                }
            }
        }

        double whiteRasioAverageGrayscale = whiteRasioGrayscale / roiBitmapArea;
        double blackRasioAverageGrayscale = blackRasioGrayscale / roiBitmapArea;

        // Distance
        int blackRasioDistance = 0, whiteRasioDistance = 0;
        for (int i = 0; i < bitmap.getWidth(); i++) {
            for (int j = 0; j < bitmap.getHeight(); j++) {
                int color = bitmap.getPixel(i, j);
                int r = Color.red(color);
                int g = Color.green(color);
                int b = Color.blue(color);
                int white = 255;

                // white & not white
                double distance_white = Math.sqrt(Math.pow((r - white), 2) + Math.pow((g - white), 2) + Math.pow((b - white), 2));
                if (distance_white < 350) {
                    whiteRasioDistance++;
                } else {
                    blackRasioDistance++;
                }
            }
        }

        double whiteRasioAverageDistance = whiteRasioDistance / roiBitmapArea;
        double blackRasioAverageDistance = blackRasioDistance / roiBitmapArea;

        // thresholding classification
        // new threshold based on new ROI area
        //grayscale
        double whiteThresholdGrayscale = 0.26, blackThresholdGrayscale = 0.74;
        if (whiteRasioAverageGrayscale < whiteThresholdGrayscale && blackRasioAverageGrayscale > blackThresholdGrayscale) {
            return "\n NORMAL";
        } else {
            return "\n ABNORMAL";
        }
    }

    public void process() {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {

            }

            @Override
            protected Void doInBackground(Void... params) {

                preprocess = new Preprocess(newBitmap); //make class BitmapData

                final Bitmap finalOriginalBitmap = preprocess.getOriginalBitmap();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        gambar_crop_lingkaran.setImageBitmap(newBitmap);
                    }
                });
                final Bitmap finalGrayscaleBitmap = preprocess.getGrayScale(finalOriginalBitmap);// process the color crop bitmap with grayscale
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        gambar_grayscale.setImageBitmap(finalGrayscaleBitmap);
                    }
                });
                final Bitmap finalSobelBitmap = preprocess.getSobel(finalGrayscaleBitmap);// process the grayscale image with sobel
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        gambar_sobel.setImageBitmap(finalSobelBitmap);
                    }
                });
                final Bitmap finalSegmentedBitmap = preprocess.getSegmentation(finalSobelBitmap);// show the roi area from sobel image
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        gambar_segmentasi.setImageBitmap(finalSegmentedBitmap);
                    }
                });
                final Bitmap finalROIBitmap = preprocess.getROI(finalSobelBitmap);// get the roi area from sobel image
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        gambar_ROI.setImageBitmap(finalROIBitmap);
                    }
                });

                final String result = getFeatureExtraction(finalROIBitmap);// extraxt the feature of roi area
                BitmapData.result = result;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView_Result2.setText(result);
                    }
                });
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                //progressImage.dismiss();
//                relativeLayout.setVisibility(View.GONE);
            }
        }.execute();
    }

    public void onBackPressed() {

    }

}
