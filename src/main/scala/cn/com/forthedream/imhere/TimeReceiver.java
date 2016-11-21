package cn.com.forthedream.imhere;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by 晗涛 on 2016/10/5.
 */
public class TimeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, LocationService.class);
        i.putExtra("test","csd");
        Log.d("rec","aaaaaaa");
        context.startService(i);
    }

}
