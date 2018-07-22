package com.it.febrianadk.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

   /* private ImageView gambarROI, ivColorDetection, iv_roi_kiri, iv_roi_kanan;
    private TextView textView_Result2, tv_Result_Kolesterol, tv_result_sclera_kiri, tv_result_sclera_kanan;
    private RelativeLayout relativeLayout;
    private Preprocess preprocess;
    private Bitmap newBitmap=null;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

       /* newBitmap = BitmapData.bitmap;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        gambarROI = (ImageView)findViewById(R.id.gambar_ROI);
        ivColorDetection = (ImageView)findViewById(R.id.ivColorDetection);
        iv_roi_kiri = (ImageView)findViewById(R.id.iv_roi_kiri);
        iv_roi_kanan = (ImageView)findViewById(R.id.iv_roi_kanan);

        textView_Result2 = (TextView)findViewById(R.id.textView_Result2);
        tv_Result_Kolesterol = (TextView)findViewById(R.id.tv_Result_Kolesterol);
        tv_result_sclera_kiri = (TextView)findViewById(R.id.tv_result_sclera_kiri);
        tv_result_sclera_kanan = (TextView)findViewById(R.id.tv_result_sclera_kanan);
*/

       /* Intent resultSclera = getIntent();
        String nerimaScleraKanan = resultSclera.getStringExtra("var_ScleraKanan");
        TextView tv_result_sclera_kanan = (TextView)findViewById(R.id.tv_result_sclera_kanan);
        tv_result_sclera_kanan.setText(nerimaScleraKanan);*/

    }

    ////onResume////////////
    @Override
    protected void onResume() {
        super.onResume();
    }



    public void onBackPressed() {
        super.onBackPressed();
        Intent back = new Intent(ResultActivity.this,MainActivity.class);
        startActivity(back);
    }

}
