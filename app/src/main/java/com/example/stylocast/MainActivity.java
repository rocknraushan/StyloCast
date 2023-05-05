package com.example.stylocast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity  {
//    CardView card;
    RecyclerView recycleV;
   protected static ArrayList<Contact_Model> arrContacts = new ArrayList<>();
    RecyleAdapter adapter;
    FloatingActionButton AddNew;

    private static final  int CAMERA_PERMISSION_REQUEST_CODE = 101;
    private static final int STORAGE_PERMISSION_REQUEST_CODE= 102;
    private  static final String CHANNEL_ID = "JAI SHREE RAM";
    private static final int NOTIFICATION_ID = 100;
    private static final int POST_NOTIFICATION_CODE = 103;
    private static final int NOTIFICATION_REQ_CODE = 50;


//    private ActivityResultLauncher<String> mGetContentLauncher;

    Bitmap imgdata;
    Toolbar toolbar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//

        //Restore data

        LoadData();

//      Grant permissions:=--=
        postNotificationPermission();
        CameraPermission();
        StoragePermission();
        Intent NotifyIntent = new Intent(getApplicationContext(),MainActivity.class);

        getnotification(NotifyIntent);
//


        Objects.requireNonNull(getSupportActionBar()).setTitle("Contact");
        Objects.requireNonNull(getSupportActionBar()).setSubtitle("Dev. by Raushan");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


//        card = findViewById(R.id.cont_card);
        recycleV = findViewById(R.id.REcycle);
        Intent intentList;
//        card.setCardElevation(11.0f);
//        card.setRadius(5.0f);
//        card.setUseCompatPadding(true);
        AddNew = (FloatingActionButton) findViewById(R.id.AddNew);

        AddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.new_update_contack);
                EditText addname = dialog.findViewById(R.id.Update_Name);
                EditText addnumber = dialog.findViewById(R.id.Update_Number);
                ImageView addimg = dialog.findViewById(R.id.Update_img);
                Button savebtn = dialog.findViewById(R.id.Save_new);
                dialog.show();


                addimg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Imagepick();
                        addimg.setImageBitmap(imgdata);
                    }
                });


                savebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = "", number = "";
//                        int imgval= -1;
                        Intent iok = new Intent(getApplicationContext(),Lottie_toast.class);

                        if (!addname.getText().toString().equals("")&&!addnumber.getText().toString().equals("")&&imgdata != null){
                            name = addname.getText().toString();
                            number = addnumber.getText().toString();
                            arrContacts.add(new Contact_Model(imgdata, name, number));
                            adapter.notifyItemInserted(arrContacts.size() - 1);
                            recycleV.scrollToPosition(arrContacts.size() - 1);
                            dialog.dismiss();
                            dialog.hide();
                            saveData();
                            startActivity(iok);
                        } else if (!addname.getText().toString().equals("")&&!addnumber.getText().toString().equals("")&&imgdata == null) {
                            name = addname.getText().toString();
                            number = addnumber.getText().toString();
                            arrContacts.add(new Contact_Model(name, number));
                            adapter.notifyItemInserted(arrContacts.size() - 1);
                            recycleV.scrollToPosition(arrContacts.size() - 1);
                            dialog.dismiss();
                            dialog.hide();
                            saveData();
                            startActivity(iok);
                        } else {
                            Toast.makeText(MainActivity.this, "Enter data", Toast.LENGTH_SHORT).show();
//                            dialog.dismiss();
                        }


//                        if (!addname.getText().toString().equals("")) {
//                            name = addname.getText().toString();
//                        } else {
//                            Toast.makeText(MainActivity.this, "Enter Name!", Toast.LENGTH_SHORT).show();
//                        }
//
//                        if (!addnumber.getText().toString().equals("")) {
//                            number = addnumber.getText().toString();
//                        } else {
//                            Toast.makeText(MainActivity.this, "Enter Mobile Number!", Toast.LENGTH_SHORT).show();
//                        }
//
//                        if (imgdata != null) {
//                            arrContacts.add(new Contact_Model(imgdata, name, number));
////                            saveData();
//                        } else {
//                            arrContacts.add(new Contact_Model(name, number));
////                            saveData();
//                        }
                        adapter.notifyItemInserted(arrContacts.size() - 1);
                        recycleV.scrollToPosition(arrContacts.size() - 1);
                        dialog.dismiss();
                        dialog.hide();
                        saveData();

                    }


                });

            }
        });


        Intent ihome = getIntent();


        int Change_req = ihome.getIntExtra("Change_request", 0);


//        arrContacts.add(new Contact_Model("helo","+91787455474"));
        //onClick Listner for all contacts:::==:::
        if (Change_req == 1) {

            try {
                int Request_Code = ihome.getIntExtra("Request_code", 0),
                        position = ihome.getIntExtra("Result_code", -1);
//                ArrayList<Contact_Model> updatedList = (ArrayList<Contact_Model>) ihome.getSerializableExtra("Updated_list");
//                arrContacts.clear();
//                arrContacts.addAll(updatedList);
                if (Request_Code == 1) {
//                adapter.notifyItemChanged(position);
                    recycleV.scrollToPosition(position);
                    Toast.makeText(this, "Contact Edited successfully!", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this, "Item deleted successfully!", Toast.LENGTH_SHORT).show();
//
                }
//                saveData();
            } catch (Exception e) {
            }
        }

        recycleV.setLayoutManager(new LinearLayoutManager(this));
        //Making A Recycler Adapter:::===:::
        adapter = new RecyleAdapter(this, arrContacts);
        recycleV.setAdapter(adapter);
        if (arrContacts != null) {
            saveData();
        }
    }



//    private void saveProgressData() {
//        try {
//            FileOutputStream fos = openFileOutput("progress_data.txt", Context.MODE_PRIVATE);
//            fos.write("50".getBytes()); // Write your progress data here
//            fos.close();
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            e.printStackTrace();
//
//        }
//    }
//    @Override
//    protected void onPause() {
//        super.onPause();
//        saveProgressData();
//    }


    private void CameraPermission(){

        //check if camera request is granted!
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Request camera permission
            ActivityCompat.requestPermissions(this,
                    new String[] { android.Manifest.permission.CAMERA },
                    CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    private   void StoragePermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
        PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_REQUEST_CODE);
        }
    }
    private void postNotificationPermission(){
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.POST_NOTIFICATIONS)!=
        PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    POST_NOTIFICATION_CODE);
        }
    }



    private void Imagepick(){

        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("outputX", 256);
        intent.putExtra("outputY", 256);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if (resultCode != RESULT_OK) {
                return;
            }
            if (requestCode == 1) {
                final Bundle extras = data.getExtras();
                if (extras != null) {
                    //Get image
                    imgdata = extras.getParcelable("data");

                }
            }
    }


//int flag =0;
    boolean doubleBackToExitPressedOnce =false;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (doubleBackToExitPressedOnce) {
          saveData();
          finishAffinity();
        } else {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
        finishAffinity();
        return super.onOptionsItemSelected(item);
    }
//
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            saveData();
            finishAffinity();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000); // You can change the delay time as per your requirement

    }


    private  void saveData() {
        try {
            File internalStorageDir =  getFilesDir();
            File ContactFile = new File(internalStorageDir,"contactlist.ser");

            FileOutputStream fileOutputStream = openFileOutput(ContactFile.getName(),Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(arrContacts);
            objectOutputStream.close();
//              fileOutputStream.write(arrContacts);
//            fileOutputStream
//            Toast.makeText(getApplicationContext()," Entered",Toast.LENGTH_SHORT);
        } catch (IOException e) {
            e.printStackTrace();
//            Toast.makeText(getApplicationContext(),"Exception Entered",Toast.LENGTH_SHORT);
        }
    }

    private void LoadData(){
        try {
//            File internalStorageDir = getFilesDir();
            FileInputStream fileInputStream = openFileInput("contactlist.ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            arrContacts = (ArrayList<Contact_Model>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
//            Toast.makeText(getApplicationContext()," Entered",Toast.LENGTH_SHORT);
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
//            Toast.makeText(getApplicationContext(),"Exception Entered",Toast.LENGTH_SHORT);
        }

    }

    private void getnotification(Intent intent){
        Drawable drawable = ResourcesCompat.getDrawable(getResources(),R.drawable.user,null);

        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap bitImg = bitmapDrawable.getBitmap();


        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Notification notification;
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pi = PendingIntent.getActivity(this,NOTIFICATION_REQ_CODE,intent,PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_MUTABLE);

        //Big Picture Style
        Notification.BigPictureStyle bigPictureStyle = new Notification.BigPictureStyle()
                .bigPicture(((BitmapDrawable) (ResourcesCompat.getDrawable(getResources(),R.drawable.user,null))).getBitmap())
                .bigLargeIcon(bitImg)
                .setSummaryText("OM NAMAH SHIVAY!");

        //Inbox Style
        Notification.InboxStyle inboxStyle = new Notification.InboxStyle()
                .addLine("A")
                .addLine("B")
                .addLine("C")
                .addLine("d")
                .addLine("E")
                .addLine("F")
                .addLine("AG")
                .setBigContentTitle("Radhe Radhe!")
                .setSummaryText("msg from Ram ji");



        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification= new Notification.Builder(this)
                    .setLargeIcon(bitImg)
                    .setSmallIcon(R.drawable.user)
                    .setContentText("Hare krishna Hare Ram")
                    .setContentIntent(pi)
                    .setStyle(inboxStyle)
                    .setOngoing(true)
                    .setChannelId(CHANNEL_ID)
                    .build();
            nm.createNotificationChannel(new NotificationChannel(CHANNEL_ID,"new chanel",NotificationManager.IMPORTANCE_HIGH));

        } else {
            notification= new Notification.Builder(this)
                    .setLargeIcon(bitImg)
                    .setSmallIcon(R.drawable.user)
                    .setStyle(bigPictureStyle)
                    .setContentText("Hare krishna Hare Ram")
                    .build();

        }
        nm.notify(NOTIFICATION_ID,notification);
    }



    //    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        saveData();
//    }
}