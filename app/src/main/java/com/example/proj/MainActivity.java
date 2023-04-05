package com.example.proj;

import android.annotation.SuppressLint;
import android.os.Bundle;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

public class MainActivity extends Activity  {
    public Thread bluetoothThread;
    //Экземпляры классов наших кнопок
    Button redButton, greenButton, blueButton, whiteButton, onB, offB, noB,sendB;
    int kol,onf;
    ImageButton btb ;
    int[] marks = new int[4];
    public BluetoothAdapter bluetooth;
    int[] im = new int[]{R.id.im1, R.id.im2, R.id.im3, R.id.im4};
    //Сокет, с помощью которого мы будем отправлять данные на Arduino
    public BluetoothSocket clientSocket;


    //Эта функция запускается автоматически при запуске приложения
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        redButton = findViewById(R.id.button4);
        btb = findViewById(R.id.btb);
        btb.setOnClickListener(this::OnReconnect);
        greenButton = findViewById(R.id.button3);
        blueButton = findViewById(R.id.button5);
        whiteButton = findViewById(R.id.button6);
        onB = findViewById(R.id.button);
        sendB=findViewById(R.id.button10);
        offB = findViewById(R.id.button2);
        onB.setOnClickListener(this::onClickB);
        offB.setOnClickListener(this::onClickB);
        //Добавлем "слушатель нажатий" к кнопке
        sendB.setOnClickListener(this::onFinal);
        redButton.setOnClickListener(this::onClickB);
        greenButton.setOnClickListener(this::onClickB);
        blueButton.setOnClickListener(this::onClickB);
        whiteButton.setOnClickListener(this::onClickB);

        kol = 0;

        String enableBT = BluetoothAdapter.ACTION_REQUEST_ENABLE;

        startActivityForResult(new Intent(enableBT), 0);

        //Мы хотим использовать тот bluetooth-адаптер, который задается по умолчанию
        connectt();

    }


    //Как раз эта функция и будет вызываться


    public void onClickB(View v) {

        //Пытаемся послать данные
        //Получаем выходной поток для передачи данных



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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (clientSocket != null) {
                clientSocket.close();

            }
        } catch (IOException e) {
            Log.e("BLUETOOTH", "Error closing client socket", e);
        }
    }

int kowl=0;
    public void onFinal(View v) {
        kowl+=1;
        StringBuilder send1= new StringBuilder(Integer.toString(onf));
        for(int i=0;i<3;i+=2) {
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
        if ((marks[0] == marks[1])){
            send1 = new StringBuilder();
            send1.append(Integer.toString(onf));
            send1.append(Integer.toString(marks[0] + 4));
            send1.append(Integer.toString(marks[2] + 4));
        }
        int send2=Integer.parseInt(send1.toString());
        Byte bt = (byte) send2;
        sendData(bt);
        System.out.println(send2);


    }





    public void connectt() {
        new Thread(() -> {
            try {
                BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
                BluetoothDevice device = bluetooth.getRemoteDevice("00:19:08:00:10:52");
                UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
                BluetoothSocket socket = device.createRfcommSocketToServiceRecord(MY_UUID);
                socket.connect();
                clientSocket = socket;

                Log.d("BLUETOOTH", "Connected");
            } catch (IOException e) {
                Log.e("BLUETOOTH", "Error connecting to Bluetooth device", e);
            }
        }).start();
    }

    public void OnReconnect(View v) {

    }

    public void sendData(byte data) {
        try {
            if (clientSocket != null && clientSocket.isConnected()) {
                OutputStream outputStream = clientSocket.getOutputStream();
                outputStream.write(data);
                outputStream.flush();
                Log.d("BLUETOOTH", "Data sent: " + data);
            } else {
                Log.d("BLUETOOTH", "Bluetooth is not connected");
            }
        }
        catch (IOException e) {
            Log.d("BLUETOOTH", e.getMessage());
        }
    }



}
