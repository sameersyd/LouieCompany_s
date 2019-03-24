package com.kreator.sameer.louie;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class AddReferalActivity extends AppCompatActivity {

    TextView mainTxt,nameTxt,phoneTxt,emailTxt,additionalTxt;
    EditText nameEdit,phoneEdit,emailEdit,additionalEdit;
    ImageView closeImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_referal);

        mainTxt = (TextView)findViewById(R.id.addreferal_mainTxt);
        nameTxt = (TextView)findViewById(R.id.addreferal_nameTxt);
        phoneTxt = (TextView)findViewById(R.id.addreferal_phoneTxt);
        emailTxt = (TextView)findViewById(R.id.addreferal_emailTxt);
        additionalTxt = (TextView)findViewById(R.id.addreferal_additionalTxt);

        nameEdit = (EditText)findViewById(R.id.addreferal_nameEdit);
        phoneEdit = (EditText)findViewById(R.id.addreferal_phoneEdit);
        emailEdit = (EditText)findViewById(R.id.addreferal_emailEdit);
        additionalEdit = (EditText)findViewById(R.id.addreferal_additionalEdit);

        closeImg = (ImageView)findViewById(R.id.addreferal_closeImg);
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Typeface myCustomFont_montserrat_bold = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Bold.ttf");
        Typeface myCustomFont_montserrat_regular = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Regular.ttf");
        mainTxt.setTypeface(myCustomFont_montserrat_bold);
        nameTxt.setTypeface(myCustomFont_montserrat_regular);
        phoneTxt.setTypeface(myCustomFont_montserrat_regular);
        emailTxt.setTypeface(myCustomFont_montserrat_regular);
        additionalTxt.setTypeface(myCustomFont_montserrat_regular);
        nameEdit.setTypeface(myCustomFont_montserrat_regular);
        phoneEdit.setTypeface(myCustomFont_montserrat_regular);
        emailEdit.setTypeface(myCustomFont_montserrat_regular);
        additionalEdit.setTypeface(myCustomFont_montserrat_regular);



    }
}
