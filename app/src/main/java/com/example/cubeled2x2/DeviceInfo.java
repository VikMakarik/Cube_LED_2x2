package com.example.cubeled2x2;

public class DeviceInfo {
    private final String deviceName;
    private final String deviceHardwareAddress;

    public DeviceInfo(String deviceName, String deviceHardwareAddress){
        this.deviceName = deviceName;
        this.deviceHardwareAddress = deviceHardwareAddress;
    }

    public String getDeviceName(){return deviceName;}

    public String getDeviceHardwareAddress()
    {
        return deviceHardwareAddress;
    }
}
