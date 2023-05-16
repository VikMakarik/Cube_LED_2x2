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
    public static ConnectedThread connectedThread;
    @SuppressLint("StaticFieldLeak")
    public static CreateConnectThread createConnectThread;

    private final static int CONNECTING_STATUS = 1; // используется в обработчике Bluetooth для определения статуса сообщения

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UI переменные
        final Button buttonConnect = findViewById(R.id.buttonConnect);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        final ImageView imageView = findViewById(R.id.imageView);
        imageView.setBackgroundColor(Color.parseColor("#002429"));
        final TextView textViewModeWorking = findViewById(R.id.ModeWorking);

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //Включаем Bluetooth. Если он уже активен, то игнорируется этот шаг
        if (!bluetoothAdapter.isEnabled()) {
            imageView.setImageResource(R.drawable.baseline_bluetooth_connected_24);
            imageView.setColorFilter(Color.RED);
            Toast.makeText(getApplicationContext(), "Модуль Bluetooth отключен",
                    Toast.LENGTH_LONG).show();
        }
//        else if(bluetoothAdapter == null){
//            // Устройство не поддерживает Bluetooth
//            imageView.setImageResource(R.drawable.baseline_bluetooth_connected_24);
//            Toast.makeText(getApplicationContext(), "Это устройство не поддерживает Bluetooth",
//                    Toast.LENGTH_LONG).show();
//        }
        else if (bluetoothAdapter.isEnabled()){
            imageView.setImageResource(R.drawable.baseline_bluetooth_connected_24);
            imageView.setColorFilter(Color.parseColor("#00BCD4"));
            Toast.makeText(getApplicationContext(), "Модуль Bluetooth включен",
                    Toast.LENGTH_LONG).show();
        }

        // Ссылаемся на выбор устройства

        deviceName = getIntent().getStringExtra("deviceName");
        if (deviceName != null) {

            // Получение MAC адреса устройства BT

            String deviceAddress = getIntent().getStringExtra("deviceAddress");

            // Получение прогреса о соединении

            toolbar.setSubtitle("Подключение к: " + deviceName + "...");
            progressBar.setVisibility(View.VISIBLE);
            buttonConnect.setEnabled(false);

            /*
            Когда будет найдено "имя устройства" строчки ниже вызывают новый поток для создания соединения Bluetooth с выбранным устройством
             */
            createConnectThread = new CreateConnectThread(bluetoothAdapter, deviceAddress, getApplicationContext());
            createConnectThread.start();
        }

        /*
        Немного магии Handler, не спрашивайте как это работает
         */
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == CONNECTING_STATUS) {
                    switch (msg.arg1) {
                        case 1:
                            toolbar.setSubtitle("Подключено к: " + deviceName);
                            progressBar.setVisibility(View.GONE);
                            buttonConnect.setEnabled(true);
                            imageView.setImageResource(R.drawable.baseline_bluetooth_connected_24);
                            break;
                        case -1:
                            toolbar.setSubtitle("Сбой подключения");
                            progressBar.setVisibility(View.GONE);
                            buttonConnect.setEnabled(true);
                            imageView.setImageResource(R.drawable.baseline_bluetooth_connected_24);
                            imageView.setColorFilter(Color.RED);
                            break;
                    }
                }
            }
        };

        // Выбор устройства

//        buttonConnect.setOnClickListener(view -> {
//
//            // Помешаем Адаптер в список
//
//            Intent intent = new Intent(MainScreen.this, SelectDeviceActivity.class);
//            startActivity(intent);
//        });

         // Управление режимами работы ленты

//        buttonToggle.setOnClickListener(view -> {
//            String cmdText = null;
//            String btnState = buttonToggle.getText().toString().toLowerCase(); //получение текста с кнопки маленькими буквами
//            textViewModeWorking.setTextColor(Color.WHITE);
//            switch (btnState) {
//
//                //можно добавлять команд сколько угодно, главное помнить про задержку при передаче сигнала по Bluetooth и скорость работы проца ардуины
//
//                case "режим №1":
//                    buttonToggle.setText("Режим №2");
//                    textViewModeWorking.setText("Режим №2 - Плавная бегущая радуга");
//                    cmdText = "1";
//                    buttonToggle.setEnabled(false);
//                    break;
//                case "режим №2":
//                    buttonToggle.setText("Режим №3");
//                    textViewModeWorking.setText("Режим №3 - Бегущие частоты 2.0");
//                    cmdText = "7";
//                    buttonToggle.setEnabled(false);
//                    break;
//                case "режим №3":
//                    buttonToggle.setText("Режим №4");
//                    textViewModeWorking.setText("Режим №4 - Анализатор спектра");
//                    cmdText = "8";
//                    buttonToggle.setEnabled(false);
//                    break;
//                case "режим №4":
//                    buttonToggle.setText("Режим №1");
//                    textViewModeWorking.setText("Режим №1 - Бегущие частоты");
//                    cmdText = "0";
//                    buttonToggle.setEnabled(false);
//                    break;
//            }
//            // Отправка команды Ардуине и задержка включения кнопки, чтобы Ардуино успела обработать команды
//
//            connectedThread.write(cmdText);
//            new Handler().postDelayed(() -> buttonToggle.setEnabled(true),3000);
//        });

    }


    /* ============================ Поток для создания Bluetooth соединения =================================== */
    public static class CreateConnectThread extends Thread {
        private final Context context;

        public CreateConnectThread(BluetoothAdapter bluetoothAdapter, String address, Context context) {

            /* Создание временного объекта сокета */

            this.context = context;
            BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(address);
            BluetoothSocket tmp = null;
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                 simplify int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            UUID uuid = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                uuid = bluetoothDevice.getUuids()[0].getUuid();
            }

            try {
                /*
                Пробуем получить BluetoothSocket для подключения к данному устройству (BluetoothDevice).
                В зависимости от различных моделей девайсов и версий андроид, методы могут различаться,
                можно попробовать что-то из этого, если стандартные методы не работают (документация гугла):

                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
                 */

                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                 simplify int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                tmp = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuid);

            } catch (IOException e) {
                Log.e(TAG, "Сокет создан, метод выдал ошибку", e);
            }
            mmSocket = tmp;
        }

        public void run() {

            // Отмена обнаружения.

            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            bluetoothAdapter.cancelDiscovery();
            try {
                /* Подключение к девайсу. Дальнейшая блокировка Сокета, Иначе выкинуть ошибку */

                mmSocket.connect();
                Log.e("Status", "Устройство подключено");
                handler.obtainMessage(CONNECTING_STATUS, 1, -1).sendToTarget();
            } catch (IOException connectException) {
                // Неудачное подключение, закрывает сокет и возвращается
                try {
                    mmSocket.close();
                    Log.e("Status", "Не удается подключиться к устройству");
                    handler.obtainMessage(CONNECTING_STATUS, -1, -1).sendToTarget();
                } catch (IOException closeException) {
                    Log.e(TAG, "Не возможно закрыть сокет", closeException);
                }
                return;
            }

            /* Попытка подключения удалась. Выполнить работу, связанную с соединением в отдельном потоке. */
            connectedThread = new ConnectedThread(mmSocket);
            connectedThread.start();
        }

        // Окончательное закрытие сокета и дальнейшая работа с подключенным девайсом

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Не возможно закрыть сокет", e);
            }
        }
    }

    /* =============================== Поток обработки данных =========================================== */
    public static class ConnectedThread extends Thread {
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Получить входные и выходные потоки, используя временные объекты, потому что
            // потоки участников являются окончательными

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException ignored) { }

            InputStream mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }
        /* =============================== Использовалось про отладке работы с ардуино, в основной программе это не нужно, работает без этого =========================================== */

//        public void run() {
//            byte[] buffer = new byte[1024];  // Буфер потока
//            int bytes; // Вернутые байты для прочтения
//            // Продолжайте прослушивать входной поток до тех пор, пока не возникнет исключение
//            while (true) {
//                try {
//                    /*
//                    Считывайте из входного потока из Arduino до тех пор, пока не будет достигнут символ завершения.
//                    Затем отправьте целое строковое сообщение обработчику.
//                     */
//                    bytes = mmInStream.read(buffer);
//                    buffer[bytes] = (byte) mmInStream.read();
//                    String readMessage;
//                    if (buffer[bytes] == '\n'){
//                        readMessage = new String(buffer,0,bytes);
//                        Log.e("Сообщение от Arduino",readMessage);
//                        handler.obtainMessage(MESSAGE_READ,readMessage).sendToTarget();
//                        bytes = 0;
//                    } else {
//                        bytes += 1;
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    break;
//                }
//            }
//        }

        /* Отправка данных для работы с подключенным устройством*/
        public void write(String input) {
            if (input != null) { //условие на пустоту конвертации байтов
                byte[] bytes = input.getBytes(); //конвертация массива
                try {
                    mmOutStream.write(bytes);
                } catch (IOException e) {
                    Log.e("Ошибка отправки","Невозможно отправить сообщение",e);
                }
            }

        }

        /* Завершение соединения */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException ignored) { }
        }
    }

    /* =============================== Использовалось про отладке работы с ардуино, в основной программе это не нужно, работает без этого =========================================== */
//    /* ============================ Прерывание соединения ====================== */
//    @Override
//    public void onBackPressed() {
//        // Прервать соединение и закрыть приложение
//        if (createConnectThread != null){
//            createConnectThread.cancel();
//        }
//        Intent a = new Intent(Intent.ACTION_MAIN);
//        a.addCategory(Intent.CATEGORY_HOME);
//        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(a);
//    }
}