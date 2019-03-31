package com.kreator.sameer.louie;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PayActivity extends AppCompatActivity {

    TextView custNameTxt,contactTxt,numberTxt,emailTxt,amountTxt;
    Button payBtn;
    EditText amountEdit;
    ImageView closeImg,profileImg;
    String custUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            custUid = extras.getString("custUid");
        }

        custNameTxt = (TextView)findViewById(R.id.pay_nameTxt);
        contactTxt = (TextView)findViewById(R.id.pay_contactTxt);
        numberTxt = (TextView)findViewById(R.id.pay_numberTxt);
        emailTxt = (TextView)findViewById(R.id.pay_emailTxt);
        amountTxt = (TextView)findViewById(R.id.pay_amountTxt);
        payBtn = (Button)findViewById(R.id.pay_payBtn);
        closeImg = (ImageView)findViewById(R.id.pay_closeImg);
        profileImg = (ImageView)findViewById(R.id.pay_profilePic);
        amountEdit = (EditText)findViewById(R.id.pay_emailEdit);

        Typeface myCustomFont_montserrat_bold = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Bold.ttf");
        Typeface myCustomFont_montserrat_regular = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Regular.ttf");
        custNameTxt.setTypeface(myCustomFont_montserrat_bold);
        contactTxt.setTypeface(myCustomFont_montserrat_bold);
        numberTxt.setTypeface(myCustomFont_montserrat_regular);
        emailTxt.setTypeface(myCustomFont_montserrat_regular);
        amountTxt.setTypeface(myCustomFont_montserrat_regular);
        payBtn.setTypeface(myCustomFont_montserrat_regular);
        amountEdit.setTypeface(myCustomFont_montserrat_regular);

        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child(Configs.users)
                .child(custUid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                custNameTxt.setText(dataSnapshot.child(Configs.name).getValue(String.class));
                numberTxt.setText(dataSnapshot.child(Configs.phone).getValue(String.class));
                emailTxt.setText(dataSnapshot.child(Configs.email).getValue(String.class));
                payBtn.setText("Pay "+dataSnapshot.child(Configs.name).getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PayActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
