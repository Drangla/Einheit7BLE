package com.fhtechnikum.einheit7ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;


public class Scanner_BTLE {

    private MainActivity ma;
    final static String TAG = "bluetoothLog";
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBluetoothLeScanner;
    private Handler mHandler;
    private boolean mScanning;
    private  long scanPeriod;

    private final static int REQUEST_ENABLE_BT = 1;

    public Scanner_BTLE(MainActivity ma, long scanPeriod) {
        this.ma = ma;
        this.scanPeriod = scanPeriod;
        mHandler = new Handler();

        final BluetoothManager bluetoothManager = (BluetoothManager) ma.getSystemService(Context.BLUETOOTH_SERVICE);

        mBluetoothAdapter = bluetoothManager.getAdapter();

        if(mBluetoothAdapter == null || !mBluetoothAdapter.enable()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            ma.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();


    }

    public void start() {
        scanLeDevice(true);

    }
    public void stop() {
        scanLeDevice(false);

    }

    private void scanLeDevice(final boolean enable) {
        if(enable && !mScanning) {

            mHandler.postDelayed(()-> {
                mScanning = false;
                mBluetoothLeScanner.stopScan(mLeScanCallback);
                stop();
            }, scanPeriod);

            mScanning = true;

            mBluetoothLeScanner.startScan(mLeScanCallback);

        } else {
            mBluetoothLeScanner.stopScan(mLeScanCallback);
            mScanning = false;
        }
    }
    private ScanCallback mLeScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);

            Log.d(TAG, "device found");
            if(result.getDevice().getName() != null || result.getDevice().getAddress() != null) {
                mHandler.post(() ->{
                    ma.addToList(result.getDevice());
                });
            }

        }
    };
}
