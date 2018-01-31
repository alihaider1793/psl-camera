package com.otaliastudios.cameraview.demo;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.hardware.SensorManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraLogger;
import com.otaliastudios.cameraview.CameraOptions;
import com.otaliastudios.cameraview.CameraUtils;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.Size;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CameraActivity extends AppCompatActivity implements View.OnClickListener, ControlView.Callback {

    int or;
    int PERMISSION_ALL = 1;
    int PERMISSION_ALL1 = 2;
    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    String[] PERMISSION1 = {Manifest.permission.READ_EXTERNAL_STORAGE};
    private CameraView camera;
    private ViewGroup controlPanel;
    private ImageView menuImageView,galleryBtn;

    private static final String TAG = "CameraActivity";
    private static final int GALLERY_REQ = 1;

    private boolean mCapturingPicture;

    // To show stuff in the callback
    private Size mCaptureNativeSize;
    private long mCaptureTime;

    int rotation_check;

    String uri;
    private InterstitialAd interstitialAd;

    ImageView verticalbar_img_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        CameraLogger.setLogLevel(CameraLogger.LEVEL_VERBOSE);
        camera = findViewById(R.id.camera);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);


        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        verticalbar_img_view = findViewById(R.id.vertical_menu_iv);

        if (!hasPermissions(CameraActivity.this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(CameraActivity.this, PERMISSIONS, PERMISSION_ALL);
        } else if (hasPermissions(CameraActivity.this, PERMISSIONS)) {

            callCamera();
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        return true;
    }

    private void message(String content, boolean important) {
        int length = important ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
        Toast.makeText(this, content, length).show();
    }


    private void onOpened() {
        ViewGroup group = (ViewGroup) controlPanel.getChildAt(0);
        for (int i = 0; i < group.getChildCount(); i++) {
            ControlView view = (ControlView) group.getChildAt(i);
//            view.onCameraOpened(camera);
            //show ad here
            if(interstitialAd.isLoaded()){
                interstitialAd.show();
            }
            else {
                Log.d(TAG, "onOpened: ad not loaded");
            }
        }
    }

    private void onPicture(final byte[] jpeg) {
        mCapturingPicture = false;
        long callbackTime = System.currentTimeMillis();


//        Toast.makeText(this, "or: "+or, Toast.LENGTH_SHORT).show();
        // This can happen if picture was taken with a gesture.
        if (mCaptureTime == 0) mCaptureTime = callbackTime - 300;
        if (mCaptureNativeSize == null) mCaptureNativeSize = camera.getPictureSize();

        //show ad here
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        } else {
            Log.d(TAG, "onPicture: ad not loaded");
        }

        if (!hasPermissions(CameraActivity.this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(CameraActivity.this, PERMISSIONS, PERMISSION_ALL);
        }


        SharedPreferences sharedPreferences = getSharedPreferences("source", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("GALLERY", false);
        editor.apply();

        if (hasPermissions(CameraActivity.this, PERMISSIONS)) {

//            android.hardware.Camera.CameraInfo cameraInfo;

//            getCorrectCameraOrientation( , )
            Filters.setImage(jpeg);
            Intent intent = new Intent(CameraActivity.this, Filters.class);
            Pair[] pairs = new Pair[2];
            pairs[0] = new Pair<View, String>(camera , "camera_transition");
            pairs[1] = new Pair<View, String>(verticalbar_img_view, "menu_icon");
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(CameraActivity.this, pairs);

            intent.putExtra("orientation", or);
            intent.putExtra("uri", uri);
            startActivity(intent, options.toBundle());
//
//        CameraUtils.decodeBitmap(jpeg, 1000, 1000, new CameraUtils.BitmapCallback() {
//            @Override
//            public void onBitmapReady(Bitmap bitmap) {
//                Bitmap b = bitmap;
//
//                //SAVING FILE
//                FileOutputStream outStream = null;
//                File dir = new File(
//                        Environment
//                                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
//                        "My Camera");
//                if (!dir.exists()) {
//                    if (!dir.mkdirs()) {
//                        Log.d("My Camera", "failed to create directory");
//                    }
//                }
//                String fileName = String.format("%d.jpg", System.currentTimeMillis());
//                File outFile = new File(dir, fileName);
//                uri =String.valueOf(outFile);
//                try {
//                    outStream = new FileOutputStream(outFile);
////                    b.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
//                    outStream.flush();
//                    outStream.close();
//
//
//                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                    intent.setData(Uri.fromFile(dir));
//                    sendBroadcast(intent);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                SharedPreferences sharedPreferences = getSharedPreferences("source",MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putBoolean("GALLERY",false);
//                editor.apply();
//
//                if(hasPermissions(CameraActivity.this,PERMISSIONS)){
//                Filters.setImage(jpeg);
//                    Intent intent = new Intent(CameraActivity.this,Filters.class);
//                    intent.putExtra("uri",uri);
//                    startActivity(intent);
//                }
//
//
//            }
//        });

            mCaptureTime = 0;
            mCaptureNativeSize = null;
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.capturePhoto: capturePhoto(); break;
            case R.id.toggleCamera: toggleCamera(); break;
            case R.id.vertical_menu_iv: popupMenu(); break;
            case R.id.gallery:loadFromGallery(); break;
        }
    }

    private void loadFromGallery() {

        if(!hasPermissions(CameraActivity.this,PERMISSION1)){
            ActivityCompat.requestPermissions(CameraActivity.this, PERMISSION1, PERMISSION_ALL1);
        }

        if(hasPermissions(CameraActivity.this,PERMISSION1)){
            long callbackTime = System.currentTimeMillis();
            Intent gallery_intent = new Intent();
            gallery_intent.setAction(Intent.ACTION_GET_CONTENT);
            gallery_intent.setType("image/*");
            startActivityForResult(gallery_intent,GALLERY_REQ);
            camera.stop();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_REQ && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            uri = String.valueOf(imageUri);

            Log.d(TAG, "onActivityResult: URI FROM GALLERY: "+uri);


            SharedPreferences sharedPreferences = getSharedPreferences("source",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("GALLERY",true);
            editor.apply();


            Intent intent = new Intent(CameraActivity.this,Filters.class);

            intent.putExtra("orientation", "");
            intent.putExtra("uri",uri);
            startActivity(intent);
        }
    }///////

    private void popupMenu() {

        PopupMenu popupMenu = new PopupMenu(CameraActivity.this, menuImageView);
        popupMenu.getMenuInflater().inflate(R.menu.settingspopup, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                int id = item.getItemId();

                if(id == R.id.privacy_policy){
                    startActivity(new Intent(getApplicationContext(),privacyPolicy.class));
                    finish();
                } else if(id == R.id.rateus){

                    try {

                        Uri marketUri = Uri.parse("market://details?id=" + getPackageName());
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, marketUri);
                        startActivity(goToMarket);

                    }catch (ActivityNotFoundException e){

                        Uri playstoreUri = Uri.parse("https://play.google.com/apps/testing/com.otaliastudios.cameraview.demo");
                        Intent goTOPlaystore = new Intent(Intent.ACTION_VIEW, playstoreUri);
                        startActivity(goTOPlaystore);
                    }
                }
                else if(id == R.id.terms_of_use){
                    startActivity(new Intent(CameraActivity.this,termsAndConditions.class));
                    finish();
                }
                return true;
            }
        });

        popupMenu.show();

    }

    /*@Override
    public void onBackPressed() {
        BottomSheetBehavior b = BottomSheetBehavior.from(controlPanel);
        if (b.getState() != BottomSheetBehavior.STATE_HIDDEN) {
            b.setState(BottomSheetBehavior.STATE_HIDDEN);
            return;
        }
        super.onBackPressed();
    }*/

    private void capturePhoto() {
        if (mCapturingPicture) return;
        mCapturingPicture = true;
        mCaptureTime = System.currentTimeMillis();
        mCaptureNativeSize = camera.getPictureSize();
        camera.capturePicture();

    }


    private void toggleCamera() {
        if (mCapturingPicture) return;
        switch (camera.toggleFacing()) {
            case BACK:
                Toast.makeText(this, "switching camera", Toast.LENGTH_SHORT).show();
                break;

            case FRONT:
                break;
        }
    }

    @Override
    public boolean onValueChanged(Control control, Object value, String name) {
        if (!camera.isHardwareAccelerated() && (control == Control.WIDTH || control == Control.HEIGHT)) {
            if ((Integer) value > 0) {
                message("This device does not support hardware acceleration. " +
                        "In this case you can not change width or height. " +
                        "The view will act as WRAP_CONTENT by default.", true);
                return false;
            }
        }
        control.applyValue(camera, value);
        BottomSheetBehavior b = BottomSheetBehavior.from(controlPanel);
        b.setState(BottomSheetBehavior.STATE_HIDDEN);
        message("Changed " + control.getName() + " to " + name, false);
        return true;
    }

    //region Boilerplate

    @Override
    protected void onResume() {
        super.onResume();
        if (!camera.isStarted() && hasPermissions(CameraActivity.this, PERMISSIONS)) {
            camera.start();
        }
//        camera.start();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(hasPermissions(CameraActivity.this, PERMISSIONS)) {
            camera.stop();
        }
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
    }

    @Override
    protected void onStart() {
        if(!camera.isStarted() && hasPermissions(CameraActivity.this,PERMISSIONS))
        {
            camera.start();
        }

        super.onStart();
    }

    @Override
    protected void onStop() {

        if(hasPermissions(CameraActivity.this, PERMISSIONS)) {
            camera.stop();
        }
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean valid = true;
        for (int grantResult : grantResults) {
            valid = valid && grantResult == PackageManager.PERMISSION_GRANTED;
        }
        if (valid) {
//            camera.start();

            callCamera();
        }
    }


    private void callCamera() {
        MobileAds.initialize(this, "ca-app-pub-2442416013065911~8207211638");
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-2442416013065911/7821813222");
        interstitialAd.loadAd(new AdRequest.Builder().build());

        SharedPreferences sharedPreferences = getSharedPreferences("source", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("GALLERY", true);
        editor.apply();

        camera.addCameraListener(new CameraListener() {
            public void onCameraOpened(CameraOptions options) {
                camera.addCameraListener(new CameraListener() {
                    @Override
                    public void onOrientationChanged(int orientation) {
                        super.onOrientationChanged(orientation);
                        or = orientation;
                    }
                });
                onOpened();
            }

            public void onPictureTaken(byte[] jpeg)
            {
                onPicture(jpeg);
            }
        });

        camera.start();
        findViewById(R.id.capturePhoto).setOnClickListener(this);
        findViewById(R.id.toggleCamera).setOnClickListener(this);
        menuImageView = (ImageView) findViewById(R.id.vertical_menu_iv);
        menuImageView.setOnClickListener(this);
        galleryBtn = (ImageView) findViewById(R.id.gallery);
        galleryBtn.setOnClickListener(this);

        controlPanel = findViewById(R.id.controls);
        ViewGroup group = (ViewGroup) controlPanel.getChildAt(0);
        Control[] controls = Control.values();
        for (Control control : controls) {
            ControlView view = new ControlView(this, control, this);
            group.addView(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        /*controlPanel.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                BottomSheetBehavior b
                 = BottomSheetBehavior.from(controlPanel);
                b.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });*/
    }

//    public int getCorrectCameraOrientation(android.hardware.Camera.CameraInfo info, Camera camera) {
//
//        int rotation = getWindowManager().getDefaultDisplay().getRotation();
//        int degrees = 0;
//
//        switch(rotation){
//            case Surface.ROTATION_0:
//                degrees = 0;
//                break;
//
//            case Surface.ROTATION_90:
//                degrees = 90;
//                break;
//
//            case Surface.ROTATION_180:
//                degrees = 180;
//                break;
//
//            case Surface.ROTATION_270:
//                degrees = 270;
//                break;
//
//        }
//
//        int result;
//        if(info.facing== android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT){
//            result = (info.orientation + degrees) % 360;
//            result = (360 - result) % 360;
//        }else{
//            result = (info.orientation - degrees + 360) % 360;
//        }
//
//        return result;
//    }


    //endregion
}
