package com.it.febrianadk.camera;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Toolbar t=(Toolbar)findViewById(R.id.toolbar);
        Button buttonBack;
        buttonBack=(Button)findViewById(R.id.buttonBack);

        buttonBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent m = new Intent(HelpActivity.this, MainActivity.class);
                startActivity(m);
            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent back = new Intent(HelpActivity.this,MainActivity.class);
        startActivity(back);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

}
