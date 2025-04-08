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
import com.example.myapplication.model.UserModel;

public class ForgetPasswordActivity extends AppCompatActivity {
    EditText edtAccount, edtEmail;
    Button btnConfirm, btnCancel;
    UserDB userDB; // Declare userDB object

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        userDB = new UserDB(ForgetPasswordActivity.this); // Initialize the database helper
        edtAccount = findViewById(R.id.edtAccount);
        edtEmail = findViewById(R.id.edtEmail);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnCancel = findViewById(R.id.btnCancel);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = edtAccount.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();

                if (TextUtils.isEmpty(account)) {
                    edtAccount.setError("Account cannot be empty");
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    edtEmail.setError("Email cannot be empty");
                    return;
                }

                UserModel infoUser = userDB.getInfoUser(account, email, 1);
                if (infoUser != null && infoUser.getUsername() != null && infoUser.getEmail() != null) {
                    // Success: User exists
                    // Add logic here, such as sending a password reset email or showing a message
                    Intent intentPassword = new Intent(ForgetPasswordActivity.this, UpdatePasswordActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("ID_ACCOUNT_USER", infoUser.getId());
                    intentPassword.putExtras(bundle);
                    startActivity(intentPassword);
                } else {
                    // Failure: User not found
                    Toast.makeText(ForgetPasswordActivity.this,"Invalid Account", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtAccount.setText("");
                edtEmail.setText("");
                Intent intent = new Intent(ForgetPasswordActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });
    }
}
