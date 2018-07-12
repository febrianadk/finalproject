package com.it.febrianadk.camera;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

public class Proses3Activity extends AppCompatActivity {

    public Button button_Next_Result, button_Back_Mulai3;
    Bitmap gray=null, sclera=null;
    int Rbesar = 0;
    int hxputih [];
    int flagGalatauCam=0;
    int jum;
    Bitmap resized=null, gambarutuh=null, gambaraslicrop=null;
    ProgressDialog progress;
    private Bitmap newBitmap=null;
    private ImageView iv_gambarasli, iv_medianfilter, iv_sharpness,iv_deteksi_pthsclera_iris,iv_grayscale,
            iv_binerisasi, iv_deteksitepi, iv_segmentasi;
    private TextView  tv_result3;
    private RelativeLayout relativeLayout;
    private Preprocess preprocess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proses3);

        newBitmap = BitmapData.bitmap;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        iv_gambarasli=(ImageView)findViewById(R.id.iv_gambarasli);
        iv_medianfilter=(ImageView)findViewById(R.id.iv_medianfilter);
        iv_sharpness=(ImageView)findViewById(R.id.iv_sharpness);
        iv_binerisasi=(ImageView)findViewById(R.id.iv_binerisasi);
        iv_deteksitepi=(ImageView)findViewById(R.id.iv_deteksitepi);
        //iv_segmentasi=(ImageView)findViewById(R.id.iv_segmentasi);
        //iv_deteksi_pthsclera_iris=(ImageView)findViewById(R.id.iv_deteksi_pthsclera_iris);
        iv_grayscale=(ImageView)findViewById(R.id.iv_grayscale);

        button_Next_Result=(Button)findViewById(R.id.button_Next_finalresult);
        button_Back_Mulai3=(Button)findViewById(R.id.button_Back_Mulai3);

        button_Back_Mulai3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent string = new Intent(Proses3Activity.this, MulaiProses3Activity.class);
                startActivity(string);
            }
        });


        /////Next button proses//////

        /////////////////////////////

        tv_result3=(TextView)findViewById(R.id.tv_result3);

        if(getIntent().hasExtra("flagGalatauCam")){
            flagGalatauCam=getIntent().getIntExtra("flagGalatauCam",0);
        }

        if(flagGalatauCam==0){
            byte[] byteArray2 = getIntent().getByteArrayExtra("gambarutuh");
            gambarutuh = BitmapFactory.decodeByteArray(byteArray2, 0, byteArray2.length); //nyalakan
            sclera = gambarutuh.copy(gambarutuh.getConfig(), true);
        }
        else{
            byte[] byteArray2 = getIntent().getByteArrayExtra("gambarutuh");
            gambarutuh = BitmapFactory.decodeByteArray(byteArray2, 0, byteArray2.length); //nyalakan
            sclera = gambarutuh.copy(gambarutuh.getConfig(), true);
        }

        progress = new ProgressDialog(Proses3Activity.this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        new LoadViewTask2().execute(); //Nyalakan

        process();
        BitmapData.processed=true;
    }

    ////onResume
    @Override
    protected void onResume() {
        super.onResume();
    }



    public void process() {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {

            }

            @Override
            protected Void doInBackground(Void... params) {

                preprocess = new Preprocess(sclera); //make class sclera

                final Bitmap finalOriginalBitmap = preprocess.getOriginalBitmap();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iv_gambarasli.setImageBitmap(sclera);
                    }
                });

                final Bitmap finalmedianFilterBitmap = preprocess.getMedianFilter(finalOriginalBitmap);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iv_medianfilter.setImageBitmap(finalmedianFilterBitmap);
                    }
                });

                //sharpness
                final Bitmap finalSharpenBitmap = preprocess.getSharpen(finalmedianFilterBitmap);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iv_sharpness.setImageBitmap(finalSharpenBitmap);
                    }
                });

                //deteksi iris dan putih sclera
               /* final Bitmap finalDeteksiIrisScleraBitmap = preprocess.getDeteksiIrisSclera(finalSharpenBitmap);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iv_deteksi_pthsclera_iris.setImageBitmap(finalDeteksiIrisScleraBitmap);
                    }
                });*/

                //Grayscale
                final Bitmap finalGrayscaleBitmap = preprocess.getGrayScale(finalSharpenBitmap);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iv_grayscale.setImageBitmap(finalGrayscaleBitmap);
                    }
                });

                //deteksi tepi Sobel
                final Bitmap finalSobelBitmap = preprocess.getSobel(finalGrayscaleBitmap);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iv_deteksitepi.setImageBitmap(finalSobelBitmap);
                    }
                });

                //binerisasi
                final Bitmap finalBinarizationBitmap = preprocess.getBinarization(finalSobelBitmap);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iv_binerisasi.setImageBitmap(finalBinarizationBitmap);
                    }
                });

                //segmentasi

                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {

            }
        }.execute();
    }

    public void onBackPressed() {

    }


   /* private void autoCropuntukgalery(Bitmap bitmap) {
        int tinggigambar=bitmap.getHeight();
        int lebargambar=bitmap.getWidth();
        int titiktengahx=lebargambar/2;
        int titiktengahy=tinggigambar/2;
        int seperempat=lebargambar/4;
        Bitmap croppedBitmap = Bitmap.createBitmap(bitmap,titiktengahx-(seperempat/2), titiktengahy-(seperempat/2), seperempat, seperempat);

        gambaraslicrop = croppedBitmap.copy(croppedBitmap.getConfig(), true);
        getBinerization(gambaraslicrop);
    }*/



    private class LoadViewTask2 extends AsyncTask<Void, Integer, Void>
    {
        //Before running code in the separate thread
        @Override
        protected void onPreExecute()
        {
            progress.show();
        }
        @Override
        protected Void doInBackground(Void... params)
        {
            if(sclera!=null) {
                if(flagGalatauCam==0){//kalau 1 kamera

                    gambaraslicrop = sclera.copy(sclera.getConfig(), true);
                   // getBinerization(sclera);
                }
                else{
                    gambaraslicrop = sclera.copy(sclera.getConfig(), true);
                   // getBinerization(sclera); //buka lagi
                }

            }
            return null;
        }

        //Update the TextView and the progress at progress bar
        @Override
        protected void onProgressUpdate(Integer... values)
        {

        }

        @Override
        protected void onPostExecute(Void result)
        {
            if(progress.isShowing())
            {
                progress.dismiss();
            }
            if(sclera!=null) {
                iv_gambarasli.setImageBitmap(gambarutuh);

            }
        }
    }

}
