package sg.rp.edu.rp.c346.id22038845.demoweather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<Weather> {

    Context parent_content;
    int layout_id;
    ArrayList<Weather> al;

    public CustomAdapter(Context context, int resource, ArrayList<Weather> objects){
        super(context, resource, objects);

        parent_content = context;
        layout_id = resource;
        al = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater)
                parent_content.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(layout_id,parent,false);

        TextView tv = rowView.findViewById(R.id.tv);
        ImageView iv = rowView.findViewById(R.id.iv);

        Weather currentWeather = al.get(position);

        tv.setText(currentWeather.toString());
        if(currentWeather.getForecast().contains("Clear") || currentWeather.getForecast().contains("clear")){
            Picasso.get().load("https://img.icons8.com/?size=512&id=8LM7-CYX4BPD&format=png").into(iv);
        } else if(currentWeather.getForecast().contains("Cloudy") || currentWeather.getForecast().contains("cloudy") ){
            Picasso.get().load("https://img.icons8.com/?size=2x&id=rhba9Nt5N4jA&format=png").into(iv);
        }else if(currentWeather.getForecast().contains("Thunder")||currentWeather.getForecast().contains("thunder")){
            Picasso.get().load("https://img.icons8.com/?size=512&id=ziNIfsFS8p_p&format=png").into(iv);
        }else if(currentWeather.getForecast().contains("Rain")||currentWeather.getForecast().contains("rain")||currentWeather.getForecast().contains("drizzle")){
            Picasso.get().load("https://img.icons8.com/?size=2x&id=ulJA5JddHJKv&format=png").into(iv);
        }

        return rowView;
    }

}
