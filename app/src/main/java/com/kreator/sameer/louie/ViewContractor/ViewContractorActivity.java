package com.kreator.sameer.louie.ViewContractor;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kreator.sameer.louie.Configs;
import com.kreator.sameer.louie.Contractor.ContractorCustomersObject;
import com.kreator.sameer.louie.Customer.CustomerReferralCustomadapter;
import com.kreator.sameer.louie.R;
import com.kreator.sameer.louie.ReportActivity;

import java.util.ArrayList;

public class ViewContractorActivity extends AppCompatActivity {

    String firmKey;
    ImageView closeImg;
    TextView nameTxt,reportPlainTxt,theTeamTxt,contPlainTxt;
    RelativeLayout reportBtn;

    Dialog loadDialog;
    ListView listView;
    ViewContractorCustomadapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contractor);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            firmKey = extras.getString("firmKey");
        }

        listView = (ListView)findViewById(R.id.contProfile_listview);

        contPlainTxt = (TextView)findViewById(R.id.contProfile_contMainTxt);
        nameTxt = (TextView)findViewById(R.id.contProfile_contNameTxt);
        reportPlainTxt = (TextView)findViewById(R.id.contProfile_reportPlain);
        theTeamTxt = (TextView)findViewById(R.id.contProfile_theTeam);
        reportBtn = (RelativeLayout)findViewById(R.id.contProfile_reportRela);
        closeImg = (ImageView)findViewById(R.id.contProfile_closeImg);
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewContractorActivity.this,ReportActivity.class);
                i.putExtra("firmKey",firmKey);
                startActivity(i);
            }
        });

        Typeface myCustomFont_montserrat_bold = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Bold.ttf");
        Typeface myCustomFont_montserrat_regular = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Regular.ttf");
        contPlainTxt.setTypeface(myCustomFont_montserrat_bold);
        nameTxt.setTypeface(myCustomFont_montserrat_bold);
        theTeamTxt.setTypeface(myCustomFont_montserrat_bold);
        reportPlainTxt.setTypeface(myCustomFont_montserrat_regular);

        loadDialog = new Dialog(this);
        loadDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadDialog.setContentView(R.layout.loading_one);
        loadDialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    loadDialog.dismiss();
                }
                return true;
            }
        });
        LottieAnimationView animSelect;
        animSelect = (LottieAnimationView) loadDialog.findViewById(R.id.loading_one);
        loadDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        animSelect.setAnimation("blueline.json");
        animSelect.playAnimation();
        animSelect.loop(true);

        Window window = loadDialog.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        adapter = new ViewContractorCustomadapter(this);
        listView.setAdapter(adapter);

        mainFetch();

    }

    //Get contractor key
    public void mainFetch(){
        loadDialog.show();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child(Configs.users)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(Configs.contractor_link_uid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //fetch contractor name from key
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                        .child(Configs.users)
                        .child(dataSnapshot.getValue(String.class))
                        .child(Configs.name);
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        nameTxt.setText(dataSnapshot.getValue(String.class));
                        retrieve();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(ViewContractorActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        loadDialog.dismiss();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ViewContractorActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                loadDialog.dismiss();
            }
        });
    }

    //retrieve uid of all the contractors in the team
    public void retrieve() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child(Configs.firms)
                .child(firmKey)
                .child("members");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    fetchContDetails(ds.getRef().getKey(), i+1);
                    i++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ViewContractorActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                loadDialog.dismiss();
            }
        });
    }

    //Fetch details of contractors
    public void fetchContDetails(String contUid, final int i){
        final String idC = contUid;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child(Configs.users)
                .child(contUid);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                        .child(Configs.users)
                        .child(idC);
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        adapter.spacecrafts.add(new ViewContractorObject(
                                dataSnapshot.child(Configs.name).getValue().toString(),
                                dataSnapshot.child(Configs.email).getValue().toString(),
                                dataSnapshot.child(Configs.phone).getValue().toString(),
                                i+""));
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(ViewContractorActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                loadDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ViewContractorActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                loadDialog.dismiss();
            }
        });
    }


}
