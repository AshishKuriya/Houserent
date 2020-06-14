package com.example.houserent;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Contact extends AppCompatActivity {
    private TextView contect,to,subject,message;
    private EditText memail,msubject,msg,mobile_no;
    private Button send_email;
    ImageButton btCall;
    String sellerID;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        send_email=findViewById(R.id.contact_button);
        memail = findViewById(R.id.pemail);
        msubject = findViewById(R.id.msubject);
        msg = findViewById(R.id.message_text);

        sellerID = getIntent().getStringExtra("sellerId");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Users").child(sellerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Userprofile userprofile = dataSnapshot.getValue(Userprofile.class);
                memail.setText(userprofile.getTxtEmail());
                msubject.setText("Regarding to rent your house");
                mobile_no.setText(userprofile.getMobileNumber());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        send_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });

        mobile_no = findViewById(R.id.pnum1);
        btCall = findViewById(R.id.bt_call);
        btCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = mobile_no.getText().toString();
                if(phone.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please Enter Number",Toast.LENGTH_SHORT).show();
                }else{
                    String s = "tel:" + phone;
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse(s));
                    startActivity(intent);
                }
            }
        });
    }
    private void sendMail(){
        String recipientList = memail.getText().toString();
        String[] recipients =  recipientList.split(",");
        String subject = msubject.getText().toString();
        String message = msg.getText().toString();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL,recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT,subject);
        intent.putExtra(Intent.EXTRA_TEXT,message);

        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent,"Choose an Email Client"));

    }
}