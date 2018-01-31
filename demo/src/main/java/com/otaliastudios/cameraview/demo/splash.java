package com.otaliastudios.cameraview.demo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


public class splash extends AppCompatActivity {


//    private static int MY_PERMISSION_CAMERA = 0;
//    private static int MY_PERMISSION_READ_STORAGE = 1;
//    private static int MY_PERMISSION_WRITE_STORAGE = 2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_privacy_policy);
        setContentView(R.layout.activity_splash);





        Thread myThread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(2000);
                    Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                    startActivity(intent);
                    finish();

//                    else{
//                        ActivityCompat.requestPermissions(splash.this, PERMISSIONS, PERMISSION_ALL);
//                        Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }



//                if (ContextCompat.checkSelfPermission(splash.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
//                        && ContextCompat.checkSelfPermission(splash.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED
//                        && ContextCompat.checkSelfPermission(splash.this,Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
//                    ActivityCompat.requestPermissions(splash.this,
//                            new String[] {
//                                    Manifest.permission.WRITE_EXTERNAL_STORAGE ,
//                                    Manifest.permission.READ_EXTERNAL_STORAGE,
//                                    Manifest.permission.CAMERA
//                            }, PERMISSION_ALL);
//                }


//                while((ContextCompat.checkSelfPermission(splash.this,Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED)
//                        && (ContextCompat.checkSelfPermission(splash.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)
//                        &&(ContextCompat.checkSelfPermission(splash.this,Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)){
//                    getCameraPermission();
//                    getWritePermission();
//                    getReadPermission();
//                }
//
//                 The request code used in ActivityCompat.requestPermissions()
//                 and returned in the Activity's onRequestPermissionsResult()
//
            }
        };
        myThread.start();




    }


//
//    private void getCameraPermission(){
//        if(ContextCompat.checkSelfPermission(splash.this, Manifest.permission.CAMERA)
//                != PackageManager.PERMISSION_GRANTED){
//
//            //should we show description?
//            if(ActivityCompat.shouldShowRequestPermissionRationale(splash.this, Manifest.permission.CAMERA)){
//                /*
//                DESCRIBE WHY THE PERMISSION IS REQUIRED
//                 */
//            }else{
//                //NO EXPLANATION NEEDED
//
//                ActivityCompat.requestPermissions(splash.this,new String[]{Manifest.permission.CAMERA},MY_PERMISSION_CAMERA);
//            }
//        }
//    }
//
//    private void getReadPermission(){
//        if(ContextCompat.checkSelfPermission(splash.this, Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED){
//
//            //should we show description?
//            if(ActivityCompat.shouldShowRequestPermissionRationale(splash.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
//                /*
//                DESCRIBE WHY THE PERMISSION IS REQUIRED
//                 */
//            }else{
//                //NO EXPLANATION NEEDED
//
//                ActivityCompat.requestPermissions(splash.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSION_READ_STORAGE);
//            }
//        }
//    }
//
//    private void getWritePermission(){
//        if(ContextCompat.checkSelfPermission(splash.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED){
//
//            //should we show description?
//            if(ActivityCompat.shouldShowRequestPermissionRationale(splash.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
//                /*
//                DESCRIBE WHY THE PERMISSION IS REQUIRED
//                 */
//            }else{
//                //NO EXPLANATION NEEDED
//
//                ActivityCompat.requestPermissions(splash.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},MY_PERMISSION_WRITE_STORAGE);
//            }
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//
//        if (requestCode == MY_PERMISSION_CAMERA) {
//            // If request is cancelled, the result arrays are empty.
//            if (grantResults.length > 0
//                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                // permission was granted, yay! Do the
//                // camera-related task you need to do.
//                getWritePermission();
//
//            } else {
//                getWritePermission();
//                // permission denied, boo! Disable the
//                // functionality that depends on this permission.
//            }
//        }
//        if (requestCode == MY_PERMISSION_READ_STORAGE) {
//            // If request is cancelled, the result arrays are empty.
//            if (grantResults.length > 0
//                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                // permission was granted, yay! Do the
//                // contacts-related task you need to do.
//
//            } else {
//
//                // permission denied, boo! Disable the
//                // functionality that depends on this permission.
//            }
//        }
//        if (requestCode == MY_PERMISSION_WRITE_STORAGE) {
//            // If request is cancelled, the result arrays are empty.
//            if (grantResults.length > 0
//                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // permission was granted, yay! Do the
//                // storage-related task you need to do.
//            } else {
//                // permission denied, boo! Disable the
//                // functionality that depends on this permission.
//            }
//        }
//        // other 'case' lines to check for other
//        // permissions this app might request
//    }
}