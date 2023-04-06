package com.example.proj;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends Activity {
    Button redButton, greenButton, blueButton, whiteButton, onB, offB, sendB;
    int kol, onf;
    ImageButton btb;
    private BluetoothAdapter bluetooth ;
    private BluetoothDevice device;
    private BluetoothSocket socket;
    int[] marks = new int[4];
    int[] im = new int[]{R.id.im1, R.id.im2, R.id.im3, R.id.im4};


    public Thread thr;

    private static final String DEVICE_ADDRESS = "00:19:08:00:10:52";
    private static final UUID SERVICE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        redButton = findViewById(R.id.button4);
        btb = findViewById(R.id.btb);
        greenButton = findViewById(R.id.button3);
        blueButton = findViewById(R.id.button5);
        whiteButton = findViewById(R.id.button6);
        onB = findViewById(R.id.button);
        sendB = findViewById(R.id.button10);
        offB = findViewById(R.id.button2);
        btb.setOnClickListener(this::onReconnect);
        onB.setOnClickListener(this::onClickB);
        offB.setOnClickListener(this::onClickB);
        sendB.setOnClickListener(this::onFinal);
        redButton.setOnClickListener(this::onClickB);
        greenButton.setOnClickListener(this::onClickB);
        blueButton.setOnClickListener(this::onClickB);
        whiteButton.setOnClickListener(this::onClickB);

        kol = 0;
        String enableBT = BluetoothAdapter.ACTION_REQUEST_ENABLE;
        startActivityForResult(new Intent(enableBT), 0);
        if(socket!=null){
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            socket=null;
        }
        connectt();
    }

    public void onClickB(View v) {
        onf = 0;
        if (v == redButton) {
            marks[kol] = 2;
            ImageView ql = findViewById(im[kol]);
            ql.setImageResource(R.drawable.red);
            if (kol < 3) {
                kol += 1;
            } else {
                kol = 0;
            }
        }

        if (v == greenButton) {
            marks[kol] = 3;
            ImageView ql = findViewById(im[kol]);
            ql.setImageResource(R.drawable.green);
            if (kol < 3) {
                kol += 1;
            } else {
                kol = 0;
            }
        }

        if (v == blueButton) {
            marks[kol] = 4;
            ImageView ql = findViewById(im[kol]);
            ql.setImageResource(R.drawable.blue);
            if (kol < 3) {
                kol += 1;
            } else {
                kol = 0;
            }
        }

        if (v == whiteButton) {
            marks[kol] = 5;
            ImageView ql = findViewById(im[kol]);
            ql.setImageResource(R.drawable.black);
            if (kol < 3) {
                kol += 1;
            } else {
                kol = 0;
            }
        }

        if (v == onB) {
            onf = 1;
        }
        if (v == offB) {
            onf = 0;
        }
    }

    public int kowl = 0;

    public void onFinal(View v) {
        kowl += 1;
        StringBuilder send1 = new StringBuilder(Integer.toString(onf));
        for (int i = 0; i < 3; i += 2) {
            if ((marks[i] == 2 && marks[i + 1] == 3) | (marks[i] == 3 && marks[i + 1] == 2)) {
                send1.append("0");
            }
            if ((marks[i] == 2 && marks[i + 1] == 4) | (marks[i] == 4 && marks[i + 1] == 2)) {
                send1.append("1");
            }
            if ((marks[i] == 2 && marks[i + 1] == 5) | (marks[i] == 5 && marks[i + 1] == 2)) {
                send1.append("2");
            }
            if ((marks[i] == 3 && marks[i + 1] == 4) | (marks[i] == 4 && marks[i + 1] == 3)) {
                send1.append("3");
            }
            if ((marks[i] == 3 && marks[i + 1] == 5) | (marks[i] == 5 && marks[i + 1] == 3)) {
                send1.append("4");
            }
            if ((marks[i] == 4 && marks[i + 1] == 5) | (marks[i] == 5 && marks[i + 1] == 4)) {
                send1.append("5");
            }
        }
        if ((marks[0] == marks[1])) {
            send1 = new StringBuilder();
            send1.append(onf);
            send1.append(marks[0] + 4);
            send1.append(marks[2] + 4);
        }
        int send2 = Integer.parseInt(send1.toString());
        byte bt = (byte) send2;
        sendData(bt);
        System.out.println(send2);
    }

    public void connectt() {
        if (thr != null) {
            thr.interrupt();
            thr = null;
        }
        thr = new Thread(() -> {
            try {
                bluetooth = BluetoothAdapter.getDefaultAdapter();
                device = bluetooth.getRemoteDevice(DEVICE_ADDRESS);
                socket = device.createRfcommSocketToServiceRecord(SERVICE_UUID);
                socket.connect();

                Log.d("BLUETOOTH", "Connected");
            } catch (IOException e) {
                Log.e("BLUETOOTH", "Error connecting to Bluetooth device", e);
                try {
                    if(socket!=null) {
                        socket.close();
                    }
                } catch (IOException ex) {
                    Log.e("BLUETOOTH", "Error closing client socket", ex);
                }
                socket = null;
                // если Bluetooth не подключен, попробуйте подключиться
                new Handler().postDelayed(this::connectt, 2000); // задержка в 2 секунды
            }
        });
        thr.start();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (thr != null) {
            thr.interrupt();
            thr = null;
        }
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                Log.e("BLUETOOTH", "Error closing client socket", e);
            }
            socket = null;
        }
    }


    public void onReconnect(View v) {
        onPause(); // остановить поток и закрыть сокет
        // создать новый поток и подключиться к устройству
        new Handler().postDelayed(this::connectt, 2000); // задержка в 1 секунду
    }

    public void sendData(byte data) {
        try {
            if (socket != null && socket.isConnected()) {
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(data);
                outputStream.flush();
                Log.d("BLUETOOTH", "Data sent: " + data);
            } else {
                Log.d("BLUETOOTH", "Bluetooth is not connected");
                onPause(); // остановить поток и закрыть сокет
                connectt(); // создать новый поток и подключиться к устройству// если Bluetooth не подключен, попробуйте подключиться
            }
        } catch (IOException e) {
            Log.e("BLUETOOTH", "Error sending data over Bluetooth", e);
        }
    }
}
