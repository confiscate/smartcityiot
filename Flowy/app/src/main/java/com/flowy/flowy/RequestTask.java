package com.flowy.flowy;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.HttpStatus;
import cz.msebera.android.httpclient.StatusLine;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

/**
 * Created by henrylau on 11/21/15.
 */
public class RequestTask extends AsyncTask<String, String, String> {
    private ProgressBar bar;
    private TextView left;
    private String tank_size;
    private String remaining_notice;
    private UsedActivity usedActivity;

    public RequestTask(UsedActivity activity, String remaining_notice, String tank_size, ProgressBar bar, TextView left) {
        this.bar = bar;
        this.left = left;
        this.tank_size = tank_size;
        this.remaining_notice = remaining_notice;
        this.usedActivity = activity;
    }

    @Override
    protected String doInBackground(String... uri) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        try {
            System.out.println("sending to " + uri[0]);
            response = httpclient.execute(new HttpGet(uri[0]));
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                responseString = out.toString();
                out.close();

                System.out.println(responseString);

                int gasLeft = Integer.parseInt(responseString);
                if (UsedActivity.gasPercentLeft != gasLeft && gasLeft < Integer.parseInt(remaining_notice)) {
                    this.usedActivity.sendNotification(responseString);
                }
                UsedActivity.gasPercentLeft = gasLeft;
            } else {
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (IOException e) {
            //TODO Handle problems..
        }
        try {
            Thread.sleep(2 * 1000);                 //1000 milliseconds is one second.
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        return uri[0];
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        //Do anything with response..

        float used = (float) (100 - UsedActivity.gasPercentLeft) / 100.0f * 1.0f;
        System.out.println("New used: " + used);
        String galLeft = String.format("%.2f", Integer.parseInt(tank_size) * used);
        left.setText("Used: " + galLeft + " gal");
        bar.setProgress((int) ((1 - used) * 100));
        new RequestTask(this.usedActivity, this.remaining_notice, this.tank_size, this.bar, this.left).execute(result);
    }
}
