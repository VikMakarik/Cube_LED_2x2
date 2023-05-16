package com.example.cubeled2x2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private SharedPreferences preferences;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authorization);

        username = findViewById(R.id.edit_user_text);
        password = findViewById(R.id.edit_password_text);
        Button login = findViewById(R.id.button_login);
        preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        // Получение данных пользователя из SharedPreferences

        String savedUsername = preferences.getString("username", "");
        String savedPassword = preferences.getString("password", "");

        // Проверка и автоматическая авторизация, если данные пользователя сохранены

        if (!savedUsername.equals("") && !savedPassword.equals("")) {
//            login(savedUsername, savedPassword);
        }

        // Если введенные логин ("makarik") и пароль ("10023") будут введены правильно,
        // показываем Toast сообщение об успешном входе:

        login.setOnClickListener(v -> {
            String enteredUsername = username.getText().toString();
            String enteredPassword = password.getText().toString();
            if (enteredUsername.equals("makarik") && enteredPassword.equals("10023")) {
                preferences.edit().putString("username", enteredUsername).apply();
                preferences.edit().putString("password", enteredPassword).apply();
                Toast.makeText(this, "Вход выполнен успешно", Toast.LENGTH_SHORT).show();
            }

            else {

                //Если данные неверные, показываем пользователю ошибку

                Toast.makeText(this, "Неверный логин или пароль", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    public void login(String savedUsername, String savedPassword) {
//        Intent intent = new Intent(this, MainScreen.class);
//        startActivity(intent);
//        finish();
//    }
}