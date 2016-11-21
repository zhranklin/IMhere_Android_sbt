package cn.com.forthedream.imhere;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 晗涛 on 2016/10/5.
 */
public class ListViewAdapter extends ArrayAdapter<Item>{
    private int resouceId;
    public ListViewAdapter(Context context, int textViewResourceId, List<Item> objects){
        super(context,textViewResourceId,objects);
        resouceId = textViewResourceId;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Item item = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resouceId,null);
        TextView list_main_title = (TextView)  view.findViewById(R.id.list_main_title);
        TextView list_main_time = (TextView)  view.findViewById(R.id.list_main_time);
        TextView list_main_type = (TextView)  view.findViewById(R.id.list_main_type);
        list_main_time.setText(""+item.getDate().getYear()+"."+item.getDate().getMonth()+"."+item.getDate().getDay()+"  "+item.getDate().getHours()+":"+item.getDate().getMinutes());
        list_main_title.setText(item.getTitle());
        list_main_type.setText(item.getType());
        return view;
    }
}
