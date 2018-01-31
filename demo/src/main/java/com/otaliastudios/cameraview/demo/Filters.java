package com.otaliastudios.cameraview.demo;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by haide on 12/12/2017.
 */

public class Filters extends Activity{

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private boolean source = false;
    ViewPager viewPager;
    static String uri;
    LinearLayout saveBtn,shareBtn;
    ImageView imageView,capturedImg;
    RelativeLayout mFilters;
    Bitmap capturedimage;
    Bitmap bitmapPicture;
    int getOrientation;

    ImageView testingFilter, shareFacebook, shareWhatsapp;

    Bitmap bmOverlay,testingFilter1;

    int h2;
    int w2;

    int h1;
    int w1;

    String uri1;
    TextView savingText;

    CallbackManager callbackManager;
    ShareDialog shareDialog;


    private static WeakReference<byte[]> image;

    public static void setImage(@Nullable byte[] im) {
        image = im != null ? new WeakReference<>(im) : null;
    }

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
        setContentView(R.layout.activity_filters);

        //initialize faccbook sdk
//        printHashKey();
        FacebookSdk.sdkInitialize(this);
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(Filters.this);

        if(getIntent().getStringExtra("uri")!=null){
            uri = getIntent().getStringExtra("uri");
        }

        testingFilter = (ImageView)findViewById(R.id.filter1);
        imageView = (ImageView)findViewById(R.id.background);
        saveBtn = findViewById(R.id.saveBtn);
        capturedImg = (ImageView)findViewById(R.id.imgg);
        shareBtn = findViewById(R.id.shareBtn);
        mFilters = (RelativeLayout)findViewById(R.id.filters);
        savingText = (TextView)findViewById(R.id.savingText);

        getOrientation = getIntent().getExtras().getInt("orientation");

        SharedPreferences sharedPreferences = getSharedPreferences("source",MODE_PRIVATE);
        source = sharedPreferences.getBoolean("GALLERY",false);

        if(!source){

        byte[] b = image == null ? null : image.get();
        if (b == null) {
            finish();
            return;
        }

            int orientation = Exif.getOrientation(b);
            Toast.makeText(this, "ORIENTATION: "+orientation, Toast.LENGTH_SHORT).show();
            capturedimage = BitmapFactory.decodeByteArray(b,0,b.length);
            bitmapPicture = capturedimage;

            switch(orientation) {
                case 90:
                    bitmapPicture= rotateImage(capturedimage, 90);
                    break;
                case 180:
                    bitmapPicture= rotateImage(capturedimage, 180);
                    break;
                case 270:
                    bitmapPicture= rotateImage(capturedimage, 270);
                    break;
                case 0:
                    // if orientation is zero we don't need to rotate this
                default:
                    break;
            }

            //get width and height of captured picture
            w1 = bitmapPicture.getWidth();
            h1 = bitmapPicture.getHeight();
        }

        else if(source){
            try {
                capturedimage = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(uri));
            } catch (IOException e) {
                e.printStackTrace();
            }
            capturedImg.setImageBitmap(capturedimage);
            h1 = capturedimage.getHeight();
            w1 = capturedimage.getWidth();
            bitmapPicture = capturedimage;
//            showFilters();
        }

        showFilters();
    }

    private void printHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.otaliastudios.cameraview.demo"
            ,PackageManager.GET_SIGNATURES);

            for(Signature signature : info.signatures){
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(),Base64.DEFAULT));

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (capturedimage != null) {
            capturedimage.recycle();
            capturedimage = null;
        }
        if(bitmapPicture!=null) {
            bitmapPicture.recycle();
            bitmapPicture = null;
        }
        if(bmOverlay!=null) {
            bmOverlay.recycle();
            bmOverlay = null;
        }
        if(testingFilter1!=null){
            testingFilter1.recycle();
            testingFilter1 = null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (capturedimage != null) {
//            capturedimage.recycle();
            capturedimage = null;
        }
        if(bitmapPicture!=null) {
//            bitmapPicture.recycle();
            bitmapPicture = null;
        }
        if(bmOverlay!=null) {
//            bmOverlay.recycle();
            bmOverlay = null;
        }
        if(testingFilter1!=null){
//            testingFilter1.recycle();
            testingFilter1 = null;
        }
    }

    private void showFilters(){


        viewPager = (ViewPager)findViewById(R.id.viewpager);
        SharedPreferences sh = getSharedPreferences("orient", MODE_PRIVATE);
        SharedPreferences.Editor et = sh.edit();

        if(w1<h1){
//            testingFilter1 = BitmapFactory.decodeResource(getResources(),R.mipmap.multan_portrait);
//            w2 = testingFilter1.getWidth();
//            double aspectRatio = (double) bitmapPicture.getHeight() / (double) bitmapPicture.getWidth();
//            int targetWidth = w2;
//            int targetHeight = (int) (targetWidth * aspectRatio);
//            bitmapPicture = Bitmap.createScaledBitmap(bitmapPicture,w2,targetHeight,true);
//            viewPager.getLayoutParams().width = w2;
//            viewPager.getLayoutParams().height = targetHeight;

            testingFilter1 = BitmapFactory.decodeResource(getResources(),R.mipmap.multan_landscape);
            w2 = testingFilter1.getWidth();
            double aspectRatio = (double) bitmapPicture.getHeight() / (double) bitmapPicture.getWidth();
            int targetWidth = w2;
            int targetHeight = (int) (targetWidth * aspectRatio);
            bitmapPicture = Bitmap.createScaledBitmap(bitmapPicture,w2,targetHeight,true);
            viewPager.getLayoutParams().width = w2;
            viewPager.getLayoutParams().height = targetHeight;

            et.putBoolean("orient", false);
            et.apply();
            viewPager.setAdapter(new CustomPagerAdapter(Filters.this,uri));

            Toast.makeText(this, "ViewPager W: "+viewPager.getLayoutParams().width+" H: "+viewPager.getLayoutParams().height, Toast.LENGTH_SHORT).show();

            et.putBoolean("orient", false);
            et.apply();
            viewPager.setAdapter(new CustomPagerAdapter(Filters.this,uri));

        }
        else if(h1<w1){

//            bitmapPicture = Bitmap.createScaledBitmap(bitmapPicture,bitmapPicture.getHeight(),bitmapPicture.getWidth(),true);

            switch (getOrientation)
            {
                case 90:
                    bitmapPicture = rotateImage(bitmapPicture , 270);
                    break;
                case 270:
                    bitmapPicture = rotateImage(bitmapPicture , 90);
                    break;
                case 180:
                    bitmapPicture = rotateImage(bitmapPicture , 180);
                    break;

                    default:
                        bitmapPicture = rotateImage(bitmapPicture , 0);
                        break;


            }


            testingFilter1 = BitmapFactory.decodeResource(getResources(),R.mipmap.multan_landscape);
            w2 = testingFilter1.getWidth();
            double aspectRatio = (double) bitmapPicture.getHeight() / (double) bitmapPicture.getWidth();
            int targetWidth = w2;
            int targetHeight = (int) (targetWidth * aspectRatio);
            bitmapPicture = Bitmap.createScaledBitmap(bitmapPicture,w2,targetHeight,true);
            viewPager.getLayoutParams().width = w2;
            viewPager.getLayoutParams().height = targetHeight;

            et.putBoolean("orient", false);
            et.apply();
            viewPager.setAdapter(new CustomPagerAdapter(Filters.this,uri));

//            testingFilter1 = BitmapFactory.decodeResource(getResources(),R.mipmap.multan_landscape);
//            h2 = testingFilter1.getHeight();
//            double aspectRatio = (double) bitmapPicture.getHeight() / (double) bitmapPicture.getWidth();
//            int targetHeight = h2;
//            int targetWidth = (int) (targetHeight / aspectRatio);
////            Toast.makeText(this, "target H: "+targetHeight+" W: "+targetWidth, Toast.LENGTH_SHORT).show();
//            bitmapPicture = Bitmap.createScaledBitmap(bitmapPicture,targetWidth,h2,true);
//            Toast.makeText(this, "img after: H: "+h2+" W: "+targetWidth, Toast.LENGTH_SHORT).show();
//            testingFilter1 = Bitmap.createScaledBitmap(testingFilter1,targetWidth,targetHeight,true);
//            h2 = testingFilter1.getHeight();
//            Toast.makeText(this, "filter after: H: "+testingFilter1.getHeight()+" W: "+testingFilter1.getWidth(), Toast.LENGTH_SHORT).show();
//
//            viewPager.getLayoutParams().width = targetWidth;
//            viewPager.getLayoutParams().height = h2;
//            Toast.makeText(this, "ViewPager after H: "+viewPager.getLayoutParams().height+ "W: "+viewPager.getLayoutParams().width, Toast.LENGTH_SHORT).show();

            et.putBoolean("orient", true);
            et.apply();

            viewPager.setAdapter(new CustomPagerAdapter(Filters.this,uri));

        }

        capturedImg = (ImageView)findViewById(R.id.imgg);
        capturedImg.setImageBitmap(bitmapPicture);

        imageView = (ImageView)findViewById(R.id.background);

        //on save button
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savingText.setText("Saving...");

//                Toast.makeText(getApplicationContext(), "before filter size " + testingFilter1.getWidth() + " X " + testingFilter1.getHeight(), Toast.LENGTH_LONG).show();

                if(!hasPermissions(Filters.this,PERMISSIONS)){
                    ActivityCompat.requestPermissions(Filters.this, PERMISSIONS, PERMISSION_ALL);
                }

                imageView = (ImageView)findViewById(R.id.background);

                String uri = getIntent().getStringExtra("uri");


                h1 = bitmapPicture.getHeight();
                w1 = bitmapPicture.getWidth();

                if(w1<h1) {

                    int page = viewPager.getCurrentItem();
                    switch (page) {
                        case 0:
                            testingFilter1 = BitmapFactory.decodeResource(getResources(), R.mipmap.multan_portrait);
                            break;
                        case 1:
                            testingFilter1 = BitmapFactory.decodeResource(getResources(), R.mipmap.multan_portrait);
                            break;
                        case 2:
                            testingFilter1 = BitmapFactory.decodeResource(getResources(), R.mipmap.multan_portrait);
                            break;
                        case 3:
                            testingFilter1 = BitmapFactory.decodeResource(getResources(), R.mipmap.multan_portrait);
                            break;
                        case 4:
                            testingFilter1 = BitmapFactory.decodeResource(getResources(), R.mipmap.multan_portrait);
                            break;
                        case 5:
                            testingFilter1 = BitmapFactory.decodeResource(getResources(), R.mipmap.multan_portrait);
                            break;
                        default:
                            testingFilter1 = BitmapFactory.decodeResource(getResources(), R.mipmap.multan_portrait);
                            break;
                    }
                }
                else if(h1<w1){
                    int page = viewPager.getCurrentItem();
                    switch (page) {
                        case 0:
                            testingFilter1 = BitmapFactory.decodeResource(getResources(), R.mipmap.multan_landscape);
                            break;
                        case 1:
                            testingFilter1 = BitmapFactory.decodeResource(getResources(), R.mipmap.multan_landscape);
                            break;
                        case 2:
                            testingFilter1 = BitmapFactory.decodeResource(getResources(), R.mipmap.multan_landscape);
                            break;
                        case 3:
                            testingFilter1 = BitmapFactory.decodeResource(getResources(), R.mipmap.multan_landscape);
                            break;
                        case 4:
                            testingFilter1 = BitmapFactory.decodeResource(getResources(), R.mipmap.multan_landscape);
                            break;
                        case 5:
                            testingFilter1 = BitmapFactory.decodeResource(getResources(), R.mipmap.multan_landscape);
                            break;
                        default:
                            testingFilter1 = BitmapFactory.decodeResource(getResources(), R.mipmap.multan_landscape);
                            break;
                    }
                }

                if(hasPermissions(Filters.this,PERMISSIONS)){

                    h2 = testingFilter1.getHeight();
                    w2 = testingFilter1.getWidth();


                    if(w1<h1){
                        double aspectRatio = (double) bitmapPicture.getHeight() / (double) bitmapPicture.getWidth();
                        int targetWidth = w2;
                        int targetHeight = (int) (targetWidth * aspectRatio);
                        testingFilter1 = Bitmap.createScaledBitmap(testingFilter1,w2, (targetHeight),true);
                        bitmapPicture = Bitmap.createScaledBitmap(bitmapPicture,w2,targetHeight,true);
                        bmOverlay = Bitmap.createBitmap(bitmapPicture.getWidth(),bitmapPicture.getHeight(),bitmapPicture.getConfig());
                        Canvas canvas = new Canvas(bmOverlay);
                        canvas.drawBitmap(bitmapPicture, new Matrix(), null);
                        canvas.drawBitmap(testingFilter1, new Matrix(), null);
                    }
                    else if(h1<w1){

                        double aspectRatio = (double) bitmapPicture.getHeight() / (double) bitmapPicture.getWidth();
                        int targetHeight = h2;
                        int targetWidth = (int) (targetHeight / aspectRatio);
                        testingFilter1 = Bitmap.createScaledBitmap(testingFilter1,targetWidth,h2,true);
                        bitmapPicture = Bitmap.createScaledBitmap(bitmapPicture,targetWidth,h2,true);
                        bmOverlay = Bitmap.createBitmap(bitmapPicture.getWidth(),bitmapPicture.getHeight(),bitmapPicture.getConfig());
                        Canvas canvas = new Canvas(bmOverlay);
                        canvas.drawBitmap(bitmapPicture, new Matrix(), null);
                        canvas.drawBitmap(testingFilter1, new Matrix(), null);

                    }


                    //SAVING FILE
                    FileOutputStream outStream = null;
                    File dir = new File(
                            Environment
                                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                            "PSL");
                    if (!dir.exists()) {
                        if (!dir.mkdirs()) {
                            Log.d("PSL", "failed to create directory");
                        }
                    }
                    String fileName = String.format("%d.jpg", System.currentTimeMillis());
                    File outFile = new File(dir, fileName);
                    try {
                        outStream = new FileOutputStream(outFile);
                        bmOverlay.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                        outStream.flush();
                        outStream.close();
                        bmOverlay.recycle();
                        bmOverlay = null;
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                savingText.setText("Save");
                                Toast.makeText(Filters.this, "SAVED!", Toast.LENGTH_SHORT).show();
                            }
                        }, 1000); // 5000ms delay

                        MediaScannerConnection.scanFile(Filters.this,
                                new String[] { outFile.toString() }, null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    public void onScanCompleted(String path, Uri uri) {
                                        Log.i("ExternalStorage", "Scanned " + path + ":");
                                        Log.i("ExternalStorage", "-> uri=" + uri);
                                    }
                        });

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        });

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            imageView = (ImageView)findViewById(R.id.background);
            h1 = bitmapPicture.getHeight();
            w1 = bitmapPicture.getWidth();

            if(w1<h1) {

                int page = viewPager.getCurrentItem();
                switch (page) {
                    case 0:
                        testingFilter1 = BitmapFactory.decodeResource(getResources(), R.mipmap.multan_portrait);
                        break;
                    case 1:
                        testingFilter1 = BitmapFactory.decodeResource(getResources(), R.mipmap.multan_portrait);
                        break;
                    case 2:
                        testingFilter1 = BitmapFactory.decodeResource(getResources(), R.mipmap.multan_portrait);
                        break;
                    case 3:
                        testingFilter1 = BitmapFactory.decodeResource(getResources(), R.mipmap.multan_portrait);
                        break;
                    case 4:
                        testingFilter1 = BitmapFactory.decodeResource(getResources(), R.mipmap.multan_portrait);
                        break;
                    case 5:
                        testingFilter1 = BitmapFactory.decodeResource(getResources(), R.mipmap.multan_portrait);
                        break;
                    default:
                        testingFilter1 = BitmapFactory.decodeResource(getResources(), R.mipmap.multan_portrait);
                        break;
                }
            }
            else if(h1<w1){
                int page = viewPager.getCurrentItem();
                switch (page) {
                    case 0:
                        testingFilter1 = BitmapFactory.decodeResource(getResources(), R.mipmap.multan_landscape);
                        break;
                    case 1:
                        testingFilter1 = BitmapFactory.decodeResource(getResources(), R.mipmap.multan_landscape);
                        break;
                    case 2:
                        testingFilter1 = BitmapFactory.decodeResource(getResources(), R.mipmap.multan_landscape);
                        break;
                    case 3:
                        testingFilter1 = BitmapFactory.decodeResource(getResources(), R.mipmap.multan_landscape);
                        break;
                    case 4:
                        testingFilter1 = BitmapFactory.decodeResource(getResources(), R.mipmap.multan_landscape);
                        break;
                    case 5:
                        testingFilter1 = BitmapFactory.decodeResource(getResources(), R.mipmap.multan_landscape);
                        break;
                    default:
                        testingFilter1 = BitmapFactory.decodeResource(getResources(), R.mipmap.multan_landscape);
                        break;
                }
            }

            if(hasPermissions(Filters.this,PERMISSIONS)){

                h2 = testingFilter1.getHeight();
                w2 = testingFilter1.getWidth();

                if(w1<h1){
                    double aspectRatio = (double) bitmapPicture.getHeight() / (double) bitmapPicture.getWidth();
                    int targetWidth = w2;
                    int targetHeight = (int) (targetWidth * aspectRatio);
                    testingFilter1 = Bitmap.createScaledBitmap(testingFilter1,w2, (targetHeight),true);
                    bitmapPicture = Bitmap.createScaledBitmap(bitmapPicture,w2,targetHeight,true);
                    bmOverlay = Bitmap.createBitmap(bitmapPicture.getWidth(),bitmapPicture.getHeight(),bitmapPicture.getConfig());
                    Canvas canvas = new Canvas(bmOverlay);
                    canvas.drawBitmap(bitmapPicture, new Matrix(), null);
                    canvas.drawBitmap(testingFilter1, new Matrix(), null);
                }
                else if(h1<w1){

                    double aspectRatio = (double) bitmapPicture.getHeight() / (double) bitmapPicture.getWidth();
                    int targetHeight = h2;
                    int targetWidth = (int) (targetHeight / aspectRatio);
                    testingFilter1 = Bitmap.createScaledBitmap(testingFilter1,targetWidth,h2,true);
                    bitmapPicture = Bitmap.createScaledBitmap(bitmapPicture,targetWidth,h2,true);
                    bmOverlay = Bitmap.createBitmap(bitmapPicture.getWidth(),bitmapPicture.getHeight(),bitmapPicture.getConfig());
                    Canvas canvas = new Canvas(bmOverlay);
                    canvas.drawBitmap(bitmapPicture, new Matrix(), null);
                    canvas.drawBitmap(testingFilter1, new Matrix(), null);

                }


                //SAVING FILE
                FileOutputStream outStream = null;
                File dir = new File(
                        Environment
                                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        "PSL");
                if (!dir.exists()) {
                    if (!dir.mkdirs()) {
                        Log.d("PSL", "failed to create directory");
                    }
                }
                String fileName = String.format("%d.jpg", System.currentTimeMillis());
                File outFile = new File(dir, fileName);
                try {
                    outStream = new FileOutputStream(outFile);
                    uri1 = String.valueOf(outFile);
                    bmOverlay.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                    outStream.flush();
                    outStream.close();
                    Toast.makeText(Filters.this, "SAVED!", Toast.LENGTH_SHORT).show();


                    MediaScannerConnection.scanFile(Filters.this,
                            new String[] { outFile.toString() }, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> uri=" + uri);
                                }
                            });

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

                /**
                 * Dialog box for sharing options appear here
                 */
                final Dialog dialog = new Dialog(Filters.this, android.R.style.Widget_PopupWindow);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.share_dialog);
                shareFacebook = (ImageView)dialog.findViewById(R.id.shareFacebook);
                shareWhatsapp = (ImageView)dialog.findViewById(R.id.shareWhatsapp);

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.getWindow().setAttributes(lp);

                dialog.show();

                shareWhatsapp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(Filters.this, "SHARING ON WHATSAPP", Toast.LENGTH_SHORT).show();
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.setType("image/JPEG");
                        shareIntent.setPackage("com.whatsapp");
                        Uri uriShare = Uri.parse(uri1);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, uriShare);
                        shareIntent.putExtra(Intent.EXTRA_TEXT, "");
                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(Intent.createChooser(shareIntent, ""));
                        dialog.dismiss();
                    }
                });

                shareFacebook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(Filters.this, "SHARING ON FACEBOOK", Toast.LENGTH_SHORT).show();

                        SharePhoto photo = new SharePhoto.Builder()
                                .setBitmap(bmOverlay)
                                .build();
                        if(ShareDialog.canShow(SharePhotoContent.class)){
                            SharePhotoContent content = new SharePhotoContent.Builder()
                                    .addPhoto(photo)
                                    .build();
                            shareDialog.show(content, ShareDialog.Mode.NATIVE);
                        }


                        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                            @Override
                            public void onSuccess(Sharer.Result result) {
                                Toast.makeText(Filters.this, "Shared Successfully", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }

                            @Override
                            public void onCancel() {
                                Toast.makeText(Filters.this, "Share Cancelled", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(FacebookException error) {
                                Toast.makeText(Filters.this, "Can not share image", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

            }
        });
    }


        @Override
    protected void onResume() {
        super.onResume();
        if(Build.VERSION.SDK_INT < 19){
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else {
            //for higher api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
}