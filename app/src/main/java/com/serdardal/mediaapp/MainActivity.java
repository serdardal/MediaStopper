package com.serdardal.mediaapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {
    EditText editText;
    GestureDetectorCompat gestureDetectorCompat;
    static int time;
    int lastValue;
    int lastDeltaY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.minutes);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });

        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(event.getAction() == KeyEvent.ACTION_DOWN){
                    if(keyCode == KeyEvent.KEYCODE_ENTER){
                        startService();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                        View view = getCurrentFocus();
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        return true;
                    }
                }

                return false;
            }
        });

        gestureDetectorCompat = new GestureDetectorCompat(this,this);
        lastValue=0;
        lastDeltaY=0;

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetectorCompat.onTouchEvent(event);

        if(event.getAction() == MotionEvent.ACTION_DOWN){
            if(editText.getText().toString().isEmpty()){
                editText.setText("0");
            }
        }
        if(event.getAction()== MotionEvent.ACTION_UP){

            lastValue=Integer.parseInt(editText.getText().toString());

        }

        return super.onTouchEvent(event);
    }


    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        float deltaY = e1.getY()- e2.getY();
        int currentDeltaY =(int) deltaY/100;

        if (currentDeltaY != lastDeltaY){
            int newValue = lastValue + currentDeltaY*5;

            if(newValue >= 0){
                editText.setText(""+ newValue);
            }
            else{
                editText.setText("0");
            }

            lastDeltaY = currentDeltaY;

        }


        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    public void startService(){
        if(editText.getText().toString().isEmpty()){
            Toast.makeText(this,"Can not be empty!",Toast.LENGTH_SHORT).show();
        }
        else{
            time = Integer.parseInt(editText.getText().toString());
            if(time >= 0 && time<=1440){
                Intent intent = new Intent(this,MyService.class);
                startService(intent);
            }
            else{
                Toast.makeText(this,"It must be between 0-1440!",Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Start the  service
    public void startNewService(View view) {

        startService();

    }

    // Stop the  service
    public void stopNewService(View view) {

        stopService(new Intent(this, MyService.class));
    }


}
