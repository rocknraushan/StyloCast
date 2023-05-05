package com.example.stylocast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class IndividualList extends AppCompatActivity {

    Bitmap NewImgData;

    TextView nametxt,numbertxt;
    Button editbtn,deletebtn;

    ImageView indivImg;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_list);


//        MainActivity mainActiv = new MainActivity();



        nametxt = findViewById(R.id.ind_name);
        numbertxt = findViewById(R.id.ind_number);
        indivImg = findViewById(R.id.Indevidual_Img);

        editbtn = findViewById(R.id.EditContact);
        deletebtn = findViewById(R.id.Delete_contact);
        Intent ihome = new Intent(this,MainActivity.class);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Delete?")
                .setMessage("Do you want to delete?");



        Intent iget = getIntent();
//        String  name = iget.getStringExtra("name"),
//                number = iget.getStringExtra("number");
//        Bitmap imgval = iget.getParcelableExtra("contact_img");
        int position = iget.getIntExtra("position",-1);
//        list = new ArrayList<>();
//       list = (ArrayList<Contact_Model>) iget.getSerializableExtra("Contact List");
        String name = MainActivity.arrContacts.get(position).name,
                number = MainActivity.arrContacts.get(position).number;
        Bitmap imgval = MainActivity.arrContacts.get(position).imgVal;

        nametxt.setText(name);
        numbertxt.setText(number);
        indivImg.setImageBitmap(imgval);
        Dialog dialog = new Dialog(IndividualList.this);



        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setContentView(R.layout.new_update_contack);
                EditText addname = dialog.findViewById(R.id.Update_Name);
                EditText addnumber = dialog.findViewById(R.id.Update_Number);
                TextView heading = dialog.findViewById(R.id.dialog_head);
                Button save = dialog.findViewById(R.id.Save_new);
                ImageView dialogimg = dialog.findViewById(R.id.Update_img);

                dialogimg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Imagepick();
                        dialogimg.setImageBitmap(NewImgData);
                    }
                });

                heading.setText("Update Details");
                addname.setText(name);
                addnumber.setText(number);
                dialogimg.setImageBitmap(imgval);
                dialog.show();
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    String NewName= addname.getText().toString(),
                            NewNumber = addnumber.getText().toString();

//                    Bitmap newimg = imgval.extractAlpha();
                        MainActivity.arrContacts.set(position,new Contact_Model(NewImgData,name,number));

//                    list.set(position,new Contact_Model(newimg,NewName,NewNumber));
//                    ihome.putExtra("Updated_list",list);
                    ihome.putExtra("Request_code",1);
                    ihome.putExtra("Position",position);
                    ihome.putExtra("Change_request",1);

//                        adapter.notifyItemChanged(position);
                        dialog.dismiss();
                        NewImgData = null;
                        startActivity(ihome);


                    }
                });
            }
        });





        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alert.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        list.remove(position);
//                        ihome.putExtra("Updated_list",list);
                        ihome.putExtra("Request_code",2);
                        ihome.putExtra("Position",position);
                        ihome.putExtra("Change_request",1);
                        MainActivity.arrContacts.remove(position);
//                        adapter.notifyItemRemoved(position);
                        startActivity(ihome);
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        alertshow.dismiss();
                    }
                });
                AlertDialog alertshow = alert.create();



                alertshow.show();

            }
        });
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
                NewImgData = extras.getParcelable("data");
            }
        }
    }
}