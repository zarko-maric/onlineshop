package zarko.maric.onlineshop;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import zarko.maric.onlineshop.MainActivity;
import zarko.maric.onlineshop.R;

public class SaleService extends Service {

    private static final String CHANNEL_ID = "onlineshop";
    public static final String change_sale = "zarko.maric.onlineshop.change_sale";
    public static boolean isSale = false;
    public static boolean started = false;
    private static final int saleDuration = 10;
    private static final int rescheduleTime = 15;
    private static final int NOTIFICATION_ID = 101;
    private Handler handler = new Handler();

    public SaleService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        started = true;
    }

    private static String formatTime(long time) {
        Date futureDate = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        sdf.setTimeZone(TimeZone.getDefault());

        return sdf.format(futureDate);
    }

    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sendNotification("Sale started", "Ongoing sale",rescheduleTime*1000);


        isSale = true;
        sendRefreshSignal();
        getApplicationContext().sendBroadcast(new Intent(change_sale));
        Log.d("sale", "sale started");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000L * saleDuration);
                    isSale = false;
                    sendNotification("Sale over", "Next sale at " + formatTime(System.currentTimeMillis() + rescheduleTime*1000),0);
                    scheduleNext(System.currentTimeMillis() + rescheduleTime * 1000);
                    getApplicationContext().sendBroadcast(new Intent(change_sale));
                    sendRefreshSignal();
                    Log.d("sale", "sale over");
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @SuppressLint("NewApi")
    private void scheduleNext(long time) {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, SaleService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
    }

    @SuppressLint("MissingPermission")
    private void sendNotification(String title, String text,long time) {
        Intent intent;

        intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingLogin = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.notification_icon)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingLogin)
                .setUsesChronometer(false)
                .setShowWhen(true)
                .setAutoCancel(true);
                if(time > 0) {
                builder.setUsesChronometer(true)
                            .setChronometerCountDown(true)
                            .setUsesChronometer(true)
                            .setShowWhen(true)
                            .setWhen(System.currentTimeMillis() + saleDuration * 1000L);
                }

        Notification notification = builder.build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID, notification);

    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "onlineshop";
            String description = "onlineshop";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendRefreshSignal() {
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(change_sale));
    }
}