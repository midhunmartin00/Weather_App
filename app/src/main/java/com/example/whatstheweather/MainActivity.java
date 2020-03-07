package com.example.whatstheweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    public class Download extends AsyncTask<String,Void,String > {

        @Override
        protected String doInBackground(String... urls) {
            URL url;
            HttpsURLConnection httpsURLConnection;
            try {
                url=new URL(urls[0]);
                httpsURLConnection=(HttpsURLConnection) url.openConnection();
                InputStream in =httpsURLConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data=reader.read();
                char c;
                String result="";
                while(data!=-1)
                {
                    c=(char) data;
                    result+=c;
                    data=reader.read();
                }
                return result;
            }catch(Exception e){
                e.printStackTrace();
                Log.i("error", "doInBackground: ");
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("json",s);
            try {
                JSONObject jsonObject=new JSONObject(s);
                String weather=jsonObject.getString("weather");
                Log.i("weather",weather);

                JSONArray jsonArray=new JSONArray(weather);
                int i;
                for(i=0;i<jsonArray.length();i++)
                {
                    JSONObject ob=jsonArray.getJSONObject(i);
                    Log.i("main",ob.getString("main"));
                    Log.i("description",ob.getString("description"));
                }

            } catch (Exception e) {
                Log.i("error", "onPostExecute: ");
                e.printStackTrace();
            }
        }
    }

    public void getWeather(View view){
        Download download=new Download();
        download.execute("https://samples.openweathermap.org/data/2.5/weather?lat=35&lon=139&appid=b6907d289e10d714a6e88b30761fae22");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}
