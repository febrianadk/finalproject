package com.it.febrianadk.camera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class CameraProses4Activity extends Activity {

    ImageView captureButton,target,flashButton;
    FrameLayout frame;
    Camera camera;
    Activity context;
    PreviewCamera previewCamera;
    Camera.Parameters params;
    String path = "/sdcard/Coronary Heart Detection/Cardiac Veins/";
    Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {

        @Override
        public void onShutter() {
            // TODO Auto-generated method stub

        }
    };
    Camera.PictureCallback rawCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            // Log.d(TAG, "onPictureTaken - raw");
        }
    };
    Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
        @SuppressWarnings("deprecation")
        public void onPictureTaken(byte[] data, Camera camera) {

            FileOutputStream outStream = null;
            Calendar c = Calendar.getInstance();
            File videoDirectory = new File(path);

            if (!videoDirectory.exists()) {
                videoDirectory.mkdirs();
            }

            try {
                // Write to SD Card
                outStream = new FileOutputStream(path + c.getTime().getSeconds() + ".jpg");
                outStream.write(data);
                outStream.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

            }

            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 5;

            options.inPurgeable = true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared

            options.inInputShareable = true;              //Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future


            Bitmap realImage = BitmapFactory.decodeByteArray(data, 0, data.length, options);
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(path + c.getTime().getSeconds() + ".jpg");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {
                Log.d("EXIF value",
                        exif.getAttribute(ExifInterface.TAG_ORIENTATION));
                if (exif.getAttribute(ExifInterface.TAG_ORIENTATION)
                        .equalsIgnoreCase("1")) {
                    realImage = rotate(realImage, 90);
                } else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION)
                        .equalsIgnoreCase("8")) {
                    realImage = rotate(realImage, 90);
                } else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION)
                        .equalsIgnoreCase("3")) {
                    realImage = rotate(realImage, 90);
                } else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION)
                        .equalsIgnoreCase("0")) {
                    realImage = rotate(realImage, 90);
                }
            } catch (Exception e) {

            }

            BitmapData.processed = false;
            BitmapData.result = "Go Check Your Sclera..";

            BitmapData.bitmap = realImage;
            releaseCameraAndPreview();
            finish();

            Intent intent = new Intent(CameraProses4Activity.this, MulaiProses4Activity.class);
            startActivity(intent);
        }
    };
    Camera.AutoFocusCallback mAutoFocusCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {

            try {
                camera.takePicture(mShutterCallback, null, jpegCallback);
            } catch (Exception e) {

            }

        }
    };
   /* private boolean isFlashOn = false;*/

    public static Bitmap rotate(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_proses4);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        context = this;

        captureButton = (ImageView) findViewById(R.id.captureButton);

        /*flashButton = (ImageView) findViewById(R.id.flashButton);*/
        target=(ImageView) findViewById(R.id.camera_target3);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int lebarlayar = displayMetrics.widthPixels;

        target.getLayoutParams().width = (int)(1.0/4.0*lebarlayar);
        target.getLayoutParams().height = (int)(1.0/4.0*lebarlayar);

        previewCamera = new PreviewCamera(this, (SurfaceView) findViewById(R.id.surfaceView));

        frame = (FrameLayout) findViewById(R.id.frameLayout);
       // Toast.makeText(this, "cekk", Toast.LENGTH_SHORT).show();

        frame.addView(previewCamera);
        previewCamera.setKeepScreenOn(true);

        camera = Camera.open();
        params = camera.getParameters();


        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    takeFocusedPicture();
                } catch (Exception e) {
                }
            }
        });

       /* flashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFlashOn) {
                    flashButton.setImageResource(R.drawable.camera_flash_on);
                    params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    camera.setParameters(params);
                    camera.startPreview();
                    isFlashOn = false;
                } else {
                    flashButton.setImageResource(R.drawable.camera_flash_off);
                    params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    camera.setParameters(params);
                    camera.startPreview();
                    isFlashOn = true;
                }
            }
        });*/
    }

    public void takeFocusedPicture() {
        camera.autoFocus(mAutoFocusCallback);

    }

    private void releaseCameraAndPreview() {
        previewCamera.setCamera(null);
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }
    protected void onResume() {
        super.onResume();
        // TODO Auto-generated method stub
        if (camera == null) {
            camera = Camera.open();
            camera.startPreview();
            camera.setErrorCallback(new Camera.ErrorCallback() {
                public void onError(int error, Camera mcamera) {

                    camera.release();
                    camera = Camera.open();
                    Log.d("Camera died", "error camera");

                }
            });
        }
        if (camera != null) {
            if (Build.VERSION.SDK_INT >= 14)
                setCameraDisplayOrientation(context, Camera.CameraInfo.CAMERA_FACING_BACK, camera);
            previewCamera.setCamera(camera);
        }
    }

    private void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

}
