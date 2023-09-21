package com.geminichild.medicinereminder;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.geminichild.medicinereminder.dashboardfragments.AlarmsFragment;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent alarmNotification = new Intent(context, NotiificationContainer.class);

        String head = intent.getStringExtra("headline");
        String descr = intent.getStringExtra("description");
        String request = intent.getStringExtra("codeid");
        alarmNotification.putExtra("headline", head);
        alarmNotification.putExtra("desc", descr);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, Integer.parseInt(request), alarmNotification, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "geminichild")
                .setSmallIcon(R.drawable.baseline_notifications_active_24)
                .setContentTitle(head)
                .setContentText(descr)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.notification_1);
        mediaPlayer.start();

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        notificationManagerCompat.notify(123, builder.build());
    }
}
