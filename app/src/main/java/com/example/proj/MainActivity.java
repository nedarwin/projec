package com.example.proj;

import android.annotation.SuppressLint;
import android.os.Bundle;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

public class MainActivity extends Activity  {

    //Экземпляры классов наших кнопок
    Button redButton, greenButton, blueButton, whiteButton, onB, offB, noB,sendB;
    int kol,onf;
    int[] marks = new int[4];
    int[] im = new int[]{R.id.im1, R.id.im2, R.id.im3, R.id.im4};
    //Сокет, с помощью которого мы будем отправлять данные на Arduino
    BluetoothSocket clientSocket;
    OutputStream outStream;
    //Эта функция запускается автоматически при запуске приложения
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        redButton = findViewById(R.id.button4);
        noB = findViewById(R.id.button9);
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
        noB.setOnClickListener(this::onClickB);
        kol = 0;

        String enableBT = BluetoothAdapter.ACTION_REQUEST_ENABLE;

        startActivityForResult(new Intent(enableBT), 0);

        //Мы хотим использовать тот bluetooth-адаптер, который задается по умолчанию
        BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();

        //Пытаемся проделать эти действия
        try {
            //Устройство с данным адресом - наш Bluetooth Bee
            //Адрес опредеяется следующим образом: установите соединение
            //между ПК и модулем (пин: 1234), а затем посмотрите в настройках
            //соединения адрес модуля. Скорее всего он будет аналогичным.
            BluetoothDevice device = bluetooth.getRemoteDevice("00:19:08:00:10:52");

            //Инициируем соединение с устройством
            Method m = device.getClass().getMethod(
                    "createRfcommSocket", new Class[]{int.class});

            clientSocket = (BluetoothSocket) m.invoke(device, 1);
            clientSocket.connect();
            outStream = clientSocket.getOutputStream();

            //В случае появления любых ошибок, выводим в лог сообщение
        } catch (IOException e) {
            Log.d("BLUETOOTH", e.getMessage());
        } catch (SecurityException e) {
            Log.d("BLUETOOTH", e.getMessage());
        } catch (NoSuchMethodException e) {
            Log.d("BLUETOOTH", e.getMessage());
        } catch (IllegalArgumentException e) {
            Log.d("BLUETOOTH", e.getMessage());
        } catch (IllegalAccessException e) {
            Log.d("BLUETOOTH", e.getMessage());
        } catch (InvocationTargetException e) {
            Log.d("BLUETOOTH", e.getMessage());
        }

        //Выводим сообщение об успешном подключении
        Toast.makeText(getApplicationContext(), "CONNECTED", Toast.LENGTH_LONG).show();

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
                kol = 1;
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
            ql.setImageResource(R.drawable.white);
            if (kol < 3) {
                kol += 1;
            } else {
                kol = 0;
            }

        }
        if (v == noB) {
            marks[kol] = 6;
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

    public void onFinal(View v) {
        String send1=Integer.toString(onf);
        for (int i = 0; i < 3; i++) {
            send1+=Integer.toString(marks[i]);
        }
        Integer send2=Integer.parseInt(send1);
        try {
            outStream.write(send2);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
