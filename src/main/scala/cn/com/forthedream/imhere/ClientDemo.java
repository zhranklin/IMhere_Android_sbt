package cn.com.forthedream.imhere;

import android.util.Log;
import cn.com.forthedream.util.Client;
import cn.com.forthedream.util.Models;
import scala.Tuple2;
import scala.concurrent.Future;

public class ClientDemo {
  public static void test() {
    final String cd = "clientdemo";
    Client client = new Client("sht", "pass");
    Future<Tuple2<Object, Models.Place>> placeResp = client.getPlace("001");
    Client.handleFuture(placeResp, new Client.ResponseHandler<Models.Place>() {
      @Override
      public void succ(Tuple2<Integer, Models.Place> t) {
        Log.e(cd, "the place is: " + t._2());
      }

      @Override
      public void fail(Throwable e) {
        Log.e(cd, "error", e);
      }
    });
  }
}
