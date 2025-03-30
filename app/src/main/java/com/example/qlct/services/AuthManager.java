package com.example.qlct.services;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.example.qlct.Database.DataBaseHelper;
import com.example.qlct.Database.User;
import com.example.qlct.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class AuthManager {
    public interface AuthCallback {
        void onSuccess(String userId, String username);
        void onError(String errorMessage);
    }

    public static void login(Context context, String email, String password, AuthCallback callback) {
        DataBaseHelper dbHelper = new DataBaseHelper(context);
        User user = dbHelper.checkUser(email, password);

        if (user == null) {
            showErrorPopup(context, "Sai email hoặc mật khẩu!");
            callback.onError("Sai email hoặc mật khẩu!");
        } else {
            showSuccessPopup(context, "Đăng nhập thành công!");
            callback.onSuccess(String.valueOf(user.getId()), user.getUsername());
        }
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    public static void showErrorPopup(Context context, String message) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_error, null);

        TextView tvMessage = view.findViewById(R.id.tvMessage);
        tvMessage.setText(message);

        new MaterialAlertDialogBuilder(context)
                .setView(view)
                .show();
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    public static void showSuccessPopup(Context context, String message) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_success, null);

        TextView tvMessage = view.findViewById(R.id.tvMessage);
        tvMessage.setText(message);

        new MaterialAlertDialogBuilder(context)
                .setView(view)
                .show();
    }
}
