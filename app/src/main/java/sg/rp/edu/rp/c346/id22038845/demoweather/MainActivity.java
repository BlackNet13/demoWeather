package sg.rp.edu.rp.c346.id22038845.demoweather;

import static java.lang.System.load;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide; /*https://guides.codepath.com/android/Displaying-Images-with-the-Glide-Library*/
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {

    ListView lv;
    AsyncHttpClient client; //allows the app to recieve and read JSON data
    ArrayList<Weather> alWeather;
    CustomAdapter aaWeather;

    TextView tv1,tv2,tv3;
    ImageView iV1;

    EditText edLoc;
    Button btnLoc;

    ArrayList<String> location; //https://stackoverflow.com/questions/48784202/save-an-array-to-sharedpreferences-and-show-it-in-listview
    SharedPreferences prefs;

    SharedPreferences.Editor prefEdit;

    Set<String> set;

    RecyclerView rv;
    ArrayList<String> countryList;
    ArrayList<String> weatherList;
    ArrayList<String> tempList;
    LinearLayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edLoc = findViewById(R.id.edLoc);
        btnLoc = findViewById(R.id.btnLoc);

        tv1 = findViewById(R.id.tV1);
        tv2 = findViewById(R.id.tV2);
        tv3 = findViewById(R.id.tV3);
        iV1 = findViewById(R.id.iV1);
        rv = findViewById(R.id.rv);

        lv = findViewById(R.id.lv);
        client = new AsyncHttpClient();
        alWeather = new ArrayList<Weather>(); // empty arrayList of weather objects
        aaWeather = new CustomAdapter(MainActivity.this, R.layout.row, alWeather);
        lv.setAdapter(aaWeather);

        countryList = new ArrayList<>();
        countryList.add("Singapore");
        countryList.add("Malaysia");
        countryList.add("Japan");

    //https://youtu.be/Zj9ZE6_HtEo?feature=shared

       weatherList = new ArrayList<>();
        tempList = new ArrayList<>();

       /* for(int x = 0; x<countryList.size(); x++){
            final int index = x;
            client.get("https://wttr.in/" + countryList.get(x).toString() + "?format=j1", new JsonHttpResponseHandler() {
                String forecast;
                String temp;

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                    try {
                        JSONArray jsonArrItems1 = response.getJSONArray("nearest_area");
                        JSONArray jsonArrItems2 = response.getJSONArray("current_condition");
                        JSONObject firstObj1 = jsonArrItems1.getJSONObject(0);
                        JSONObject firstObj2 = jsonArrItems2.getJSONObject(0);

                        temp = firstObj2.getString("temp_C");
                        JSONArray jsonArrForecasts1 = firstObj1.getJSONArray("country");
                        JSONArray jsonArrForecasts2 = firstObj2.getJSONArray("weatherDesc");

                        JSONObject jsonObjForecast1 = jsonArrForecasts1.getJSONObject(0);
                        JSONObject jsonObjForecast2 = jsonArrForecasts2.getJSONObject(0);
                        forecast = jsonObjForecast2.getString("value");

                    } catch (JSONException e) {
                    }

                    tempList.set(index,temp + "°C");
                    weatherList.set(index,forecast);

                }
            });
        }*/






        layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);



        prefs = getSharedPreferences("arrayStorage",MODE_PRIVATE);
        location = new ArrayList<>();
        RvAdapter rvAdapter = new RvAdapter(countryList);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(rvAdapter);

        btnLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                location.add(edLoc.getText().toString());

                prefEdit = prefs.edit();
                set.clear();
                set.addAll(location);
                prefEdit.putStringSet("arrayStorage",set);

                prefEdit.apply();

                fetchWeather();


            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Weather selectedWeather = (Weather) parent.getItemAtPosition(position);

                // Extract information from the selected Weather object
                String locationItem = selectedWeather.getArea();

                client.get("https://wttr.in/" + locationItem + "?format=j1", new JsonHttpResponseHandler() {
                    String area;
                    String forecast;
                    String temp;

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                        try {
                            JSONArray jsonArrItems1 = response.getJSONArray("nearest_area");
                            JSONArray jsonArrItems2 = response.getJSONArray("current_condition");
                            JSONObject firstObj1 = jsonArrItems1.getJSONObject(0);
                            JSONObject firstObj2 = jsonArrItems2.getJSONObject(0);

                            temp = firstObj2.getString("temp_C");
                            JSONArray jsonArrForecasts1 = firstObj1.getJSONArray("country");
                            JSONArray jsonArrForecasts2 = firstObj2.getJSONArray("weatherDesc");

                            JSONObject jsonObjForecast1 = jsonArrForecasts1.getJSONObject(0);
                            JSONObject jsonObjForecast2 = jsonArrForecasts2.getJSONObject(0);
                            area = jsonObjForecast1.getString("value");
                            forecast = jsonObjForecast2.getString("value");

                        } catch (JSONException e) {
                        }

                        tv1.setText(area);

                        tv2.setText(temp + "°C");
                        tv3.setText(forecast);

                        String currentWeather = forecast;

                        if(currentWeather.contains("Clear") || currentWeather.contains("clear")){
                            Picasso.get().load("https://img.icons8.com/?size=512&id=8LM7-CYX4BPD&format=png").resize(500, 500).into(iV1);
                        } else if(currentWeather.contains("Cloudy") || currentWeather.contains("cloudy") ){
                            Picasso.get().load("https://img.icons8.com/?size=2x&id=rhba9Nt5N4jA&format=png").into(iV1);
                        }else if(currentWeather.contains("Thunder")||currentWeather.contains("thunder")){
                            Picasso.get().load("https://img.icons8.com/?size=512&id=ziNIfsFS8p_p&format=png").resize(500, 500).into(iV1);
                        }else if(currentWeather.contains("Rain")||currentWeather.contains("rain")||currentWeather.contains("drizzle")){
                            Picasso.get().load("https://img.icons8.com/?size=2x&id=ulJA5JddHJKv&format=png").into(iV1);
                        }



                    }
                });
            }
        });


    }

    class RvAdapter extends RecyclerView.Adapter<RvAdapter.HolderI> {
        ArrayList<String> data;
        public RvAdapter(ArrayList<String> data){
            this.data = data;
        }

        @NonNull
        @Override
        public HolderI onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item, parent, false);
            return new HolderI(view);
        }

        @Override
        public void onBindViewHolder(@NonNull HolderI holder, int position) {
            holder.tvD.setText(data.get(position));
            Glide.with(MainActivity.this).load("https://img.icons8.com/?size=512&id=8LM7-CYX4BPD&format=png").into(holder.imgW);
            holder.tvC.setText("50");

            /*if(weatherList.get(position).contains("Clear") || weatherList.get(position).contains("clear")){
                Glide.with(MainActivity.this).load("https://img.icons8.com/?size=512&id=8LM7-CYX4BPD&format=png").into(holder.imgW);
            } else if(weatherList.get(position).contains("Cloudy") || weatherList.get(position).contains("cloudy") ){
                Glide.with(MainActivity.this).load("https://img.icons8.com/?size=2x&id=rhba9Nt5N4jA&format=png").into(holder.imgW);
            }else if(weatherList.get(position).contains("Thunder")||weatherList.get(position).contains("thunder")){
                Glide.with(MainActivity.this).load("https://img.icons8.com/?size=512&id=ziNIfsFS8p_p&format=png").into(holder.imgW);
            }else if(weatherList.get(position).contains("Rain")||weatherList.get(position).contains("rain")||weatherList.get(position).contains("drizzle")){
                Glide.with(MainActivity.this).load("https://img.icons8.com/?size=2x&id=ulJA5JddHJKv&format=png").into(holder.imgW);
            }*/


        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class HolderI extends RecyclerView.ViewHolder{
            TextView tvD, tvC;
            ImageView imgW;

            public HolderI(@NonNull View itemView){
                super(itemView);
                tvD = itemView.findViewById(R.id.tvD);
                imgW = itemView.findViewById(R.id.imgW);
                tvC = itemView.findViewById(R.id.tvC);
            }
        }
    }


    private void fetchWeather() {
        Set<String> set = prefs.getStringSet("arrayStorage", null);
        location.clear();
        location.addAll(set);
        alWeather.clear();

        for (String item : location) {
            client.get("https://wttr.in/" + item + "?format=j1", new JsonHttpResponseHandler() {
                String area;
                String forecast;
                String temp;

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                    try {
                        JSONArray jsonArrItems1 = response.getJSONArray("nearest_area");
                        JSONArray jsonArrItems2 = response.getJSONArray("current_condition");
                        JSONObject firstObj1 = jsonArrItems1.getJSONObject(0);
                        JSONObject firstObj2 = jsonArrItems2.getJSONObject(0);

                        temp = firstObj2.getString("temp_C");
                        JSONArray jsonArrForecasts1 = firstObj1.getJSONArray("country");
                        JSONArray jsonArrForecasts2 = firstObj2.getJSONArray("weatherDesc");

                        JSONObject jsonObjForecast1 = jsonArrForecasts1.getJSONObject(0);
                        JSONObject jsonObjForecast2 = jsonArrForecasts2.getJSONObject(0);
                        area = jsonObjForecast1.getString("value");
                        forecast = jsonObjForecast2.getString("value");

                        Weather weather = new Weather(area, forecast);
                        alWeather.add(weather);
                    } catch (JSONException e) {
                    }
                    aaWeather.notifyDataSetChanged();

                    tv1.setText(area);
                    tv2.setText(temp + "°C");
                    tv3.setText(forecast);

                   String currentWeather = forecast;


                    if(currentWeather.contains("Clear") || currentWeather.contains("clear")){
                        Picasso.get().load("https://img.icons8.com/?size=512&id=8LM7-CYX4BPD&format=png").resize(500, 500).into(iV1);
                    } else if(currentWeather.contains("Cloudy") || currentWeather.contains("cloudy") ){
                        Picasso.get().load("https://img.icons8.com/?size=2x&id=rhba9Nt5N4jA&format=png").resize(500, 500).into(iV1);
                    }else if(currentWeather.contains("Thunder")||currentWeather.contains("thunder")){
                        Picasso.get().load("https://img.icons8.com/?size=512&id=ziNIfsFS8p_p&format=png").resize(500, 500).into(iV1);
                    }else if(currentWeather.contains("Rain")||currentWeather.contains("rain")||currentWeather.contains("drizzle")){
                        Picasso.get().load("https://img.icons8.com/?size=2x&id=ulJA5JddHJKv&format=png").resize(500, 500).into(iV1);
                    }

                }
            });
        }



    }


    @Override
    protected void onResume() {
            super.onResume();

        prefEdit = prefs.edit();
        set = new HashSet<String>();
        set.clear();
        set.addAll(location);
        prefEdit.putStringSet("arrayStorage",set);
        prefEdit.apply();


        }


}