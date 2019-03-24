package com.kreator.sameer.louie;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class LoginSignupActivity extends AppCompatActivity {

    TextView loginSelectBtn,signupSelectBtn;
    RelativeLayout loginScript,signupScript;
    ImageView loginBtn,signupBtn;
    ImageView signUploadImg;

    TextView signNameTxt,signEmailTxt,signPasswordTxt,signProfileImgTxt,signUploadImgTxt;
    EditText signNameEdit,signEmailEdit,signPasswordEdit;

    TextView loginEmailTxt,loginPasswordTxt;
    EditText loginEmailEdit,loginPasswordEdit;

    public static final String FB_STORAGE_PATH = "image/";
    public static final int REQUEST_CODE = 1234;
    private Uri imgUri;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);
        auth = FirebaseAuth.getInstance();

        loginSelectBtn = (TextView)findViewById(R.id.loginSign_loginTxt);
        signupSelectBtn = (TextView)findViewById(R.id.loginSign_signupTxt);
        loginScript = (RelativeLayout)findViewById(R.id.loginSign_loginScript);
        signupScript = (RelativeLayout)findViewById(R.id.loginSign_signupScript);

        //sign-up views
        signNameTxt = (TextView)findViewById(R.id.loginSign_sign_nameTxt);
        signEmailTxt = (TextView)findViewById(R.id.loginSign_sign_emailTxt);
        signPasswordTxt = (TextView)findViewById(R.id.loginSign_sign_passwordTxt);
        signProfileImgTxt = (TextView)findViewById(R.id.loginSign_sign_profileImageTxt);
        signUploadImgTxt = (TextView)findViewById(R.id.loginSign_sign_uploadImageTxt);
        signNameEdit = (EditText)findViewById(R.id.loginSign_sign_nameEdit);
        signEmailEdit = (EditText)findViewById(R.id.loginSign_sign_emailEdit);
        signPasswordEdit = (EditText)findViewById(R.id.loginSign_sign_passwordEdit);
        signupBtn = (ImageView)findViewById(R.id.loginSign_sign_floatingNext);
        signUploadImg = (ImageView)findViewById(R.id.loginSign_sign_uploadImg);

        //login views
        loginEmailTxt = (TextView)findViewById(R.id.loginSign_login_emailTxt);
        loginPasswordTxt = (TextView)findViewById(R.id.loginSign_login_passwordTxt);
        loginEmailEdit = (EditText)findViewById(R.id.loginSign_login_emailEdit);
        loginPasswordEdit = (EditText)findViewById(R.id.loginSign_login_passwordEdit);
        loginBtn = (ImageView)findViewById(R.id.loginSign_login_floatingNext);

        Typeface myCustomFont_montserrat_regular = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Regular.ttf");
        loginSelectBtn.setTypeface(myCustomFont_montserrat_regular);
        signupSelectBtn.setTypeface(myCustomFont_montserrat_regular);

        signNameTxt.setTypeface(myCustomFont_montserrat_regular);
        signEmailTxt.setTypeface(myCustomFont_montserrat_regular);
        signPasswordTxt.setTypeface(myCustomFont_montserrat_regular);
        signNameEdit.setTypeface(myCustomFont_montserrat_regular);
        signEmailEdit.setTypeface(myCustomFont_montserrat_regular);
        signPasswordEdit.setTypeface(myCustomFont_montserrat_regular);
        signProfileImgTxt.setTypeface(myCustomFont_montserrat_regular);
        signUploadImgTxt.setTypeface(myCustomFont_montserrat_regular);

        loginEmailTxt.setTypeface(myCustomFont_montserrat_regular);
        loginPasswordTxt.setTypeface(myCustomFont_montserrat_regular);
        loginEmailEdit.setTypeface(myCustomFont_montserrat_regular);
        loginPasswordEdit.setTypeface(myCustomFont_montserrat_regular);

        loginSelectBtn.setBackground(getDrawable(R.drawable.login_selected));
        signupSelectBtn.setBackground(getDrawable(R.drawable.signup_unselected));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            loginSelectBtn.setTextColor(getColor(R.color.loginSignupSelectedColor));
            signupSelectBtn.setTextColor(getColor(R.color.white_color));
        }
        loginScript.setVisibility(View.VISIBLE);
        signupScript.setVisibility(View.GONE);

        loginSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginSelected();
            }
        });
        signupSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupSelected();
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupUser();
            }
        });
        signUploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBrowse_Click();
            }
        });

    }

    public void loginSelected(){
        loginSelectBtn.setBackground(getDrawable(R.drawable.login_selected));
        signupSelectBtn.setBackground(getDrawable(R.drawable.signup_unselected));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            loginSelectBtn.setTextColor(getColor(R.color.loginSignupSelectedColor));
            signupSelectBtn.setTextColor(getColor(R.color.white_color));
        }
        loginScript.setVisibility(View.VISIBLE);
        signupScript.setVisibility(View.GONE);
    }

    public void signupSelected(){
        loginSelectBtn.setBackground(getDrawable(R.drawable.login_unselected));
        signupSelectBtn.setBackground(getDrawable(R.drawable.signup_selected));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            loginSelectBtn.setTextColor(getColor(R.color.white_color));
            signupSelectBtn.setTextColor(getColor(R.color.loginSignupSelectedColor));
        }
        loginScript.setVisibility(View.GONE);
        signupScript.setVisibility(View.VISIBLE);
    }

    public void loginUser(){
        if (loginEmailEdit.getText().toString().isEmpty()||loginEmailEdit.getText().toString().equals("")){
            Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
            return;
        }else if (loginPasswordEdit.getText().toString().isEmpty()||loginPasswordEdit.getText().toString().equals("")){
            Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
            return;
        }else{
            final Dialog loadDialog = new Dialog(LoginSignupActivity.this);
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
            animSelect = (LottieAnimationView)loadDialog.findViewById(R.id.loading_one);
            animSelect.setAnimation("blueline.json");
            animSelect.playAnimation();
            animSelect.loop(true);

            Window window = loadDialog.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            loadDialog.show();

            loginBtn.setEnabled(false);
            signupBtn.setEnabled(false);

            auth.signInWithEmailAndPassword(loginEmailEdit.getText().toString(), loginPasswordEdit.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {

                }
            }).addOnCompleteListener(LoginSignupActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()){
                        loadDialog.dismiss();
                        Toast.makeText(LoginSignupActivity.this, "email/password incorrect or check connectivity", Toast.LENGTH_SHORT).show();
                        loginBtn.setEnabled(true);
                        signupBtn.setEnabled(true);
                    }else{
                        loadDialog.dismiss();
                        startActivity(new Intent(LoginSignupActivity.this,SplashActivity.class));
                        finish();
                    }
                }
            });
        }
    }

    public void signupUser(){
        if (signNameEdit.getText().toString().isEmpty()||signNameEdit.getText().toString().equals("")){
            Toast.makeText(this, "Enter name", Toast.LENGTH_SHORT).show();
            return;
        }else if (signEmailEdit.getText().toString().isEmpty()||signEmailEdit.getText().toString().equals("")){
            Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
            return;
        }else if (signPasswordEdit.getText().toString().isEmpty()||signPasswordEdit.getText().toString().equals("")){
            Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
            return;
        }else if (signPasswordEdit.getText().toString().length() < 8){
            Toast.makeText(this, "Password too small", Toast.LENGTH_SHORT).show();
            return;
        }else if (signPasswordEdit.getText().toString().length() > 32){
            Toast.makeText(this, "Password too long! Enter between 8 to 32 letters", Toast.LENGTH_SHORT).show();
            return;
        }else if (imgUri==null){
            Toast.makeText(this, "Select profile image", Toast.LENGTH_SHORT).show();
            return;
        }else{
            btnUpload_click();
        }
    }

    public void btnUpload_click(){

        final Dialog loadDialog = new Dialog(LoginSignupActivity.this);
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
        animSelect = (LottieAnimationView)loadDialog.findViewById(R.id.loading_one);
        animSelect.setAnimation("blueline.json");
        animSelect.playAnimation();
        animSelect.loop(true);

        Window window = loadDialog.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        loadDialog.show();

        loginBtn.setEnabled(false);
        signupBtn.setEnabled(false);

        String email = signEmailEdit.getText().toString();
        String password = signPasswordEdit.getText().toString();

        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {

                        if (!task.isSuccessful()){

                            try{
                                throw task.getException();
                            }catch (FirebaseAuthWeakPasswordException weak){
                                Toast.makeText(LoginSignupActivity.this, "Password too weak", Toast.LENGTH_SHORT).show();
                                loginBtn.setEnabled(true);
                                signupBtn.setEnabled(true);
                                return;
                            }catch (FirebaseAuthInvalidCredentialsException malw){
                                Toast.makeText(LoginSignupActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                                loginBtn.setEnabled(true);
                                signupBtn.setEnabled(true);
                                return;
                            }catch (FirebaseAuthUserCollisionException col){
                                Toast.makeText(LoginSignupActivity.this, "Email already registered!", Toast.LENGTH_SHORT).show();
                                Toast.makeText(LoginSignupActivity.this, "Try Signing In", Toast.LENGTH_SHORT).show();
                                loginBtn.setEnabled(true);
                                signupBtn.setEnabled(true);
                                return;
                            }catch (Exception e){
                                Toast.makeText(LoginSignupActivity.this, e+"", Toast.LENGTH_SHORT).show();
                                loginBtn.setEnabled(true);
                                signupBtn.setEnabled(true);
                                return;
                            }
                        }else {
                            StorageReference mStorageRef;
                            mStorageRef = FirebaseStorage.getInstance().getReference();
                            StorageReference ref = mStorageRef.child(FB_STORAGE_PATH + System.currentTimeMillis() + "." + getImageExt(imgUri));
                            ref.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    HashMap s = new HashMap();
                                    s.put(Configs.name,signNameEdit.getText().toString().trim());
                                    s.put(Configs.email,signEmailEdit.getText().toString().trim());
                                    s.put(Configs.email_verified,false);
                                    s.put(Configs.profile_image,taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                                    s.put(Configs.uid,FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    try {
                                        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                                        db.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(s).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(LoginSignupActivity.this, "Signup Successful", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(LoginSignupActivity.this, AccountSetupActivity.class));
                                                    finish();
                                                } else {
                                                    loginBtn.setEnabled(true);
                                                    signupBtn.setEnabled(true);
                                                    Toast.makeText(LoginSignupActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(LoginSignupActivity.this, e + "", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }catch (Exception e){
                                        Toast.makeText(LoginSignupActivity.this, e+"", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    loadDialog.dismiss();
                                    loginBtn.setEnabled(true);
                                    signupBtn.setEnabled(true);
                                    Toast.makeText(getApplicationContext(), e.getMessage() + "", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadDialog.dismiss();
                loginBtn.setEnabled(true);
                signupBtn.setEnabled(true);
                Toast.makeText(LoginSignupActivity.this, e+"", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void btnBrowse_Click(){
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Select image"), REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null){
            imgUri = data.getData();

            try {
                Bitmap bmp = ImagePicker.getImageFromResult(this, resultCode, data);//your compressed bitmap here
                signUploadImg.setImageBitmap(bmp);
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
