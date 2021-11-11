package com.fhtechnikum.einheit7ble;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class DetailActivity extends AppCompatActivity {
    private final static String TAG = "bluetoothLog";

    public static final  String DEVICE_KEY = "DEVICE";
    private BluetoothDevice device;
    private String mDeviceAddress;

    private BluetoothLeServive mBluetoothLeService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        device = getIntent().getParcelableExtra(DEVICE_KEY);
    startServiceForDevice(device);

    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBluetoothLeService = ((BluetoothLeServive.LocalBinder) service).getService(); //typecast weil kein IBinder
            if(!mBluetoothLeService.initialize()) {
                Log.e(Scanner_BTLE.TAG, "unable to initialize bt");
                finish();
            }

            mBluetoothLeService.connect(mDeviceAddress);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBluetoothLeService = null;
        }
    };

    private void startServiceForDevice(BluetoothDevice device) {

        mDeviceAddress = device.getAddress();
        Intent gattServiceIntent = new Intent(this, BluetoothLeServive.class);
        Log.d(TAG, "bind Service: "+ bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE));
    }

}