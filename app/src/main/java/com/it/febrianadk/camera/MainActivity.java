package com.it.febrianadk.camera;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

Button button,button2,button3, button4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button=(Button)findViewById(R.id.button);
        button2=(Button)findViewById(R.id.button2);
        button3=(Button)findViewById(R.id.button3);
        button4=(Button)findViewById(R.id.button4);


        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent m = new Intent(MainActivity.this, MulaiActivity.class);
                startActivity(m);
            }
        });
        button2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent m = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(m);
            }
        });
        button3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent m = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(m);
            }
        });
        button4.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



}
