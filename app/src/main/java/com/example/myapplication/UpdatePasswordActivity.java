package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.database.UserDB;

public class UpdatePasswordActivity extends AppCompatActivity {
    EditText edtNewPassword, edtConfirmPassword;
    Button btnSaveChange;

    Intent intent;
    Bundle bundle;
    private int idUser = 0;
    UserDB userDB;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmNewPassword);
        btnSaveChange = findViewById(R.id.btnSaveChangePassword);
        intent = getIntent();
        bundle = intent.getExtras();

        if (bundle != null){
            idUser = bundle.getInt("ID_ACCOUNT_USER", 0);
        }
        userDB = new UserDB(UpdatePasswordActivity.this);
        btnSaveChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = edtNewPassword.getText().toString().trim();
                if (TextUtils.isEmpty(password)){
                    edtNewPassword.setError("New password can not empty");
                    return;
                }
                String confirmPassword = edtConfirmPassword.getText().toString().trim();
                if (TextUtils.isEmpty(confirmPassword)){
                    edtConfirmPassword.setError("Confirm password can not empty");
                }
                if (!confirmPassword.equals(password)){
                    edtConfirmPassword.setError("New confirm password is not same password");
                    return;
                }
                int update = userDB.updateAccountPassword(idUser, password);
                if (update == -1){
                    // fail
                    Toast.makeText(UpdatePasswordActivity.this, "Please, Error try again", Toast.LENGTH_SHORT).show();
                } else {
                    // success
                    Toast.makeText(UpdatePasswordActivity.this, "Update password success", Toast.LENGTH_SHORT).show();
                    Intent login = new Intent(UpdatePasswordActivity.this, SignInActivity.class);
                    startActivity(login);
                    finish();
                }
            }
        });
    }
}
