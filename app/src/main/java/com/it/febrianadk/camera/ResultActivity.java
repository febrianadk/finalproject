package com.it.febrianadk.camera;

import android.content.Intent;
import android.graphics.Bitmap;
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

public class ResultActivity extends AppCompatActivity {

    private ImageView gambarROI, ivColorDetection, iv_roi_kiri, iv_roi_kanan;
    private TextView textView_Result2, tv_Result_Kolesterol, tv_result_sclera_kiri, tv_result_sclera_kanan, tv_result_akhir;
    private RelativeLayout relativeLayout;
    private Preprocess preprocess;
    private Bitmap newBitmap=null;

    private Button btn_Finish;

    String nerimaHasilKolesterol, nerimaHasilJantung, nerimaCoronary, nerimaCardiac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        newBitmap = BitmapData.bitmap;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        btn_Finish = (Button)findViewById(R.id.btn_Finish);

        gambarROI = (ImageView)findViewById(R.id.gambar_ROI);
        ivColorDetection = (ImageView)findViewById(R.id.ivColorDetection);
        iv_roi_kiri = (ImageView)findViewById(R.id.iv_roi_kiri);
        iv_roi_kanan = (ImageView)findViewById(R.id.iv_roi_kanan);

        textView_Result2 = (TextView)findViewById(R.id.textView_Result2);
        tv_Result_Kolesterol = (TextView)findViewById(R.id.tv_Result_Kolesterol);
        tv_result_sclera_kiri = (TextView)findViewById(R.id.tv_result_sclera_kiri);
        tv_result_sclera_kanan = (TextView)findViewById(R.id.tv_result_sclera_kanan);

        tv_result_akhir = (TextView)findViewById(R.id.tv_result_akhir);

        Intent hasilKolesterol = getIntent();
        nerimaHasilKolesterol = hasilKolesterol.getStringExtra("variabelKolesterol");
        nerimaHasilJantung = hasilKolesterol.getStringExtra("variabelJantung");
        nerimaCoronary = hasilKolesterol.getStringExtra("variabelCoronary");
        nerimaCardiac = hasilKolesterol.getStringExtra("variabelCardiac");

        TextView tv_result_sclera_kanan= (TextView)findViewById(R.id.tv_result_sclera_kanan);
        tv_result_sclera_kanan.setText(nerimaCardiac);

        TextView tv_result_sclera_kiri = (TextView)findViewById(R.id.tv_result_sclera_kiri);
        tv_result_sclera_kiri.setText(nerimaCoronary);

        TextView textView_Result2 = (TextView)findViewById(R.id.textView_Result2);
        textView_Result2.setText(nerimaHasilJantung);

        TextView tv_Result_Kolesterol = (TextView)findViewById(R.id.tv_Result_Kolesterol);
        tv_Result_Kolesterol.setText(nerimaHasilKolesterol);

        String hsl_Jantung = nerimaHasilJantung;
      //  Log.d("ayam","isi hsl jantung :"+hsl_Jantung);
        String hsl_Kolesterol = nerimaHasilKolesterol;
       // Log.d("ayam","isi hsl kolesterol :"+hsl_Kolesterol);
        String hsl_Coronery = nerimaCoronary;
      //  Log.d("ayam","isi hsl coronary :"+hsl_Coronery);
        String hsl_Cardiac = nerimaCardiac;
      //  Log.d("ayam","isi hsl cardiac :"+hsl_Cardiac);

        //////Klasifikasi Akhir (Resiko PJK)///////
        if (hsl_Jantung.equals("Heart Spot Diagnosis : ABNORMAL")&& hsl_Kolesterol.equals("Cholesterol Ring Diagnosis : ABNORMAL") &&
                hsl_Coronery.equals("Coronary-Artery Diagnosis : ABNORMAL")&& hsl_Cardiac.equals("Cardiac-Vein Diagnosis : ABNORMAL")) {
            tv_result_akhir.setText("HIGH RISK");
        } else if (hsl_Jantung.equals("Heart Spot Diagnosis : ABNORMAL")&& hsl_Kolesterol.equals("Cholesterol Ring Diagnosis : ABNORMAL") &&
                hsl_Coronery.equals("Coronary-Artery Diagnosis : ABNORMAL") && hsl_Cardiac.equals("Cardiac-Vein Diagnosis : NORMAL")){
            tv_result_akhir.setText("MEDIUM RISK");
        } else if (hsl_Jantung.equals("Heart Spot Diagnosis : ABNORMAL")&& hsl_Kolesterol.equals("Cholesterol Ring Diagnosis : ABNORMAL") &&
                hsl_Coronery.equals("Coronary-Artery Diagnosis : NORMAL") && hsl_Cardiac.equals("Cardiac-Vein Diagnosis : NORMAL")){
            tv_result_akhir.setText("MEDIUM RISK");
        } else{
            tv_result_akhir.setText("LOW RISK / NORMAL");
        }

        btn_Finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent m = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(m);
            }
        });

    }

    ////onResume////////////
    @Override
    protected void onResume() {
        super.onResume();
    }


    public void onBackPressed() {
        super.onBackPressed();
        Intent back = new Intent(ResultActivity.this,MulaiProses4Activity.class);
        startActivity(back);
    }

}
