package com.it.febrianadk.camera;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Button button5;
        button5=(Button)findViewById(R.id.button5);
        Toolbar t=(Toolbar)findViewById(R.id.toolbar);

        button5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent m = new Intent(AboutActivity.this, MainActivity.class);
                startActivity(m);
            }
        });


    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent back = new Intent(AboutActivity.this,MainActivity.class);
        startActivity(back);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
