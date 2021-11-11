package com.fhtechnikum.einheit7ble;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.nio.charset.Charset;

public class BluetoothLeServive extends Service {

    private final static String TAG = BluetoothLeServive.class.getSimpleName();

    private BluetoothManager mBluetoothmanager;
    private BluetoothAdapter mBluetoothadapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private int mConnectionstate = STATE_DISCONNECTED;

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;


    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            //super.onConnectionStateChange(gatt, status, newState);
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                mBluetoothGatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.i(TAG, "disconnected from GATT server");
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            for (BluetoothGattService s : gatt.getServices()) {
                Log.d(TAG, "\n\nSERVICE: " + s.getUuid().toString());

                for(BluetoothGattCharacteristic c: s.getCharacteristics()){
                    Log.d(TAG, "Characteristic: " + c.getUuid().toString());

                    mBluetoothGatt.readCharacteristic(c);
                    Log.d(TAG, "\n\n");

                }

                Log.d(TAG, "SERVICE OVER\n");
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
           // super.onCharacteristicRead(gatt, characteristic, status);
            byte[] data = characteristic.getValue();
            Log.d(TAG, "result: " + new String(data, Charset.forName("UTF-8")));

        }
    };





    public BluetoothLeServive() {
    }

    public class LocalBinder extends Binder {
        BluetoothLeServive getService(){
            return BluetoothLeServive.this;
        }
    }

    private final IBinder mBinder= new LocalBinder();


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
    return mBinder;
    }

    public boolean initialize() {
        if (mBluetoothmanager == null) {
            mBluetoothmanager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothmanager == null) {
                Log.e(TAG, "unable to initialize Bluetoothmanager");
                return false;
            }
        }
        mBluetoothadapter = mBluetoothmanager.getAdapter();
        if (mBluetoothadapter == null) {
            Log.e(TAG, "unable to obtain Bluetoothadapter");
            return  false;
        }
        return true;
    }

    public boolean connect(final String address){
        if(mBluetoothadapter == null || address == null) {
            return false;
        }

        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress) && mBluetoothGatt != null){

            if(mBluetoothGatt.connect()){
                mConnectionstate = STATE_CONNECTED;
                return true;

            } else {
                return false;
            }
        }
        final BluetoothDevice device = mBluetoothadapter.getRemoteDevice(address);

        if (device == null) {
            return false;
        }

        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        mBluetoothDeviceAddress = address;
        mConnectionstate = STATE_CONNECTED;
        return true;

    }
}