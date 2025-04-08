package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LogoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        // Bắt sự kiện khi người dùng chạm vào màn hình
        View mainLayout = findViewById(R.id.logo);
        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến màn hình đăng nhập
                Intent intent = new Intent(LogoActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
