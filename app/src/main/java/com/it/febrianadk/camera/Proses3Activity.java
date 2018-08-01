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
import java.text.DecimalFormat;

public class Proses3Activity extends AppCompatActivity {

    public Button button_Next_proses4, button_Back_Mulai3;
    Bitmap gray=null, sclera=null;
    int flagGalatauCam=0;
    Bitmap resized=null, gambarutuh=null, gambaraslicrop=null;
    ProgressDialog progress;
    DecimalFormat df;
    private Bitmap newBitmap=null;
    private ImageView iv_gambarasli, iv_medianfilter, iv_sharpness,iv_deteksi_pthsclera_iris,iv_grayscale,
            iv_binerisasi, iv_deteksitepi, iv_segmentasi,iv_roi_kiri;
    private TextView  tv_result_sclera_kiri, textRatio_sclera_kiri;
    private RelativeLayout relativeLayout;
    private Preprocess preprocess;

    String nerimaHasilKolesterol, nerimaHasilJantung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proses3);

        newBitmap = BitmapData.bitmap;

        df = new DecimalFormat("##.###");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        iv_gambarasli=(ImageView)findViewById(R.id.iv_gambarasli);
        iv_medianfilter=(ImageView)findViewById(R.id.iv_medianfilter);
        iv_sharpness=(ImageView)findViewById(R.id.iv_sharpness);
        iv_binerisasi=(ImageView)findViewById(R.id.iv_binerisasi);
        iv_deteksitepi=(ImageView)findViewById(R.id.iv_deteksitepi);
        iv_segmentasi=(ImageView)findViewById(R.id.iv_segmentasi);
        //iv_deteksi_pthsclera_iris=(ImageView)findViewById(R.id.iv_deteksi_pthsclera_iris);
        iv_grayscale=(ImageView)findViewById(R.id.iv_grayscale);
        iv_roi_kiri=(ImageView)findViewById(R.id.iv_roi_kiri);
        textRatio_sclera_kiri = (TextView) findViewById(R.id.textRatio_sclera_kiri);
        tv_result_sclera_kiri =(TextView)findViewById(R.id.tv_result_sclera_kiri);

        button_Next_proses4=(Button)findViewById(R.id.button_Next_proses4);
        button_Back_Mulai3=(Button)findViewById(R.id.button_Back_Mulai3);

        Intent hasilKolesterol = getIntent();
        nerimaHasilKolesterol = hasilKolesterol.getStringExtra("variabelKolesterol");
        nerimaHasilJantung = hasilKolesterol.getStringExtra("variabelJantung");

        /////Next button proses//////
        button_Next_proses4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent m = new Intent(Proses3Activity.this, MulaiProses4Activity.class);
                startActivity(m);*/

                //Log.d("ayam","isi hsl cardiac :"+nerimaHasilJantung);
                //Log.d("ayam","isi hsl cardiac :"+nerimaHasilKolesterol);

                Intent hasilJantung = new Intent(getApplicationContext(), MulaiProses4Activity.class);
                hasilJantung.putExtra("variabelKolesterol", nerimaHasilKolesterol);
                hasilJantung.putExtra("variabelJantung", nerimaHasilJantung);
                hasilJantung.putExtra("variabelCoronary", tv_result_sclera_kiri.getText().toString());
                startActivity(hasilJantung);

            }
        });
        /////////////////////////////

        button_Back_Mulai3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent string = new Intent(Proses3Activity.this, MulaiProses3Activity.class);
               // startActivity(string);
                try{

                    if(flagGalatauCam==0){
                        ByteArrayOutputStream stream2 = new ByteArrayOutputStream();

                        gambarutuh.compress(Bitmap.CompressFormat.PNG, 100, stream2);
                        byte[] byteArray2 = stream2.toByteArray();
                        stream2.close();

                        string.putExtra("gambarutuh", byteArray2);
                        string.putExtra("flagGalatauCam", flagGalatauCam);

                        startActivity(string);
                    }
                    else
                    {
                        ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
                        gambarutuh.compress(Bitmap.CompressFormat.PNG, 100, stream2);
                        byte[] byteArray2 = stream2.toByteArray();
                        stream2.close();

                        string.putExtra("gambarutuh", byteArray2);
                        string.putExtra("flagGalatauCam", flagGalatauCam);

                        startActivity(string);
                    }
                }catch (Exception e){}
            }
        });


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

                //segmentasi coronary artery (sclera kiri)
                final Bitmap finalSegmentationKiri = preprocess.getSegmentationKiri(finalBinarizationBitmap);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iv_segmentasi.setImageBitmap(finalSegmentationKiri);
                    }
                });

                final Bitmap finalROIkiri = preprocess.getROIkiri(finalBinarizationBitmap);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iv_roi_kiri.setImageBitmap(finalROIkiri);
                    }
                });


                final Double[] ratio = preprocess.getRatio(finalROIkiri);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textRatio_sclera_kiri.setText("White Rasio : "+df.format(ratio[1]));
                    }
                });

                final String result = preprocess.getResultScleraKiri(ratio);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_result_sclera_kiri.setText("Coronary-Artery Diagnosis : "+ result);
                    }
                });

                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {

            }
        }.execute();
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent back = new Intent(Proses3Activity.this,MulaiProses3Activity.class);
        startActivity(back);
    }

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
                }
                else{
                    gambaraslicrop = sclera.copy(sclera.getConfig(), true);
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
