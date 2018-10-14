package pe.com.lycsoftware.cibertecproject;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationManagerCompat;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;

import pe.com.lycsoftware.cibertecproject.model.Task;
import pe.com.lycsoftware.cibertecproject.util.Constants;

public class AlarmReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getBundleExtra("bundle");
        Task task = bundle.getParcelable(Constants.TASK_PARAM);
        pe.com.lycsoftware.cibertecproject.model.Notification notif = bundle.getParcelable(Constants.NOTIFICATION_PARAM);
        //final Task task = intent.getParcelableExtra(Constants.TASK_PARAM);
        //pe.com.lycsoftware.cibertecproject.model.Notification notif = intent.getParcelableExtra(Constants.NOTIFICATION_PARAM);
        Intent notificationIntent = new Intent(context, TaskDetailActivity.class);
        notificationIntent.putExtra(Constants.TASK_PARAM, task);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(new Intent(context, MenuActivity.class));
        //stackBuilder.addParentStack(MenuActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(context);

        Notification notification = builder.setContentTitle(task.getName())
                .setContentText(DateTimeComparator.getDateOnlyInstance().compare(task.getTaskDate(), DateTime.now()) == 0
                        ? Constants.getTimeFormatter().print(task.getTaskTimeStart())
                        : Constants.getDateTimeFormatter().print(task.getTaskTimeStart()))
                //.setTicker()
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setDefaults(Notification.DEFAULT_ALL)
                //.setPriority(Notification.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent).build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(Constants.CHANNEL_ID);
        }

        //NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(context);
        }

        notificationManager.notify(notif.getNotificationDate().getMillisOfDay(), notification);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannel(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(new NotificationChannel(
                Constants.CHANNEL_ID, Constants.CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT));
    }
}