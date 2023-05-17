package com.example.cubeled2x2;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DeviceActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    final Handler handler = new Handler();
    private SwipeRefreshLayout SwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_activity);

        SwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        SwipeRefreshLayout.setOnRefreshListener(this);
        handler.postDelayed(this::update, 1000);
    }
    @Override
    public void onBackPressed() {
        handler.removeCallbacksAndMessages(null); // остановить таймер обновления
        Intent intent = new Intent(this, MainScreen.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    // Обновление списка сопряженных блютуз устройств
    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SwipeRefreshLayout.setRefreshing(false);
                updateBT();
            }
        }, 2000);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
    private void update(){

        // Bluetooth настройки

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Получение списка сопряженных устройств

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        List<Object> deviceList = new ArrayList<>();
        if (pairedDevices.size() > 0) {

            // Здесь будут отображаться все сопряженные устройства. Получаем имя и MAC адресс каждого.

            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC адрес
                DeviceInfo deviceInfo = new DeviceInfo(deviceName, deviceHardwareAddress);
                deviceList.add(deviceInfo);
            }

            // Отображение сопряженного устройства с помощью RecyclerView

            RecyclerView recyclerView = findViewById(R.id.DeviceName);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            DeviceList deviceList_1 = new DeviceList(this, deviceList);
            recyclerView.setAdapter(deviceList_1);
            recyclerView.setItemAnimator(new DefaultItemAnimator());


        } else {
            View view = findViewById(R.id.DeviceName);
            Snackbar snackbar = Snackbar.make(view, "Активируйте Bluetooth или добавьте устройство в список сопряженных", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Включаем Bluetooth. Если он уже активен, то игнорируется этот шаг

                    if (ActivityCompat.checkSelfPermission(DeviceActivity.this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    if (bluetoothAdapter.disable()) {
                        Toast.makeText(getApplicationContext(), "Модуль Bluetooth отключен",
                                Toast.LENGTH_LONG).show();
                    }
                    if (!bluetoothAdapter.isEnabled()) {
                        String enableBT = BluetoothAdapter.ACTION_REQUEST_ENABLE;
                        if (ActivityCompat.checkSelfPermission(DeviceActivity.this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            Toast.makeText(getApplicationContext(), "Модуль Bluetooth включен",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }
                        startActivityForResult(new Intent(enableBT), 1);
                    }
                    else {
                        // Устройство не поддерживает Bluetooth
                        Toast.makeText(getApplicationContext(), "Это устройство не поддерживает Bluetooth",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
            snackbar.show();
        }
    }
    private void updateBT(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            handler.postDelayed(() -> {
                finish();
                onBackPressed();
                startActivity(getIntent());
            }, 500);
            recreate();
        }
    }
}
