package com.example.tiendavirtual;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

public class Perfil_User_Activity extends AppCompatActivity {
    Button btn_user_view;
    ImageView imageView_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_user);

        btn_user_view = findViewById(R.id.btn_cuenta);
        imageView_user = findViewById(R.id.imageView_yo);

        btn_user_view.setTextColor(Color.parseColor("#F85F6F")); //cambia color de letra
        imageView_user.setColorFilter(Color.parseColor("#F85F6F")); //cambia color de imagen
    }
}