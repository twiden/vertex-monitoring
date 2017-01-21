package com.twiden.vertxmonitoring;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    public final static String CONNECT_STR = "com.twiden.vertxmonitoring.CONNECT_STR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void connectBackend(View view) {
        Intent intent = new Intent(this, DisplayServicesActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(CONNECT_STR, message);
        startActivity(intent);
    }
}
