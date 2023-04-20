package com.example.cubeled2x2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private SharedPreferences preferences;

    final Handler handler = new Handler();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authorization);

        username = findViewById(R.id.edit_user);
        password = findViewById(R.id.edit_password);
        Button login = findViewById(R.id.button_login);
        TextView loginLocked = findViewById(R.id.login_locked);
        preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        // Получение данных пользователя из SharedPreferences

        String savedUsername = preferences.getString("username", "");
        String savedPassword = preferences.getString("password", "");

        // Проверка и автоматическая авторизация, если данные пользователя сохранены

        if (!savedUsername.equals("") && !savedPassword.equals("")) {
            login(savedUsername, savedPassword);
        }

        // Если введенные логин ("user") и пароль ("pass") будут введены правильно,
        // показываем Toast сообщение об успешном входе:

        login.setOnClickListener(v -> {
            String enteredUsername = username.getText().toString();
            String enteredPassword = password.getText().toString();
            if (enteredUsername.equals("user") && enteredPassword.equals("pass")) {
                preferences.edit().putString("username", enteredUsername).apply();
                preferences.edit().putString("password", enteredPassword).apply();
            }

            else {

                //Если данные неверные, показываем пользователю ошибку

                Toast.makeText(this, "Неверные логин или пароль", Toast.LENGTH_SHORT).show();
                loginLocked.setVisibility(View.VISIBLE);
                loginLocked.setBackgroundColor(Color.RED);
                loginLocked.setText("Вход заблокирован! Попробуйте еще раз!");
            }
        });
    }

    public void login(String savedUsername, String savedPassword) {
        Intent intent = new Intent(this, MainScreen.class);
        startActivity(intent);
        finish();
    }
}