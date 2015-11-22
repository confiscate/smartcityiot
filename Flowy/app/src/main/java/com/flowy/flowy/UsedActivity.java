package com.flowy.flowy;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by henrylau on 11/21/15.
 */
public class UsedActivity extends AppCompatActivity {
    private String URL = "http://ooflo.ngrok.com/status";
    private String remaining_notice = "";
    private String tank_size = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.used_activity);

        Intent intent = getIntent();
        tank_size = intent.getStringExtra(MainActivity.TANK_MESSAGE);
        remaining_notice = intent.getStringExtra(RemainNoticeActivity.REMAIN_MESSAGE);


        System.out.println("tank size: " + tank_size);

        float used = 0.25f;

        TextView leftOver = (TextView) findViewById(R.id.setUsedText);
        leftOver.setText("Used: " + (Integer.parseInt(tank_size) * used) + " gal");

        ProgressBar progress = (ProgressBar) findViewById(R.id.used_progress);
        progress.setProgress((int) ((1 - used) * 100));

        sendNotification(tank_size);

        new RequestTask().execute(URL);
    }

    private void sendNotification(String gasLeft) {
        Intent intent = new Intent(this, UsedActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.gas)
                .setContentTitle("OOflo")
                .setContentText("Alert! You have " + gasLeft + " gal left!")
                .setSound(defaultSoundUri)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }


    public void refillTank(View view) {
        TextView usedText = (TextView) findViewById(R.id.setUsedText);
        usedText.setText("Used: " + "0" + " gal");

        ProgressBar progress = (ProgressBar) findViewById(R.id.used_progress);
        progress.setProgress(100);

        sendNotification(tank_size);
    }

}
