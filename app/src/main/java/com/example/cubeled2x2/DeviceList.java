package com.example.cubeled2x2;

import android.content.Intent;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DeviceList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final List<Object> deviceList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textAddress;
        LinearLayout linearLayout;

        public ViewHolder(View v) {
            super(v);
            textName = v.findViewById(R.id.DeviceName);
            textAddress = v.findViewById(R.id.DeviceAddress);
            linearLayout = v.findViewById(R.id.linearDeviceInfo);
        }
    }

    public DeviceList(Context context, List<Object> deviceList) {
        this.context = context;
        this.deviceList = deviceList;

    }

    //Храним данные о девайсах Bluetooth с помощью ViewHolder

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_info, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ViewHolder itemHolder = (ViewHolder) holder;
        final DeviceInfo deviceInfo = (DeviceInfo) deviceList.get(position);
        itemHolder.textName.setText(deviceInfo.getDeviceName());
        itemHolder.textAddress.setText(deviceInfo.getDeviceHardwareAddress());

        // Когда девайс выбран, отправляем его на главный экран приложения

        itemHolder.linearLayout.setOnClickListener(view -> {
            Intent intent = new Intent(context, MainScreen.class);
            intent.putExtra("deviceName", deviceInfo.getDeviceName());
            intent.putExtra("deviceAddress", deviceInfo.getDeviceHardwareAddress());
            context.startActivity(intent);

        });
    }

    //Получить размер списка устройств
    @Override
    public int getItemCount() {
        int dataCount = deviceList.size();
        return dataCount;
    }
}