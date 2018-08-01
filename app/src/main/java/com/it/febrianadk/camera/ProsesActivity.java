package com.it.febrianadk.camera;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ProsesActivity extends AppCompatActivity {

    Bitmap gray=null,putih2=null;
    Bitmap areairis =null,areaNormalisasiCrop,areaIrisBersih;
    Bitmap areaIrisLingkaranLuar=null;

    ImageView ivDeteksiWarnaSklera,ivCropBiner,ivPutih,gambarasli,ivCrop,
            ivLingkarHitamLuar,ivNormalisasi,ivNormalisasiCrop,
            ivColorDetection;
    TextView tvJumlahPutih,tvKolesterol,tvJumlahPutihTotal,tv_Result_Kolesterol;

    int Rbesar = 0;
    int hxputih [];
    int flagGalatauCam=0;

    Button buttonBackprocess, buttonNextprocess;
    Bitmap resized=null, gambarutuh=null;
    ProgressDialog progress;
    Bitmap untukIVCROP=null,untukIVPUTIH=null,untukIVDETEKSIWARNASKLERA=null,untukIVCROPBINER=null;
    Bitmap gambaraslicrop=null,bitmapbiner;
    String ukurangIris="";
    Bitmap untukLINGKARLUAR=null;
    Bitmap untukIVNORMALISASI=null;
    Bitmap untukIVNORMALISASICROP=null;
    int jum;
    String untukTVJUMLAHPUTIH="",untuktvJumlahPutihTotal="",untuktv_Result_Kolesterol="";
    Bitmap untukIVCOLORDETECTION=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proses);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ivPutih=(ImageView)findViewById(R.id.ivPutih);
        ivCropBiner=(ImageView)findViewById(R.id.ivCropBiner);
        ivDeteksiWarnaSklera=(ImageView)findViewById(R.id.ivDeteksiWarnaSklera);
        gambarasli=(ImageView)findViewById(R.id.gambarasli);
        ivCrop=(ImageView)findViewById(R.id.ivCrop);

        tv_Result_Kolesterol =(TextView)findViewById(R.id.tv_Result_Kolesterol);
        tvJumlahPutih =(TextView)findViewById(R.id.tvJumlahPutih);
        tvJumlahPutihTotal =(TextView)findViewById(R.id.tvJumlahPutihTotal);
        ivLingkarHitamLuar=(ImageView)findViewById(R.id.ivLingkarHitamLuar);
        ivNormalisasi=(ImageView)findViewById(R.id.ivNormalisasi);
        ivNormalisasiCrop=(ImageView)findViewById(R.id.ivNormalisasiCrop);
        ivColorDetection=(ImageView)findViewById(R.id.ivColorDetection);

        buttonBackprocess=(Button)findViewById(R.id.buttonBackprocess);
        buttonNextprocess=(Button)findViewById(R.id.buttonNextprocess);

        buttonBackprocess.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent string = new Intent(ProsesActivity.this, MulaiActivity.class);
                startActivity(string);
                if (jum > 160) {
                    string.putExtra("abc", "Cholesterol Ring Diagnosis : ABNORMAL");
                }
                else{
                    string.putExtra("abc", "Cholesterol Ring Diagnosis : NORMAL");
                }

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

                }
                catch (Exception e){}
            }
        });

        buttonNextprocess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(areaIrisLingkaranLuar != null) {
                        /*Intent string = new Intent(ProsesActivity.this, Proses2Activity.class);
                        startActivity(string);*/

                        Intent hasilKolesterol = new Intent(getApplicationContext(), Proses2Activity.class);
                        hasilKolesterol.putExtra("variabelKolesterol", tv_Result_Kolesterol.getText().toString());
                        startActivity(hasilKolesterol);
                    } else{
                        Log.d("ProsesActivity", "Bitmap Kosong");
                    }
            }
        });


        if(getIntent().hasExtra("flagGalatauCam")){
            flagGalatauCam=getIntent().getIntExtra("flagGalatauCam",0);
        }
        if(flagGalatauCam==0){
            byte[] byteArray2 = getIntent().getByteArrayExtra("gambarutuh");
            gambarutuh = BitmapFactory.decodeByteArray(byteArray2, 0, byteArray2.length); //nyalakan
            putih2 = gambarutuh.copy(gambarutuh.getConfig(), true);
        }
        else{
            byte[] byteArray2 = getIntent().getByteArrayExtra("gambarutuh");
            gambarutuh = BitmapFactory.decodeByteArray(byteArray2, 0, byteArray2.length); //nyalakan
            putih2 = gambarutuh.copy(gambarutuh.getConfig(), true);
        }

        progress = new ProgressDialog(ProsesActivity.this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        new LoadViewTask2().execute(); //Nyalakan
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void putih(Bitmap gambarasli){
        gray = gambarasli.copy(gambarasli.getConfig(), true);

        int lebar=gray.getWidth();
        int tinggi=gray.getHeight();
        //Mencari putih bukan putih
        for (int x = 0; x < lebar; x++)
            for (int y = 0; y < tinggi; y++)
            {
                int w = gray.getPixel(x, y);
                int r = Color.red(w);
                int g = Color.green(w);
                int b = Color.blue(w);
                if (r>=200&&g>=200&&b>=200){
                    int warnabaru = Color.rgb(255, 255, 255);
                    gray.setPixel(x,y,warnabaru);
                }
            }

        Bitmap putih = gray.copy(gray.getConfig(), true);
        untukIVPUTIH= gray.copy(gray.getConfig(), true);

        if(flagGalatauCam==0)
            croppingIris(putih);
        else
        {
            untukIVCROP= untukIVPUTIH.copy(untukIVPUTIH.getConfig(), true);
            Lingkaranluar(putih);
        }
    }

    public void getBinerization (Bitmap bitmap){
        Bitmap cropBmp = bitmap.copy(bitmap.getConfig(), true);
        int w1;
        /////////////deteksi warna coklat sclera///////////
        for (int x = 0; x < cropBmp.getWidth(); x++)
        {
            for (int y = 0; y < cropBmp.getHeight(); y++)
            {
                int w=cropBmp.getPixel(x, y);

                if (((Color.red(w) >= 150) && (Color.red(w) <= 226)) && ((Color.green(w) >= 120) && (Color.green(w) <= 220)) && ((Color.blue(w) >= 127)&&(Color.blue(w) <= 200)))//(w.R > 136) && (w.R < 179)) && ((w.G > 140) && (w.G < 214)) && ((w.B < 254) && (w.B > 169)
                {
                    w1 = Color.rgb(255, 255, 255);
                }
                else
                    w1 = w;

                cropBmp.setPixel(x, y, w1);
            }
        }

        //////////////// deteksi warna putih iris///////////
        for (int x = 0; x < cropBmp.getWidth(); x++)
        {
            for (int y = 0; y < cropBmp.getHeight(); y++)
            {
                int w=cropBmp.getPixel(x, y);
                if (((Color.red(w) >= 122) && (Color.red(w) <= 200)) && ((Color.green(w) >= 130) && (Color.green(w) <= 220)) && ((Color.blue(w) >= 160)&&(Color.blue(w) <= 255)))//(w.R > 136) && (w.R < 179)) && ((w.G > 140) && (w.G < 214)) && ((w.B < 254) && (w.B > 169)
                {
                    w1 = Color.rgb(0, 0, 0);
                }
                else
                    w1 = w;

                cropBmp.setPixel(x, y, w1);
            }
        }
        untukIVDETEKSIWARNASKLERA= cropBmp.copy(cropBmp.getConfig(), true);

        ///////////////////////binerisasi//////////////////////
        Bitmap binerBmp = Bitmap.createBitmap(cropBmp.getWidth(), cropBmp.getHeight(), Bitmap.Config.RGB_565);
        Canvas cBiner = new Canvas(binerBmp);
        cBiner.drawBitmap(binerBmp, 0, 0, new Paint());
        int white = 255;
        for (int x = 0; x < binerBmp.getWidth(); x++)
        {
            for (int y = 0; y < binerBmp.getHeight(); y++)
            {
                int w = cropBmp.getPixel(x, y);
                int r = Color.red(w); int g = Color.green(w); int b = Color.blue(w);

                double xr = Math.sqrt((Math.pow(r - white, 2)) + (Math.pow(g - white, 2)) + (Math.pow(b - white, 2)));

                int new_xr = 0;
                if(x>=binerBmp.getWidth()-binerBmp.getWidth()/4){
                    if (xr < 230)
                        new_xr = 255;
                }
                else{
                    if (xr < 175)
                        new_xr = 255;
                }

                int new_w = Color.rgb(new_xr, new_xr, new_xr);
                binerBmp.setPixel(x, y, new_w);
            }
        }
        untukIVPUTIH= binerBmp.copy(binerBmp.getConfig(), true);
        getCrop1(binerBmp);
    }

    private void autoCropuntukgalery(Bitmap bitmap) {
        int tinggigambar=bitmap.getHeight();
        int lebargambar=bitmap.getWidth();
        int titiktengahx=lebargambar/2;
        int titiktengahy=tinggigambar/2;
        int seperempat=lebargambar/4;
        Bitmap croppedBitmap = Bitmap.createBitmap(bitmap,titiktengahx-(seperempat/2), titiktengahy-(seperempat/2), seperempat, seperempat);

        gambaraslicrop = croppedBitmap.copy(croppedBitmap.getConfig(), true);
        getBinerization(gambaraslicrop);
    }

    public void getCrop1 (Bitmap bitmap){
        int binerWhite = 255;

        int[] horizontal= new int[bitmap.getWidth()];

        //inisialisasi nilai horizontal dan vertical
        for(int i=0; i<bitmap.getWidth(); i++)
            horizontal[i]=0;


        for(int x = 0; x < bitmap.getWidth(); x++){
            for(int y = 0; y < bitmap.getHeight(); y++){
                int w = bitmap.getPixel(x,y);
                if(Color.red(w)==binerWhite && Color.green(w)==binerWhite && Color.blue(w)==binerWhite)
                {
                    horizontal[x]++;
                }
            }
        }
        ///////////////////////////find point/////////////////////////////////

        Bitmap bmp_point1 = gambaraslicrop;
        int leftPoint = 0, rightPoint = 0, topPoint = 0, bottomPoint = 0;
        int leftThreshold = 0, rightThreshold = 0, topThreshold = 0, bottomThreshold = 0;

        int mid_left_right = bmp_point1.getWidth() / 2;
        int mid_top_bottom = bmp_point1.getHeight() / 2;
        int left = 0; int top = 0;
        int rght = bmp_point1.getWidth()-1;
        int btm = bmp_point1.getHeight()-1;

        ///////////

        //////////////////CARI POINT START/////////////////////////////////

        //////////left////////
        int Thresholdperubahan=0;
        int perubahan=0;
        for (int i = left; i <= mid_left_right; i++)
        {
            perubahan=horizontal[i]-horizontal[i+1];
            if (perubahan >= Thresholdperubahan)
            {
                Thresholdperubahan =perubahan;
                leftPoint = i;
            }
        }
        //////// right////////

        Thresholdperubahan=0;
        perubahan=0;
        for (int i = rght; i >= mid_left_right; i--)
        {
            if (horizontal[i] >= rightThreshold)
            {
                rightThreshold = horizontal[i];
                rightPoint = i;
                //break;
            }
        }

        ///////////// gunakan batas kanan untuk integral proyeksi vertikal utk mengurangi resiko hitam di kanan
        int[] vertical=new int[bitmap.getHeight()];
        for(int i=0; i<bitmap.getHeight(); i++)
            vertical[i]=0;
        for(int y = 0; y < bitmap.getHeight(); y++){
            for(int x = 0; x < bitmap.getWidth(); x++){
                int w = bitmap.getPixel(x,y);
                if(Color.red(w)==binerWhite && Color.green(w)==binerWhite && Color.blue(w)==binerWhite)
                {
                    vertical[y]++;
                }
            }
        }
        ////////////////////top/////////////////////////////////////////
        Thresholdperubahan=0;
        perubahan=0;
        for (int i = top; i <= mid_top_bottom; i++)
        {
            perubahan=vertical[i]-vertical[i+1];
            if (perubahan >= Thresholdperubahan)
            {
                Thresholdperubahan =perubahan;
                topPoint = i;
                //break;
            }
        }
        //////////bottom////////
         Thresholdperubahan=0;
         perubahan=0;
        for (int i = btm; i >= mid_top_bottom; i--)
        {
            perubahan=vertical[i]-vertical[i-1];
            if (perubahan >= Thresholdperubahan)
            {
                Thresholdperubahan =perubahan;
                bottomPoint = i;
                //break;
            }
        }
        //////////////////CARI POINT END/////////////////////////////////

        //////////////////CROP 1 START////////////////////////////////////
        int crop_width = rightPoint - leftPoint;
        int crop_height = bottomPoint - topPoint;

        Bitmap crop1Bmp = Bitmap.createBitmap(crop_width, crop_height, Bitmap.Config.RGB_565);
        Bitmap crop1BinerBmp = Bitmap.createBitmap(crop_width, crop_height, Bitmap.Config.RGB_565);
        ////////////
        Canvas crop = new Canvas(crop1Bmp);
        crop.drawBitmap(crop1Bmp, 0, 0, new Paint());

        int xc = 0, yc = 0;
        for (int x = leftPoint; x < rightPoint; x++)
        {
            yc = 0;
            for (int y = topPoint ; y < bottomPoint; y++)
            {
                int color_ori = gambaraslicrop.getPixel(x, y);
                int color_biner = bitmap.getPixel(x, y);

                crop1Bmp.setPixel(xc, yc, color_ori);
                crop1BinerBmp.setPixel(xc, yc, color_biner);
                yc++;
            }
            xc++;
        }
        //////////////////CROP 1 END///////////////////////////////////
        ////////////

        untukIVCROPBINER=crop1BinerBmp.copy(crop1BinerBmp.getConfig(), true);

        bitmap = crop1Bmp;
        ukurangIris=ukurangIris+"\nlebar:"+crop1Bmp.getWidth()+" , tinggi:"+crop1Bmp.getHeight();
        untukIVCROP= crop1Bmp.copy(crop1Bmp.getConfig(), true);
        Lingkaranluar(crop1Bmp);
    }

/////////////////////////////////////
    public void croppingIris(Bitmap putihbukanputih) {
        int i, j;

        int maxkiri = 0, maxkanan = 0, puncakkiriv = 0, puncakkananv = 0;
        int maxkiri1 = 0, maxkanan1 = 0,puncakkirih = 0, puncakkananh = 0;
        int kiriatasx = 0, kiriatasy = 0, kananbawahx = 0, kananbawahy = 0;

        //integral proyeksi horisontal
        int hx1[] = new int[putihbukanputih.getWidth()];

        for (j = 0; j < putihbukanputih.getWidth() - 1; j++) {
            hx1[j] = 0;
            for (i = 0; i <= putihbukanputih.getHeight() - 1 ; i++) { //0 saya ganti kiriatasy, gambarhitamputih.getHeight() saya ganti kananbawahy

                int w = putihbukanputih.getPixel(j, i);
                int r = Color.red(w);
                hx1[j] = hx1[j] + (int) ((r) / 255);

            }
        }
        for (j = 0; j < hx1.length / 2; j++)
        {
            if (j > (hx1.length / 2)-((hx1.length / 3)/2)) {

            }
            else {
                if (hx1[j] > maxkiri1) {
                    maxkiri1 = hx1[j];
                    puncakkirih = j;
                }
            }
        }

        for (j = hx1.length / 2; j < hx1.length; j++)
        {
            if (j < (hx1.length / 2)+((hx1.length / 3)/2)) {

            }
            else  {
                if (hx1[j] > maxkanan1) {
                    maxkanan1 = hx1[j];
                    puncakkananh = j;
                }
            }
        }
        /////integral proyeksi vertikal

        //dicari integral proyeksi vertikal dulu, trus dicrop tingginya dulu
        int hy1[] = new int[putihbukanputih.getHeight()];

        for (j = 0; j < putihbukanputih.getHeight() - 1; j++) {
            hy1[j] = 0;
            for (i = 0; i < putihbukanputih.getWidth() - 1; i++) {
                int w = putihbukanputih.getPixel(i, j);
                int r = Color.red(w);
                hy1[j] = hy1[j] + (int) ((r) / 255);
            }
        }

        for (j = 0; j < hy1.length / 2; j++)
        {
            if (j > (hy1.length / 2)-((hx1.length / 3)/2)) { }
            else
            {
                if (hy1[j] > maxkiri)
                {
                    maxkiri = hy1[j];
                    puncakkiriv = j;//batas atas
                }
            }
        }
        for (j = hy1.length / 2; j < hy1.length;j++)
        {
            if (j < (hy1.length / 2)+((hx1.length / 3)/2)) {

            }
            else
            {
                if (hy1[j] > maxkanan)  {
                    maxkanan = hy1[j];
                    puncakkananv = j;//batas bawah
                }
            }
        }

        kiriatasx = puncakkirih;
        kananbawahx = puncakkananh;
        kiriatasy = puncakkiriv;
        kananbawahy=puncakkananv;

        areairis = Bitmap.createBitmap(putihbukanputih, kiriatasx, kiriatasy, Math.abs(kananbawahx - kiriatasx), Math.abs(kiriatasy - kananbawahy));
        areaIrisBersih = areairis.copy(areairis.getConfig(), true);
        untukIVCROP=areairis.copy(areairis.getConfig(), true);

        Lingkaranluar(areaIrisBersih);
    }

    public void pluslingkaran(Bitmap gambarAreaIris) {
        Bitmap Iris = gambarAreaIris.copy(gambarAreaIris.getConfig(), true);
        int r=45;
        int a = gambarAreaIris.getWidth() / 2, b = gambarAreaIris.getHeight() /2;
        for (float i = 0; i < 360; i=i + 0.01f)
        {
            for (int j = r; j >= 0; j--) {
                int x = (int)(Math.floor(a + j * Math.cos((i * Math.PI) / 180)));
                int y = (int)(Math.floor(b + j * Math.sin((i * Math.PI) / 180)));
                int newpixelColor = Color.rgb(255, 0, 0);
                Iris.setPixel(x, y, newpixelColor);
            }
        }
        Bitmap areaIrisplusLingk = Iris.copy(Iris.getConfig(), true);
    }

    public void Lingkaranluar (Bitmap gambarAreaIrisCrop) {
        Bitmap IrisLingkaranLuar = gambarAreaIrisCrop.copy(gambarAreaIrisCrop.getConfig(), true);
        int x, y;
        int a = gambarAreaIrisCrop.getWidth()/ 2, b = gambarAreaIrisCrop.getHeight() / 2;

        Rbesar = b;
        for (float i = 0; i < 360; i = i + 0.01f)
        {
            x = (int)(Math.floor(a + Rbesar * Math.cos((i * Math.PI) / 180)));
            y = (int)(Math.floor(b + Rbesar * Math.sin((i * Math.PI) / 180)));
            int newpixelColor = Color.rgb(255, 0, 0);
            if (x <= IrisLingkaranLuar.getWidth() - 1 && x >= 0 && y >= 0 && y <= IrisLingkaranLuar.getHeight() - 1)
                IrisLingkaranLuar.setPixel(x, y, newpixelColor);
        }
        areaIrisLingkaranLuar = IrisLingkaranLuar.copy(IrisLingkaranLuar.getConfig(), true);
        BitmapData.bitmap = areaIrisLingkaranLuar;
        untukLINGKARLUAR= IrisLingkaranLuar.copy(IrisLingkaranLuar.getConfig(), true);
        Normalisasi(areaIrisLingkaranLuar); //kasih kondisi gara@ rbesar=0
    }

    public void Normalisasi (Bitmap gambarIrisLingkaranLuar) {
        int nilaigambar[][]= new int[Rbesar*360][3];
        Bitmap areaIrisLingkaranLuarHitam = gambarIrisLingkaranLuar.copy(gambarIrisLingkaranLuar.getConfig(), true);
        int a = gambarIrisLingkaranLuar.getWidth()/ 2, b = gambarIrisLingkaranLuar.getHeight() / 2;
        int k = 0;

        for (int i = 0; i < 360; i++)
        {
            for (int j = Rbesar-1; j >= 0; j--)
            {
                int x1 = (int)(Math.floor(a + j * Math.cos((i * Math.PI) / 180)));
                int y1 = (int)(Math.floor(b + j * Math.sin((i * Math.PI) / 180)));
                if (x1 <= gambarIrisLingkaranLuar.getWidth() - 1 && x1 >= 0 && y1 >= 0 && y1 <= gambarIrisLingkaranLuar.getHeight() - 1)
                {
                    int c=areaIrisLingkaranLuarHitam.getPixel(x1, y1);
                    nilaigambar[k][0] = Color.red(c);
                    nilaigambar[k][1] = Color.green(c);
                    nilaigambar[k][2] = Color.blue(c);
                }
                else {
                    nilaigambar[k][0] = 0;
                    nilaigambar[k][1] = 0;
                    nilaigambar[k][2] = 0;
                }
                k++;
            }
        }

        Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
        Bitmap kotaksip = Bitmap.createBitmap(360, Rbesar, conf);

        k=0;
        for (int i = 0; i < 360; i++)
        {
            for (int j = Rbesar-1; j >= 0; j--)
            {
                int newpixelColor = Color.rgb(nilaigambar[k][ 0], nilaigambar[k][ 1], nilaigambar[k][ 2]);
                kotaksip.setPixel(i, j, newpixelColor);
                k++;
            }
        }
        Bitmap areaNormalisasi = kotaksip.copy(kotaksip.getConfig(), true);
        untukIVNORMALISASI=kotaksip.copy(kotaksip.getConfig(), true);
        cropNormalisasi(areaNormalisasi);
    }

    public void cropNormalisasi (Bitmap gambarNormalisasi) {
        Bitmap normalisasi = gambarNormalisasi.copy(gambarNormalisasi.getConfig(), true);
        int b = (int)((float)(gambarNormalisasi.getHeight()-1)/ 1.5);

        Bitmap grp = Bitmap.createBitmap(normalisasi, 0, b, normalisasi.getWidth() - 1, normalisasi.getHeight() - 1 - b);

        areaNormalisasiCrop = grp.copy(grp.getConfig(), true);
        untukIVNORMALISASICROP= grp.copy(grp.getConfig(), true);

        DeteksiPutih(areaNormalisasiCrop);
    }

    public void DeteksiPutih (Bitmap gambarNormalisasiCrop) {
        Bitmap putih = gambarNormalisasiCrop.copy(gambarNormalisasiCrop.getConfig(), true);

        hxputih = new int [putih.getWidth()];
        int hxputih2 []= new int[putih.getWidth()];
        int w1=0;
        for (int x = 0; x < areaNormalisasiCrop.getWidth(); x++)
        {
            hxputih[x] = 0; hxputih2[x] = 0;
            for (int y = 0; y < areaNormalisasiCrop.getHeight(); y++)
            {
                int w=putih.getPixel(x, y);

                if (((Color.red(w) >= 80) && (Color.red(w) <= 185)) && ((Color.green(w) >= 90) && (Color.green(w) <= 220)) && ((Color.blue(w) >= 129)&&(Color.blue(w) <= 255)))//(w.R > 136) && (w.R < 179)) && ((w.G > 140) && (w.G < 214)) && ((w.B < 254) && (w.B > 169)
                {
                    hxputih[x] = hxputih[x] + 1; hxputih2[x] = hxputih2[x] + 1;
                    w1 = w;
                }
                else
                    w1 = Color.rgb(0, 0, 0);

                putih.setPixel(x, y, w1);
            }
        }
        int i,jumputih=0;jum=0;
        for (i = 0; i < hxputih.length; i++)
        {
            if (hxputih[i] >=2)
                jum++;
            jumputih++;
        }
        untukTVJUMLAHPUTIH="Jumlah putih >=2 : "+jum;
      //  untuktvJumlahPutihTotal="\nJumlah total putih = "+jumputih;

        if (jum > 160) {//139 70
            untuktv_Result_Kolesterol="Cholesterol Ring Diagnosis : ABNORMAL";
        }
        else {
            untuktv_Result_Kolesterol="Cholesterol Ring Diagnosis : NORMAL";
        }

        Bitmap areaPutih = putih.copy(putih.getConfig(), true);
        untukIVCOLORDETECTION= putih.copy(putih.getConfig(), true);

    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent back = new Intent(ProsesActivity.this,MulaiActivity.class);
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
                if(putih2!=null) {
                    if(flagGalatauCam==0){//kalau 1 kamera

                        gambaraslicrop = putih2.copy(putih2.getConfig(), true);
                        getBinerization(putih2);
                    }
                    else{
                        gambaraslicrop = putih2.copy(putih2.getConfig(), true);
                        getBinerization(putih2); //buka lagi
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
                if(putih2!=null) {
                    gambarasli.setImageBitmap(gambarutuh);

                    ivPutih.setImageBitmap(untukIVPUTIH);
                    ivCrop.setImageBitmap(untukIVCROP);
                    ivLingkarHitamLuar.setImageBitmap(untukLINGKARLUAR);
                    ivNormalisasi.setImageBitmap(untukIVNORMALISASI);
                    ivNormalisasiCrop.setImageBitmap(untukIVNORMALISASICROP);
                    ivColorDetection.setImageBitmap(untukIVCOLORDETECTION);
                    tvJumlahPutih.setText(untukTVJUMLAHPUTIH);
                    tvJumlahPutihTotal.setText(untuktvJumlahPutihTotal);
                    tv_Result_Kolesterol.setText(untuktv_Result_Kolesterol);
                    ivCropBiner.setImageBitmap(untukIVCROPBINER);
                    ivDeteksiWarnaSklera.setImageBitmap(untukIVDETEKSIWARNASKLERA);
            }
        }
    }
}
