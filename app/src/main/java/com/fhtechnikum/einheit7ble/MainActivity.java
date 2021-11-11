package com.fhtechnikum.einheit7ble;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView list;
    private List<BluetoothDevice> bluetoothDevices;
    private  ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = findViewById(R.id.rv_list);
        Scanner_BTLE myScanner = new Scanner_BTLE(MainActivity.this, 10000);
        myScanner.start();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);
        list.setHasFixedSize(true);
        bluetoothDevices = new ArrayList<>();

        listAdapter = new ListAdapter(bluetoothDevices);
        list.setAdapter(listAdapter);

        listAdapter.setOnListItemClickListener((item)-> {
            myScanner.stop();
            Intent i = new Intent(MainActivity.this, DetailActivity.class);
            i.putExtra(DetailActivity.DEVICE_KEY, item);
            startActivity(i);
        });




    }

    public void addToList(BluetoothDevice bluetoothDevice){
        if(!bluetoothDevices.contains(bluetoothDevice)){
            bluetoothDevices.add(bluetoothDevice);
            listAdapter.notifyDataSetChanged();
        }

    }
}