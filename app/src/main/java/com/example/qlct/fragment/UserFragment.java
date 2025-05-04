package com.example.qlct.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.qlct.Database.UserSession;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.qlct.R;
import com.example.qlct.Database.User;

public class UserFragment extends Fragment {


    public UserFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user, container, false);

        TextView textUsername = view.findViewById(R.id.txtUsername);
        TextView textEmail = view.findViewById(R.id.txtEmail);

        textUsername.setText(UserSession.username);
        textEmail.setText(UserSession.userEmail);

        return view;
    }
}