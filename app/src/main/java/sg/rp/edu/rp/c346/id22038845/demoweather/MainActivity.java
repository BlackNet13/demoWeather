package sg.rp.edu.rp.c346.id22038845.demoweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {

    ListView lv;
    AsyncHttpClient client; //allows the app to recieve and read JSON data
    ArrayList<Weather> alWeather;
    CustomAdapter aaWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = findViewById(R.id.lv);
        client = new AsyncHttpClient();
        alWeather = new ArrayList<Weather>(); // empty arrayList of weather objects
        aaWeather = new CustomAdapter(MainActivity.this, R.layout.row, alWeather);
        lv.setAdapter(aaWeather);

    }

    @Override
    protected void onResume(){
        super.onResume();
        alWeather.clear();
        //client.get("https://api.data.gov.sg/v1/environment/2-hour-weather-forecast",new JsonHttpResponseHandler(){
        client.get("https://wttr.in/Singapore?format=j1",new JsonHttpResponseHandler(){
            String area;
            String forecast;
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                try{
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
                }
                catch(JSONException e){
                }
                //code to display list view
                aaWeather.notifyDataSetChanged();


            }//end onSuccess
        });
    }//end onResume


}