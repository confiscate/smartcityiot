package com.flowy.flowy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by henrylau on 11/21/15.
 */
public class RemainNoticeActivity extends AppCompatActivity {
    public final static String REMAIN_MESSAGE = "REMAIN_SIZE";
    private String tank_size = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remain_notice_activity);

        Intent intent = getIntent();
        tank_size = intent.getStringExtra(MainActivity.TANK_MESSAGE);
    }

    public void nextClick(View view) {
        Intent intent = new Intent(this, UsedActivity.class);
        intent.putExtra(MainActivity.TANK_MESSAGE, tank_size);

        EditText editText = (EditText) findViewById(R.id.remainingSizeField);
        intent.putExtra(REMAIN_MESSAGE, editText.getText().toString());

        startActivity(intent);
    }

}
