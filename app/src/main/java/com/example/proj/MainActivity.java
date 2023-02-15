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
    int kol;
    int onf=2;
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


    }


    //Как раз эта функция и будет вызываться


    public void onClickB(View v) {

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
            onf = 2;
        }
        if (v == offB) {
            onf =1;
        }


    }

    public void onFinal(View v) {
        String send1=Integer.toString(onf);
        for(int i=0;i<3;i+=2) {
            if ((marks[i] == 2 && marks[i + 1] == 3) | (marks[i] == 3 && marks[i + 1] == 2)) {
                send1 += "0";
            }
            if ((marks[i] == 2 && marks[i + 1] == 4) | (marks[i] == 4 && marks[i + 1] == 2)) {
                send1 += "1";
            }
            if ((marks[i] == 2 && marks[i + 1] == 5) | (marks[i] == 5 && marks[i + 1] == 2)) {
                send1 += "2";
            }
            if ((marks[i] == 3 && marks[i + 1] == 4) | (marks[i] == 4 && marks[i + 1] == 3)) {
                send1 += "3";
            }
            if ((marks[i] == 3 && marks[i + 1] == 5) | (marks[i] == 5 && marks[i + 1] == 3)) {
                send1 += "4";
            }
            if ((marks[i] == 4 && marks[i + 1] == 5) | (marks[i] == 5 && marks[i + 1] == 4)) {
                send1 += "5";
            }
        }
        if ((marks[0] == marks[1]) ) {
            send1 ="0";
            send1+=Integer.toString(onf);
            if ((marks[0] == 2 && marks[3] == 3) ) {
                send1 += "0";
            }
            if ((marks[0] == 2 && marks[3] == 4)) {
                send1 += "1";
            }
            if ((marks[0] == 2 && marks[3] == 5)) {
                send1 += "2";
            }
            if ((marks[0] == 3 && marks[3] == 4)) {
                send1 += "3";
            }
            if ((marks[0] == 3 && marks[3] == 5) ) {
                send1 += "4";
               }
            if ((marks[0] == 4 && marks[3] == 5) ) {
                send1 += "5";
            }
            if ((marks[0] == 3 && marks[3] == 2)) {
                send1 += "6";
            }
            if ( (marks[0] == 4 && marks[3] == 2)) {
                send1 += "7";
            }
            if ( (marks[0] == 5 && marks[3] == 2)) {
                send1 += "8";
            }
            if ( (marks[0] == 4 && marks[3] == 3)) {
                send1 += "9";
            }
            if ( (marks[0] == 5 && marks[3] == 3)) {
                send1 ="0";
                send1+=onf+2;
                send1+=1;
            }
            if ( (marks[0] == 5 && marks[3] == 4)) {
                send1 ="0";
                send1+=onf+3;
                send1+=1;
            }


        }
        System.out.println(send1);
    }
}
