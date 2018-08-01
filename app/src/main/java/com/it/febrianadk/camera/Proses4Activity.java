package com.it.febrianadk.camera;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;

import static com.it.febrianadk.camera.BitmapData.result;

public class Proses4Activity extends AppCompatActivity {

    public Button button_Next_finalresult, button_Back_Mulai4;
    Bitmap gray=null, sclera=null;
    int flagGalatauCam=0;
    Bitmap resized=null, gambarutuh=null, gambaraslicrop=null;
    ProgressDialog progress;
    DecimalFormat df;
    private Bitmap newBitmap=null;
    private ImageView iv_gambarasli, iv_medianfilter, iv_sharpness,
            iv_deteksi_pthsclera_iris, iv_grayscale, iv_binerisasi,
            iv_deteksitepi, iv_segmentasi, iv_roi_kanan;
    private TextView  tv_result_sclera_kanan, textRatio_sclera_kanan;
    private RelativeLayout relativeLayout;
    private Preprocess preprocess;

    String nerimaHasilKolesterol, nerimaHasilJantung, nerimaCoronary, nerimaCardiac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proses4);

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
        iv_roi_kanan=(ImageView)findViewById(R.id.iv_roi_kanan);
        textRatio_sclera_kanan = (TextView) findViewById(R.id.textRatio_sclera_kanan);
        tv_result_sclera_kanan =(TextView)findViewById(R.id.tv_result_sclera_kanan);

        button_Next_finalresult=(Button)findViewById(R.id.button_Next_finalresult);
        button_Back_Mulai4=(Button)findViewById(R.id.button_Back_Mulai4);

        Intent hasilKolesterol = getIntent();
        nerimaHasilKolesterol = hasilKolesterol.getStringExtra("variabelKolesterol");
        nerimaHasilJantung = hasilKolesterol.getStringExtra("variabelJantung");
        nerimaCoronary = hasilKolesterol.getStringExtra("variabelCoronary");

        button_Back_Mulai4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent string = new Intent(Proses4Activity.this, MulaiProses4Activity.class);
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

        /////Next button proses//////
        button_Next_finalresult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent m = new Intent(Proses4Activity.this,ResultActivity.class);
                startActivity(m);*/


                Log.d("ayam","isi hsl cardiac :"+nerimaHasilJantung);
                Log.d("ayam","isi hsl cardiac :"+nerimaHasilKolesterol);
                Log.d("ayam","isi hsl cardiac :"+nerimaCoronary);
                Log.d("ayam","isi hsl cardiac :"+nerimaCardiac);



                Intent hasilJantung = new Intent(getApplicationContext(), ResultActivity.class);
                hasilJantung.putExtra("variabelJantung", nerimaHasilJantung);
                hasilJantung.putExtra("variabelKolesterol", nerimaHasilKolesterol);
                hasilJantung.putExtra("variabelCoronary", nerimaCoronary);
                hasilJantung.putExtra("variabelCardiac", nerimaCardiac);
                startActivity(hasilJantung);
            }
        });
        /////////////////////////////


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

        progress = new ProgressDialog(Proses4Activity.this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        new LoadViewTask2().execute(); //Nyalakan

        process();
        BitmapData.processed=true;
    }

    ////onResume/////////////////
    @Override
    protected void onResume() {
        super.onResume();
    }

    ///Proses Background////////////
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

                //segmentasi coronary artery (sclera kiri)
                final Bitmap finalSegmentationKanan = preprocess.getSegmentationKanan(finalBinarizationBitmap);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iv_segmentasi.setImageBitmap(finalSegmentationKanan);
                    }
                });

                final Bitmap finalROIkanan = preprocess.getROIkanan(finalBinarizationBitmap);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iv_roi_kanan.setImageBitmap(finalROIkanan);
                    }
                });

                /*final Bitmap ekstraksiFiturBmp = preprocess.getEkstraksiFitur(roiBmp);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imgEkstraksiFitur.setImageBitmap(ekstraksiFiturBmp);
                    }
                });*/

                final Double[] ratio = preprocess.getRatio(finalROIkanan);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textRatio_sclera_kanan.setText("Rasio Putih: "+df.format(ratio[1]));
                    }
                });

                final String result = preprocess.getResultScleraKanan(ratio);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_result_sclera_kanan.setText("Cardiac-Vein Diagnosis : "+ result);
                        nerimaCardiac = "Cardiac-Vein Diagnosis : "+ result;
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
        Intent back = new Intent(Proses4Activity.this,MulaiProses4Activity.class);
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
