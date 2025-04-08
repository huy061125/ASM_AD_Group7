package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SettingFragment extends Fragment {

    public SettingFragment() {
        // Constructor mặc định
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        // Lấy tham chiếu đến nút Logout
        TextView logoutButton = view.findViewById(R.id.logout_button);

        // Xử lý sự kiện khi nhấn Logout
        logoutButton.setOnClickListener(v -> logoutUser());

        return view;
    }

    private void logoutUser() {
        // Chuyển đến màn hình đăng nhập
        Intent intent = new Intent(getActivity(), SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Xóa ngăn xếp activity trước đó
        startActivity(intent);
        getActivity().finish(); // Kết thúc activity hiện tại
    }
}
