package com.it.febrianadk.camera;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class MulaiProses4Activity extends AppCompatActivity {

    private static final int PICK_FROM_CAMERA= 1;
    private static final int PICK_FROM_GALLERY = 2;
    final int permissionCamera=1,permissionReadStorage=3;
    private final int RESULT_CROP = 400;
    Bitmap img=null;
    ImageView resultImage;
    Button load, check, take, capture;
    TextView alertText, resultText;
    Bitmap captureImage,gambarutuh;
    int flagGalatauCam=0;
    Uri fileUri;
    Bitmap apublik;

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > 1) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }

    public static Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {
        ExifInterface ei = new ExifInterface(selectedImage.getPath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    public static Bitmap rotateImageIfRequired(String path, Bitmap bitmap) {
        ExifInterface ei;
        int orientation = 0;
        try {
            ei = new ExifInterface(path);
            orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        } catch (IOException e) {
            e.printStackTrace();
        }

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                bitmap = rotateImage(bitmap, 90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                bitmap = rotateImage(bitmap, 180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                bitmap = rotateImage(bitmap, 270);
                break;

        }
        return bitmap;
    }

    public static Bitmap rotateImage(Bitmap imgk, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(imgk, 0, 0, imgk.getWidth(), imgk.getHeight(), matrix, true);
        imgk.recycle();
        return rotatedImg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mulai_proses3);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        check = (Button) findViewById(R.id.checkBtn);
        capture = (Button) findViewById(R.id.captureBtn);
        resultImage = (ImageView) findViewById(R.id.resultImage);
        alertText = (TextView) findViewById(R.id.alertText);
        resultText = (TextView) findViewById(R.id.resultText);



        final String[] option = new String[] { "Take from Camera", "Select from Gallery","Cancel" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, option);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Select Option");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {

            @TargetApi(Build.VERSION_CODES.M)
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                Log.e("Selected Item", String.valueOf(which));
                if (which == 0) {
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, permissionCamera);

                    }
                    else {
                        callCamera();
                    }
                }
                if (which == 1) {
                    if (ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, permissionReadStorage);
                    }
                    else{
                        callGallery();
                    }
                }
                if (which == 2) {
                    dialog.dismiss();
                }
            }
        });
        final AlertDialog dialog = builder.create();

        capture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
        ////////////
        if(!getIntent().hasExtra("abc")) {
            if (BitmapData.bitmap != null) {//// cek data apakah ada gambar atau tidak
                captureImage = BitmapData.bitmap;

                resultText.setText(BitmapData.result);

                Bitmap cropBitmap = captureImage;

                if (!BitmapData.processed) {
                    cropBitmap = autoCrop(cropBitmap);
                }
                cropBitmap=resize(cropBitmap, 350,350);
                gambarutuh = cropBitmap.copy(cropBitmap.getConfig(), true);

                apublik = cropBitmap.copy(cropBitmap.getConfig(), true);

                flagGalatauCam = 1;
                resultImage.setImageBitmap(gambarutuh);
                alertText.setVisibility(View.INVISIBLE);

            } else {

            }
        }

        if (getIntent().hasExtra("abc")){
            resultText.setText(getIntent().getStringExtra("abc"));

            flagGalatauCam=getIntent().getIntExtra("flagGalatauCam",0);
            if(flagGalatauCam==0){
                byte[] byteArray2 = getIntent().getByteArrayExtra("gambarutuh");
                gambarutuh = BitmapFactory.decodeByteArray(byteArray2, 0, byteArray2.length);
                apublik=  gambarutuh.copy(gambarutuh.getConfig(), true);

                alertText.setVisibility(View.INVISIBLE);
                resultImage.setImageBitmap(gambarutuh);
            }
            else{
                byte[] byteArray2 = getIntent().getByteArrayExtra("gambarutuh");
                gambarutuh = BitmapFactory.decodeByteArray(byteArray2, 0, byteArray2.length);
                apublik=  gambarutuh.copy(gambarutuh.getConfig(), true);

                alertText.setVisibility(View.INVISIBLE);
                resultImage.setImageBitmap(gambarutuh);
            }
        }
        check.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(apublik!=null) {

                    try {

                        ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
                        gambarutuh.compress(Bitmap.CompressFormat.PNG, 100, stream2);
                        //apublik.recycle();
                        byte[] byteArray2 = stream2.toByteArray();

                        Intent i = new Intent(MulaiProses4Activity.this, Proses4Activity.class);

                        i.putExtra("gambarutuh", byteArray2);
                        i.putExtra("flagGalatauCam", flagGalatauCam);
                        //i.putExtra("flag", 1);

                        stream2.close();
                        startActivity(i);

                    }
                    catch (Exception e){};
                }
            }
        });

    }

    ////////////////
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case permissionCamera: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callCamera();
                } else {

                }
                return;
            }

            case permissionReadStorage: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callGallery();

                } else {

                }
                return;
            }

        }
    }

    public void callCamera(){

        Intent captureIntent = new Intent(MulaiProses4Activity.this, CameraProses3Activity.class);
        startActivity(captureIntent);
    }

    public void callGallery(){
        try {

            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, PICK_FROM_GALLERY);

        } catch (ActivityNotFoundException e) {
            // Do nothing for now
            Toast.makeText(this, "Tidak bisa mengambil dari gallery", Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {


            if (requestCode == PICK_FROM_GALLERY) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

                String picturePath = cursor.getString(columnIndex);
                File f = new File(picturePath);

                String imageName = f.getName();
                Toast.makeText(this, "Nama Image = "+imageName, Toast.LENGTH_LONG).show();
                cursor.close();
                performCrop(picturePath);

            }
            if (requestCode == RESULT_CROP ) {
                if(resultCode == Activity.RESULT_OK){

                    Bundle extras = data.getExtras();
                    Bitmap selectedBitmap = extras.getParcelable("data");
                    // Set The Bitmap Data To ImageView
                    if(apublik!=null || gambarutuh!=null){
                        apublik=null;
                        gambarutuh=null;
                    }
                    apublik= selectedBitmap.copy(selectedBitmap.getConfig(), true);

                    gambarutuh= apublik.copy(apublik.getConfig(), true);
                    flagGalatauCam=0;

                    alertText.setText("");
                    resultText.setText("Go Check Your Sclera..");
                    resultImage.setImageBitmap(selectedBitmap);
                }
            }
        }

        catch (Exception e) {
            Toast.makeText(this.getApplicationContext(), "Mohon coba lagi!\n", Toast.LENGTH_SHORT).show();

        }

    }

    //crop from gallery
    private void performCrop(String picUri) {
        try {
            //Start Crop Activity
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            File f = new File(picUri);

            Uri contentUri = Uri.fromFile(f);
            
            cropIntent.setDataAndType(contentUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 300);
            cropIntent.putExtra("outputY", 300);

            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, RESULT_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private Bitmap autoCrop(Bitmap bitmap) {

        int tinggigambar=bitmap.getHeight();
        int lebargambar=bitmap.getWidth();
        int titiktengahx=lebargambar/2;
        int titiktengahy=tinggigambar/2;
        int seperempat=lebargambar/4;
        Bitmap croppedBitmap = Bitmap.createBitmap(bitmap,titiktengahx-(seperempat/2), titiktengahy-(seperempat/2), seperempat, seperempat);

        bitmap = croppedBitmap;

        return croppedBitmap;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent back = new Intent(MulaiProses4Activity.this,Proses2Activity.class);
        startActivity(back);
    }

}
