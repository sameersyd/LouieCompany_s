package com.kreator.sameer.louie;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {

    TextView mainTxt,nameTxt,phoneTxt,profileImgTxt,uploadImgTxt;
    EditText nameEdit,phoneEdit;
    ImageView uploadImg,updateFloatImg,closeImg;

    public static final String FB_STORAGE_PATH = "image/";
    public static final int REQUEST_CODE = 1234;
    private Uri imgUri;
    FirebaseAuth auth;
    Dialog loadDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        auth = FirebaseAuth.getInstance();

        mainTxt = (TextView)findViewById(R.id.editProfile_mainTxt);
        nameTxt = (TextView)findViewById(R.id.editProfile_nameTxt);
        phoneTxt = (TextView)findViewById(R.id.editProfile_phoneTxt);
        profileImgTxt = (TextView)findViewById(R.id.editProfile_profileImageTxt);
        uploadImgTxt = (TextView)findViewById(R.id.editProfile_uploadImageTxt);
        nameEdit = (EditText) findViewById(R.id.editProfile_nameEdit);
        phoneEdit = (EditText) findViewById(R.id.editProfile_phoneEdit);
        uploadImg = (ImageView) findViewById(R.id.editProfile_uploadImg);
        updateFloatImg = (ImageView)findViewById(R.id.editProfile_floatingNext);
        closeImg = (ImageView)findViewById(R.id.editProfile_closeImg);

        Typeface myCustomFont_montserrat_bold = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Bold.ttf");
        Typeface myCustomFont_montserrat_regular = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Regular.ttf");
        mainTxt.setTypeface(myCustomFont_montserrat_bold);
        nameTxt.setTypeface(myCustomFont_montserrat_regular);
        phoneTxt.setTypeface(myCustomFont_montserrat_regular);
        profileImgTxt.setTypeface(myCustomFont_montserrat_regular);
        uploadImgTxt.setTypeface(myCustomFont_montserrat_regular);

        loadDialog = new Dialog(EditProfileActivity.this);
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
        loadDialog.show();

        setInitialData();

        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBrowse_Click();
            }
        });
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        updateFloatImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameEdit.getText().toString().isEmpty()||nameEdit.getText().toString().equals("")){
                    Toast.makeText(EditProfileActivity.this, "Enter name", Toast.LENGTH_SHORT).show();
                    return;
                }else if (phoneEdit.getText().toString().isEmpty()||phoneEdit.getText().toString().equals("")){
                    Toast.makeText(EditProfileActivity.this, "Enter phone number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (imgUri == null){
                    updateFloatImg.setEnabled(false);
                    updateProfileWithoutImageMethod();          //User have not changed the profile pic
                }else {
                    updateProfileWithImageMethod();             //User have changed the profile pic, update it too
                }
            }
        });
    }

    public void updateProfileWithImageMethod(){

        loadDialog.show();

        StorageReference mStorageRef;
        mStorageRef = FirebaseStorage.getInstance().getReference();
        final String fileChild = FirebaseAuth.getInstance().getCurrentUser().getUid()+FB_STORAGE_PATH + System.currentTimeMillis() + "." + getImageExt(imgUri);
        StorageReference ref = mStorageRef.child(fileChild);
        ref.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                HashMap hsm = new HashMap();
                hsm.put(Configs.name,nameEdit.getText().toString());
                hsm.put(Configs.phone,phoneEdit.getText().toString());
                hsm.put(Configs.profile_image,fileChild);
                try {
                    DatabaseReference dbChang = FirebaseDatabase.getInstance().getReference();
                    dbChang.child(Configs.users)
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .updateChildren(hsm).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(EditProfileActivity.this, "Profile Updated!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            updateFloatImg.setEnabled(true);
                            Toast.makeText(EditProfileActivity.this, e+"", Toast.LENGTH_SHORT).show();
                            loadDialog.dismiss();
                        }
                    });
                }catch (Exception e){
                    updateFloatImg.setEnabled(true);
                    loadDialog.dismiss();
                    Toast.makeText(EditProfileActivity.this, e+"", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                updateFloatImg.setEnabled(true);
                loadDialog.dismiss();
                Toast.makeText(getApplicationContext(), e.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void updateProfileWithoutImageMethod(){

        loadDialog.show();

        DatabaseReference dbChang = FirebaseDatabase.getInstance().getReference();

        HashMap hsm = new HashMap();
        hsm.put(Configs.name,nameEdit.getText().toString());
        hsm.put(Configs.phone,phoneEdit.getText().toString());
        dbChang.child(Configs.users)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .updateChildren(hsm).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(EditProfileActivity.this, "Profile Updated!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                updateFloatImg.setEnabled(true);
                Toast.makeText(EditProfileActivity.this, e+"", Toast.LENGTH_SHORT).show();
                loadDialog.dismiss();
            }
        });

    }

    public void setInitialData(){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        DatabaseReference data = db
                .child(Configs.users)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nameEdit.setText(dataSnapshot.child(Configs.name).getValue(String.class));
                phoneEdit.setText(dataSnapshot.child(Configs.phone).getValue(String.class));
                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                storageRef.child(dataSnapshot.child(Configs.profile_image).getValue(String.class)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(EditProfileActivity.this).load(uri).into(uploadImg);
                        loadDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(EditProfileActivity.this, exception+"", Toast.LENGTH_SHORT).show();
                        loadDialog.dismiss();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EditProfileActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                loadDialog.dismiss();
            }
        });
    }

    public void btnBrowse_Click(){
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null){
            imgUri = data.getData();

            try {
                Bitmap bmp = ImagePicker.getImageFromResult(this, resultCode, data);//your compressed bitmap here
                uploadImg.setImageBitmap(bmp);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public String getImageExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

}