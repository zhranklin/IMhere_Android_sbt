package cn.com.forthedream.imhere;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

/**
 * Created by 晗涛 on 2016/10/16.
 */
public class NotificationService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //String name =intent.getStringExtra("name");
        String name = "aaa";
        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setContentTitle("有一个新的通知")
                .setContentText(name+"通知发生，具体请到app中查看")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_menu_manage)
                .setDefaults(Notification.DEFAULT_ALL)
                .setTicker("IMHere有新通知");
        Intent it = new Intent(this,MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this,0,it,PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pIntent);
        Notification notification = builder.build();
        NotificationManager manger = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manger.notify(99,notification);
        return super.onStartCommand(intent, flags, startId);
    }
}
