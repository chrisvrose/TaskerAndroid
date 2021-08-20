package in.aravindweb.tasker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

/**
 * Build a notification and send out new announcements every fifteen minutes
 */
public class NotifJobService extends JobService {
    private String authToken;
    private boolean isTeacher;

    String latestAnDate;

    public NotifJobService() {
    }

    String NOTIFICATION_CHANNEL_ID = "hello_bring";

    @Override
    public boolean onStartJob(JobParameters params) {
        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (this, 0, new Intent(this, MainActivity.class),
                        PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager manager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // if newer then aravind
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_LOW);

            // Configure the notification channel.
            notificationChannel.setDescription("Announcements");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            manager.createNotificationChannel(notificationChannel);
        }

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.tokenLocation), Context.MODE_PRIVATE);
        authToken = sharedPref.getString("token", "-");
        isTeacher = sharedPref.getBoolean("isTeacher", false);
        latestAnDate = sharedPref.getString("notiflatestdate", "");
        AndroidNetworking.get("https://tasker.aravindweb.in/api/rooms").addHeaders("X-Auth-Token", authToken).build().getAsJSONArray(new JSONArrayRequestListener() {
            @Override
            public void onResponse(JSONArray response) {
                int l = response.length();
                LinkedList<String> ids = new LinkedList<>();
                for (int i = 0; i < l; i++) {
                    try {
                        ids.add(response.getJSONObject(i).getString("_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                setLength(l);
                for (int i=0;i<l;i++) {
                    String x = ids.get(i);
                    int finalI = i;
                    AndroidNetworking.get("https://tasker.aravindweb.in/api/rooms/" + x + "/announcements").addHeaders("X-Auth-Token", authToken).build().getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            int l = response.length();
                            for (int i = 0; i < l; i++) {
                                try {
                                    JSONObject item = response.getJSONObject(i);
                                    String id = item.getString("_id");
                                    String date = item.getString("createdAt");
                                    String contents = item.getString("content");


                                    int compare = latestAnDate.compareTo(date);
                                    // if latestdate is smaller, yeet it out
                                    if (compare < 0) {
                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(NotifJobService.this, NOTIFICATION_CHANNEL_ID)
                                                .setContentTitle(("Tasker"))
                                                .setContentText((contents))
                                                .setContentIntent(contentPendingIntent)
                                                .setSmallIcon(R.drawable.ic_assignment_icon)
                                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                                .setDefaults(NotificationCompat.DEFAULT_ALL)
                                                .setAutoCancel(true);
                                        manager.notify(finalI<<2|i, builder.build());

                                        latestAnDate=date;
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            doneLength(finalI,sharedPref);
                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });


                }
            }

            @Override
            public void onError(ANError anError) {

            }
        });
        Log.d("i.run", "ran");
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }
    int reqlength=0;
    void setLength(int reqlength){
        this.reqlength=reqlength-1;
    }
    void doneLength(int doneLength, SharedPreferences sharedPref){
        if(doneLength==reqlength){
            sharedPref.edit().putString("notiflatestdate",latestAnDate).commit();
            Toast.makeText(this,"Done notifs"+doneLength+"."+reqlength+" "+latestAnDate,Toast.LENGTH_SHORT).show();

            // loop is over, now set the editor
        }
    }

}