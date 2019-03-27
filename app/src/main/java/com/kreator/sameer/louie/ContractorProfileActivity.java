package com.kreator.sameer.louie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class ContractorProfileActivity extends AppCompatActivity {

    String firmKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contractor_profile);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            firmKey = extras.getString("firmKey");
        }

    }
}
