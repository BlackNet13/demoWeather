package sg.rp.edu.rp.c346.id22038845.demoweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

        lv = findViewById(R.id.lv);
        client = new AsyncHttpClient();
        alWeather = new ArrayList<Weather>(); // empty arrayList of weather objects
        aaWeather = new CustomAdapter(MainActivity.this, R.layout.row, alWeather);
        lv.setAdapter(aaWeather);

        prefs = getSharedPreferences("arrayStorage",MODE_PRIVATE);
        location = new ArrayList<>();




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

            //alWeather.clear();
            //fetchWeather();


            /*alWeather.clear();
            //client.get("https://api.data.gov.sg/v1/environment/2-hour-weather-forecast",new JsonHttpResponseHandler(){

            Set<String> set = prefs.getStringSet("arrayStorage", null);
            location.clear();
            location.addAll(set);

            for (String item : location) {
                client.get("https://wttr.in/" + item + "?format=j1", new JsonHttpResponseHandler() {
                    String area;
                    String forecast;

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            //JSONArray jsonArrItems = response.getJSONArray("items");
                            JSONArray jsonArrItems1 = response.getJSONArray("nearest_area");
                            JSONArray jsonArrItems2 = response.getJSONArray("weather");
                            JSONObject firstObj1 = jsonArrItems1.getJSONObject(0);
                            JSONObject firstObj2 = jsonArrItems2.getJSONObject(0);
                            //JSONArray jsonArrForecasts = firstObj.getJSONArray("forecasts");
                            JSONArray jsonArrForecasts1 = firstObj1.getJSONArray("areaName");
                            JSONArray jsonArrForecasts2 = firstObj2.getJSONArray("hourly");

                            //for(int i = 0; i<jsonArrForecasts.length(); i++){
                            //get jsonObject first then get the string in the object
                            //JSONObject jsonObjForecast = jsonArrForecasts.getJSONObject(i);
                            JSONObject jsonObjForecast1 = jsonArrForecasts1.getJSONObject(0);
                            JSONObject jsonObjForecast2 = jsonArrForecasts2.getJSONObject(0);
                            area = jsonObjForecast1.getString("value");
                            forecast = jsonObjForecast2.getJSONArray("weatherDesc").getJSONObject(0).getString("value");
                            //forecast = jsonObjForecast.getString("forecast");
                            Weather weather = new Weather(area, forecast); //insert values into weather object
                            alWeather.add(weather);
                            //}
                        } catch (JSONException e) {
                        }
                        //code to display list view
                        aaWeather.notifyDataSetChanged();


                    }//end onSuccess
                });



        }*/


        }


}