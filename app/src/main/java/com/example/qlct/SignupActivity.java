package com.example.qlct;

import static com.example.qlct.services.AuthManager.showErrorPopup;
import static com.example.qlct.services.AuthManager.showSuccessPopup;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qlct.Database.DataBaseHelper;

public class SignupActivity extends AppCompatActivity {
    private EditText username, email, password, confirmpassword;
    private Button btnDangKy;
    private DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        dbHelper = new DataBaseHelper(this);

        username = findViewById(R.id.edtUsername);
        email = findViewById(R.id.edtEmail);
        password = findViewById(R.id.edtPassword);
        confirmpassword = findViewById(R.id.edtConfirmPassword);
        btnDangKy = findViewById(R.id.btnSignup);

        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = email.getText().toString().trim();
                String Username = username.getText().toString().trim();
                String Password = password.getText().toString().trim();
                String Confirm = confirmpassword.getText().toString().trim();

                if (Email.isEmpty() || Confirm.isEmpty() || Username.isEmpty() || Password.isEmpty()) {
                    showErrorPopup(SignupActivity.this, "Vui lòng nhập đầy đủ thông tin!");
                    focusFirstEmptyField();
                    return;
                }

                if (!Password.equals(Confirm)) {
                    showErrorPopup(SignupActivity.this, "Mật khẩu xác nhận không khớp!");
                    password.setText("");
                    confirmpassword.setText("");
                    password.requestFocus();
                    return;
                }

                if (dbHelper.checkEmailExists(Email)) {
                    showErrorPopup(SignupActivity.this, "Email đã tồn tại! Vui lòng đăng nhập!");
                    email.setText("");
                    email.requestFocus();
                    return;
                }

                boolean success = dbHelper.addUser(Email, Password, Username);
                if (success) {
                    showSuccessPopup(SignupActivity.this, "Đăng ký thành công!");

                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 1500);

                } else {
                    showErrorPopup(SignupActivity.this, "Lỗi khi đăng ký, vui lòng thử lại!");
                }
            }
        });
    }
    private void focusFirstEmptyField() {
        if (email.getText().toString().trim().isEmpty()) {
            email.requestFocus();
        } else if (username.getText().toString().trim().isEmpty()) {
            username.requestFocus();
        } else if (password.getText().toString().trim().isEmpty()) {
            password.requestFocus();
        } else {
            confirmpassword.requestFocus();
        }
    }
}
