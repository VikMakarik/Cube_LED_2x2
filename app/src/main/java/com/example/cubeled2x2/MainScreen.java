package com.example.cubeled2x2;

import static android.content.ContentValues.TAG;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import com.google.android.material.snackbar.Snackbar;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class MainScreen extends AppCompatActivity {
    private String deviceName = null;
    private int btnpotent = 0;
    public static Handler handler;
    public static BluetoothSocket mmSocket;
    //public static ConnectedThread connectedThread;
    @SuppressLint("StaticFieldLeak")
    //public static CreateConnectThread createConnectThread;

    private final static int CONNECTING_STATUS = 1; // используется в обработчике Bluetooth для определения статуса сообщения

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}