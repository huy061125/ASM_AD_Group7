package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.database.UserDB;
import com.example.myapplication.model.UserModel;

import java.io.FileInputStream;

public class SignInActivity extends AppCompatActivity {
    EditText editUsername, editPassword;
    Button btnlogin;
    TextView tvRegister;
    UserDB userDB;
    TextView tvForgetPassword;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        userDB = new UserDB(SignInActivity.this);
        tvForgetPassword = findViewById(R.id.tvForgetPassword);
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        btnlogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intForgetPassword = new Intent(SignInActivity.this, ForgetPasswordActivity.class);
                startActivity(intForgetPassword);
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        LoginWithDatabaseUser();
    }
    private void LoginWithDatabaseUser(){
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = editUsername.getText().toString().trim();
                String password = editPassword.getText().toString().trim();
                if (TextUtils.isEmpty(user)){
                    editUsername.setError("Username not empty");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    editPassword.setError("Password not empty");
                    return;
                }
                UserModel userData = userDB.getInfoUser(user, password,0);
                assert userData != null;
                if (userData.getUsername()!=null){
                    //login success
                    Intent intent = new Intent(SignInActivity.this, DashboardActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("USER_ID", userData.getId());
                    bundle.putString("USERNAME_ACCOUNT", userData.getUsername());
                    bundle.putString("USER_EMAIL", userData.getEmail());
                    bundle.putString("USER_PHONE", userData.getPhone());
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                } else {
                    //login fail
                    Toast.makeText(SignInActivity.this, "Account Invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loginWithDataInternalFile(){
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = editUsername.getText().toString().trim();
                String password = editPassword.getText().toString().trim();
                if(TextUtils.isEmpty(user)||TextUtils.isEmpty(password)){
                    Toast.makeText(SignInActivity.this, "Username and Password is not empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                //xu ly lay du lieu tu trong file de kiem tra dang nhap
                try {
                    FileInputStream fileInputStream = openFileInput("account.txt");
                    int read = -1;
                    StringBuilder builder = new StringBuilder();
                    while ((read = fileInputStream.read())!= -1){
                        builder.append((char) read);
                    }
                    fileInputStream.close();
                    String[] infoAccount = null;
                    infoAccount = builder.toString().trim().split("\n");//convert string to array
                    //mang chua toan bo tai khoan da dang ky
                    boolean checkAccount = false;
                    int sizeArray = infoAccount.length;
                    for (int i = 0; i < sizeArray; i++){
                        String username = infoAccount[i].substring(0,infoAccount[i].indexOf("|"));
                        String pass = infoAccount[i].substring(infoAccount[i].indexOf("|")+1);
                        if (username.equals(user) && pass.equals(password)){
                            //login success
                            checkAccount = true;
                            break;
                        }
                    }
                    if (checkAccount){
                        //login thanh cong, chuyen vao ung dung chinh
                        Intent intent = new Intent(SignInActivity.this, DashboardActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("USERNAME_ACCOUNT", user);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    } else {
                        //dang nhap that bai
                        Toast.makeText(SignInActivity.this, "Invalid Account", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e){
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
